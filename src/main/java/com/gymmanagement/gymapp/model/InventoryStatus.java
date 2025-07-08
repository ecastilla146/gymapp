package com.gymmanagement.gymapp.model;

/**
 * Enumeración que define los posibles estados de un item de inventario.
 * 
 * @author Gym Management System
 * @version 1.0
 */
public enum InventoryStatus {
    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    OUT_OF_ORDER("Fuera de Servicio"),
    MAINTENANCE("En Mantenimiento"),
    RETIRED("Retirado");

    private final String displayName;

    InventoryStatus(String displayName) {
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