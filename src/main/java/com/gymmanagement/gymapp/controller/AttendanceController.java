package com.gymmanagement.gymapp.controller;

import com.gymmanagement.gymapp.config.AppConstants;
import com.gymmanagement.gymapp.model.Attendance;
import com.gymmanagement.gymapp.service.AttendanceService;
import com.gymmanagement.gymapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Controlador para la gestión de asistencia al gimnasio.
 * Maneja el control de acceso y los registros de asistencia.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final UserService userService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, UserService userService) {
        this.attendanceService = attendanceService;
        this.userService = userService;
    }

    /**
     * Muestra la lista de asistencias con paginación y búsqueda.
     */
    @GetMapping
    public String listAttendances(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "checkInTime,desc") String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            HttpServletRequest request) {

        Pageable pageable = createPageable(page, size, sort);
        Page<Attendance> attendancePage;

        // Filtrar por rango de fechas si se proporcionan
        if (startDate != null && !startDate.isEmpty() && endDate != null && !endDate.isEmpty()) {
            LocalDateTime start = LocalDateTime.parse(startDate + "T00:00:00");
            LocalDateTime end = LocalDateTime.parse(endDate + "T23:59:59");
            attendancePage = attendanceService.findAttendancesByDateRange(start, end, pageable);
        } else if (keyword != null && !keyword.trim().isEmpty()) {
            attendancePage = attendanceService.searchAttendances(keyword.trim(), pageable);
        } else {
            attendancePage = attendanceService.findAllAttendances(pageable);
        }

        populateListModel(model, attendancePage, size, sort, keyword, startDate, endDate, request);
        
        // Agregar estadísticas adicionales
        model.addAttribute("activeUsersCount", attendanceService.countActiveUsers());
        model.addAttribute("activeAttendances", attendanceService.getActiveAttendances());

        return AppConstants.Views.ADMIN_ATTENDANCE_LIST;
    }

    /**
     * Realiza check-in de un usuario.
     */
    @PostMapping("/checkin")
    public String checkIn(@RequestParam Long userId, 
                         RedirectAttributes redirectAttributes) {
        try {
            Attendance attendance = attendanceService.checkIn(userId);
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, 
                "Check-in realizado exitosamente para " + attendance.getUser().getFullName());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, 
                "Error en check-in: " + e.getMessage());
        }
        return AppConstants.Redirects.ADMIN_ATTENDANCE;
    }

    /**
     * Realiza check-in por username.
     */
    @PostMapping("/checkin-username")
    public String checkInByUsername(@RequestParam String username, 
                                   RedirectAttributes redirectAttributes) {
        try {
            Attendance attendance = attendanceService.checkInByUsername(username);
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, 
                "Check-in realizado exitosamente para " + attendance.getUser().getFullName());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, 
                "Error en check-in: " + e.getMessage());
        }
        return AppConstants.Redirects.ADMIN_ATTENDANCE;
    }

    /**
     * Realiza check-out de un usuario.
     */
    @PostMapping("/checkout")
    public String checkOut(@RequestParam Long userId, 
                          RedirectAttributes redirectAttributes) {
        try {
            Attendance attendance = attendanceService.checkOut(userId);
            long duration = attendance.getDurationInMinutes();
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, 
                "Check-out realizado exitosamente para " + attendance.getUser().getFullName() + 
                ". Duración de la visita: " + duration + " minutos");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, 
                "Error en check-out: " + e.getMessage());
        }
        return AppConstants.Redirects.ADMIN_ATTENDANCE;
    }

    /**
     * Realiza check-out por username.
     */
    @PostMapping("/checkout-username")
    public String checkOutByUsername(@RequestParam String username, 
                                    RedirectAttributes redirectAttributes) {
        try {
            Attendance attendance = attendanceService.checkOutByUsername(username);
            long duration = attendance.getDurationInMinutes();
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, 
                "Check-out realizado exitosamente para " + attendance.getUser().getFullName() + 
                ". Duración de la visita: " + duration + " minutos");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, 
                "Error en check-out: " + e.getMessage());
        }
        return AppConstants.Redirects.ADMIN_ATTENDANCE;
    }

    /**
     * Alterna entre check-in y check-out automáticamente.
     */
    @PostMapping("/toggle/{userId}")
    public String toggleAttendance(@PathVariable Long userId, 
                                  RedirectAttributes redirectAttributes) {
        try {
            Attendance attendance = attendanceService.toggleAttendance(userId);
            String action = attendance.isStillInGym() ? "Check-in" : "Check-out";
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, 
                action + " realizado exitosamente para " + attendance.getUser().getFullName());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, 
                "Error: " + e.getMessage());
        }
        return AppConstants.Redirects.ADMIN_ATTENDANCE;
    }

    /**
     * Elimina un registro de asistencia.
     */
    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Long id, 
                                  RedirectAttributes redirectAttributes) {
        try {
            attendanceService.deleteAttendance(id);
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, 
                "Registro de asistencia eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, 
                "Error al eliminar registro: " + e.getMessage());
        }
        return AppConstants.Redirects.ADMIN_ATTENDANCE;
    }

    /**
     * API endpoint para obtener el estado de un usuario (en el gimnasio o no).
     */
    @GetMapping("/status/{userId}")
    @ResponseBody
    public boolean getUserStatus(@PathVariable Long userId) {
        return attendanceService.isUserInGym(userId);
    }

    /**
     * API endpoint para obtener estadísticas de asistencia.
     */
    @GetMapping("/stats")
    @ResponseBody
    public Object getAttendanceStats(@RequestParam(required = false) String period) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(7); // Última semana por defecto
        
        if ("month".equals(period)) {
            startDate = endDate.minusMonths(1);
        } else if ("year".equals(period)) {
            startDate = endDate.minusYears(1);
        }

        return attendanceService.getDailyAttendanceStats(startDate, endDate);
    }

    // --- Métodos privados de utilidad ---

    /**
     * Crea un objeto Pageable basado en los parámetros de paginación.
     */
    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1].toUpperCase());
        
        return PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy));
    }

    /**
     * Popula el modelo para la vista de lista de asistencias.
     */
    private void populateListModel(Model model, Page<Attendance> attendancePage, int size, String sort, 
                                 String keyword, String startDate, String endDate, HttpServletRequest request) {
        String[] sortParams = sort.split(",");
        
        model.addAttribute("attendances", attendancePage.getContent());
        model.addAttribute(AppConstants.ModelAttributes.CURRENT_PAGE, attendancePage.getNumber() + 1);
        model.addAttribute(AppConstants.ModelAttributes.TOTAL_PAGES, attendancePage.getTotalPages());
        model.addAttribute(AppConstants.ModelAttributes.TOTAL_ITEMS, attendancePage.getTotalElements());
        model.addAttribute(AppConstants.ModelAttributes.PAGE_SIZE, size);
        model.addAttribute(AppConstants.ModelAttributes.SORT_FIELD, sortParams[0]);
        model.addAttribute(AppConstants.ModelAttributes.SORT_DIRECTION, sortParams[1].toLowerCase());
        model.addAttribute(AppConstants.ModelAttributes.KEYWORD, keyword);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute(AppConstants.ModelAttributes.CURRENT_URI, request.getRequestURI());
        
        // Agregar lista de usuarios para el selector de check-in
        model.addAttribute("allUsers", userService.findAllActiveUsers());
    }
}