package com.gymmanagement.gymapp.model;

/**
 * Enumeración que define los posibles estados de un ticket de soporte.
 * 
 * @author Gym Management System
 * @version 1.0
 */
public enum TicketStatus {
    OPEN("Abierto"),
    IN_PROGRESS("En Progreso"),
    RESOLVED("Resuelto"),
    CLOSED("Cerrado");

    private final String displayName;

    TicketStatus(String displayName) {
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