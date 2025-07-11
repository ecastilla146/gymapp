import React from "react";
import { FaUserFriends } from "react-icons/fa";

export default function GestionUsuarios() {
  return (
    <div style={{display: 'flex', alignItems: 'center', gap: 16}}>
      <FaUserFriends size={40} color="#0072ff" />
      <h1>Gestión de Usuarios</h1>
    </div>
  );
}