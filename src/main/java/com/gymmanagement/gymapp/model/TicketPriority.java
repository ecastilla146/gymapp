package com.gymmanagement.gymapp.model;

/**
 * Enumeración que define las prioridades de un ticket de soporte.
 * 
 * @author Gym Management System
 * @version 1.0
 */
public enum TicketPriority {
    LOW("Baja"),
    MEDIUM("Media"),
    HIGH("Alta"),
    URGENT("Urgente");

    private final String displayName;

    TicketPriority(String displayName) {
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