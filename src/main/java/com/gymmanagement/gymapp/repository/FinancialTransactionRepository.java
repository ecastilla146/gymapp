package com.gymmanagement.gymapp.repository;

import com.gymmanagement.gymapp.model.FinancialTransaction;
import com.gymmanagement.gymapp.model.TransactionStatus;
import com.gymmanagement.gymapp.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

    Page<FinancialTransaction> findByTransactionTypeOrderByTransactionDateDesc(TransactionType type, Pageable pageable);

    Page<FinancialTransaction> findByStatusOrderByTransactionDateDesc(TransactionStatus status, Pageable pageable);

    Page<FinancialTransaction> findByCategoryOrderByTransactionDateDesc(String category, Pageable pageable);

    @Query("SELECT ft FROM FinancialTransaction ft WHERE ft.transactionDate BETWEEN :startDate AND :endDate ORDER BY ft.transactionDate DESC")
    Page<FinancialTransaction> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                             @Param("endDate") LocalDateTime endDate, 
                                             Pageable pageable);

    @Query("SELECT SUM(ft.amount) FROM FinancialTransaction ft WHERE ft.transactionType = :type " +
           "AND ft.status = 'COMPLETED' AND ft.transactionDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalAmountByTypeAndDateRange(@Param("type") TransactionType type,
                                              @Param("startDate") LocalDateTime startDate,
                                              @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT DATE(transaction_date) as date, " +
           "SUM(CASE WHEN transaction_type = 'INCOME' THEN amount ELSE 0 END) as income, " +
           "SUM(CASE WHEN transaction_type = 'EXPENSE' THEN amount ELSE 0 END) as expenses " +
           "FROM financial_transactions " +
           "WHERE status = 'COMPLETED' AND transaction_date BETWEEN ?1 AND ?2 " +
           "GROUP BY DATE(transaction_date) ORDER BY date", nativeQuery = true)
    List<Object[]> getDailyFinancialSummary(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT ft.category, SUM(ft.amount) " +
           "FROM FinancialTransaction ft " +
           "WHERE ft.transactionType = :type AND ft.status = 'COMPLETED' " +
           "AND ft.transactionDate BETWEEN :startDate AND :endDate " +
           "GROUP BY ft.category ORDER BY SUM(ft.amount) DESC")
    List<Object[]> getCategoryTotals(@Param("type") TransactionType type,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT ft FROM FinancialTransaction ft WHERE " +
           "LOWER(ft.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(ft.category) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY ft.transactionDate DESC")
    Page<FinancialTransaction> searchTransactions(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT DISTINCT ft.category FROM FinancialTransaction ft ORDER BY ft.category")
    List<String> findAllCategories();
}