package com.example.bankcards.repository;

import com.example.bankcards.entity.Transaction;
import com.example.bankcards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Find transactions by user with pagination
    Page<Transaction> findByUser(User user, Pageable pageable);

    // Find transactions by user
    List<Transaction> findByUser(User user);

    // Find transactions by card (as sender or receiver)
    @Query("SELECT t FROM Transaction t WHERE t.fromCard.id = :cardId OR t.toCard.id = :cardId")
    Page<Transaction> findByCardId(@Param("cardId") UUID cardId, Pageable pageable);

    // Find transactions by card with pagination
    @Query("SELECT t FROM Transaction t WHERE t.fromCard.id = :cardId OR t.toCard.id = :cardId")
    List<Transaction> findByCardId(@Param("cardId") UUID cardId);

    // Find transactions between two cards
    @Query("SELECT t FROM Transaction t WHERE (t.fromCard.id = :cardId1 AND t.toCard.id = :cardId2) OR (t.fromCard.id = :cardId2 AND t.toCard.id = :cardId1)")
    List<Transaction> findTransactionsBetweenCards(@Param("cardId1") UUID cardId1, @Param("cardId2") UUID cardId2);

    // Find transactions by date range
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    Page<Transaction> findByTransactionDateBetween(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate,
                                                   Pageable pageable);

    // Find transactions by user and date range
    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND t.transactionDate BETWEEN :startDate AND :endDate")
    Page<Transaction> findByUserAndTransactionDateBetween(@Param("user") User user,
                                                          @Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate,
                                                          Pageable pageable);

    // Find transactions by amount greater than
    @Query("SELECT t FROM Transaction t WHERE t.amount >= :amount")
    Page<Transaction> findByAmountGreaterThanEqual(@Param("amount") BigDecimal amount, Pageable pageable);

    // Find transactions by amount less than
    @Query("SELECT t FROM Transaction t WHERE t.amount <= :amount")
    Page<Transaction> findByAmountLessThanEqual(@Param("amount") BigDecimal amount, Pageable pageable);

    // Get total amount transferred from a card
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.fromCard.id = :cardId")
    BigDecimal getTotalSentAmountByCardId(@Param("cardId") UUID cardId);

    // Get total amount received by a card
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.toCard.id = :cardId")
    BigDecimal getTotalReceivedAmountByCardId(@Param("cardId") UUID cardId);

    // Get transaction count by user
    long countByUser(User user);

    // Get recent transactions by user (last 10)
    @Query("SELECT t FROM Transaction t WHERE t.user = :user ORDER BY t.transactionDate DESC")
    List<Transaction> findTop10ByUserOrderByTransactionDateDesc(@Param("user") User user);

    // Get transactions by description containing text
    @Query("SELECT t FROM Transaction t WHERE LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))")
    Page<Transaction> findByDescriptionContainingIgnoreCase(@Param("description") String description, Pageable pageable);

    // Get transactions by user and card
    @Query("SELECT t FROM Transaction t WHERE t.user = :user AND (t.fromCard.id = :cardId OR t.toCard.id = :cardId)")
    Page<Transaction> findByUserAndCardId(@Param("user") User user,
                                          @Param("cardId") UUID cardId,
                                          Pageable pageable);

    // Get daily transaction summary for user
    @Query("SELECT DATE(t.transactionDate) as date, COUNT(t) as count, SUM(t.amount) as total " +
            "FROM Transaction t WHERE t.user = :user AND t.transactionDate >= :startDate " +
            "GROUP BY DATE(t.transactionDate) ORDER BY DATE(t.transactionDate) DESC")
    List<Object[]> getDailyTransactionSummary(@Param("user") User user, @Param("startDate") LocalDateTime startDate);
}
