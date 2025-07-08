package com.gymmanagement.gymapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
