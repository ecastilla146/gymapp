package com.gymmanagement.gymapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String redirectToDashboard() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        return "admin/dashboard";
    }

    @GetMapping("/attendance")
    public String redirectToAttendance() {
        return "redirect:/admin/attendance";
    }

    @GetMapping("/classes")
    public String redirectToClasses() {
        return "redirect:/admin/classes";
    }

    @GetMapping("/staff")
    public String redirectToStaff() {
        return "redirect:/admin/staff";
    }

    @GetMapping("/inventory")
    public String redirectToInventory() {
        return "redirect:/admin/inventory";
    }

    @GetMapping("/finance")
    public String redirectToFinance() {
        return "redirect:/admin/finance";
    }

    @GetMapping("/communication")
    public String redirectToCommunication() {
        return "redirect:/admin/communication";
    }
}
