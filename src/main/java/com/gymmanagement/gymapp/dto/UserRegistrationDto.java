package com.gymmanagement.gymapp.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para el registro y actualización de usuarios.
 * Contiene validaciones básicas y maneja tanto la creación como la edición de usuarios.
 * 
 * @author Gym Management System
 * @version 1.0
 */
public class UserRegistrationDto {

    /**
     * ID del usuario. Null para usuarios nuevos, presente para edición.
     */
    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
    private String username;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "Formato de email inválido.")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres.")
    private String email;

    /**
     * Contraseña del usuario.
     * Requerida para nuevos usuarios, opcional para edición.
     * La validación específica se maneja en el servicio de validación.
     */
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    /**
     * Confirmación de contraseña.
     * Debe coincidir con el campo password.
     */
    private String confirmPassword;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres.")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres.")
    private String lastName;

    /**
     * Estado del usuario (activo/inactivo).
     * Por defecto true para nuevos usuarios.
     */
    private boolean enabled = true;

    /**
     * Lista de roles seleccionados para el usuario.
     * Al menos un rol debe ser seleccionado.
     */
    @NotNull(message = "Debe seleccionar al menos un rol.")
    @Size(min = 1, message = "Debe seleccionar al menos un rol.")
    private List<String> selectedRoles;

    // --- Constructores ---

    /**
     * Constructor vacío requerido para el binding de formularios.
     */
    public UserRegistrationDto() {
    }

    /**
     * Constructor para crear un DTO con datos básicos.
     * 
     * @param username Nombre de usuario
     * @param email Email del usuario
     * @param firstName Nombre
     * @param lastName Apellido
     */
    public UserRegistrationDto(String username, String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // --- Métodos de utilidad ---

    /**
     * Verifica si el DTO representa un usuario nuevo.
     * 
     * @return true si es un usuario nuevo (id == null), false si es edición
     */
    public boolean isNewUser() {
        return id == null;
    }

    /**
     * Verifica si se está actualizando la contraseña.
     * 
     * @return true si hay una contraseña nueva, false en caso contrario
     */
    public boolean isUpdatingPassword() {
        return password != null && !password.trim().isEmpty();
    }

    /**
     * Obtiene el nombre completo del usuario.
     * 
     * @return Nombre completo concatenado
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return String.format("%s %s", firstName, lastName).trim();
        }
        return "";
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(List<String> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    // --- Método toString ---

    @Override
    public String toString() {
        return String.format(
            "UserRegistrationDto{id=%d, username='%s', email='%s', firstName='%s', lastName='%s', enabled=%s, rolesCount=%d}",
            id, username, email, firstName, lastName, enabled, 
            selectedRoles != null ? selectedRoles.size() : 0
        );
    }
}