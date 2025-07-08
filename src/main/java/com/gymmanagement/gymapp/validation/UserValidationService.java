package com.gymmanagement.gymapp.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.gymmanagement.gymapp.config.AppConstants;
import com.gymmanagement.gymapp.dto.UserRegistrationDto;
import com.gymmanagement.gymapp.service.UserService;

/**
 * Servicio de validación para operaciones relacionadas con usuarios.
 * Centraliza la lógica de validación personalizada para mantener los controladores limpios.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Service
public class UserValidationService {

    private final UserService userService;

    @Autowired
    public UserValidationService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Valida los datos de un nuevo usuario durante la creación.
     * 
     * @param userDto Datos del usuario a validar
     * @param result Objeto de resultado de validación donde se agregan los errores
     */
    public void validateNewUser(UserRegistrationDto userDto, BindingResult result) {
        validateUniqueFields(userDto, result, null);
        validatePasswordsForNewUser(userDto, result);
    }

    /**
     * Valida los datos de un usuario existente durante la actualización.
     * 
     * @param userDto Datos del usuario a validar
     * @param result Objeto de resultado de validación donde se agregan los errores
     */
    public void validateExistingUser(UserRegistrationDto userDto, BindingResult result) {
        validateUniqueFields(userDto, result, userDto.getId());
        validatePasswordsForExistingUser(userDto, result);
    }

    /**
     * Valida que el nombre de usuario y email sean únicos.
     * 
     * @param userDto Datos del usuario
     * @param result Resultado de validación
     * @param excludeId ID del usuario a excluir de la validación (para edición)
     */
    private void validateUniqueFields(UserRegistrationDto userDto, BindingResult result, Long excludeId) {
        validateUniqueUsername(userDto.getUsername(), result, excludeId);
        validateUniqueEmail(userDto.getEmail(), result, excludeId);
    }

    /**
     * Valida que el nombre de usuario sea único.
     */
    private void validateUniqueUsername(String username, BindingResult result, Long excludeId) {
        if (!StringUtils.hasText(username)) {
            return; // La validación @NotBlank se encarga de esto
        }

        userService.findByUsername(username.trim()).ifPresent(existingUser -> {
            boolean isConflict = (excludeId == null) || !existingUser.getId().equals(excludeId);
            if (isConflict) {
                result.rejectValue("username", AppConstants.Validation.USERNAME_EXISTS, 
                    "El nombre de usuario ya está registrado.");
            }
        });
    }

    /**
     * Valida que el email sea único.
     */
    private void validateUniqueEmail(String email, BindingResult result, Long excludeId) {
        if (!StringUtils.hasText(email)) {
            return; // La validación @NotBlank y @Email se encargan de esto
        }

        userService.findByEmail(email.trim()).ifPresent(existingUser -> {
            boolean isConflict = (excludeId == null) || !existingUser.getId().equals(excludeId);
            if (isConflict) {
                result.rejectValue("email", AppConstants.Validation.EMAIL_EXISTS, 
                    "El email ya está registrado.");
            }
        });
    }

    /**
     * Valida las contraseñas para un nuevo usuario.
     * Para nuevos usuarios, la contraseña es obligatoria.
     */
    private void validatePasswordsForNewUser(UserRegistrationDto userDto, BindingResult result) {
        String password = userDto.getPassword();
        String confirmPassword = userDto.getConfirmPassword();

        if (!StringUtils.hasText(password)) {
            result.rejectValue("password", AppConstants.Validation.PASSWORD_REQUIRED, 
                "La contraseña es obligatoria para nuevos usuarios.");
            return;
        }

        validatePasswordsMatch(password, confirmPassword, result);
    }

    /**
     * Valida las contraseñas para un usuario existente.
     * Para usuarios existentes, la contraseña es opcional, pero si se proporciona debe ser válida.
     */
    private void validatePasswordsForExistingUser(UserRegistrationDto userDto, BindingResult result) {
        String password = userDto.getPassword();
        String confirmPassword = userDto.getConfirmPassword();

        // Solo validar si se proporciona una nueva contraseña
        if (StringUtils.hasText(password)) {
            validatePasswordsMatch(password, confirmPassword, result);
        }
    }

    /**
     * Valida que las contraseñas coincidan.
     */
    private void validatePasswordsMatch(String password, String confirmPassword, BindingResult result) {
        if (!password.equals(confirmPassword)) {
            result.rejectValue("confirmPassword", AppConstants.Validation.PASSWORDS_MISMATCH, 
                "Las contraseñas no coinciden.");
        }
    }
}