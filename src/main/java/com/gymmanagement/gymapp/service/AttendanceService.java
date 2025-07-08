package com.gymmanagement.gymapp.service;

import com.gymmanagement.gymapp.model.Attendance;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.repository.AttendanceRepository;
import com.gymmanagement.gymapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de asistencia al gimnasio.
 * Maneja el check-in, check-out y consultas de asistencia.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository, UserRepository userRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
    }

    // --- Métodos de Check-in/Check-out ---

    /**
     * Realiza el check-in de un usuario al gimnasio.
     */
    @Transactional
    public Attendance checkIn(Long userId) {
        User user = findUserById(userId);
        
        // Verificar si el usuario ya tiene un check-in activo
        Optional<Attendance> activeAttendance = attendanceRepository.findByUserAndCheckOutTimeIsNull(user);
        if (activeAttendance.isPresent()) {
            throw new RuntimeException("El usuario ya está registrado en el gimnasio");
        }

        Attendance attendance = new Attendance(user, LocalDateTime.now());
        return attendanceRepository.save(attendance);
    }

    /**
     * Realiza el check-in de un usuario por su username.
     */
    @Transactional
    public Attendance checkInByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        return checkIn(user.getId());
    }

    /**
     * Realiza el check-out de un usuario del gimnasio.
     */
    @Transactional
    public Attendance checkOut(Long userId) {
        User user = findUserById(userId);
        
        Attendance activeAttendance = attendanceRepository.findByUserAndCheckOutTimeIsNull(user)
                .orElseThrow(() -> new RuntimeException("El usuario no tiene un check-in activo"));

        activeAttendance.checkOut();
        return attendanceRepository.save(activeAttendance);
    }

    /**
     * Realiza el check-out de un usuario por su username.
     */
    @Transactional
    public Attendance checkOutByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
        return checkOut(user.getId());
    }

    /**
     * Alterna entre check-in y check-out según el estado actual del usuario.
     */
    @Transactional
    public Attendance toggleAttendance(Long userId) {
        User user = findUserById(userId);
        Optional<Attendance> activeAttendance = attendanceRepository.findByUserAndCheckOutTimeIsNull(user);
        
        if (activeAttendance.isPresent()) {
            return checkOut(userId);
        } else {
            return checkIn(userId);
        }
    }

    // --- Métodos de Consulta ---

    /**
     * Obtiene todas las asistencias con paginación.
     */
    public Page<Attendance> findAllAttendances(Pageable pageable) {
        return attendanceRepository.findAll(pageable);
    }

    /**
     * Busca asistencias por término de búsqueda.
     */
    public Page<Attendance> searchAttendances(String keyword, Pageable pageable) {
        return attendanceRepository.searchAttendances(keyword, pageable);
    }

    /**
     * Obtiene asistencias en un rango de fechas.
     */
    public Page<Attendance> findAttendancesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return attendanceRepository.findByDateRange(startDate, endDate, pageable);
    }

    /**
     * Obtiene las asistencias de un usuario específico.
     */
    public Page<Attendance> findUserAttendances(Long userId, Pageable pageable) {
        User user = findUserById(userId);
        return attendanceRepository.findByUserOrderByCheckInTimeDesc(user, pageable);
    }

    /**
     * Obtiene los usuarios actualmente en el gimnasio.
     */
    public List<Attendance> getActiveAttendances() {
        return attendanceRepository.findActiveAttendances();
    }

    /**
     * Cuenta los usuarios actualmente en el gimnasio.
     */
    public long countActiveUsers() {
        return attendanceRepository.countActiveAttendances();
    }

    /**
     * Verifica si un usuario está actualmente en el gimnasio.
     */
    public boolean isUserInGym(Long userId) {
        User user = findUserById(userId);
        return attendanceRepository.findByUserAndCheckOutTimeIsNull(user).isPresent();
    }

    // --- Métodos de Estadísticas ---

    /**
     * Obtiene estadísticas diarias de asistencia.
     */
    public List<Object[]> getDailyAttendanceStats(LocalDateTime startDate, LocalDateTime endDate) {
        return attendanceRepository.getDailyAttendanceStats(startDate, endDate);
    }

    /**
     * Obtiene estadísticas por hora de un día específico.
     */
    public List<Object[]> getHourlyAttendanceStats(LocalDateTime date) {
        return attendanceRepository.getHourlyAttendanceStats(date);
    }

    /**
     * Obtiene el promedio de duración de visitas.
     */
    public Double getAverageVisitDuration(LocalDateTime startDate, LocalDateTime endDate) {
        return attendanceRepository.getAverageVisitDurationInMinutes(startDate, endDate);
    }

    /**
     * Obtiene los usuarios más frecuentes.
     */
    public List<Object[]> getMostFrequentUsers(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return attendanceRepository.getMostFrequentUsers(startDate, endDate, pageable);
    }

    // --- Métodos de CRUD ---

    /**
     * Obtiene una asistencia por ID.
     */
    public Optional<Attendance> findAttendanceById(Long id) {
        return attendanceRepository.findById(id);
    }

    /**
     * Actualiza una asistencia existente.
     */
    @Transactional
    public Attendance updateAttendance(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    /**
     * Elimina una asistencia.
     */
    @Transactional
    public void deleteAttendance(Long id) {
        attendanceRepository.deleteById(id);
    }

    // --- Métodos de utilidad privados ---

    /**
     * Busca un usuario por ID o lanza excepción si no existe.
     */
    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));
    }
}