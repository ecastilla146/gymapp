package com.gymmanagement.gymapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una clase o actividad del gimnasio.
 * Incluye información sobre horarios, instructores y participantes.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Entity
@Table(name = "gym_classes", indexes = {
    @Index(name = "idx_class_start_time", columnList = "start_time"),
    @Index(name = "idx_class_instructor", columnList = "instructor_id"),
    @Index(name = "idx_class_status", columnList = "status")
})
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Positive
    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClassStatus status = ClassStatus.SCHEDULED;

    @Column(name = "room", length = 50)
    private String room;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "class_participants",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants = new ArrayList<>();

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public GymClass() {
        this.createdAt = LocalDateTime.now();
    }

    public GymClass(String name, User instructor, LocalDateTime startTime, LocalDateTime endTime, Integer maxParticipants) {
        this();
        this.name = name;
        this.instructor = instructor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxParticipants = maxParticipants;
    }

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Business methods

    /**
     * Verifica si la clase tiene cupos disponibles.
     */
    public boolean hasAvailableSpots() {
        return participants.size() < maxParticipants;
    }

    /**
     * Obtiene el número de cupos disponibles.
     */
    public int getAvailableSpots() {
        return maxParticipants - participants.size();
    }

    /**
     * Verifica si un usuario está inscrito en la clase.
     */
    public boolean hasParticipant(User user) {
        return participants.contains(user);
    }

    /**
     * Inscribe un usuario a la clase si hay cupos disponibles.
     */
    public boolean enrollParticipant(User user) {
        if (hasAvailableSpots() && !hasParticipant(user) && status == ClassStatus.SCHEDULED) {
            participants.add(user);
            return true;
        }
        return false;
    }

    /**
     * Desinscribe un usuario de la clase.
     */
    public boolean removeParticipant(User user) {
        return participants.remove(user);
    }

    /**
     * Verifica si la clase está en progreso.
     */
    public boolean isInProgress() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startTime) && now.isBefore(endTime) && status == ClassStatus.IN_PROGRESS;
    }

    /**
     * Verifica si la clase ha terminado.
     */
    public boolean isFinished() {
        return LocalDateTime.now().isAfter(endTime) || status == ClassStatus.COMPLETED;
    }

    /**
     * Inicia la clase.
     */
    public void startClass() {
        if (status == ClassStatus.SCHEDULED) {
            this.status = ClassStatus.IN_PROGRESS;
        }
    }

    /**
     * Termina la clase.
     */
    public void completeClass() {
        if (status == ClassStatus.IN_PROGRESS) {
            this.status = ClassStatus.COMPLETED;
        }
    }

    /**
     * Cancela la clase.
     */
    public void cancelClass() {
        if (status == ClassStatus.SCHEDULED) {
            this.status = ClassStatus.CANCELLED;
        }
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

    public User getInstructor() {
        return instructor;
    }

    public void setInstructor(User instructor) {
        this.instructor = instructor;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public ClassStatus getStatus() {
        return status;
    }

    public void setStatus(ClassStatus status) {
        this.status = status;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants != null ? participants : new ArrayList<>();
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
        if (!(o instanceof GymClass)) return false;
        GymClass gymClass = (GymClass) o;
        return id != null && id.equals(gymClass.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "GymClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", instructor=" + (instructor != null ? instructor.getFullName() : "null") +
                ", startTime=" + startTime +
                ", status=" + status +
                ", participants=" + participants.size() + "/" + maxParticipants +
                '}';
    }
}