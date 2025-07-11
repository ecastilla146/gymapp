package com.gymmanagement.gymapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/attendance")
public class AttendanceController {

    @GetMapping
    public String showAttendance(HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        model.addAttribute("pageTitle", "Kronos Gym - Control de Asistencia");
        model.addAttribute("navbarTitle", "Control de Asistencia");
        model.addAttribute("contentFragment", "admin/attendance/attendance-control");
        return "admin/layout-admin";
    }

    @PostMapping("/check-in")
    public String checkIn(@RequestParam String membershipId, RedirectAttributes redirectAttributes) {
        // Aquí implementarías la lógica para registrar entrada
        redirectAttributes.addFlashAttribute("successMessage", "Entrada registrada exitosamente para el miembro: " + membershipId);
        return "redirect:/admin/attendance";
    }

    @PostMapping("/check-out")
    public String checkOut(@RequestParam String membershipId, RedirectAttributes redirectAttributes) {
        // Aquí implementarías la lógica para registrar salida
        redirectAttributes.addFlashAttribute("successMessage", "Salida registrada exitosamente para el miembro: " + membershipId);
        return "redirect:/admin/attendance";
    }

    @GetMapping("/history")
    public String showAttendanceHistory(Model model) {
        model.addAttribute("pageTitle", "Kronos Gym - Historial de Asistencia");
        model.addAttribute("navbarTitle", "Historial de Asistencia");
        model.addAttribute("contentFragment", "admin/attendance/attendance-history");
        return "admin/layout-admin";
    }
}