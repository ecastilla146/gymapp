package com.gymmanagement.gymapp.service;

import com.gymmanagement.gymapp.model.*;
import com.gymmanagement.gymapp.repository.MembershipRepository;
import com.gymmanagement.gymapp.repository.MembershipPlanRepository;
import com.gymmanagement.gymapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MembershipService {

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Obtiene todas las membresías con paginación
     */
    public Page<Membership> getAllMemberships(Pageable pageable) {
        return membershipRepository.findAll(pageable);
    }

    /**
     * Obtiene una membresía por ID
     */
    public Optional<Membership> getMembershipById(Long id) {
        return membershipRepository.findById(id);
    }

    /**
     * Obtiene las membresías de un usuario específico
     */
    public List<Membership> getMembershipsByUser(User user) {
        return membershipRepository.findByUserOrderByStartDateDesc(user);
    }

    /**
     * Obtiene la membresía activa de un usuario
     */
    public Optional<Membership> getActiveMembershipByUser(User user) {
        return membershipRepository.findByUserAndStatus(user, MembershipStatus.ACTIVE)
                .stream().findFirst();
    }

    /**
     * Crea una nueva membresía para un usuario
     */
    public Membership createMembership(Long userId, Long planId, LocalDate startDate, 
                                     PaymentMethod paymentMethod, BigDecimal amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        MembershipPlan plan = membershipPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan de membresía no encontrado"));

        // Verificar si el usuario ya tiene una membresía activa
        Optional<Membership> activeMembership = getActiveMembershipByUser(user);
        if (activeMembership.isPresent()) {
            // Expirar la membresía anterior
            activeMembership.get().setStatus(MembershipStatus.EXPIRED);
            membershipRepository.save(activeMembership.get());
        }

        // Crear nueva membresía
        Membership membership = new Membership();
        membership.setUser(user);
        membership.setMembershipPlan(plan);
        membership.setStartDate(startDate);
        membership.setEndDate(startDate.plusMonths(plan.getDurationMonths()));
        membership.setAmount(amount != null ? amount : plan.getPrice());
        membership.setPaymentMethod(paymentMethod);
        membership.setStatus(MembershipStatus.ACTIVE);
        membership.setCreatedAt(LocalDateTime.now());
        membership.setUpdatedAt(LocalDateTime.now());

        return membershipRepository.save(membership);
    }

    /**
     * Renueva una membresía existente
     */
    public Membership renewMembership(Long membershipId, PaymentMethod paymentMethod, BigDecimal amount) {
        Membership currentMembership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));

        // Crear nueva membresía basada en la actual
        Membership newMembership = new Membership();
        newMembership.setUser(currentMembership.getUser());
        newMembership.setMembershipPlan(currentMembership.getMembershipPlan());
        newMembership.setStartDate(currentMembership.getEndDate().plusDays(1));
        newMembership.setEndDate(newMembership.getStartDate().plusMonths(currentMembership.getMembershipPlan().getDurationMonths()));
        newMembership.setAmount(amount != null ? amount : currentMembership.getMembershipPlan().getPrice());
        newMembership.setPaymentMethod(paymentMethod);
        newMembership.setStatus(MembershipStatus.ACTIVE);

        // Marcar la membresía anterior como expirada
        currentMembership.setStatus(MembershipStatus.EXPIRED);
        membershipRepository.save(currentMembership);

        return membershipRepository.save(newMembership);
    }

    /**
     * Cancela una membresía
     */
    public void cancelMembership(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        
        membership.setStatus(MembershipStatus.CANCELLED);
        membershipRepository.save(membership);
    }

    /**
     * Suspende una membresía
     */
    public void suspendMembership(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        
        membership.setStatus(MembershipStatus.SUSPENDED);
        membershipRepository.save(membership);
    }

    /**
     * Reactiva una membresía suspendida
     */
    public void reactivateMembership(Long membershipId) {
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membresía no encontrada"));
        
        if (membership.getStatus() == MembershipStatus.SUSPENDED) {
            membership.setStatus(MembershipStatus.ACTIVE);
            membershipRepository.save(membership);
        } else {
            throw new RuntimeException("Solo se pueden reactivar membresías suspendidas");
        }
    }

    /**
     * Obtiene estadísticas de membresías
     */
    public MembershipStats getMembershipStats() {
        long totalMemberships = membershipRepository.count();
        long activeMemberships = membershipRepository.countByStatus(MembershipStatus.ACTIVE);
        long expiredMemberships = membershipRepository.countByStatus(MembershipStatus.EXPIRED);
        long suspendedMemberships = membershipRepository.countByStatus(MembershipStatus.SUSPENDED);

        return new MembershipStats(totalMemberships, activeMemberships, expiredMemberships, suspendedMemberships);
    }

    /**
     * Busca membresías por criterios múltiples
     */
    public Page<Membership> searchMemberships(String userEmail, MembershipStatus status, 
                                            LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return membershipRepository.searchMemberships(userEmail, status, startDate, endDate, pageable);
    }

    /**
     * Obtiene membresías que expiran pronto
     */
    public List<Membership> getMembershipsExpiringInDays(int days) {
        LocalDate limitDate = LocalDate.now().plusDays(days);
        return membershipRepository.findMembershipsExpiringBefore(limitDate, MembershipStatus.ACTIVE);
    }

    /**
     * Actualiza estados de membresías expiradas automáticamente
     */
    @Transactional
    public int updateExpiredMemberships() {
        LocalDate today = LocalDate.now();
        return membershipRepository.updateExpiredMemberships(today);
    }

    // Clase interna para estadísticas
    public static class MembershipStats {
        private long total;
        private long active;
        private long expired;
        private long suspended;

        public MembershipStats(long total, long active, long expired, long suspended) {
            this.total = total;
            this.active = active;
            this.expired = expired;
            this.suspended = suspended;
        }

        // Getters
        public long getTotal() { return total; }
        public long getActive() { return active; }
        public long getExpired() { return expired; }
        public long getSuspended() { return suspended; }
    }
}