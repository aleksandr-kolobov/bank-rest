package com.example.bankcards.repository;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.CardStatus;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID>, JpaSpecificationExecutor<Card> {

    // Find cards by user with pagination
    Page<Card> findByUser(User user, Pageable pageable);

    // Find all cards by user
    List<Card> findByUser(User user);

    // Find cards by user and status
    List<Card> findByUserAndStatus(User user, CardStatus status);

    // Find card by id and user
    Optional<Card> findByIdAndUser(UUID id, User user);

    // Find cards by user id and card ids
    @Query("SELECT c FROM Card c WHERE c.user.id = :userId AND c.id IN :cardIds")
    List<Card> findAllByUserIdAndCardIds(@Param("userId") UUID userId, @Param("cardIds") List<UUID> cardIds);

    // Check if card exists and belongs to user
    boolean existsByUserAndId(User user, UUID cardId);

    // Find cards by status
    List<Card> findByStatus(CardStatus status);

    // Find cards with balance less than specified amount
    List<Card> findByBalanceLessThan(BigDecimal amount);

    // Find cards expiring soon (within next 30 days)
    @Query("SELECT c FROM Card c WHERE c.expiryDate BETWEEN CURRENT_DATE AND CURRENT_DATE + 30")
    List<Card> findCardsExpiringSoon();

    // Update card status
    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.status = :status WHERE c.id = :cardId")
    int updateCardStatus(@Param("cardId") UUID cardId, @Param("status") CardStatus status);

    // Update card balance
    @Modifying
    @Transactional
    @Query("UPDATE Card c SET c.balance = :balance WHERE c.id = :cardId")
    int updateCardBalance(@Param("cardId") UUID cardId, @Param("balance") BigDecimal balance);

    // Find active cards by user
    @Query("SELECT c FROM Card c WHERE c.user = :user AND c.status = 'ACTIVE'")
    List<Card> findActiveCardsByUser(@Param("user") User user);

    // Count cards by user and status
    long countByUserAndStatus(User user, CardStatus status);

    // Get total balance for user
    @Query("SELECT COALESCE(SUM(c.balance), 0) FROM Card c WHERE c.user.id = :userId AND c.status = 'ACTIVE'")
    BigDecimal getTotalBalanceByUserId(@Param("userId") UUID userId);

    // Find cards by masked number (for search)
    @Query("SELECT c FROM Card c WHERE c.maskedCardNumber LIKE CONCAT('%', :maskedNumber, '%')")
    List<Card> findByMaskedCardNumberContaining(@Param("maskedNumber") String maskedNumber);

    // Find cards with balance between min and max
    @Query("SELECT c FROM Card c WHERE c.balance BETWEEN :minBalance AND :maxBalance")
    List<Card> findByBalanceBetween(@Param("minBalance") BigDecimal minBalance, @Param("maxBalance") BigDecimal maxBalance);
}
