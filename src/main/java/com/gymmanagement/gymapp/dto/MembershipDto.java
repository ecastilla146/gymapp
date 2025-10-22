package com.gymmanagement.gymapp.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.gymmanagement.gymapp.model.MembershipStatus;
import com.gymmanagement.gymapp.model.PaymentMethod;

public class MembershipDto {
    
    private Long id;
    private Long userId;
    private Long membershipPlanId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal amount;
    private MembershipStatus status;
    private PaymentMethod paymentMethod;

    // Constructors
    public MembershipDto() {}

    public MembershipDto(Long userId, Long membershipPlanId, LocalDate startDate, LocalDate endDate, 
                       BigDecimal amount, MembershipStatus status, PaymentMethod paymentMethod) {
        this.userId = userId;
        this.membershipPlanId = membershipPlanId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    // Getters and Setters
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
