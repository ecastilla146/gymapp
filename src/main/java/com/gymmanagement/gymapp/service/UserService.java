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

import com.gymmanagement.gymapp.dto.UserRegistrationDto;
import com.gymmanagement.gymapp.model.Role;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.repository.RoleRepository;
import com.gymmanagement.gymapp.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!StringUtils.hasText(email)) {
            throw new UsernameNotFoundException("El email no puede ser nulo o vacío.");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isEnabled(),
                true, true, true,
                user.getRoles()
        );
    }

    @Transactional
    public User saveUser(UserRegistrationDto registrationDto) {
        User user;
        if (registrationDto.getId() != null) {
            user = userRepository.findById(registrationDto.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado para edición con ID: " + registrationDto.getId()));
            user.setUpdatedAt(LocalDateTime.now());
        } else {
            user = new User();
            user.setCreatedAt(LocalDateTime.now()); // @PrePersist ya lo maneja si no hay constructor con args
            user.setEnabled(true); // Asegúrate de que los nuevos usuarios estén habilitados
        }

        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEnabled(registrationDto.isEnabled());

        if (StringUtils.hasText(registrationDto.getPassword())) {
            user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        } else if (registrationDto.getId() == null) {
            // Esto solo debería pasar si la validación del DTO no funcionó correctamente
            throw new RuntimeException("La contraseña es requerida para nuevos usuarios.");
        }

        if (registrationDto.getSelectedRoles() != null && !registrationDto.getSelectedRoles().isEmpty()) {
            Set<Role> roles = registrationDto.getSelectedRoles().stream()
                    .map(roleName -> roleRepository.findByName(roleName)
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        } else {
            // Asigna un rol por defecto si no se selecciona ninguno
            Role defaultRole = roleRepository.findByName(Role.RoleName.ROLE_CLIENT.getFullName())
                                .orElseThrow(() -> new RuntimeException("Rol por defecto ROLE_CLIENT no encontrado en la base de datos."));
            user.setRoles(Collections.singleton(defaultRole));
        }

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return Optional.empty();
        }
        return userRepository.findByEmail(email);
    }

    public Page<User> findAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public List<User> findAllUsers() { // Nuevo método para obtener todos los usuarios sin paginación (para selectbox)
        return userRepository.findAll();
    }

    public Page<User> searchUsers(String keyword, Pageable pageable) {
        String actualKeyword = keyword != null ? keyword : "";
        return userRepository.searchUsers(actualKeyword, pageable);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        user.setEnabled(!user.isEnabled());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}