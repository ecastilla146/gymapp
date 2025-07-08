package com.gymmanagement.gymapp.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.gymmanagement.gymapp.dto.UserRegistrationDto;
import com.gymmanagement.gymapp.model.Role;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.repository.RoleRepository;
import com.gymmanagement.gymapp.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

// ELIMINAR esta importación, ya que no se usa en variables locales
// import org.springframework.lang.Nullable;

@Controller
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listUsers(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String keyword,
            HttpServletRequest request
    ) {
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1].toUpperCase());

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy));

        Page<User> userPage;
        if (keyword != null && !keyword.isEmpty()) {
            userPage = userService.searchUsers(keyword, pageable);
        } else {
            userPage = userService.findAllUsers(pageable);
        }

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber() + 1);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("sortField", sortBy);
        model.addAttribute("sortDirection", sortDirection.toString().toLowerCase());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentUri", request.getRequestURI());

        return "admin/users/list";
    }

    @GetMapping("/new")
    public String createUserForm(Model model, HttpServletRequest request) {
        model.addAttribute("userDto", new UserRegistrationDto());
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("isEdit", false);
        model.addAttribute("currentUri", request.getRequestURI());
        return "admin/users/form";
    }

    @PostMapping("/new")
    public String saveUser(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model,
                           HttpServletRequest request) {

        // Validaciones personalizadas de unicidad
        // ANTERIORMENTE: @Nullable String username = userDto.getUsername();
        String username = userDto.getUsername(); // Sin @Nullable aquí
        // ANTERIORMENTE: @Nullable String email = userDto.getEmail();
        String email = userDto.getEmail(); // Sin @Nullable aquí

        if (username != null && !username.isEmpty() && userService.findByUsername(username).isPresent()) { // Añadido !username.isEmpty() para seguridad
            result.rejectValue("username", null, "El nombre de usuario ya está registrado.");
        }
        if (email != null && !email.isEmpty() && userService.findByEmail(email).isPresent()) { // Añadido !email.isEmpty() para seguridad
            result.rejectValue("email", null, "El email ya está registrado.");
        }
        
        // Validación de contraseñas si aplica para la creación
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
            result.rejectValue("password", null, "La contraseña es obligatoria para nuevos usuarios.");
        } else if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            result.rejectValue("confirmPassword", null, "Las contraseñas no coinciden.");
        }


        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("isEdit", false);
            model.addAttribute("currentUri", request.getRequestURI());
            return "admin/users/form";
        }

        try {
            userService.saveUser(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario guardado exitosamente!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("isEdit", false);
            model.addAttribute("currentUri", request.getRequestURI());
            return "admin/users/form";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        User user = userService.findUserById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEnabled(user.isEnabled());
        userDto.setSelectedRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toList()));

        model.addAttribute("userDto", userDto);
        model.addAttribute("allRoles", roleRepository.findAll());
        model.addAttribute("isEdit", true);
        model.addAttribute("currentUri", request.getRequestURI());
        return "admin/users/form";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model,
                             HttpServletRequest request) {
        userDto.setId(id);

        // Validaciones personalizadas de unicidad para edición
        // ANTERIORMENTE: @Nullable String username = userDto.getUsername();
        String username = userDto.getUsername(); // Sin @Nullable aquí
        // ANTERIORMENTE: @Nullable String email = userDto.getEmail();
        String email = userDto.getEmail(); // Sin @Nullable aquí

        if (email != null && !email.isEmpty() && !result.hasFieldErrors("email")) {
            userService.findByEmail(email).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(id)) {
                    result.rejectValue("email", null, "El email ya está en uso por otro usuario.");
                }
            });
        }
        if (username != null && !username.isEmpty() && !result.hasFieldErrors("username")) {
            userService.findByUsername(username).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(id)) {
                    result.rejectValue("username", null, "El nombre de usuario ya está en uso por otro usuario.");
                }
            });
        }
        
        // Validación de contraseñas para actualización (opcional)
        // Solo si se proporciona una nueva contraseña, verificar que coincida
        if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                result.rejectValue("confirmPassword", null, "Las contraseñas no coinciden.");
            }
        }


        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("isEdit", true);
            model.addAttribute("currentUri", request.getRequestURI());
            return "admin/users/form";
        }

        try {
            userService.saveUser(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario actualizado exitosamente!");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("allRoles", roleRepository.findAll());
            model.addAttribute("isEdit", true);
            model.addAttribute("currentUri", request.getRequestURI());
            return "admin/users/form";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "Usuario eliminado exitosamente!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar usuario: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute("successMessage", "Estado del usuario actualizado!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}