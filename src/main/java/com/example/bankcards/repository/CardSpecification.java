package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Component
public class CardSpecification {

    public static Specification<Card> byUser(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    public static Specification<Card> byUserId(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<Card> byStatus(CardStatus status) {
        return (root, query, cb) -> status == null ? cb.conjunction() : cb.equal(root.get("status"), status);
    }

    public static Specification<Card> byMaskedCardNumber(String maskedNumber) {
        return (root, query, cb) -> maskedNumber == null || maskedNumber.isEmpty()
                ? cb.conjunction()
                : cb.like(root.get("maskedCardNumber"), "%" + maskedNumber + "%");
    }

    public static Specification<Card> byMinBalance(BigDecimal minBalance) {
        return (root, query, cb) -> minBalance == null
                ? cb.conjunction()
                : cb.greaterThanOrEqualTo(root.get("balance"), minBalance);
    }

    public static Specification<Card> byMaxBalance(BigDecimal maxBalance) {
        return (root, query, cb) -> maxBalance == null
                ? cb.conjunction()
                : cb.lessThanOrEqualTo(root.get("balance"), maxBalance);
    }

    public static Specification<Card> byCardholderName(String cardholderName) {
        return (root, query, cb) -> cardholderName == null || cardholderName.isEmpty()
                ? cb.conjunction()
                : cb.like(root.get("cardholderName"), "%" + cardholderName + "%");
    }

    public static Specification<Card> byExpiryDateBefore(LocalDate date) {
        return (root, query, cb) -> date == null
                ? cb.conjunction()
                : cb.lessThan(root.get("expiryDate"), date);
    }

    public static Specification<Card> byExpiryDateAfter(LocalDate date) {
        return (root, query, cb) -> date == null
                ? cb.conjunction()
                : cb.greaterThan(root.get("expiryDate"), date);
    }

    public static Specification<Card> withActiveStatus() {
        return (root, query, cb) -> cb.equal(root.get("status"), CardStatus.ACTIVE);
    }

    public static Specification<Card> withPositiveBalance() {
        return (root, query, cb) -> cb.greaterThan(root.get("balance"), BigDecimal.ZERO);
    }
}
