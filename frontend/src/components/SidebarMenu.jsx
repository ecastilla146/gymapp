import React from "react";
import { NavLink } from "react-router-dom";
import { FaDumbbell, FaUserFriends, FaIdCard, FaClipboardList, FaChalkboardTeacher, FaUsersCog, FaBoxes, FaChartLine, FaHeadset } from "react-icons/fa";
import "./SidebarMenu.css";

const menuItems = [
  { to: "/", label: "Dashboard", icon: <FaDumbbell /> },
  { to: "/usuarios", label: "Gestión de Usuarios", icon: <FaUserFriends /> },
  { to: "/membresias", label: "Gestión de Membresías", icon: <FaIdCard /> },
  { to: "/asistencia", label: "Control de Asistencia", icon: <FaClipboardList /> },
  { to: "/clases", label: "Clases y Actividades", icon: <FaChalkboardTeacher /> },
  { to: "/entrenadores", label: "Entrenadores y Personal", icon: <FaUsersCog /> },
  { to: "/inventario", label: "Control de Inventario", icon: <FaBoxes /> },
  { to: "/finanzas", label: "Gestión Financiera", icon: <FaChartLine /> },
  { to: "/soporte", label: "Soporte y Comunicación", icon: <FaHeadset /> },
];

export default function SidebarMenu() {
  return (
    <nav className="sidebar-menu">
      {menuItems.map((item) => (
        <NavLink
          key={item.to}
          to={item.to}
          className={({ isActive }) =>
            isActive ? "menu-item active" : "menu-item"
          }
        >
          <span className="icon">{item.icon}</span>
          <span className="label">{item.label}</span>
        </NavLink>
      ))}
    </nav>
  );
}