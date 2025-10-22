package com.gymmanagement.gymapp.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gymmanagement.gymapp.model.Attendance;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.service.AttendanceService;
import com.gymmanagement.gymapp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showAttendanceControl(HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        model.addAttribute("pageTitle", "Kronos Gym - Control de Asistencia");
        model.addAttribute("navbarTitle", "Control de Asistencia");
        
        // Obtener estadísticas del día actual
        LocalDate today = LocalDate.now();
        AttendanceService.AttendanceStats stats = attendanceService.getAttendanceStats(today);
        model.addAttribute("todayStats", stats);
        
        // Obtener asistencias del día
        List<Attendance> todayAttendance = attendanceService.getAttendanceByDate(today);
        model.addAttribute("todayAttendance", todayAttendance);
        
        // Obtener usuarios activos para el formulario de check-in
        List<User> activeUsers = userService.findActiveUsers();
        model.addAttribute("activeUsers", activeUsers);
        
        return "admin/attendance/attendance-control";
    }

    @PostMapping("/checkin")
    public String checkIn(@RequestParam("userId") Long userId, RedirectAttributes redirectAttributes) {
        try {
            Attendance attendance = attendanceService.checkIn(userId);
            redirectAttributes.addFlashAttribute("success", "Check-in realizado exitosamente para el usuario ID: " + userId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al realizar check-in: " + e.getMessage());
        }
        return "redirect:/admin/attendance";
    }

    @PostMapping("/checkout")
    public String checkOut(@RequestParam("userId") Long userId, RedirectAttributes redirectAttributes) {
        try {
            Attendance attendance = attendanceService.checkOut(userId);
            redirectAttributes.addFlashAttribute("success", "Check-out realizado exitosamente para el usuario ID: " + userId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al realizar check-out: " + e.getMessage());
        }
        return "redirect:/admin/attendance";
    }

    @GetMapping("/report")
    public String showAttendanceReport(@RequestParam(value = "date", required = false) String dateStr, 
                                     HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        model.addAttribute("pageTitle", "Kronos Gym - Reporte de Asistencia");
        model.addAttribute("navbarTitle", "Reporte de Asistencia");
        
        LocalDate date = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();
        List<Attendance> attendanceList = attendanceService.getAttendanceByDate(date);
        AttendanceService.AttendanceStats stats = attendanceService.getAttendanceStats(date);
        
        model.addAttribute("attendanceList", attendanceList);
        model.addAttribute("stats", stats);
        model.addAttribute("selectedDate", date);
        
        return "admin/attendance/attendance-report";
    }

    @GetMapping("/user")
    public String showUserAttendance(@RequestParam("userId") Long userId, 
                                   @RequestParam(value = "date", required = false) String dateStr,
                                   HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        model.addAttribute("pageTitle", "Kronos Gym - Asistencia de Usuario");
        model.addAttribute("navbarTitle", "Asistencia de Usuario");
        
        LocalDate date = dateStr != null ? LocalDate.parse(dateStr) : LocalDate.now();
        List<Attendance> userAttendance = attendanceService.getAttendanceByUserAndDate(userId, date);
        
        User user = userService.findById(userId).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("userAttendance", userAttendance);
        model.addAttribute("selectedDate", date);
        
        return "admin/attendance/user-attendance";
    }
}