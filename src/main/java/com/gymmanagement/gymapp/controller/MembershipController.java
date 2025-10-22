package com.gymmanagement.gymapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gymmanagement.gymapp.dto.MembershipDto;
import com.gymmanagement.gymapp.model.Membership;
import com.gymmanagement.gymapp.model.MembershipPlan;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.service.MembershipPlanService;
import com.gymmanagement.gymapp.service.MembershipService;
import com.gymmanagement.gymapp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/admin/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private UserService userService;

    @Autowired
    private MembershipPlanService membershipPlanService;

    @GetMapping
    public String listMemberships(HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        model.addAttribute("pageTitle", "Kronos Gym - Membresías");
        model.addAttribute("navbarTitle", "Gestión de Membresías");
        
        List<Membership> memberships = membershipService.findAllMemberships();
        model.addAttribute("memberships", memberships);
        
        return "admin/memberships/list-memberships";
    }

    @GetMapping("/new")
    public String showMembershipForm(HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        model.addAttribute("pageTitle", "Kronos Gym - Nueva Membresía");
        model.addAttribute("navbarTitle", "Nueva Membresía");
        
        List<User> users = userService.findAllUsers();
        List<MembershipPlan> plans = membershipPlanService.findAllPlans();
        
        model.addAttribute("users", users);
        model.addAttribute("plans", plans);
        model.addAttribute("membershipDto", new MembershipDto());
        
        return "admin/memberships/membership-form";
    }

    @PostMapping("/save")
    public String saveMembership(MembershipDto membershipDto, RedirectAttributes redirectAttributes) {
        try {
            membershipService.saveMembership(membershipDto);
            redirectAttributes.addFlashAttribute("success", "Membresía guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar membresía: " + e.getMessage());
        }
        return "redirect:/admin/memberships";
    }

    @PostMapping("/renew")
    public String renewMembership(@RequestParam("membershipId") Long membershipId, RedirectAttributes redirectAttributes) {
        try {
            membershipService.renewMembership(membershipId);
            redirectAttributes.addFlashAttribute("success", "Membresía renovada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al renovar membresía: " + e.getMessage());
        }
        return "redirect:/admin/memberships";
    }

    @GetMapping("/details")
    public String showMembershipDetails(@RequestParam("id") Long id, HttpServletRequest request, Model model) {
        String currentUri = request.getRequestURI();
        model.addAttribute("currentUri", currentUri);
        model.addAttribute("pageTitle", "Kronos Gym - Detalles de Membresía");
        model.addAttribute("navbarTitle", "Detalles de Membresía");
        
        Membership membership = membershipService.findMembershipById(id).orElse(null);
        model.addAttribute("membership", membership);
        
        return "admin/memberships/membership-details";
    }
}
