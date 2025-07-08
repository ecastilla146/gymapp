// src/main/java/com/gymmanagement/gymapp/model/Role.java
package com.gymmanagement.gymapp.model;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // Importar

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority { // Implementar GrantedAuthority

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    public Role() {
        // Constructor vacío requerido por JPA
    }

    public Role(String name) {
        this.name = name;
    }

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

    @Override
    public String getAuthority() {
        return name; // Spring Security usa el nombre del rol como autoridad
    }

    @Override
    public String toString() {
        return "Role{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }

    // Opcional: enumeración de roles para facilitar la gestión
    public enum RoleName {
        ROLE_ADMIN,
        ROLE_CLIENT,
        ROLE_TRAINER,
        ROLE_RECEPTIONIST,
        ROLE_NUTRITIONIST;

        public String getFullName() {
            return name(); // Devuelve "ROLE_ADMIN", "ROLE_CLIENT", etc.
        }
    }
}