package com.example.bankcards.service;

import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.InvalidTransferException;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.TransactionRepository;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.util.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final CardService cardService;
    private final TransactionMapper transactionMapper;

    public TransactionDTO transferBetweenOwnCards(TransferRequest request) {
        log.debug("Processing transfer request: {} -> {}, amount: {}",
                request.getFromCardId(), request.getToCardId(), request.getAmount());

        User currentUser = getCurrentUser();

        // Валидация что карты принадлежат текущему пользователю
        cardService.validateCardOwnership(request.getFromCardId(), currentUser.getId());
        cardService.validateCardOwnership(request.getToCardId(), currentUser.getId());

        if (request.getFromCardId().equals(request.getToCardId())) {
            throw new InvalidTransferException("Cannot transfer to the same card");
        }

        BigDecimal amount = BigDecimal.valueOf(request.getAmount());

        // Выполнение перевода
        cardService.transferAmount(request.getFromCardId(), request.getToCardId(), amount);

        // Создание записи транзакции
        Transaction transaction = new Transaction();
        transaction.setUser(currentUser);

        Card fromCard = cardRepository.findById(request.getFromCardId())
                .orElseThrow(() -> new CardNotFoundException("Source card not found"));
        Card toCard = cardRepository.findById(request.getToCardId())
                .orElseThrow(() -> new CardNotFoundException("Destination card not found"));

        transaction.setFromCard(fromCard);
        transaction.setToCard(toCard);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(request.getDescription());

        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("Transfer completed successfully. Transaction ID: {}", savedTransaction.getId());

        return transactionMapper.toDto(savedTransaction);
    }

    @Transactional(readOnly = true)
    public Page<TransactionDTO> getMyTransactions(Pageable pageable) {
        User currentUser = getCurrentUser();
        return transactionRepository.findByUser(currentUser, pageable)
                .map(transactionMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<TransactionDTO> getCardTransactions(UUID cardId, Pageable pageable) {
        User currentUser = getCurrentUser();

        // Проверка что карта принадлежит пользователю
        cardService.validateCardOwnership(cardId, currentUser.getId());

        return transactionRepository.findByCardId(cardId, pageable)
                .map(transactionMapper::toDto);
    }

    private User getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
