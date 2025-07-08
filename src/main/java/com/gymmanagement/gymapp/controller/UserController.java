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

import com.gymmanagement.gymapp.config.AppConstants;
import com.gymmanagement.gymapp.dto.UserRegistrationDto;
import com.gymmanagement.gymapp.model.Role;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.repository.RoleRepository;
import com.gymmanagement.gymapp.service.UserService;
import com.gymmanagement.gymapp.validation.UserValidationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

/**
 * Controlador para la gestión de usuarios en el panel de administración.
 * Maneja las operaciones CRUD de usuarios incluyendo paginación, búsqueda y gestión de roles.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@Controller
@RequestMapping("/admin/users")
public class UserController {

    // Se usan las constantes centralizadas de AppConstants
    
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final UserValidationService userValidationService;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository, UserValidationService userValidationService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userValidationService = userValidationService;
    }

    /**
     * Muestra la lista paginada de usuarios con funcionalidad de búsqueda y ordenamiento.
     * 
     * @param model Modelo de la vista
     * @param page Número de página (por defecto 1)
     * @param size Tamaño de página (por defecto 10)
     * @param sort Campo y dirección de ordenamiento (por defecto "id,asc")
     * @param keyword Término de búsqueda opcional
     * @param request Objeto de solicitud HTTP para obtener la URI actual
     * @return Vista de lista de usuarios
     */
    @GetMapping
    public String listUsers(
            Model model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(required = false) String keyword,
            HttpServletRequest request) {

        Pageable pageable = createPageable(page, size, sort);
        Page<User> userPage = searchUsers(keyword, pageable);
        
        populateListModel(model, userPage, size, sort, keyword, request);
        
        return AppConstants.Views.ADMIN_USERS_LIST;
    }

    /**
     * Muestra el formulario para crear un nuevo usuario.
     */
    @GetMapping("/new")
    public String createUserForm(Model model, HttpServletRequest request) {
        populateFormModel(model, new UserRegistrationDto(), false, request);
        return AppConstants.Views.ADMIN_USERS_FORM;
    }

    /**
     * Procesa la creación de un nuevo usuario.
     */
    @PostMapping("/new")
    public String saveUser(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           Model model,
                           HttpServletRequest request) {

        // Validaciones personalizadas para creación
        userValidationService.validateNewUser(userDto, result);

        if (result.hasErrors()) {
            populateFormModel(model, userDto, false, request);
            return AppConstants.Views.ADMIN_USERS_FORM;
        }

        try {
            userService.saveUser(userDto);
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, AppConstants.SuccessMessages.USER_CREATED);
        } catch (RuntimeException e) {
            model.addAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, e.getMessage());
            populateFormModel(model, userDto, false, request);
            return AppConstants.Views.ADMIN_USERS_FORM;
        }
        
        return AppConstants.Redirects.ADMIN_USERS;
    }

    /**
     * Muestra el formulario para editar un usuario existente.
     */
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        User user = findUserOrThrow(id);
        UserRegistrationDto userDto = createUserDtoFromUser(user);
        
        populateFormModel(model, userDto, true, request);
        return AppConstants.Views.ADMIN_USERS_FORM;
    }

    /**
     * Procesa la actualización de un usuario existente.
     */
    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                             BindingResult result,
                             RedirectAttributes redirectAttributes,
                             Model model,
                             HttpServletRequest request) {
        
        userDto.setId(id);
        
        // Validaciones personalizadas para actualización
        userValidationService.validateExistingUser(userDto, result);

        if (result.hasErrors()) {
            populateFormModel(model, userDto, true, request);
            return AppConstants.Views.ADMIN_USERS_FORM;
        }

        try {
            userService.saveUser(userDto);
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, AppConstants.SuccessMessages.USER_UPDATED);
        } catch (RuntimeException e) {
            model.addAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, e.getMessage());
            populateFormModel(model, userDto, true, request);
            return AppConstants.Views.ADMIN_USERS_FORM;
        }
        
        return AppConstants.Redirects.ADMIN_USERS;
    }

    /**
     * Elimina un usuario por su ID.
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, AppConstants.SuccessMessages.USER_DELETED);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, "Error al eliminar usuario: " + e.getMessage());
        }
        return AppConstants.Redirects.ADMIN_USERS;
    }

    /**
     * Alterna el estado activo/inactivo de un usuario.
     */
    @GetMapping("/toggle-status/{id}")
    public String toggleUserStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.toggleUserStatus(id);
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.SUCCESS_MESSAGE, AppConstants.SuccessMessages.USER_STATUS_UPDATED);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute(AppConstants.ModelAttributes.ERROR_MESSAGE, "Error al cambiar estado: " + e.getMessage());
        }
        return AppConstants.Redirects.ADMIN_USERS;
    }

    // --- Métodos privados de utilidad ---

    /**
     * Crea un objeto Pageable basado en los parámetros de paginación y ordenamiento.
     */
    private Pageable createPageable(int page, int size, String sort) {
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction sortDirection = Sort.Direction.fromString(sortParams[1].toUpperCase());
        
        return PageRequest.of(page - 1, size, Sort.by(sortDirection, sortBy));
    }

    /**
     * Busca usuarios aplicando filtros de búsqueda si se proporciona una palabra clave.
     */
    private Page<User> searchUsers(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return userService.searchUsers(keyword.trim(), pageable);
        }
        return userService.findAllUsers(pageable);
    }

    /**
     * Popula el modelo para la vista de lista de usuarios.
     */
    private void populateListModel(Model model, Page<User> userPage, int size, String sort, String keyword, HttpServletRequest request) {
        String[] sortParams = sort.split(",");
        
        model.addAttribute(AppConstants.ModelAttributes.USERS, userPage.getContent());
        model.addAttribute(AppConstants.ModelAttributes.CURRENT_PAGE, userPage.getNumber() + 1);
        model.addAttribute(AppConstants.ModelAttributes.TOTAL_PAGES, userPage.getTotalPages());
        model.addAttribute(AppConstants.ModelAttributes.TOTAL_ITEMS, userPage.getTotalElements());
        model.addAttribute(AppConstants.ModelAttributes.PAGE_SIZE, size);
        model.addAttribute(AppConstants.ModelAttributes.SORT_FIELD, sortParams[0]);
        model.addAttribute(AppConstants.ModelAttributes.SORT_DIRECTION, sortParams[1].toLowerCase());
        model.addAttribute(AppConstants.ModelAttributes.KEYWORD, keyword);
        model.addAttribute(AppConstants.ModelAttributes.CURRENT_URI, request.getRequestURI());
    }

    /**
     * Popula el modelo para la vista de formulario de usuario.
     */
    private void populateFormModel(Model model, UserRegistrationDto userDto, boolean isEdit, HttpServletRequest request) {
        model.addAttribute(AppConstants.ModelAttributes.USER_DTO, userDto);
        model.addAttribute(AppConstants.ModelAttributes.ALL_ROLES, roleRepository.findAll());
        model.addAttribute(AppConstants.ModelAttributes.IS_EDIT, isEdit);
        model.addAttribute(AppConstants.ModelAttributes.CURRENT_URI, request.getRequestURI());
    }

    /**
     * Busca un usuario por ID o lanza una excepción si no se encuentra.
     */
    private User findUserOrThrow(Long id) {
        return userService.findUserById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    /**
     * Crea un DTO de usuario a partir de una entidad User.
     */
    private UserRegistrationDto createUserDtoFromUser(User user) {
        UserRegistrationDto userDto = new UserRegistrationDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEnabled(user.isEnabled());
        userDto.setSelectedRoles(user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        
        return userDto;
    }
}