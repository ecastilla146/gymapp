package com.gymmanagement.gymapp.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.gymmanagement.gymapp.config.AppConstants;
import com.gymmanagement.gymapp.dto.UserRegistrationDto;
import com.gymmanagement.gymapp.model.Role;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.repository.RoleRepository;
import com.gymmanagement.gymapp.repository.UserRepository;

/**
 * Servicio para la gestión de usuarios del sistema.
 * Implementa UserDetailsService para la integración con Spring Security.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    // Se usan las constantes centralizadas de AppConstants

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // --- Implementación de UserDetailsService ---

    /**
     * Carga un usuario por su email para la autenticación de Spring Security.
     * 
     * @param email Email del usuario
     * @return UserDetails para Spring Security
     * @throws UsernameNotFoundException Si no se encuentra el usuario
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!StringUtils.hasText(email)) {
            throw new UsernameNotFoundException("El email no puede ser nulo o vacío.");
        }
        
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new UsernameNotFoundException(AppConstants.ErrorMessages.USER_NOT_FOUND_EMAIL + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true, // accountNonExpired
                true, // credentialsNonExpired  
                true, // accountNonLocked
                user.getRoles()
        );
    }

    // --- Operaciones CRUD ---

    /**
     * Guarda o actualiza un usuario basado en el DTO de registro.
     * 
     * @param registrationDto Datos del usuario a guardar
     * @return Usuario guardado
     * @throws RuntimeException Si ocurre un error durante el guardado
     */
    @Transactional
    public User saveUser(UserRegistrationDto registrationDto) {
        User user = determineUserToSave(registrationDto);
        populateUserData(user, registrationDto);
        assignRoles(user, registrationDto.getSelectedRoles());
        
        return userRepository.save(user);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     * 
     * @param username Nombre de usuario a buscar
     * @return Optional con el usuario si existe
     */
    public Optional<User> findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username.trim());
    }

    /**
     * Busca un usuario por su email.
     * 
     * @param email Email a buscar
     * @return Optional con el usuario si existe
     */
    public Optional<User> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email.trim());
    }

    /**
     * Busca un usuario por su ID.
     * 
     * @param id ID del usuario
     * @return Optional con el usuario si existe
     */
    public Optional<User> findUserById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return userRepository.findById(id);
    }

    /**
     * Obtiene todos los usuarios con paginación.
     * 
     * @param pageable Información de paginación
     * @return Página de usuarios
     */
    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * Obtiene todos los usuarios sin paginación.
     * Útil para selectboxes y listados simples.
     * 
     * @return Lista de todos los usuarios
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Obtiene todos los usuarios activos ordenados por nombre.
     * 
     * @return Lista de usuarios activos
     */
    public List<User> findAllActiveUsers() {
        return userRepository.findByEnabledTrueOrderByFirstNameAsc();
    }

    /**
     * Busca usuarios por palabra clave con paginación.
     * 
     * @param keyword Término de búsqueda
     * @param pageable Información de paginación
     * @return Página de usuarios que coinciden con la búsqueda
     */
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        String searchTerm = StringUtils.hasText(keyword) ? keyword.trim() : "";
        return userRepository.searchUsers(searchTerm, pageable);
    }

    /**
     * Elimina un usuario por su ID.
     * 
     * @param id ID del usuario a eliminar
     * @throws RuntimeException Si el usuario no existe
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException(AppConstants.ErrorMessages.USER_NOT_FOUND + id);
        }
        userRepository.deleteById(id);
    }

    /**
     * Alterna el estado activo/inactivo de un usuario.
     * 
     * @param id ID del usuario
     * @throws RuntimeException Si el usuario no existe
     */
    @Transactional
    public void toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(AppConstants.ErrorMessages.USER_NOT_FOUND + id));
                
        user.setEnabled(!user.isEnabled());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // --- Métodos privados de utilidad ---

    /**
     * Determina si se está creando un nuevo usuario o actualizando uno existente.
     */
    private User determineUserToSave(UserRegistrationDto registrationDto) {
        if (registrationDto.getId() != null) {
            return userRepository.findById(registrationDto.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado para edición con ID: " + registrationDto.getId()));
        }
        return new User();
    }

    /**
     * Popula los datos básicos del usuario desde el DTO.
     */
    private void populateUserData(User user, UserRegistrationDto dto) {
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEnabled(dto.isEnabled());

        // Establecer timestamp de actualización para usuarios existentes
        if (user.getId() != null) {
            user.setUpdatedAt(LocalDateTime.now());
        }

        // Manejar contraseña
        handlePassword(user, dto);
    }

    /**
     * Maneja la lógica de contraseñas para usuarios nuevos y existentes.
     */
    private void handlePassword(User user, UserRegistrationDto dto) {
        if (StringUtils.hasText(dto.getPassword())) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        } else if (user.getId() == null) {
            // Nuevos usuarios requieren contraseña
            throw new RuntimeException(AppConstants.ErrorMessages.PASSWORD_REQUIRED_NEW_USER);
        }
        // Para usuarios existentes, si no hay contraseña nueva, se mantiene la actual
    }

    /**
     * Asigna roles al usuario, usando un rol por defecto si no se especifican roles.
     */
    private void assignRoles(User user, List<String> selectedRoles) {
        if (selectedRoles != null && !selectedRoles.isEmpty()) {
            Set<Role> roles = selectedRoles.stream()
                    .map(this::findRoleByNameOrThrow)
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        } else {
            assignDefaultRole(user);
        }
    }

    /**
     * Busca un rol por nombre o lanza excepción si no existe.
     */
    private Role findRoleByNameOrThrow(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException(AppConstants.ErrorMessages.ROLE_NOT_FOUND + roleName));
    }

    /**
     * Asigna el rol por defecto (ROLE_CLIENT) al usuario.
     */
    private void assignDefaultRole(User user) {
        Role defaultRole = roleRepository.findByName(Role.RoleName.ROLE_CLIENT.getFullName())
                .orElseThrow(() -> new RuntimeException(AppConstants.ErrorMessages.DEFAULT_ROLE_NOT_FOUND));
        user.setRoles(Collections.singleton(defaultRole));
    }
}