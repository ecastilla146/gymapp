package com.gymmanagement.gymapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.gymmanagement.gymapp.model.MembershipStatus;
import com.gymmanagement.gymapp.model.PaymentMethod;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public class MembershipDto {

    private Long id; // Para edición

    @NotNull(message = "El usuario no puede estar vacío.")
    private Long userId; // ID del usuario, no el objeto completo para el DTO

    @NotNull(message = "El plan de membresía no puede estar vacío.")
    private Long membershipPlanId; // ID del plan, no el objeto completo para el DTO

    @NotNull(message = "La fecha de inicio no puede estar vacía.")
    @PastOrPresent(message = "La fecha de inicio no puede ser futura.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "La fecha de vencimiento no puede estar vacía.")
    @FutureOrPresent(message = "La fecha de vencimiento no puede ser pasada.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "El monto no puede estar vacío.")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0.")
    private BigDecimal amount;

    @NotNull(message = "El estado no puede estar vacío.")
    private MembershipStatus status;

    @NotNull(message = "El método de pago no puede estar vacío.")
    private PaymentMethod paymentMethod;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMembershipPlanId() {
        return membershipPlanId;
    }

    public void setMembershipPlanId(Long membershipPlanId) {
        this.membershipPlanId = membershipPlanId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    @Override
    public String toString() {
        return "MembershipDto{" +
               "id=" + id +
               ", userId=" + userId +
               ", membershipPlanId=" + membershipPlanId +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", amount=" + amount +
               ", status=" + status +
               ", paymentMethod=" + paymentMethod +
               '}';
    }
}
