package com.example.bankcards.controller;

import com.example.bankcards.api.*;
import com.example.bankcards.dto.CardDTO;
import com.example.bankcards.dto.CardStatus;
import com.example.bankcards.dto.CreateCardRequest;
import com.example.bankcards.dto.GetAllCards200Response;
import com.example.bankcards.dto.GetAllUsers200Response;
import com.example.bankcards.dto.GetMyCards200Response;
import com.example.bankcards.dto.GetMyTransactions200Response;
import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.dto.UpdateCardStatusRequest;
import com.example.bankcards.dto.UpdateUserRequest;
import com.example.bankcards.dto.UserDTO;
import com.example.bankcards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/bankcards")
@RequiredArgsConstructor
public class CardController implements CardManagementApi, TransferManagementApi, AdminUserManagementApi {

    private final CardService cardService;

    @Override
    public ResponseEntity<Void> _deleteUser(UUID userId) {
        return AdminUserManagementApi.super._deleteUser(userId);
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        return AdminUserManagementApi.super.deleteUser(userId);
    }

    @Override
    public ResponseEntity<GetAllUsers200Response> _getAllUsers(Integer page, Integer size) {
        return AdminUserManagementApi.super._getAllUsers(page, size);
    }

    @Override
    public ResponseEntity<GetAllUsers200Response> getAllUsers(Integer page, Integer size) {
        return AdminUserManagementApi.super.getAllUsers(page, size);
    }

    @Override
    public ResponseEntity<UserDTO> _getUserById(UUID userId) {
        return AdminUserManagementApi.super._getUserById(userId);
    }

    @Override
    public ResponseEntity<UserDTO> getUserById(UUID userId) {
        return AdminUserManagementApi.super.getUserById(userId);
    }

    @Override
    public ResponseEntity<UserDTO> _updateUser(UUID userId, UpdateUserRequest updateUserRequest) {
        return AdminUserManagementApi.super._updateUser(userId, updateUserRequest);
    }

    @Override
    public ResponseEntity<UserDTO> updateUser(UUID userId, UpdateUserRequest updateUserRequest) {
        return AdminUserManagementApi.super.updateUser(userId, updateUserRequest);
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return CardManagementApi.super.getRequest();
    }

    @Override
    public ResponseEntity<Void> _getCardTransactions(UUID cardId, Integer page, Integer size) {
        return TransferManagementApi.super._getCardTransactions(cardId, page, size);
    }

    @Override
    public ResponseEntity<Void> getCardTransactions(UUID cardId, Integer page, Integer size) {
        return TransferManagementApi.super.getCardTransactions(cardId, page, size);
    }

    @Override
    public ResponseEntity<GetMyTransactions200Response> _getMyTransactions(Integer page, Integer size) {
        return TransferManagementApi.super._getMyTransactions(page, size);
    }

    @Override
    public ResponseEntity<GetMyTransactions200Response> getMyTransactions(Integer page, Integer size) {
        return TransferManagementApi.super.getMyTransactions(page, size);
    }

    @Override
    public ResponseEntity<TransactionDTO> _transferBetweenOwnCards(TransferRequest transferRequest) {
        return TransferManagementApi.super._transferBetweenOwnCards(transferRequest);
    }

    @Override
    public ResponseEntity<TransactionDTO> transferBetweenOwnCards(TransferRequest transferRequest) {
        return TransferManagementApi.super.transferBetweenOwnCards(transferRequest);
    }

    @Override
    public ResponseEntity<CardDTO> _createCard(CreateCardRequest createCardRequest) {
        return CardManagementApi.super._createCard(createCardRequest);
    }

    @Override
    public ResponseEntity<CardDTO> createCard(CreateCardRequest createCardRequest) {
        return CardManagementApi.super.createCard(createCardRequest);
    }

    @Override
    public ResponseEntity<Void> _deleteCard(UUID cardId) {
        return CardManagementApi.super._deleteCard(cardId);
    }

    @Override
    public ResponseEntity<Void> deleteCard(UUID cardId) {
        return CardManagementApi.super.deleteCard(cardId);
    }

    @Override
    public ResponseEntity<GetAllCards200Response> _getAllCards(String maskedCardNumber, CardStatus status, Double minBalance, Double maxBalance, Integer page, Integer size) {
        return CardManagementApi.super._getAllCards(maskedCardNumber, status, minBalance, maxBalance, page, size);
    }

    @Override
    public ResponseEntity<GetAllCards200Response> getAllCards(String maskedCardNumber, CardStatus status, Double minBalance, Double maxBalance, Integer page, Integer size) {
        return CardManagementApi.super.getAllCards(maskedCardNumber, status, minBalance, maxBalance, page, size);
    }

    @Override
    public ResponseEntity<CardDTO> _getCardById(UUID cardId) {
        return CardManagementApi.super._getCardById(cardId);
    }

    @Override
    public ResponseEntity<CardDTO> getCardById(UUID cardId) {
        return CardManagementApi.super.getCardById(cardId);
    }

    @Override
    public ResponseEntity<GetMyCards200Response> _getMyCards(String maskedCardNumber, CardStatus status, Double minBalance, Double maxBalance, Integer page, Integer size, String sort) {
        return CardManagementApi.super._getMyCards(maskedCardNumber, status, minBalance, maxBalance, page, size, sort);
    }

    @Override
    public ResponseEntity<GetMyCards200Response> getMyCards(String maskedCardNumber, CardStatus status, Double minBalance, Double maxBalance, Integer page, Integer size, String sort) {
        return CardManagementApi.super.getMyCards(maskedCardNumber, status, minBalance, maxBalance, page, size, sort);
    }

    @Override
    public ResponseEntity<CardDTO> _updateCardStatus(UUID cardId, UpdateCardStatusRequest updateCardStatusRequest) {
        return CardManagementApi.super._updateCardStatus(cardId, updateCardStatusRequest);
    }

    @Override
    public ResponseEntity<CardDTO> updateCardStatus(UUID cardId, UpdateCardStatusRequest updateCardStatusRequest) {
        return CardManagementApi.super.updateCardStatus(cardId, updateCardStatusRequest);
    }

}