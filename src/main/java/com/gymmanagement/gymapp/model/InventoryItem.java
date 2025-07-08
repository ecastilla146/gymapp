package com.gymmanagement.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad que representa un elemento del inventario del gimnasio.
 * Incluye información sobre productos, equipos y suministros.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Entity
@Table(name = "inventory_items", indexes = {
    @Index(name = "idx_inventory_category", columnList = "category"),
    @Index(name = "idx_inventory_status", columnList = "status"),
    @Index(name = "idx_inventory_stock", columnList = "current_stock")
})
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @NotBlank
    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "brand", length = 50)
    private String brand;

    @Column(name = "model", length = 50)
    private String model;

    @PositiveOrZero
    @Column(name = "current_stock", nullable = false)
    private Integer currentStock = 0;

    @PositiveOrZero
    @Column(name = "min_stock_level", nullable = false)
    private Integer minStockLevel = 0;

    @PositiveOrZero
    @Column(name = "max_stock_level")
    private Integer maxStockLevel;

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "supplier", length = 100)
    private String supplier;

    @Column(name = "location", length = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private InventoryStatus status = InventoryStatus.ACTIVE;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "warranty_expiry")
    private LocalDateTime warrantyExpiry;

    @Column(name = "last_maintenance")
    private LocalDateTime lastMaintenance;

    @Column(name = "next_maintenance")
    private LocalDateTime nextMaintenance;

    @Column(name = "notes", length = 1000)
    private String notes;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public InventoryItem() {
        this.createdAt = LocalDateTime.now();
    }

    public InventoryItem(String name, String category, Integer currentStock, Integer minStockLevel) {
        this();
        this.name = name;
        this.category = category;
        this.currentStock = currentStock;
        this.minStockLevel = minStockLevel;
    }

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods

    /**
     * Verifica si el stock está por debajo del nivel mínimo.
     */
    public boolean isLowStock() {
        return currentStock <= minStockLevel;
    }

    /**
     * Verifica si el item está agotado.
     */
    public boolean isOutOfStock() {
        return currentStock == 0;
    }

    /**
     * Verifica si necesita mantenimiento.
     */
    public boolean needsMaintenance() {
        return nextMaintenance != null && LocalDateTime.now().isAfter(nextMaintenance);
    }

    /**
     * Verifica si la garantía ha expirado.
     */
    public boolean isWarrantyExpired() {
        return warrantyExpiry != null && LocalDateTime.now().isAfter(warrantyExpiry);
    }

    /**
     * Aumenta el stock del item.
     */
    public void addStock(int quantity) {
        if (quantity > 0) {
            this.currentStock += quantity;
        }
    }

    /**
     * Reduce el stock del item.
     */
    public boolean removeStock(int quantity) {
        if (quantity > 0 && this.currentStock >= quantity) {
            this.currentStock -= quantity;
            return true;
        }
        return false;
    }

    /**
     * Programa el próximo mantenimiento.
     */
    public void scheduleNextMaintenance(LocalDateTime nextMaintenanceDate) {
        this.nextMaintenance = nextMaintenanceDate;
    }

    /**
     * Marca el mantenimiento como completado.
     */
    public void completeMaintenance() {
        this.lastMaintenance = LocalDateTime.now();
    }

    /**
     * Calcula el valor total del inventario para este item.
     */
    public BigDecimal getTotalValue() {
        if (unitPrice != null) {
            return unitPrice.multiply(BigDecimal.valueOf(currentStock));
        }
        return BigDecimal.ZERO;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public Integer getMinStockLevel() {
        return minStockLevel;
    }

    public void setMinStockLevel(Integer minStockLevel) {
        this.minStockLevel = minStockLevel;
    }

    public Integer getMaxStockLevel() {
        return maxStockLevel;
    }

    public void setMaxStockLevel(Integer maxStockLevel) {
        this.maxStockLevel = maxStockLevel;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public InventoryStatus getStatus() {
        return status;
    }

    public void setStatus(InventoryStatus status) {
        this.status = status;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public LocalDateTime getWarrantyExpiry() {
        return warrantyExpiry;
    }

    public void setWarrantyExpiry(LocalDateTime warrantyExpiry) {
        this.warrantyExpiry = warrantyExpiry;
    }

    public LocalDateTime getLastMaintenance() {
        return lastMaintenance;
    }

    public void setLastMaintenance(LocalDateTime lastMaintenance) {
        this.lastMaintenance = lastMaintenance;
    }

    public LocalDateTime getNextMaintenance() {
        return nextMaintenance;
    }

    public void setNextMaintenance(LocalDateTime nextMaintenance) {
        this.nextMaintenance = nextMaintenance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Object methods
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryItem)) return false;
        InventoryItem that = (InventoryItem) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", currentStock=" + currentStock +
                ", minStockLevel=" + minStockLevel +
                ", status=" + status +
                ", lowStock=" + isLowStock() +
                '}';
    }
}