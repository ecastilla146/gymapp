package com.gymmanagement.gymapp.repository;

import com.gymmanagement.gymapp.model.Attendance;
import com.gymmanagement.gymapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de registros de asistencia.
 * Proporciona métodos de consulta especializados para el control de acceso.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * Busca la asistencia activa (sin check-out) de un usuario.
     */
    Optional<Attendance> findByUserAndCheckOutTimeIsNull(User user);

    /**
     * Obtiene todas las asistencias de un usuario ordenadas por fecha de entrada.
     */
    Page<Attendance> findByUserOrderByCheckInTimeDesc(User user, Pageable pageable);

    /**
     * Obtiene las asistencias en un rango de fechas.
     */
    @Query("SELECT a FROM Attendance a WHERE a.checkInTime BETWEEN :startDate AND :endDate ORDER BY a.checkInTime DESC")
    Page<Attendance> findByDateRange(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate, 
                                   Pageable pageable);

    /**
     * Obtiene los usuarios actualmente en el gimnasio.
     */
    @Query("SELECT a FROM Attendance a WHERE a.checkOutTime IS NULL ORDER BY a.checkInTime DESC")
    List<Attendance> findActiveAttendances();

    /**
     * Cuenta las asistencias activas (usuarios en el gimnasio).
     */
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.checkOutTime IS NULL")
    long countActiveAttendances();

    /**
     * Obtiene las asistencias diarias para estadísticas.
     */
    @Query(value = "SELECT DATE(check_in_time) as date, COUNT(*) as count " +
           "FROM attendance " +
           "WHERE check_in_time BETWEEN ?1 AND ?2 " +
           "GROUP BY DATE(check_in_time) " +
           "ORDER BY date", nativeQuery = true)
    List<Object[]> getDailyAttendanceStats(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Obtiene las asistencias por hora para estadísticas.
     */
    @Query(value = "SELECT HOUR(check_in_time) as hour, COUNT(*) as count " +
           "FROM attendance " +
           "WHERE DATE(check_in_time) = DATE(?1) " +
           "GROUP BY HOUR(check_in_time) " +
           "ORDER BY hour", nativeQuery = true)
    List<Object[]> getHourlyAttendanceStats(@Param("date") LocalDateTime date);

    /**
     * Busca asistencias por término de búsqueda (nombre o apellido del usuario).
     */
    @Query("SELECT a FROM Attendance a " +
           "WHERE LOWER(a.user.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.user.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "OR LOWER(a.user.username) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
           "ORDER BY a.checkInTime DESC")
    Page<Attendance> searchAttendances(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Obtiene el promedio de duración de visitas en minutos.
     */
    @Query(value = "SELECT AVG(TIME_TO_SEC(TIMEDIFF(check_out_time, check_in_time)) / 60) " +
           "FROM attendance " +
           "WHERE check_out_time IS NOT NULL " +
           "AND check_in_time BETWEEN ?1 AND ?2", nativeQuery = true)
    Double getAverageVisitDurationInMinutes(@Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * Obtiene los usuarios más frecuentes.
     */
    @Query("SELECT a.user, COUNT(a) as visitCount " +
           "FROM Attendance a " +
           "WHERE a.checkInTime BETWEEN :startDate AND :endDate " +
           "GROUP BY a.user " +
           "ORDER BY visitCount DESC")
    List<Object[]> getMostFrequentUsers(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate, 
                                      Pageable pageable);
}