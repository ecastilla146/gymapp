package com.gymmanagement.gymapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gymmanagement.gymapp.model.Membership;
import com.gymmanagement.gymapp.service.MembershipService;

@Controller
@RequestMapping("/admin/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @GetMapping
    public String listMemberships(Model model) {
        List<Membership> memberships = membershipService.findAllMemberships();
        model.addAttribute("memberships", memberships);
        model.addAttribute("pageTitle", "Kronos Gym - Membresías");
        model.addAttribute("navbarTitle", "Gestión de Membresías");
        model.addAttribute("contentFragment", "admin/memberships/list-memberships");
        return "admin/layout-admin";
    }

    @GetMapping("/new")
    public String showMembershipForm(Model model) {
        model.addAttribute("pageTitle", "Kronos Gym - Nueva Membresía");
        model.addAttribute("navbarTitle", "Nueva Membresía");
        model.addAttribute("contentFragment", "admin/memberships/membership-form");
        return "admin/layout-admin";
    }

    @GetMapping("/details/{id}")
    public String showMembershipDetails(@PathVariable Long id, Model model) {
        // Aquí implementarías la lógica para mostrar detalles de la membresía
        model.addAttribute("pageTitle", "Kronos Gym - Detalles de Membresía");
        model.addAttribute("navbarTitle", "Detalles de Membresía");
        model.addAttribute("contentFragment", "admin/memberships/membership-details");
        return "admin/layout-admin";
    }

    @GetMapping("/edit/{id}")
    public String showEditMembershipForm(@PathVariable Long id, Model model) {
        // Aquí implementarías la lógica para editar membresía
        model.addAttribute("pageTitle", "Kronos Gym - Editar Membresía");
        model.addAttribute("navbarTitle", "Editar Membresía");
        model.addAttribute("contentFragment", "admin/memberships/membership-form");
        return "admin/layout-admin";
    }

    @PostMapping("/save")
    public String saveMembership(RedirectAttributes redirectAttributes) {
        // Aquí implementarías la lógica para guardar membresía
        redirectAttributes.addFlashAttribute("successMessage", "Membresía guardada exitosamente");
        return "redirect:/admin/memberships";
    }

    @PostMapping("/delete/{id}")
    public String deleteMembership(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Aquí implementarías la lógica para eliminar membresía
        redirectAttributes.addFlashAttribute("successMessage", "Membresía eliminada exitosamente");
        return "redirect:/admin/memberships";
    }

    @PostMapping("/renew/{id}")
    public String renewMembership(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        // Aquí implementarías la lógica para renovar membresía
        redirectAttributes.addFlashAttribute("successMessage", "Membresía renovada exitosamente");
        return "redirect:/admin/memberships";
    }

    @GetMapping("/getPlanDuration/{planId}")
    @ResponseBody
    public String getPlanDuration(@PathVariable Long planId) {
        // Aquí implementarías la lógica para obtener la duración del plan
        // Por ahora devolvemos un valor por defecto
        return "30"; // días
    }

    @GetMapping("/getPlanPrice/{planId}")
    @ResponseBody
    public String getPlanPrice(@PathVariable Long planId) {
        // Aquí implementarías la lógica para obtener el precio del plan
        // Por ahora devolvemos un valor por defecto
        return "50.00"; // precio
    }
}
