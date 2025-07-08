package com.gymmanagement.gymapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa un usuario del sistema de gestión de gimnasio.
 * Un usuario puede tener múltiples roles y membresías asociadas.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre de usuario es requerido.")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @NotBlank(message = "La contraseña es requerida.")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "El email es requerido.")
    @Email(message = "El email debe ser una dirección de correo válida.")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "El nombre es requerido.")
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank(message = "El apellido es requerido.")
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    /**
     * Roles asignados al usuario.
     * Utiliza Set para evitar roles duplicados y EAGER fetch para cargar roles inmediatamente.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    /**
     * Membresías asociadas al usuario.
     * Un usuario puede tener múltiples membresías a lo largo del tiempo.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Membership> memberships = new ArrayList<>();

    // --- Constructores ---

    /**
     * Constructor vacío requerido por JPA.
     */
    public User() {
        // Los valores por defecto se establecen en @PrePersist
    }

    /**
     * Constructor para crear un nuevo usuario con datos básicos.
     * 
     * @param username Nombre de usuario único
     * @param password Contraseña (debe ser encriptada antes de guardar)
     * @param email Email único del usuario
     * @param firstName Nombre del usuario
     * @param lastName Apellido del usuario
     */
    public User(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // --- Métodos de ciclo de vida JPA ---

    /**
     * Establece valores por defecto antes de persistir la entidad.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        // Si enabled no ha sido establecido, se establece como true por defecto
        if (!this.enabled) {
            this.enabled = true;
        }
    }

    /**
     * Actualiza la fecha de modificación antes de actualizar la entidad.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- Métodos de utilidad para relaciones ---

    /**
     * Agrega un rol al usuario de forma segura.
     * 
     * @param role Rol a agregar
     */
    public void addRole(Role role) {
        if (role != null) {
            this.roles.add(role);
        }
    }

    /**
     * Remueve un rol del usuario de forma segura.
     * 
     * @param role Rol a remover
     */
    public void removeRole(Role role) {
        if (role != null) {
            this.roles.remove(role);
        }
    }

    /**
     * Agrega una membresía al usuario manteniendo la relación bidireccional.
     * 
     * @param membership Membresía a agregar
     */
    public void addMembership(Membership membership) {
        if (membership != null) {
            this.memberships.add(membership);
            membership.setUser(this);
        }
    }

    /**
     * Remueve una membresía del usuario manteniendo la relación bidireccional.
     * 
     * @param membership Membresía a remover
     */
    public void removeMembership(Membership membership) {
        if (membership != null) {
            this.memberships.remove(membership);
            membership.setUser(null);
        }
    }

    /**
     * Obtiene el nombre completo del usuario.
     * 
     * @return Nombre completo (firstName + lastName)
     */
    public String getFullName() {
        return String.format("%s %s", firstName, lastName).trim();
    }

    /**
     * Verifica si el usuario tiene un rol específico.
     * 
     * @param roleName Nombre del rol a verificar
     * @return true si el usuario tiene el rol, false en caso contrario
     */
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getName().equals(roleName));
    }

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles != null ? roles : new HashSet<>();
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships != null ? memberships : new ArrayList<>();
    }

    // --- Métodos Object ---

    @Override
    public String toString() {
        String roleNames = roles != null 
            ? roles.stream().map(Role::getName).collect(Collectors.joining(", "))
            : "[]";
            
        return String.format(
            "User{id=%d, username='%s', email='%s', firstName='%s', lastName='%s', enabled=%s, roles=[%s]}",
            id, username, email, firstName, lastName, enabled, roleNames
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        User user = (User) obj;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}