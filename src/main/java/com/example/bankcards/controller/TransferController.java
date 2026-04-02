package com.example.bankcards.controller;

import com.example.bankcards.api.TransferManagementApi;
import com.example.bankcards.dto.GetMyTransactions200Response;
import com.example.bankcards.dto.TransactionDTO;
import com.example.bankcards.dto.TransferRequest;
import com.example.bankcards.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TransferController implements TransferManagementApi {

    private final TransactionService transactionService;

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<TransactionDTO> transferBetweenOwnCards(TransferRequest transferRequest) {
        log.debug("Transfer between own cards - from: {}, to: {}, amount: {}",
                transferRequest.getFromCardId(),
                transferRequest.getToCardId(),
                transferRequest.getAmount());

        TransactionDTO transaction = transactionService.transferBetweenOwnCards(transferRequest);
        return ResponseEntity.ok(transaction);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GetMyTransactions200Response> getMyTransactions(Integer page, Integer size) {
        log.debug("Getting my transactions - page: {}, size: {}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Page<TransactionDTO> transactionsPage = transactionService.getMyTransactions(pageable);

        GetMyTransactions200Response response = new GetMyTransactions200Response();
        response.setContent(transactionsPage.getContent());
        response.setTotalElements((int) transactionsPage.getTotalElements());
        response.setTotalPages(transactionsPage.getTotalPages());
        response.setSize(transactionsPage.getSize());
        response.setNumber(transactionsPage.getNumber());
        response.setFirst(transactionsPage.isFirst());
        response.setLast(transactionsPage.isLast());
        response.setEmpty(transactionsPage.isEmpty());

        return ResponseEntity.ok(response);
    }

    @Override
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<GetMyTransactions200Response> getCardTransactions(UUID cardId, Integer page, Integer size) {
        log.debug("Getting transactions for card: {} - page: {}, size: {}", cardId, page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Page<TransactionDTO> transactionsPage = transactionService.getCardTransactions(cardId, pageable);

        GetMyTransactions200Response response = new GetMyTransactions200Response();
        response.setContent(transactionsPage.getContent());
        response.setTotalElements((int) transactionsPage.getTotalElements());
        response.setTotalPages(transactionsPage.getTotalPages());
        response.setSize(transactionsPage.getSize());
        response.setNumber(transactionsPage.getNumber());
        response.setFirst(transactionsPage.isFirst());
        response.setLast(transactionsPage.isLast());
        response.setEmpty(transactionsPage.isEmpty());

        return ResponseEntity.ok(response);
    }
}
