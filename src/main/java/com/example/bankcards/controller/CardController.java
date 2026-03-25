package com.example.bankcards.controller;

import com.example.bankcards.api.DefaultApi;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bankcards/api/v1")
@RequiredArgsConstructor
public class CardController implements DefaultApi {
    private final CardService cardService;

/*
    @Override
    public ResponseEntity<CardResponse> createCard(CardCreateRequest request) {
        CardResponse response = cardService.createCard(request);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<PageCardResponse> getCards(
            Integer page,
            Integer size,
            CardStatus status,
            UUID ownerId) {
        PageCardResponse response = cardService.getCards(page, size, status, ownerId);
        return ResponseEntity.ok(response);
    }

    // ... другие методы
*/
}