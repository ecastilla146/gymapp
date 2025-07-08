package com.gymmanagement.gymapp.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Constraint(validatedBy = PasswordsMatchValidator.class)
@Target({ElementType.TYPE}) // Se aplica a la clase entera (UserRegistrationDto)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PasswordsMatch {
    String message() default "Las contraseñas no coinciden.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}