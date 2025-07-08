// src/main/java/com/gymmanagement/gymapp/config/CustomAuthenticationSuccessHandler.java

package com.gymmanagement.gymapp.config;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        boolean isClient = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CLIENT"));

        if (isAdmin) {
            response.sendRedirect("/admin/dashboard"); // Redirige a la página de administrador
        } else if (isClient) {
            response.sendRedirect("/client/dashboard"); // Redirige a la página de cliente
        } else {
            response.sendRedirect("/"); // Redirige a la página por defecto si no tiene un rol específico
        }
    }
}