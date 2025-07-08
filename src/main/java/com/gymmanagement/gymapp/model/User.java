package com.gymmanagement.gymapp.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors; // ¡Importación necesaria para Collectors!

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
    private boolean enabled; // Para activar/desactivar cuentas

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>(); // Usa Set para evitar roles duplicados

    // RELACIÓN ONE-TO-MANY CON MEMBRESIAS
    // Un usuario puede tener muchas membresías
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Membership> memberships = new ArrayList<>(); // Inicializar para evitar NullPointerException

    // Constructor vacío (requerido por JPA)
    public User() {
        // Las fechas y el estado habilitado se manejarán con @PrePersist.
        // No es necesario inicializar createdAt, updatedAt, enabled aquí,
        // ya que @PrePersist se encargará de ello antes de la primera persistencia.
    }

    // Constructor para registro (sin ID, con valores por defecto)
    public User(String username, String password, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        // Las fechas y el estado habilitado se manejarán con @PrePersist.
    }

    // Métodos de ciclo de vida de JPA para `createdAt` y `updatedAt`
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        // Corrección aquí: 'enabled' es un boolean primitivo, no puede ser 'null'.
        // Si necesitas un valor por defecto para 'enabled' cuando se crea un nuevo usuario,
        // puedes establecerlo directamente aquí o en el constructor si no se pasa.
        // Por ejemplo, para que un nuevo usuario esté habilitado por defecto:
        this.enabled = true; // Si no se ha asignado antes.
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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
        this.roles = roles;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
    }

    // GETTERS Y SETTERS PARA MEMBERSHIPS
    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }

    // Métodos de utilidad para manejar la relación bidireccional si es necesario
    public void addMembership(Membership membership) {
        this.memberships.add(membership);
        membership.setUser(this);
    }

    public void removeMembership(Membership membership) {
        this.memberships.remove(membership);
        membership.setUser(null);
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", firstName='" + firstName + '\'' +
               ", lastName='" + lastName + '\'' +
               ", enabled=" + enabled +
               // Corrección aquí: Asegúrate de que 'roles' no sea nulo antes de llamar a stream()
               // Aunque 'roles' está inicializado como 'new HashSet<>()', esta comprobación es defensiva
               ", roles=" + (roles != null ? roles.stream().map(Role::getName).collect(Collectors.joining(", ")) : "[]") +
               '}';
    }
}