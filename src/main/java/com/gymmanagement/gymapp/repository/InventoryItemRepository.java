package com.gymmanagement.gymapp.repository;

import com.gymmanagement.gymapp.model.InventoryItem;
import com.gymmanagement.gymapp.model.InventoryStatus;
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
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    Page<InventoryItem> findByStatusOrderByNameAsc(InventoryStatus status, Pageable pageable);

    Page<InventoryItem> findByCategoryOrderByNameAsc(String category, Pageable pageable);

    @Query("SELECT ii FROM InventoryItem ii WHERE ii.currentStock <= ii.minStockLevel ORDER BY ii.name ASC")
    List<InventoryItem> findLowStockItems();

    @Query("SELECT ii FROM InventoryItem ii WHERE ii.currentStock = 0 ORDER BY ii.name ASC")
    List<InventoryItem> findOutOfStockItems();

    @Query("SELECT ii FROM InventoryItem ii WHERE ii.nextMaintenance <= :date ORDER BY ii.nextMaintenance ASC")
    List<InventoryItem> findItemsNeedingMaintenance(@Param("date") LocalDateTime date);

    @Query("SELECT DISTINCT ii.category FROM InventoryItem ii ORDER BY ii.category")
    List<String> findAllCategories();

    @Query("SELECT ii FROM InventoryItem ii WHERE LOWER(ii.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(ii.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(ii.brand) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY ii.name ASC")
    Page<InventoryItem> searchItems(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT SUM(ii.currentStock * ii.unitPrice) FROM InventoryItem ii WHERE ii.unitPrice IS NOT NULL")
    BigDecimal getTotalInventoryValue();

    @Query("SELECT ii.category, COUNT(ii), SUM(ii.currentStock * COALESCE(ii.unitPrice, 0)) " +
           "FROM InventoryItem ii GROUP BY ii.category ORDER BY ii.category")
    List<Object[]> getInventoryStatsByCategory();
}