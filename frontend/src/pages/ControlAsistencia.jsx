import React from "react";
import { FaClipboardList } from "react-icons/fa";

export default function ControlAsistencia() {
  return (
    <div style={{display: 'flex', alignItems: 'center', gap: 16}}>
      <FaClipboardList size={40} color="#0072ff" />
      <h1>Control de Asistencia</h1>
    </div>
  );
}