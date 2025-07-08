package com.gymmanagement.gymapp.config;

/**
 * Constantes de la aplicación de gestión de gimnasio.
 * Centraliza valores constantes para mejorar la mantenibilidad.
 * 
 * @author Gym Management System
 * @version 1.0
 */
public final class AppConstants {

    // Constructor privado para evitar instanciación
    private AppConstants() {
        throw new UnsupportedOperationException("Esta es una clase de constantes y no puede ser instanciada");
    }

    // --- Constantes de Vistas ---
    public static final class Views {
        public static final String ADMIN_USERS_LIST = "admin/users/list";
        public static final String ADMIN_USERS_FORM = "admin/users/form";
        public static final String ADMIN_DASHBOARD = "admin/dashboard";
        public static final String LOGIN = "login";
        public static final String LANDING = "landing";
        public static final String ERROR = "error/error";
        
        private Views() {}
    }

    // --- Constantes de Redirección ---
    public static final class Redirects {
        public static final String ADMIN_USERS = "redirect:/admin/users";
        public static final String ADMIN_DASHBOARD = "redirect:/admin/dashboard";
        public static final String LOGIN = "redirect:/login";
        public static final String HOME = "redirect:/";
        
        private Redirects() {}
    }

    // --- Constantes de Atributos de Modelo ---
    public static final class ModelAttributes {
        public static final String SUCCESS_MESSAGE = "successMessage";
        public static final String ERROR_MESSAGE = "errorMessage";
        public static final String WARNING_MESSAGE = "warningMessage";
        public static final String INFO_MESSAGE = "infoMessage";
        
        public static final String USER_DTO = "userDto";
        public static final String ALL_ROLES = "allRoles";
        public static final String IS_EDIT = "isEdit";
        public static final String CURRENT_URI = "currentUri";
        
        public static final String USERS = "users";
        public static final String CURRENT_PAGE = "currentPage";
        public static final String TOTAL_PAGES = "totalPages";
        public static final String TOTAL_ITEMS = "totalItems";
        public static final String PAGE_SIZE = "pageSize";
        public static final String SORT_FIELD = "sortField";
        public static final String SORT_DIRECTION = "sortDirection";
        public static final String KEYWORD = "keyword";
        
        private ModelAttributes() {}
    }

    // --- Constantes de Paginación ---
    public static final class Pagination {
        public static final int DEFAULT_PAGE = 1;
        public static final int DEFAULT_SIZE = 10;
        public static final String DEFAULT_SORT = "id,asc";
        public static final int MAX_SIZE = 100;
        
        private Pagination() {}
    }

    // --- Constantes de Validación ---
    public static final class Validation {
        public static final int USERNAME_MIN_LENGTH = 3;
        public static final int USERNAME_MAX_LENGTH = 50;
        public static final int PASSWORD_MIN_LENGTH = 6;
        public static final int EMAIL_MAX_LENGTH = 100;
        public static final int NAME_MAX_LENGTH = 50;
        
        // Códigos de error de validación
        public static final String USERNAME_EXISTS = "validation.username.exists";
        public static final String EMAIL_EXISTS = "validation.email.exists";
        public static final String PASSWORD_REQUIRED = "validation.password.required";
        public static final String PASSWORDS_MISMATCH = "validation.passwords.mismatch";
        
        private Validation() {}
    }

    // --- Constantes de Roles ---
    public static final class Roles {
        public static final String ADMIN = "ROLE_ADMIN";
        public static final String CLIENT = "ROLE_CLIENT";
        public static final String TRAINER = "ROLE_TRAINER";
        public static final String MANAGER = "ROLE_MANAGER";
        
        private Roles() {}
    }

    // --- Constantes de Seguridad ---
    public static final class Security {
        public static final String LOGIN_URL = "/login";
        public static final String LOGOUT_URL = "/logout";
        public static final String DEFAULT_SUCCESS_URL = "/";
        public static final String ADMIN_SUCCESS_URL = "/admin/dashboard";
        public static final String CLIENT_SUCCESS_URL = "/client/dashboard";
        
        private Security() {}
    }

    // --- Constantes de Base de Datos ---
    public static final class Database {
        public static final String USERS_TABLE = "users";
        public static final String ROLES_TABLE = "roles";
        public static final String USERS_ROLES_TABLE = "users_roles";
        public static final String MEMBERSHIPS_TABLE = "memberships";
        
        private Database() {}
    }

    // --- Mensajes de Error ---
    public static final class ErrorMessages {
        public static final String USER_NOT_FOUND = "Usuario no encontrado con ID: ";
        public static final String USER_NOT_FOUND_EMAIL = "Usuario no encontrado con email: ";
        public static final String ROLE_NOT_FOUND = "Rol no encontrado: ";
        public static final String DEFAULT_ROLE_NOT_FOUND = "Rol por defecto ROLE_CLIENT no encontrado en la base de datos.";
        public static final String PASSWORD_REQUIRED_NEW_USER = "La contraseña es requerida para nuevos usuarios.";
        public static final String ACCESS_DENIED = "Acceso denegado. No tiene permisos para realizar esta acción.";
        
        private ErrorMessages() {}
    }

    // --- Mensajes de Éxito ---
    public static final class SuccessMessages {
        public static final String USER_CREATED = "Usuario creado exitosamente!";
        public static final String USER_UPDATED = "Usuario actualizado exitosamente!";
        public static final String USER_DELETED = "Usuario eliminado exitosamente!";
        public static final String USER_STATUS_UPDATED = "Estado del usuario actualizado!";
        
        private SuccessMessages() {}
    }
}