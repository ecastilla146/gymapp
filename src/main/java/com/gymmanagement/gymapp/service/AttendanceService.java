package com.gymmanagement.gymapp.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymmanagement.gymapp.model.Attendance;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.repository.AttendanceRepository;
import com.gymmanagement.gymapp.repository.UserRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Attendance checkIn(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
        
        // Verificar si ya tiene una sesión activa
        List<Attendance> activeSessions = attendanceRepository.findActiveSessionsByUserId(userId);
        if (!activeSessions.isEmpty()) {
            throw new RuntimeException("El usuario ya tiene una sesión activa");
        }
        
        Attendance attendance = new Attendance(user, LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    @Transactional
    public Attendance checkOut(Long userId) {
        List<Attendance> activeSessions = attendanceRepository.findActiveSessionsByUserId(userId);
        if (activeSessions.isEmpty()) {
            throw new RuntimeException("No hay sesión activa para este usuario");
        }
        
        Attendance attendance = activeSessions.get(0);
        attendance.setCheckOutTime(LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public List<Attendance> getAttendanceByUserAndDate(Long userId, LocalDate date) {
        return attendanceRepository.findByUserIdAndDate(userId, date);
    }

    public List<Attendance> getAttendanceByUser(Long userId) {
        return attendanceRepository.findByUserId(userId);
    }

    public Long getAttendanceCountByDate(LocalDate date) {
        return attendanceRepository.countByDate(date);
    }

    public Long getAttendanceCountByUserAndDate(Long userId, LocalDate date) {
        return attendanceRepository.countByUserIdAndDate(userId, date);
    }

    public List<Attendance> getAttendanceBetweenDates(LocalDateTime start, LocalDateTime end) {
        return attendanceRepository.findByCheckInTimeBetween(start, end);
    }

    public List<Attendance> getActiveSessionsByUser(Long userId) {
        return attendanceRepository.findActiveSessionsByUserId(userId);
    }

    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }

    // Método para obtener estadísticas de asistencia
    public AttendanceStats getAttendanceStats(LocalDate date) {
        Long totalAttendance = attendanceRepository.countByDate(date);
        List<Attendance> attendanceList = attendanceRepository.findByDate(date);
        
        long activeSessions = attendanceList.stream()
                .filter(a -> a.getCheckOutTime() == null)
                .count();
        
        return new AttendanceStats(totalAttendance, activeSessions, date);
    }

    // Clase interna para estadísticas
    public static class AttendanceStats {
        private final Long totalAttendance;
        private final Long activeSessions;
        private final LocalDate date;

        public AttendanceStats(Long totalAttendance, Long activeSessions, LocalDate date) {
            this.totalAttendance = totalAttendance;
            this.activeSessions = activeSessions;
            this.date = date;
        }

        public Long getTotalAttendance() {
            return totalAttendance;
        }

        public Long getActiveSessions() {
            return activeSessions;
        }

        public LocalDate getDate() {
            return date;
        }
    }
}