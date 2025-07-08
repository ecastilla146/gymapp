package com.gymmanagement.gymapp.model;

/**
 * Enumeración que define las categorías de un ticket de soporte.
 * 
 * @author Gym Management System
 * @version 1.0
 */
public enum TicketCategory {
    TECHNICAL("Técnico"),
    BILLING("Facturación"),
    MEMBERSHIP("Membresía"),
    EQUIPMENT("Equipamiento"),
    FACILITIES("Instalaciones"),
    CLASSES("Clases"),
    GENERAL("General"),
    COMPLAINT("Queja"),
    SUGGESTION("Sugerencia");

    private final String displayName;

    TicketCategory(String displayName) {
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