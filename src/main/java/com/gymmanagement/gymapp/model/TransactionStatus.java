package com.gymmanagement.gymapp.model;

/**
 * Enumeración que define los posibles estados de una transacción financiera.
 * 
 * @author Gym Management System
 * @version 1.0
 */
public enum TransactionStatus {
    PENDING("Pendiente"),
    COMPLETED("Completada"),
    CANCELLED("Cancelada"),
    REFUNDED("Reembolsada");

    private final String displayName;

    TransactionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}