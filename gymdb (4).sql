-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 11-07-2025 a las 08:49:40
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gymdb`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `attendance`
--

CREATE TABLE `attendance` (
  `id` bigint(20) NOT NULL,
  `check_in_time` datetime(6) NOT NULL,
  `check_out_time` datetime(6) DEFAULT NULL,
  `created_at` datetime(6) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `attendance`
--

INSERT INTO `attendance` (`id`, `check_in_time`, `check_out_time`, `created_at`, `updated_at`, `user_id`) VALUES
(1, '2025-07-10 22:56:19.000000', NULL, '2025-07-11 00:56:19.000000', '2025-07-11 00:56:19.000000', 3),
(2, '2025-07-10 23:56:19.000000', NULL, '2025-07-11 00:56:19.000000', '2025-07-11 00:56:19.000000', 4),
(5, '2025-07-10 23:00:48.000000', NULL, '2025-07-11 01:00:48.000000', '2025-07-11 01:00:48.000000', 3),
(6, '2025-07-11 00:00:48.000000', NULL, '2025-07-11 01:00:48.000000', '2025-07-11 01:00:48.000000', 4),
(9, '2025-07-10 23:37:02.000000', NULL, '2025-07-11 01:37:02.000000', '2025-07-11 01:37:02.000000', 3),
(10, '2025-07-11 00:37:02.000000', NULL, '2025-07-11 01:37:02.000000', '2025-07-11 01:37:02.000000', 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `class_participants`
--

CREATE TABLE `class_participants` (
  `class_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `financial_transactions`
--

CREATE TABLE `financial_transactions` (
  `id` bigint(20) NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `category` varchar(50) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(200) NOT NULL,
  `notes` varchar(500) DEFAULT NULL,
  `payment_method` enum('CARD','CASH','OTHER','TRANSFER') DEFAULT NULL,
  `reference_number` varchar(100) DEFAULT NULL,
  `status` enum('CANCELLED','COMPLETED','PENDING','REFUNDED') NOT NULL,
  `transaction_date` datetime(6) NOT NULL,
  `transaction_type` enum('EXPENSE','INCOME') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `membership_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `gym_classes`
--

CREATE TABLE `gym_classes` (
  `id` bigint(20) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `end_time` datetime(6) NOT NULL,
  `max_participants` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `room` varchar(50) DEFAULT NULL,
  `start_time` datetime(6) NOT NULL,
  `status` enum('CANCELLED','COMPLETED','IN_PROGRESS','SCHEDULED') NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `instructor_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventory_items`
--

CREATE TABLE `inventory_items` (
  `id` bigint(20) NOT NULL,
  `brand` varchar(50) DEFAULT NULL,
  `category` varchar(50) NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `current_stock` int(11) NOT NULL,
  `description` varchar(500) DEFAULT NULL,
  `last_maintenance` datetime(6) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `max_stock_level` int(11) DEFAULT NULL,
  `min_stock_level` int(11) NOT NULL,
  `model` varchar(50) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `next_maintenance` datetime(6) DEFAULT NULL,
  `notes` varchar(1000) DEFAULT NULL,
  `purchase_date` datetime(6) DEFAULT NULL,
  `serial_number` varchar(100) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','MAINTENANCE','OUT_OF_ORDER','RETIRED') NOT NULL,
  `supplier` varchar(100) DEFAULT NULL,
  `unit_price` decimal(10,2) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `warranty_expiry` datetime(6) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `memberships`
--

CREATE TABLE `memberships` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `plan_id` bigint(20) NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `amount` decimal(10,2) NOT NULL,
  `enabled` bigint(50) NOT NULL,
  `payment_method` varchar(50) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `status` enum('ACTIVE','CANCELLED','EXPIRED','PENDING_PAYMENT') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `memberships`
--

INSERT INTO `memberships` (`id`, `user_id`, `plan_id`, `start_date`, `end_date`, `amount`, `enabled`, `payment_method`, `created_at`, `updated_at`, `status`) VALUES
(1, 4, 2, '2025-05-28', '2025-08-27', 80.00, 0, 'CASH', '2025-05-29 00:59:06', '2025-05-29 00:59:06', 'ACTIVE'),
(2, 3, 2, '2025-06-11', '2025-09-11', 80.00, 0, 'TRANSFER', '2025-07-11 05:56:19', '2025-07-11 05:56:19', 'ACTIVE'),
(3, 4, 3, '2025-05-11', '2025-11-11', 150.00, 0, 'CASH', '2025-07-11 05:56:19', '2025-07-11 05:56:19', 'ACTIVE'),
(6, 3, 2, '2025-06-11', '2025-09-11', 80.00, 0, 'TRANSFER', '2025-07-11 06:00:48', '2025-07-11 06:00:48', 'ACTIVE'),
(7, 4, 3, '2025-05-11', '2025-11-11', 150.00, 0, 'CASH', '2025-07-11 06:00:48', '2025-07-11 06:00:48', 'ACTIVE'),
(10, 3, 2, '2025-06-11', '2025-09-11', 80.00, 0, 'TRANSFER', '2025-07-11 06:37:02', '2025-07-11 06:37:02', 'ACTIVE'),
(11, 4, 3, '2025-05-11', '2025-11-11', 150.00, 0, 'CASH', '2025-07-11 06:37:02', '2025-07-11 06:37:02', 'ACTIVE');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `membership_plans`
--

CREATE TABLE `membership_plans` (
  `id` bigint(20) NOT NULL,
  `name` varchar(100) NOT NULL,
  `description` text DEFAULT NULL,
  `duration_months` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `is_active` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `membership_plans`
--

INSERT INTO `membership_plans` (`id`, `name`, `description`, `duration_months`, `price`, `is_active`) VALUES
(1, 'Mensual', 'Acceso completo al gimnasio por un mes.', 1, 30.00, 1),
(2, 'Trimestral', 'Acceso completo al gimnasio por tres meses con descuento.', 3, 80.00, 1),
(3, 'Anual', 'Acceso completo al gimnasio por un año con el mejor descuento.', 12, 300.00, 1),
(4, 'Clase Personalizada (1 mes)', 'Acceso a clases personalizadas durante un mes.', 1, 50.00, 1),
(5, 'Semestral', 'Membresía semestral con descuento', 6, 150.00, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `name`) VALUES
(1, 'ROLE_ADMIN'),
(3, 'ROLE_TRAINER'),
(2, 'ROLE_USER');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `support_tickets`
--

CREATE TABLE `support_tickets` (
  `id` bigint(20) NOT NULL,
  `category` enum('BILLING','CLASSES','COMPLAINT','EQUIPMENT','FACILITIES','GENERAL','MEMBERSHIP','SUGGESTION','TECHNICAL') NOT NULL,
  `created_at` datetime(6) NOT NULL,
  `description` varchar(2000) NOT NULL,
  `priority` enum('HIGH','LOW','MEDIUM','URGENT') NOT NULL,
  `resolution` varchar(2000) DEFAULT NULL,
  `resolved_at` datetime(6) DEFAULT NULL,
  `status` enum('CLOSED','IN_PROGRESS','OPEN','RESOLVED') NOT NULL,
  `title` varchar(200) NOT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `assigned_to_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `enabled` bit(1) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `email`, `created_at`, `updated_at`, `enabled`, `first_name`, `last_name`) VALUES
(1, 'gymuser', '12345', 'testuser@kronosfit.com', '2025-05-28 01:22:16', '2025-07-11 06:43:00', b'1', '', ''),
(3, 'adminuser', 'coach', 'admin@kronosfit.com', '2025-05-28 15:36:19', '2025-07-11 06:02:28', b'1', 'Admin', 'User'),
(4, 'ecastilla', '$2a$10$SDC.crxyKSPsEJoQBdomUuGQ4v7XJN.QX9KVp2xn30aco60IlKUbG', 'ecastilla146@gmail.com', '2025-05-28 22:09:01', '2025-07-08 12:31:46', b'1', 'Emmanuel ', 'Castilla'),
(8, 'admin', 'admin12', 'admin@kronos.com', '2025-07-08 22:42:08', '2025-07-11 06:01:15', b'1', 'Administrador', 'Kronos'),
(9, 'juan.perez', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'juan.perez@email.com', '2025-07-11 05:56:19', '2025-07-11 05:56:19', b'1', 'Juan', 'Pérez'),
(10, 'maria.garcia', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'maria.garcia@email.com', '2025-07-11 05:56:19', '2025-07-11 05:56:19', b'1', 'María', 'García'),
(11, 'carlos.lopez', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'carlos.lopez@email.com', '2025-07-11 05:56:19', '2025-07-11 05:56:19', b'1', 'Carlos', 'López'),
(12, 'ana.martinez', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', 'ana.martinez@email.com', '2025-07-11 05:56:19', '2025-07-11 05:56:19', b'1', 'Ana', 'Martínez');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users_roles`
--

CREATE TABLE `users_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `users_roles`
--

INSERT INTO `users_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(3, 2),
(4, 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKjcaqd29v2qy723owsdah2t8vx` (`user_id`);

--
-- Indices de la tabla `class_participants`
--
ALTER TABLE `class_participants`
  ADD KEY `FK645u0cmr43ptw2ukrw9t6r66k` (`user_id`),
  ADD KEY `FKtl540vb94uetv9gc4lmy66ifq` (`class_id`);

--
-- Indices de la tabla `financial_transactions`
--
ALTER TABLE `financial_transactions`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_transaction_date` (`transaction_date`),
  ADD KEY `idx_transaction_type` (`transaction_type`),
  ADD KEY `idx_transaction_category` (`category`),
  ADD KEY `idx_transaction_user` (`user_id`),
  ADD KEY `FK3nr3g76l896so1pl8dnijwm0f` (`membership_id`);

--
-- Indices de la tabla `gym_classes`
--
ALTER TABLE `gym_classes`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_class_start_time` (`start_time`),
  ADD KEY `idx_class_instructor` (`instructor_id`),
  ADD KEY `idx_class_status` (`status`);

--
-- Indices de la tabla `inventory_items`
--
ALTER TABLE `inventory_items`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_inventory_category` (`category`),
  ADD KEY `idx_inventory_status` (`status`),
  ADD KEY `idx_inventory_stock` (`current_stock`);

--
-- Indices de la tabla `memberships`
--
ALTER TABLE `memberships`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKltstb0n7ilare6ei97tafb7fw` (`plan_id`),
  ADD KEY `FKdjormybfoo7f4i4d4r803qohb` (`user_id`);

--
-- Indices de la tabla `membership_plans`
--
ALTER TABLE `membership_plans`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`),
  ADD UNIQUE KEY `UKnconflq2x1uvxvs7sc0mnmh4` (`name`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UKofx66keruapi6vyqpv6f2or37` (`name`);

--
-- Indices de la tabla `support_tickets`
--
ALTER TABLE `support_tickets`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_ticket_status` (`status`),
  ADD KEY `idx_ticket_priority` (`priority`),
  ADD KEY `idx_ticket_user` (`user_id`),
  ADD KEY `idx_ticket_assigned` (`assigned_to_id`),
  ADD KEY `idx_ticket_created` (`created_at`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  ADD UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`);

--
-- Indices de la tabla `users_roles`
--
ALTER TABLE `users_roles`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKj6m8fwv7oqv74fcehir1a9ffy` (`role_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `attendance`
--
ALTER TABLE `attendance`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `financial_transactions`
--
ALTER TABLE `financial_transactions`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `gym_classes`
--
ALTER TABLE `gym_classes`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `inventory_items`
--
ALTER TABLE `inventory_items`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `memberships`
--
ALTER TABLE `memberships`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT de la tabla `membership_plans`
--
ALTER TABLE `membership_plans`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT de la tabla `support_tickets`
--
ALTER TABLE `support_tickets`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `attendance`
--
ALTER TABLE `attendance`
  ADD CONSTRAINT `FKjcaqd29v2qy723owsdah2t8vx` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `class_participants`
--
ALTER TABLE `class_participants`
  ADD CONSTRAINT `FK645u0cmr43ptw2ukrw9t6r66k` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKtl540vb94uetv9gc4lmy66ifq` FOREIGN KEY (`class_id`) REFERENCES `gym_classes` (`id`);

--
-- Filtros para la tabla `financial_transactions`
--
ALTER TABLE `financial_transactions`
  ADD CONSTRAINT `FK3nr3g76l896so1pl8dnijwm0f` FOREIGN KEY (`membership_id`) REFERENCES `memberships` (`id`),
  ADD CONSTRAINT `FKow47c24e9ahwhnm52etcbcre4` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `gym_classes`
--
ALTER TABLE `gym_classes`
  ADD CONSTRAINT `FKkgr1itwb64nv7k80wc4txuerl` FOREIGN KEY (`instructor_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `memberships`
--
ALTER TABLE `memberships`
  ADD CONSTRAINT `memberships_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `memberships_ibfk_2` FOREIGN KEY (`plan_id`) REFERENCES `membership_plans` (`id`);

--
-- Filtros para la tabla `support_tickets`
--
ALTER TABLE `support_tickets`
  ADD CONSTRAINT `FK4reg1h2465c00bg6dmqlv7ujv` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKq26j5vg16vga26rlfibd8c7p5` FOREIGN KEY (`assigned_to_id`) REFERENCES `users` (`id`);

--
-- Filtros para la tabla `users_roles`
--
ALTER TABLE `users_roles`
  ADD CONSTRAINT `FK2o0jvgh89lemvvo17cbqvdxaa` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  ADD CONSTRAINT `FKj6m8fwv7oqv74fcehir1a9ffy` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
