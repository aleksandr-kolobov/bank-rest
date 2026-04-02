package com.example.bankcards.service;

import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.CardFilterRequest;
import com.example.bankcards.dto.CardStatus;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InsufficientFundsException;
import com.example.bankcards.exception.InvalidCardStatusException;
import com.example.bankcards.exception.UnauthorizedAccessException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.CardSpecification;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.CardMapper;
import com.example.bankcards.util.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;
    private final EncryptionUtil encryptionUtil;

    public CardDTO createCard(CreateCardRequest request) {
        log.debug("Creating card for user: {}", request.getUserId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Card card = new Card();
        card.setUser(user);

        // Генерация и шифрование номера карты
        String plainCardNumber = generateCardNumber();
        String encryptedCardNumber = encryptionUtil.encrypt(plainCardNumber);
        card.setEncryptedCardNumber(encryptedCardNumber);

        // Маскирование номера карты
        String maskedCardNumber = maskCardNumber(plainCardNumber);
        card.setMaskedCardNumber(maskedCardNumber);

        card.setCardholderName(request.getCardholderName());
        card.setExpiryDate(request.getExpiryDate());
        card.setStatus(CardStatus.ACTIVE);
        card.setBalance(BigDecimal.valueOf(request.getInitialBalance()));

        Card savedCard = cardRepository.save(card);
        log.info("Card created successfully with ID: {}", savedCard.getId());

        return cardMapper.toCardDTO(savedCard);
    }

    @Transactional(readOnly = true)
    public Page<CardDTO> getMyCards(CardFilterRequest filter, Pageable pageable) {
        User currentUser = getCurrentUser();
        return cardRepository.findAll(
                CardSpecification.byUser(currentUser)
                        .and(CardSpecification.byMaskedCardNumber(filter.getMaskedCardNumber()))
                        .and(CardSpecification.byStatus(filter.getStatus()))
                        .and(CardSpecification.byBalanceBetween(filter.getMinBalance(), filter.getMaxBalance())),
                pageable
        ).map(cardMapper::toCardDTO);
    }

    @Transactional(readOnly = true)
    public Page<CardDTO> getAllCards(CardFilterRequest filter, Pageable pageable) {
        return cardRepository.findAll(
                CardSpecification.byMaskedCardNumber(filter.getMaskedCardNumber())
                        .and(CardSpecification.byStatus(filter.getStatus()))
                        .and(CardSpecification.byBalanceBetween(filter.getMinBalance(), filter.getMaxBalance())),
                pageable
        ).map(cardMapper::toCardDTO);
    }

    @Transactional(readOnly = true)
    public CardDTO getCardById(UUID cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with ID: " + cardId));

        User currentUser = getCurrentUser();
        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.name().equals("ROLE_ADMIN"));

        if (!isAdmin && !card.getUser().getId().equals(currentUser.getId())) {
            throw new UnauthorizedAccessException("You don't have permission to view this card");
        }

        return cardMapper.toCardDTO(card);
    }

    public CardDTO updateCardStatus(UUID cardId, CardStatus newStatus) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found with ID: " + cardId));

        // Проверка валидности смены статуса
        validateStatusTransition(card.getStatus(), newStatus);

        card.setStatus(newStatus);
        Card updatedCard = cardRepository.save(card);
        log.info("Card {} status updated to {}", cardId, newStatus);

        return cardMapper.toCardDTO(updatedCard);
    }

    public void deleteCard(UUID cardId) {
        if (!cardRepository.existsById(cardId)) {
            throw new CardNotFoundException("Card not found with ID: " + cardId);
        }
        cardRepository.deleteById(cardId);
        log.info("Card {} deleted successfully", cardId);
    }

    @Transactional(readOnly = true)
    public void validateCardOwnership(UUID cardId, UUID userId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));

        if (!card.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("Card does not belong to user");
        }

        if (!card.getStatus().name().equals("ACTIVE")) {
            throw new InvalidCardStatusException("Card is not active");
        }
    }

    @Transactional
    public void transferAmount(UUID fromCardId, UUID toCardId, BigDecimal amount) {
        Card fromCard = cardRepository.findById(fromCardId)
                .orElseThrow(() -> new CardNotFoundException("Source card not found"));
        Card toCard = cardRepository.findById(toCardId)
                .orElseThrow(() -> new CardNotFoundException("Destination card not found"));

        if (fromCard.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Insufficient funds on source card");
        }

        fromCard.setBalance(fromCard.getBalance().subtract(amount));
        toCard.setBalance(toCard.getBalance().add(amount));

        cardRepository.save(fromCard);
        cardRepository.save(toCard);

        log.info("Transfer of {} from card {} to card {} completed", amount, fromCardId, toCardId);
    }

    @Transactional(readOnly = true)
    public BigDecimal getCardBalance(UUID cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Card not found"));
        return card.getBalance();
    }

    private String generateCardNumber() {
        // Генерация 16-значного номера карты
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            sb.append((int) (Math.random() * 10));
        }
        return sb.toString();
    }

    private String maskCardNumber(String cardNumber) {
        // Маскирование: **** **** **** последние 4 цифры
        if (cardNumber == null || cardNumber.length() != 16) {
            return "**** **** **** 0000";
        }
        String last4 = cardNumber.substring(12);
        return String.format("**** **** **** %s", last4);
    }

    private void validateStatusTransition(CardStatus currentStatus, CardStatus newStatus) {
        if (currentStatus == CardStatus.EXPIRED && newStatus != CardStatus.EXPIRED) {
            throw new InvalidCardStatusException("Cannot change status of expired card");
        }
        if (currentStatus == CardStatus.BLOCKED && newStatus == CardStatus.ACTIVE) {
            // Администратор может разблокировать
            log.info("Unblocking card");
        }
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}