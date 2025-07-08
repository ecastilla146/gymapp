package com.gymmanagement.gymapp.model;

/**
 * Enumeración que define los posibles estados de una clase del gimnasio.
 * 
 * @author Gym Management System
 * @version 1.0
 */
public enum ClassStatus {
    SCHEDULED("Programada"),
    IN_PROGRESS("En Progreso"),
    COMPLETED("Completada"),
    CANCELLED("Cancelada");

    private final String displayName;

    ClassStatus(String displayName) {
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