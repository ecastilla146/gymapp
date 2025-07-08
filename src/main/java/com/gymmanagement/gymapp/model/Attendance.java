package com.gymmanagement.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entidad que representa el registro de asistencia de un usuario al gimnasio.
 * Controla la entrada y salida de los miembros del gimnasio.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Entity
@Table(name = "attendance", indexes = {
    @Index(name = "idx_attendance_user_date", columnList = "user_id, check_in_time"),
    @Index(name = "idx_attendance_date", columnList = "check_in_time")
})
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public Attendance() {
        this.createdAt = LocalDateTime.now();
    }

    public Attendance(User user, LocalDateTime checkInTime) {
        this();
        this.user = user;
        this.checkInTime = checkInTime;
    }

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods

    /**
     * Verifica si el usuario aún está en el gimnasio (no ha hecho check-out).
     */
    public boolean isStillInGym() {
        return checkOutTime == null;
    }

    /**
     * Calcula la duración de la visita en minutos.
     * Si aún no ha salido, calcula hasta el momento actual.
     */
    public long getDurationInMinutes() {
        LocalDateTime endTime = checkOutTime != null ? checkOutTime : LocalDateTime.now();
        return java.time.Duration.between(checkInTime, endTime).toMinutes();
    }

    /**
     * Realiza el check-out del usuario.
     */
    public void checkOut() {
        if (checkOutTime == null) {
            this.checkOutTime = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalDateTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDateTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalDateTime checkOutTime) {
        this.checkOutTime = checkOutTime;
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
        if (!(o instanceof Attendance)) return false;
        Attendance that = (Attendance) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "id=" + id +
                ", user=" + (user != null ? user.getUsername() : "null") +
                ", checkInTime=" + checkInTime +
                ", checkOutTime=" + checkOutTime +
                ", stillInGym=" + isStillInGym() +
                '}';
    }
}