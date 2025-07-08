package com.gymmanagement.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entidad que representa un ticket de soporte o comunicación.
 * Permite gestionar consultas, quejas y comunicaciones de los usuarios.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Entity
@Table(name = "support_tickets", indexes = {
    @Index(name = "idx_ticket_status", columnList = "status"),
    @Index(name = "idx_ticket_priority", columnList = "priority"),
    @Index(name = "idx_ticket_user", columnList = "user_id"),
    @Index(name = "idx_ticket_assigned", columnList = "assigned_to_id"),
    @Index(name = "idx_ticket_created", columnList = "created_at")
})
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @NotBlank
    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id")
    private User assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TicketStatus status = TicketStatus.OPEN;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private TicketPriority priority = TicketPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private TicketCategory category;

    @Column(name = "resolution", length = 2000)
    private String resolution;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public SupportTicket() {
        this.createdAt = LocalDateTime.now();
    }

    public SupportTicket(String title, String description, User user, TicketCategory category) {
        this();
        this.title = title;
        this.description = description;
        this.user = user;
        this.category = category;
    }

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods

    /**
     * Verifica si el ticket está abierto.
     */
    public boolean isOpen() {
        return status == TicketStatus.OPEN || status == TicketStatus.IN_PROGRESS;
    }

    /**
     * Verifica si el ticket está cerrado.
     */
    public boolean isClosed() {
        return status == TicketStatus.CLOSED;
    }

    /**
     * Verifica si el ticket está resuelto.
     */
    public boolean isResolved() {
        return status == TicketStatus.RESOLVED;
    }

    /**
     * Asigna el ticket a un usuario.
     */
    public void assignTo(User assignee) {
        this.assignedTo = assignee;
        if (status == TicketStatus.OPEN) {
            this.status = TicketStatus.IN_PROGRESS;
        }
    }

    /**
     * Marca el ticket como en progreso.
     */
    public void markInProgress() {
        if (status == TicketStatus.OPEN) {
            this.status = TicketStatus.IN_PROGRESS;
        }
    }

    /**
     * Resuelve el ticket.
     */
    public void resolve(String resolution) {
        this.resolution = resolution;
        this.status = TicketStatus.RESOLVED;
        this.resolvedAt = LocalDateTime.now();
    }

    /**
     * Cierra el ticket.
     */
    public void close() {
        this.status = TicketStatus.CLOSED;
        if (resolvedAt == null) {
            this.resolvedAt = LocalDateTime.now();
        }
    }

    /**
     * Reabre el ticket.
     */
    public void reopen() {
        if (status == TicketStatus.CLOSED || status == TicketStatus.RESOLVED) {
            this.status = TicketStatus.OPEN;
            this.resolvedAt = null;
        }
    }

    /**
     * Calcula el tiempo transcurrido desde la creación.
     */
    public long getAgeInHours() {
        return java.time.Duration.between(createdAt, LocalDateTime.now()).toHours();
    }

    /**
     * Calcula el tiempo de resolución en horas.
     */
    public long getResolutionTimeInHours() {
        if (resolvedAt != null) {
            return java.time.Duration.between(createdAt, resolvedAt).toHours();
        }
        return 0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public TicketCategory getCategory() {
        return category;
    }

    public void setCategory(TicketCategory category) {
        this.category = category;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
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
        if (!(o instanceof SupportTicket)) return false;
        SupportTicket that = (SupportTicket) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "SupportTicket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", status=" + status +
                ", priority=" + priority +
                ", category=" + category +
                ", ageInHours=" + getAgeInHours() +
                '}';
    }
}