package com.gymmanagement.gymapp.model;

/**
 * Enumeración que define los tipos de transacciones financieras.
 * 
 * @author Gym Management System
 * @version 1.0
 */
public enum TransactionType {
    INCOME("Ingreso"),
    EXPENSE("Gasto");

    private final String displayName;

    TransactionType(String displayName) {
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