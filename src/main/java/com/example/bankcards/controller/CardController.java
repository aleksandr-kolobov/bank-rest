package com.example.bankcards.controller;

import com.example.bankcards.api.CardManagementApi;
import com.example.bankcards.dto.*;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CardController implements CardManagementApi {

    private final CardService cardService;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CardDTO> createCard(CreateCardRequest createCardRequest) {
        log.debug("Creating card for user: {}", createCardRequest.getUserId());
        CardDTO createdCard = cardService.createCard(createCardRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCard);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GetMyCards200Response> getMyCards(String maskedCardNumber,
                                                            CardStatus status,
                                                            Double minBalance,
                                                            Double maxBalance,
                                                            Integer page,
                                                            Integer size,
                                                            String sort) {
        log.debug("Getting my cards with filters - maskedNumber: {}, status: {}, minBalance: {}, maxBalance: {}",
                maskedCardNumber, status, minBalance, maxBalance);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        CardFilterRequest filter = new CardFilterRequest();
        filter.setMaskedCardNumber(maskedCardNumber);
        filter.setStatus(status);
        filter.setMinBalance(minBalance);
        filter.setMaxBalance(maxBalance);

        Page<CardDTO> cardsPage = cardService.getMyCards(filter, pageable);

        GetMyCards200Response response = new GetMyCards200Response();
        response.setContent(cardsPage.getContent());
        response.setTotalElements((int) cardsPage.getTotalElements());
        response.setTotalPages(cardsPage.getTotalPages());
        response.setSize(cardsPage.getSize());
        response.setNumber(cardsPage.getNumber());
        response.setFirst(cardsPage.isFirst());
        response.setLast(cardsPage.isLast());
        response.setEmpty(cardsPage.isEmpty());

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetAllCards200Response> getAllCards(String maskedCardNumber,
                                                              CardStatus status,
                                                              Double minBalance,
                                                              Double maxBalance,
                                                              Integer page,
                                                              Integer size) {
        log.debug("Getting all cards with filters - maskedNumber: {}, status: {}, minBalance: {}, maxBalance: {}",
                maskedCardNumber, status, minBalance, maxBalance);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        CardFilterRequest filter = new CardFilterRequest();
        filter.setMaskedCardNumber(maskedCardNumber);
        filter.setStatus(status);
        filter.setMinBalance(minBalance);
        filter.setMaxBalance(maxBalance);

        Page<CardDTO> cardsPage = cardService.getAllCards(filter, pageable);

        GetAllCards200Response response = new GetAllCards200Response();
        response.setContent(cardsPage.getContent());
        response.setTotalElements((int) cardsPage.getTotalElements());
        response.setTotalPages(cardsPage.getTotalPages());
        response.setSize(cardsPage.getSize());
        response.setNumber(cardsPage.getNumber());
        response.setFirst(cardsPage.isFirst());
        response.setLast(cardsPage.isLast());
        response.setEmpty(cardsPage.isEmpty());

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CardDTO> getCardById(UUID cardId) {
        log.debug("Getting card by id: {}", cardId);
        CardDTO card = cardService.getCardById(cardId);
        return ResponseEntity.ok(card);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN') or @cardSecurityService.isCardOwner(#cardId, authentication)")
    public ResponseEntity<CardDTO> updateCardStatus(UUID cardId, UpdateCardStatusRequest updateCardStatusRequest) {
        log.debug("Updating card status for cardId: {} to status: {}", cardId, updateCardStatusRequest.getStatus());
        CardDTO updatedCard = cardService.updateCardStatus(cardId, updateCardStatusRequest.getStatus());
        return ResponseEntity.ok(updatedCard);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCard(UUID cardId) {
        log.debug("Deleting card: {}", cardId);
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
