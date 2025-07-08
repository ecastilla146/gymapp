package com.gymmanagement.gymapp.repository;

import com.gymmanagement.gymapp.model.Membership;
import com.gymmanagement.gymapp.model.MembershipStatus;
import com.gymmanagement.gymapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {

    // Buscar membresías por usuario ordenadas por fecha de inicio descendente
    List<Membership> findByUserOrderByStartDateDesc(User user);

    // Buscar membresías por usuario y estado
    List<Membership> findByUserAndStatus(User user, MembershipStatus status);

    // Contar membresías por estado
    long countByStatus(MembershipStatus status);

    // Buscar membresías que expiran antes de una fecha específica
    @Query("SELECT m FROM Membership m WHERE m.endDate <= :limitDate AND m.status = :status")
    List<Membership> findMembershipsExpiringBefore(@Param("limitDate") LocalDate limitDate, 
                                                   @Param("status") MembershipStatus status);

    // Buscar membresías que expiran en un rango de fechas
    @Query("SELECT m FROM Membership m WHERE m.endDate BETWEEN :startDate AND :endDate AND m.status = :status")
    List<Membership> findMembershipsExpiringBetween(@Param("startDate") LocalDate startDate, 
                                                    @Param("endDate") LocalDate endDate, 
                                                    @Param("status") MembershipStatus status);

    // Actualizar membresías expiradas automáticamente
    @Modifying
    @Query("UPDATE Membership m SET m.status = 'EXPIRED' WHERE m.endDate < :currentDate AND m.status = 'ACTIVE'")
    int updateExpiredMemberships(@Param("currentDate") LocalDate currentDate);

    // Buscar membresías por criterios múltiples (búsqueda avanzada)
    @Query("SELECT m FROM Membership m JOIN m.user u WHERE " +
           "(:userEmail IS NULL OR LOWER(u.email) LIKE LOWER(CONCAT('%', :userEmail, '%'))) AND " +
           "(:status IS NULL OR m.status = :status) AND " +
           "(:startDate IS NULL OR m.startDate >= :startDate) AND " +
           "(:endDate IS NULL OR m.endDate <= :endDate)")
    Page<Membership> searchMemberships(@Param("userEmail") String userEmail,
                                      @Param("status") MembershipStatus status,
                                      @Param("startDate") LocalDate startDate,
                                      @Param("endDate") LocalDate endDate,
                                      Pageable pageable);

    // Obtener membresías activas de un plan específico
    @Query("SELECT m FROM Membership m WHERE m.membershipPlan.id = :planId AND m.status = 'ACTIVE'")
    List<Membership> findActiveMembershipsByPlan(@Param("planId") Long planId);

    // Obtener estadísticas por fecha
    @Query("SELECT COUNT(m) FROM Membership m WHERE m.createdAt >= :startDate AND m.createdAt <= :endDate")
    long countMembershipsCreatedBetween(@Param("startDate") LocalDate startDate, 
                                       @Param("endDate") LocalDate endDate);

    // Obtener ingresos por período
    @Query("SELECT SUM(m.amount) FROM Membership m WHERE m.createdAt >= :startDate AND m.createdAt <= :endDate AND m.status != 'CANCELLED'")
    Long getTotalRevenueByPeriod(@Param("startDate") LocalDate startDate, 
                                @Param("endDate") LocalDate endDate);

    // Buscar por fecha de vencimiento y estado (método legacy mejorado)
    List<Membership> findByEndDateBeforeAndStatus(LocalDate date, MembershipStatus status);

    // Buscar membresías por rango de fechas de inicio
    List<Membership> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    // Obtener todas las membresías activas
    List<Membership> findByStatus(MembershipStatus status);

    // Buscar membresías que inician hoy
    @Query("SELECT m FROM Membership m WHERE m.startDate = :today")
    List<Membership> findMembershipsStartingToday(@Param("today") LocalDate today);

    // Obtener la membresía más reciente de un usuario
    @Query("SELECT m FROM Membership m WHERE m.user = :user ORDER BY m.createdAt DESC")
    List<Membership> findLatestMembershipsByUser(@Param("user") User user);
}