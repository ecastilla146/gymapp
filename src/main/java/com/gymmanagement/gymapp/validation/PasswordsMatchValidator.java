package com.gymmanagement.gymapp.validation;

import org.springframework.util.StringUtils; // Importar tu DTO

import com.gymmanagement.gymapp.dto.UserRegistrationDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext; // Importar para utilidades de String

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationDto> {

    @Override
    public void initialize(PasswordsMatch constraintAnnotation) {
        // No hay atributos personalizados en esta anotación, así que no se necesita inicialización especial
    }

    @Override
    public boolean isValid(UserRegistrationDto userDto, ConstraintValidatorContext context) {
        // Si el DTO es nulo, no podemos validar
        if (userDto == null) {
            return true;
        }

        // Si es una actualización (tiene ID) y las contraseñas están vacías,
        // significa que no se van a cambiar. No necesitamos validarlas.
        if (userDto.getId() != null && !StringUtils.hasText(userDto.getPassword()) && !StringUtils.hasText(userDto.getConfirmPassword())) {
            return true;
        }

        // Si las contraseñas son nulas o no tienen texto, pero una de ellas sí,
        // o si no coinciden, entonces es inválido.
        if (!StringUtils.hasText(userDto.getPassword()) || !StringUtils.hasText(userDto.getConfirmPassword()) || !userDto.getPassword().equals(userDto.getConfirmPassword())) {
            context.disableDefaultConstraintViolation(); // Deshabilitar el mensaje por defecto de la anotación
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                   .addPropertyNode("confirmPassword") // Asociar el error al campo confirmPassword
                   .addConstraintViolation();
            return false;
        }

        return true; // Las contraseñas coinciden y no están vacías
    }
}