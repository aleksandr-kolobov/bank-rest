package com.example.bankcards.repository;

import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TransactionSpecification {

    public static Specification<Transaction> byUser(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    public static Specification<Transaction> byUserId(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Transaction> byFromCardId(UUID fromCardId) {
        return (root, query, cb) -> fromCardId == null
                ? cb.conjunction()
                : cb.equal(root.get("fromCard").get("id"), fromCardId);
    }

    public static Specification<Transaction> byToCardId(UUID toCardId) {
        return (root, query, cb) -> toCardId == null
                ? cb.conjunction()
                : cb.equal(root.get("toCard").get("id"), toCardId);
    }

    public static Specification<Transaction> byCardId(UUID cardId) {
        return (root, query, cb) -> cardId == null
                ? cb.conjunction()
                : cb.or(
                cb.equal(root.get("fromCard").get("id"), cardId),
                cb.equal(root.get("toCard").get("id"), cardId)
        );
    }

    public static Specification<Transaction> byAmountBetween(BigDecimal minAmount, BigDecimal maxAmount) {
        return (root, query, cb) -> {
            if (minAmount != null && maxAmount != null) {
                return cb.between(root.get("amount"), minAmount, maxAmount);
            } else if (minAmount != null) {
                return cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
            } else if (maxAmount != null) {
                return cb.lessThanOrEqualTo(root.get("amount"), maxAmount);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Transaction> byTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, cb) -> {
            if (startDate != null && endDate != null) {
                return cb.between(root.get("transactionDate"), startDate, endDate);
            } else if (startDate != null) {
                return cb.greaterThanOrEqualTo(root.get("transactionDate"), startDate);
            } else if (endDate != null) {
                return cb.lessThanOrEqualTo(root.get("transactionDate"), endDate);
            }
            return cb.conjunction();
        };
    }

    public static Specification<Transaction> byDescriptionContaining(String description) {
        return (root, query, cb) -> description == null || description.isEmpty()
                ? cb.conjunction()
                : cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%");
    }

    public static Specification<Transaction> byAmountGreaterThan(BigDecimal amount) {
        return (root, query, cb) -> amount == null
                ? cb.conjunction()
                : cb.greaterThan(root.get("amount"), amount);
    }

    public static Specification<Transaction> byAmountLessThan(BigDecimal amount) {
        return (root, query, cb) -> amount == null
                ? cb.conjunction()
                : cb.lessThan(root.get("amount"), amount);
    }
}
