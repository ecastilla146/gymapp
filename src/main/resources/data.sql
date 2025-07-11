-- Datos de prueba para Kronos Gym

-- Insertar roles
INSERT INTO roles (name, description) VALUES 
('ROLE_ADMIN', 'Administrador del sistema'),
('ROLE_USER', 'Usuario regular'),
('ROLE_TRAINER', 'Entrenador');

-- Insertar usuarios de prueba
INSERT INTO users (username, password, email, first_name, last_name, enabled, created_at, updated_at) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'admin@kronosgym.com', 'Admin', 'Sistema', true, NOW(), NOW()),
('juan.perez', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'juan.perez@email.com', 'Juan', 'Pérez', true, NOW(), NOW()),
('maria.garcia', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'maria.garcia@email.com', 'María', 'García', true, NOW(), NOW()),
('carlos.lopez', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'carlos.lopez@email.com', 'Carlos', 'López', true, NOW(), NOW()),
('ana.martinez', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ana.martinez@email.com', 'Ana', 'Martínez', true, NOW(), NOW());

-- Asignar roles a usuarios
INSERT INTO users_roles (user_id, role_id) VALUES 
(1, 1), -- admin -> ROLE_ADMIN
(2, 2), -- juan.perez -> ROLE_USER
(3, 2), -- maria.garcia -> ROLE_USER
(4, 2), -- carlos.lopez -> ROLE_USER
(5, 2); -- ana.martinez -> ROLE_USER

-- Insertar planes de membresía
INSERT INTO membership_plans (name, description, price, duration_months, created_at, updated_at) VALUES 
('Mensual', 'Membresía mensual con acceso completo', 30.00, 1, NOW(), NOW()),
('Trimestral', 'Membresía trimestral con descuento', 80.00, 3, NOW(), NOW()),
('Semestral', 'Membresía semestral con descuento', 150.00, 6, NOW(), NOW()),
('Anual', 'Membresía anual con máximo descuento', 280.00, 12, NOW(), NOW());

-- Insertar membresías de prueba
INSERT INTO memberships (user_id, plan_id, start_date, end_date, amount, status, payment_method, created_at, updated_at) VALUES 
(2, 1, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 MONTH), 30.00, 'ACTIVE', 'CARD', NOW(), NOW()),
(3, 2, DATE_SUB(CURDATE(), INTERVAL 1 MONTH), DATE_ADD(CURDATE(), INTERVAL 2 MONTH), 80.00, 'ACTIVE', 'TRANSFER', NOW(), NOW()),
(4, 3, DATE_SUB(CURDATE(), INTERVAL 2 MONTH), DATE_ADD(CURDATE(), INTERVAL 4 MONTH), 150.00, 'ACTIVE', 'CASH', NOW(), NOW()),
(5, 4, DATE_SUB(CURDATE(), INTERVAL 3 MONTH), DATE_ADD(CURDATE(), INTERVAL 9 MONTH), 280.00, 'ACTIVE', 'CARD', NOW(), NOW());

-- Insertar asistencias de prueba para hoy
INSERT INTO attendance (user_id, check_in_time, created_at, updated_at) VALUES 
(2, NOW(), NOW(), NOW()),
(3, DATE_SUB(NOW(), INTERVAL 2 HOUR), NOW(), NOW()),
(4, DATE_SUB(NOW(), INTERVAL 1 HOUR), NOW(), NOW());

-- Insertar una asistencia completada (con check-out)
INSERT INTO attendance (user_id, check_in_time, check_out_time, created_at, updated_at) VALUES 
(5, DATE_SUB(NOW(), INTERVAL 3 HOUR), DATE_SUB(NOW(), INTERVAL 1 HOUR), NOW(), NOW());