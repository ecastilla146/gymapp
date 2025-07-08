package com.gymmanagement.gymapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymmanagement.gymapp.dto.MembershipDto; // Necesitamos el UserRepository para encontrar al usuario
import com.gymmanagement.gymapp.model.Membership;
import com.gymmanagement.gymapp.model.MembershipPlan;
import com.gymmanagement.gymapp.model.MembershipStatus;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.repository.MembershipRepository;
import com.gymmanagement.gymapp.repository.UserRepository;

@Service
public class MembershipService {

    private final MembershipRepository membershipRepository;
    private final UserRepository userRepository; // Inyectar UserRepository
    private final MembershipPlanService membershipPlanService; // Inyectar MembershipPlanService

    @Autowired
    public MembershipService(MembershipRepository membershipRepository, UserRepository userRepository, MembershipPlanService membershipPlanService) {
        this.membershipRepository = membershipRepository;
        this.userRepository = userRepository;
        this.membershipPlanService = membershipPlanService;
    }

    public List<Membership> findAllMemberships() {
        return membershipRepository.findAll();
    }

    public Optional<Membership> findMembershipById(Long id) {
        return membershipRepository.findById(id);
    }

    @Transactional
    public Membership saveMembership(MembershipDto membershipDto) {
        Membership membership;
        if (membershipDto.getId() != null) {
            membership = membershipRepository.findById(membershipDto.getId())
                    .orElseThrow(() -> new RuntimeException("Membresía no encontrada con ID: " + membershipDto.getId()));
        } else {
            membership = new Membership();
        }

        User user = userRepository.findById(membershipDto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + membershipDto.getUserId()));

        MembershipPlan plan = membershipPlanService.findPlanById(membershipDto.getMembershipPlanId())
                .orElseThrow(() -> new RuntimeException("Plan de membresía no encontrado con ID: " + membershipDto.getMembershipPlanId()));

        membership.setUser(user);
        membership.setMembershipPlan(plan);
        membership.setStartDate(membershipDto.getStartDate());
        membership.setEndDate(membershipDto.getEndDate());
        membership.setAmount(membershipDto.getAmount());
        membership.setStatus(membershipDto.getStatus());
        membership.setPaymentMethod(membershipDto.getPaymentMethod());

        return membershipRepository.save(membership);
    }

    public void deleteMembership(Long id) {
        membershipRepository.deleteById(id);
    }

    // Método para actualizar el estado de las membresías automáticamente (ejecutar diariamente con @Scheduled)
    @Transactional
    public void updateMembershipStatuses() {
        LocalDate today = LocalDate.now();

        // Marcar membresías como 'EXPIRED' si su fecha de vencimiento es hoy o antes y su estado es ACTIVE
        List<Membership> activeExpiredMemberships = membershipRepository.findByEndDateBeforeAndStatus(today.plusDays(1), MembershipStatus.ACTIVE);
        for (Membership membership : activeExpiredMemberships) {
            if (membership.getEndDate().isBefore(today) || membership.getEndDate().isEqual(today)) { // Confirmar que la fecha de vencimiento es pasada o hoy
                membership.setStatus(MembershipStatus.EXPIRED);
                membershipRepository.save(membership);
            }
        }
        // Puedes añadir más lógica aquí si necesitas cambiar de 'PENDING_PAYMENT' a 'CANCELLED' después de un tiempo, etc.
    }

    // Para la función de renovar (simplificada)
    @Transactional
    public Membership renewMembership(Long membershipId) {
        Membership existingMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada para renovar con ID: " + membershipId));

        MembershipPlan plan = existingMembership.getMembershipPlan();
        if (plan == null) {
            throw new RuntimeException("El plan de la membresía no se encontró.");
        }

        LocalDate newStartDate = LocalDate.now();
        LocalDate newEndDate = newStartDate.plusMonths(plan.getDurationMonths());

        existingMembership.setStartDate(newStartDate);
        existingMembership.setEndDate(newEndDate);
        existingMembership.setStatus(MembershipStatus.ACTIVE);
        // También podrías querer actualizar el monto si el precio del plan ha cambiado
        existingMembership.setAmount(plan.getPrice());
        // Opcional: Cambiar método de pago a algo por defecto o requerir uno nuevo

        return membershipRepository.save(existingMembership);
    }
}