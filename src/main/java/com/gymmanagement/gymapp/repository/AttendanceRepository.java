package com.gymmanagement.gymapp.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymmanagement.gymapp.model.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    List<Attendance> findByUserId(Long userId);
    
    List<Attendance> findByCheckInTimeBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT a FROM Attendance a WHERE a.user.id = :userId AND DATE(a.checkInTime) = :date")
    List<Attendance> findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Attendance a WHERE DATE(a.checkInTime) = :date")
    List<Attendance> findByDate(@Param("date") LocalDate date);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE DATE(a.checkInTime) = :date")
    Long countByDate(@Param("date") LocalDate date);
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.user.id = :userId AND DATE(a.checkInTime) = :date")
    Long countByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    @Query("SELECT a FROM Attendance a WHERE a.user.id = :userId AND a.checkOutTime IS NULL ORDER BY a.checkInTime DESC")
    List<Attendance> findActiveSessionsByUserId(@Param("userId") Long userId);
}