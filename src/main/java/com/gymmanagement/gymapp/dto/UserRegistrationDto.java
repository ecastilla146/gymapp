package com.gymmanagement.gymapp.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// ELIMINAR estas interfaces (si las tenías)
// public interface OnCreate {}
// public interface OnUpdate {}

// Ya no necesitamos las interfaces de grupo
// @PasswordsMatch(message = "Las contraseñas no coinciden.", groups = {OnCreate.class, OnUpdate.class})
public class UserRegistrationDto {

    private Long id;

    @NotBlank(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
    private String username;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "Formato de email inválido.")
    @Size(max = 100, message = "El email no puede exceder los 100 caracteres.")
    private String email;

    // Para la creación, requerimos contraseña. Para edición, puede ser opcional.
    // La validación de la contraseña se manejará en el controlador si es opcional en edición.
    // Si siempre la quieres requerida, puedes poner @NotBlank aquí.
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    private String password;

    private String confirmPassword;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50, message = "El nombre no puede exceder los 50 caracteres.")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío.")
    @Size(max = 50, message = "El apellido no puede exceder los 50 caracteres.")
    private String lastName;

    private boolean enabled = true; // Por defecto true para nuevos usuarios

    @NotNull(message = "Debe seleccionar al menos un rol.")
    @Size(min = 1, message = "Debe seleccionar al menos un rol.")
    private List<String> selectedRoles;

    // Getters y Setters
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
}