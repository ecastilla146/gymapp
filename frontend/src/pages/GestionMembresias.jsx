import React from "react";
import { FaIdCard } from "react-icons/fa";

export default function GestionMembresias() {
  return (
    <div style={{display: 'flex', alignItems: 'center', gap: 16}}>
      <FaIdCard size={40} color="#0072ff" />
      <h1>Gestión de Membresías</h1>
    </div>
  );
}