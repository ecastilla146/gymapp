package com.gymmanagement.gymapp.config;

import com.gymmanagement.gymapp.model.MembershipPlan;
import com.gymmanagement.gymapp.model.Role;
import com.gymmanagement.gymapp.model.User;
import com.gymmanagement.gymapp.repository.MembershipPlanRepository;
import com.gymmanagement.gymapp.repository.RoleRepository;
import com.gymmanagement.gymapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Component
public class DataLoader {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipPlanRepository membershipPlanRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        createRolesIfNotExists();
        createAdminUserIfNotExists();
        createMembershipPlansIfNotExists();
    }

    /**
     * Crea los roles por defecto si no existen
     */
    private void createRolesIfNotExists() {
        if (roleRepository.count() == 0) {
            log.info("Creando roles por defecto...");

            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Administrador del sistema");
            roleRepository.save(adminRole);

            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setDescription("Usuario cliente");
            roleRepository.save(userRole);

            log.info("Roles creados exitosamente");
        }
    }

    /**
     * Crea el usuario administrador por defecto si no existe
     */
    private void createAdminUserIfNotExists() {
        if (userRepository.findByEmail("admin@kronos.com").isEmpty()) {
            log.info("Creando usuario administrador por defecto...");

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));

            User admin = new User();
            admin.setFirstName("Administrador");
            admin.setLastName("Kronos");
            admin.setEmail("admin@kronos.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setRoles(Set.of(adminRole));
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());

            userRepository.save(admin);
            log.info("Usuario administrador creado exitosamente");
        }
    }

    /**
     * Crea planes de membresía por defecto si no existen
     */
    private void createMembershipPlansIfNotExists() {
        if (membershipPlanRepository.count() == 0) {
            log.info("Creando planes de membresía por defecto...");

            // Plan Elite (Premium)
            MembershipPlan elitePlan = new MembershipPlan();
            elitePlan.setName("🥇 Kronos Elite");
            elitePlan.setDescription("Entrena en todas las sedes KRONOS sin límites. Incluye acceso ilimitado a todas las sedes, invitados gratuitos, Kronos Spa, descuentos exclusivos y todas las funcionalidades premium.");
            elitePlan.setDurationMonths(12);
            elitePlan.setPrice(new BigDecimal("109900"));
            elitePlan.setIsActive(true);
            membershipPlanRepository.save(elitePlan);

            // Plan Power (Intermedio)
            MembershipPlan powerPlan = new MembershipPlan();
            powerPlan.setName("🥈 Kronos Power");
            powerPlan.setDescription("Entrena cuando quieras en tu sede preferida. Incluye plan personalizado, seguimiento de progreso, entrenamientos virtuales y clases grupales.");
            powerPlan.setDurationMonths(12);
            powerPlan.setPrice(new BigDecimal("69900"));
            powerPlan.setIsActive(true);
            membershipPlanRepository.save(powerPlan);

            // Plan Alpha (Básico)
            MembershipPlan alphaPlan = new MembershipPlan();
            alphaPlan.setName("🥉 Kronos Alpha");
            alphaPlan.setDescription("Ideal para comenzar tu transformación. Sin fidelidad, incluye plan personalizado, seguimiento de progreso, entrenamientos virtuales y clases grupales.");
            alphaPlan.setDurationMonths(1);
            alphaPlan.setPrice(new BigDecimal("89900"));
            alphaPlan.setIsActive(true);
            membershipPlanRepository.save(alphaPlan);

            log.info("Planes de membresía creados exitosamente");
        }
    }
}