package com.gymmanagement.gymapp.repository;

import com.gymmanagement.gymapp.model.SupportTicket;
import com.gymmanagement.gymapp.model.TicketCategory;
import com.gymmanagement.gymapp.model.TicketPriority;
import com.gymmanagement.gymapp.model.TicketStatus;
import com.gymmanagement.gymapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {

    Page<SupportTicket> findByStatusOrderByCreatedAtDesc(TicketStatus status, Pageable pageable);

    Page<SupportTicket> findByPriorityOrderByCreatedAtDesc(TicketPriority priority, Pageable pageable);

    Page<SupportTicket> findByCategoryOrderByCreatedAtDesc(TicketCategory category, Pageable pageable);

    Page<SupportTicket> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    Page<SupportTicket> findByAssignedToOrderByCreatedAtDesc(User assignedTo, Pageable pageable);

    @Query("SELECT st FROM SupportTicket st WHERE st.status IN ('OPEN', 'IN_PROGRESS') ORDER BY st.priority DESC, st.createdAt ASC")
    List<SupportTicket> findOpenTickets();

    @Query("SELECT st FROM SupportTicket st WHERE st.createdAt BETWEEN :startDate AND :endDate ORDER BY st.createdAt DESC")
    Page<SupportTicket> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate,
                                      Pageable pageable);

    @Query("SELECT st FROM SupportTicket st WHERE " +
           "LOWER(st.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(st.description) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY st.createdAt DESC")
    Page<SupportTicket> searchTickets(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT COUNT(st) FROM SupportTicket st WHERE st.status = :status")
    long countByStatus(@Param("status") TicketStatus status);

    @Query("SELECT st.category, COUNT(st) FROM SupportTicket st GROUP BY st.category ORDER BY COUNT(st) DESC")
    List<Object[]> getTicketCountByCategory();

    @Query("SELECT st.priority, COUNT(st) FROM SupportTicket st WHERE st.status IN ('OPEN', 'IN_PROGRESS') GROUP BY st.priority")
    List<Object[]> getOpenTicketCountByPriority();

    @Query("SELECT AVG(TIME_TO_SEC(TIMEDIFF(st.resolvedAt, st.createdAt)) / 3600) " +
           "FROM SupportTicket st WHERE st.resolvedAt IS NOT NULL " +
           "AND st.createdAt BETWEEN :startDate AND :endDate")
    Double getAverageResolutionTimeInHours(@Param("startDate") LocalDateTime startDate,
                                         @Param("endDate") LocalDateTime endDate);
}