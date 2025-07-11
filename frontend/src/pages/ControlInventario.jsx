import React from "react";
import { FaBoxes } from "react-icons/fa";

export default function ControlInventario() {
  return (
    <div style={{display: 'flex', alignItems: 'center', gap: 16}}>
      <FaBoxes size={40} color="#0072ff" />
      <h1>Control de Inventario</h1>
    </div>
  );
}