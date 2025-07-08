package com.gymmanagement.gymapp.controller.admin;

import com.gymmanagement.gymapp.model.*;
import com.gymmanagement.gymapp.service.MembershipService;
import com.gymmanagement.gymapp.service.MembershipPlanService;
import com.gymmanagement.gymapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/memberships")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private MembershipPlanService membershipPlanService;

    @Autowired
    private UserService userService;

    /**
     * Lista todas las membresías con paginación y filtros
     */
    @GetMapping
    public String listMemberships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String userEmail,
            @RequestParam(required = false) MembershipStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {

        // Configurar paginación y ordenamiento
        Sort sort = sortDir.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Obtener membresías con filtros
        Page<Membership> memberships;
        if (userEmail != null || status != null || startDate != null || endDate != null) {
            memberships = membershipService.searchMemberships(userEmail, status, startDate, endDate, pageable);
        } else {
            memberships = membershipService.getAllMemberships(pageable);
        }

        // Obtener estadísticas
        MembershipService.MembershipStats stats = membershipService.getMembershipStats();

        // Obtener membresías que expiran pronto
        List<Membership> expiringMemberships = membershipService.getMembershipsExpiringInDays(7);

        model.addAttribute("memberships", memberships);
        model.addAttribute("stats", stats);
        model.addAttribute("expiringMemberships", expiringMemberships);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", memberships.getTotalPages());
        model.addAttribute("totalElements", memberships.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("userEmail", userEmail);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("membershipStatuses", MembershipStatus.values());

        return "admin/memberships/list-memberships";
    }

    /**
     * Muestra el formulario para crear una nueva membresía
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<User> users = userService.findAllUsers();
        List<MembershipPlan> plans = membershipPlanService.findAllPlans();

        model.addAttribute("users", users);
        model.addAttribute("plans", plans);
        model.addAttribute("paymentMethods", PaymentMethod.values());
        model.addAttribute("membership", new Membership());

        return "admin/memberships/membership-form";
    }

    /**
     * Crea una nueva membresía
     */
    @PostMapping("/create")
    public String createMembership(
            @RequestParam Long userId,
            @RequestParam Long planId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam PaymentMethod paymentMethod,
            @RequestParam(required = false) BigDecimal amount,
            RedirectAttributes redirectAttributes) {

        try {
            Membership membership = membershipService.createMembership(userId, planId, startDate, paymentMethod, amount);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Membresía creada exitosamente para el usuario ID: " + userId);
            return "redirect:/admin/memberships/" + membership.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al crear la membresía: " + e.getMessage());
            return "redirect:/admin/memberships/new";
        }
    }

    /**
     * Muestra los detalles de una membresía específica
     */
    @GetMapping("/{id}")
    public String showMembershipDetails(@PathVariable Long id, Model model) {
        Optional<Membership> membershipOpt = membershipService.getMembershipById(id);
        
        if (membershipOpt.isPresent()) {
            Membership membership = membershipOpt.get();
            List<Membership> userMemberships = membershipService.getMembershipsByUser(membership.getUser());
            
            model.addAttribute("membership", membership);
            model.addAttribute("userMemberships", userMemberships);
            
            return "admin/memberships/membership-details";
        } else {
            return "redirect:/admin/memberships?error=notfound";
        }
    }

    /**
     * Muestra el formulario para editar una membresía
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Membership> membershipOpt = membershipService.getMembershipById(id);
        
        if (membershipOpt.isPresent()) {
            List<User> users = userService.findAllUsers();
            List<MembershipPlan> plans = membershipPlanService.findAllPlans();

            model.addAttribute("membership", membershipOpt.get());
            model.addAttribute("users", users);
            model.addAttribute("plans", plans);
            model.addAttribute("paymentMethods", PaymentMethod.values());
            model.addAttribute("membershipStatuses", MembershipStatus.values());

            return "admin/memberships/membership-edit";
        } else {
            return "redirect:/admin/memberships?error=notfound";
        }
    }

    /**
     * Renueva una membresía
     */
    @PostMapping("/{id}/renew")
    public String renewMembership(
            @PathVariable Long id,
            @RequestParam PaymentMethod paymentMethod,
            @RequestParam(required = false) BigDecimal amount,
            RedirectAttributes redirectAttributes) {

        try {
            Membership newMembership = membershipService.renewMembership(id, paymentMethod, amount);
            redirectAttributes.addFlashAttribute("successMessage", 
                "Membresía renovada exitosamente. Nueva membresía ID: " + newMembership.getId());
            return "redirect:/admin/memberships/" + newMembership.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al renovar la membresía: " + e.getMessage());
            return "redirect:/admin/memberships/" + id;
        }
    }

    /**
     * Cancela una membresía
     */
    @PostMapping("/{id}/cancel")
    public String cancelMembership(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            membershipService.cancelMembership(id);
            redirectAttributes.addFlashAttribute("successMessage", "Membresía cancelada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cancelar la membresía: " + e.getMessage());
        }
        return "redirect:/admin/memberships/" + id;
    }

    /**
     * Suspende una membresía
     */
    @PostMapping("/{id}/suspend")
    public String suspendMembership(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            membershipService.suspendMembership(id);
            redirectAttributes.addFlashAttribute("successMessage", "Membresía suspendida exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al suspender la membresía: " + e.getMessage());
        }
        return "redirect:/admin/memberships/" + id;
    }

    /**
     * Reactiva una membresía suspendida
     */
    @PostMapping("/{id}/reactivate")
    public String reactivateMembership(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            membershipService.reactivateMembership(id);
            redirectAttributes.addFlashAttribute("successMessage", "Membresía reactivada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al reactivar la membresía: " + e.getMessage());
        }
        return "redirect:/admin/memberships/" + id;
    }

    /**
     * Actualiza estados de membresías expiradas
     */
    @PostMapping("/update-expired")
    public String updateExpiredMemberships(RedirectAttributes redirectAttributes) {
        try {
            int updatedCount = membershipService.updateExpiredMemberships();
            redirectAttributes.addFlashAttribute("successMessage", 
                "Se actualizaron " + updatedCount + " membresías expiradas");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", 
                "Error al actualizar membresías expiradas: " + e.getMessage());
        }
        return "redirect:/admin/memberships";
    }

    /**
     * Busca usuarios por email (AJAX)
     */
    @GetMapping("/search-users")
    @ResponseBody
    public List<User> searchUsers(@RequestParam String query) {
        return userService.searchUsersByEmail(query);
    }

    /**
     * Dashboard de membresías (página principal)
     */
    @GetMapping("/dashboard")
    public String membershipDashboard(Model model) {
        MembershipService.MembershipStats stats = membershipService.getMembershipStats();
        List<Membership> expiringMemberships = membershipService.getMembershipsExpiringInDays(30);
        List<Membership> recentMemberships = membershipService.getAllMemberships(
            PageRequest.of(0, 5, Sort.by("createdAt").descending())).getContent();

        model.addAttribute("stats", stats);
        model.addAttribute("expiringMemberships", expiringMemberships);
        model.addAttribute("recentMemberships", recentMemberships);

        return "admin/memberships/dashboard";
    }
}