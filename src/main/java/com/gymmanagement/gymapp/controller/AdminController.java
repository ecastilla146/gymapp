package com.gymmanagement.gymapp.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gymmanagement.gymapp.model.Membership;
import com.gymmanagement.gymapp.model.MembershipStatus;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.service.AttendanceService;
import com.gymmanagement.gymapp.service.MembershipService;
import com.gymmanagement.gymapp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public String redirectToDashboard() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        model.addAttribute("pageTitle", "Kronos Gym - Panel de Administrador");
        model.addAttribute("navbarTitle", "Panel de Control del Administrador");

        // Obtener estadísticas reales de la base de datos
        LocalDate today = LocalDate.now();
        
        // Estadísticas de usuarios
        List<User> allUsers = userService.findAllUsers();
        long totalUsers = allUsers.size();
        long activeUsers = allUsers.stream().filter(User::isEnabled).count();
        
        // Estadísticas de membresías
        List<Membership> allMemberships = membershipService.findAllMemberships();
        long totalMemberships = allMemberships.size();
        long activeMemberships = allMemberships.stream()
                .filter(m -> m.getStatus() == MembershipStatus.ACTIVE)
                .count();
        
        // Ingresos mensuales (suma de todas las membresías activas)
        BigDecimal monthlyRevenue = allMemberships.stream()
                .filter(m -> m.getStatus() == MembershipStatus.ACTIVE)
                .map(Membership::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // Estadísticas de asistencia
        AttendanceService.AttendanceStats todayStats = attendanceService.getAttendanceStats(today);
        long todayAttendance = todayStats.getTotalAttendance();
        long activeSessions = todayStats.getActiveSessions();
        
        // Actividad reciente (últimas 5 membresías)
        List<Membership> recentMemberships = allMemberships.stream()
                .sorted((m1, m2) -> m2.getCreatedAt().compareTo(m1.getCreatedAt()))
                .limit(5)
                .collect(Collectors.toList());
        
        // Datos para gráficos
        List<BigDecimal> monthlyEarnings = List.of(
            BigDecimal.valueOf(12000), BigDecimal.valueOf(14000), BigDecimal.valueOf(13500),
            BigDecimal.valueOf(16000), BigDecimal.valueOf(15000), BigDecimal.valueOf(17000)
        );
        
        List<Long> dailyAttendance = List.of(100L, 110L, 95L, 130L, 120L, 80L, 50L);
        
        // Agregar datos al modelo
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("activeUsers", activeUsers);
        model.addAttribute("totalMemberships", totalMemberships);
        model.addAttribute("activeMemberships", activeMemberships);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        model.addAttribute("todayAttendance", todayAttendance);
        model.addAttribute("activeSessions", activeSessions);
        model.addAttribute("recentMemberships", recentMemberships);
        model.addAttribute("monthlyEarnings", monthlyEarnings);
        model.addAttribute("dailyAttendance", dailyAttendance);
        
        return "admin/dashboard";
    }
}
