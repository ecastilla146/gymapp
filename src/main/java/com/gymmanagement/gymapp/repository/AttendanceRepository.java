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
    @Query("SELECT DATE(a.checkInTime) as date, COUNT(a) as count " +
           "FROM Attendance a " +
           "WHERE a.checkInTime BETWEEN :startDate AND :endDate " +
           "GROUP BY DATE(a.checkInTime) " +
           "ORDER BY date")
    List<Object[]> getDailyAttendanceStats(@Param("startDate") LocalDateTime startDate, 
                                         @Param("endDate") LocalDateTime endDate);

    /**
     * Obtiene las asistencias por hora para estadísticas.
     */
    @Query("SELECT HOUR(a.checkInTime) as hour, COUNT(a) as count " +
           "FROM Attendance a " +
           "WHERE DATE(a.checkInTime) = DATE(:date) " +
           "GROUP BY HOUR(a.checkInTime) " +
           "ORDER BY hour")
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
    @Query("SELECT AVG(TIME_TO_SEC(TIMEDIFF(a.checkOutTime, a.checkInTime)) / 60) " +
           "FROM Attendance a " +
           "WHERE a.checkOutTime IS NOT NULL " +
           "AND a.checkInTime BETWEEN :startDate AND :endDate")
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