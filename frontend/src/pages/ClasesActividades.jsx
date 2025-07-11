import React from "react";
import { FaChalkboardTeacher } from "react-icons/fa";

export default function ClasesActividades() {
  return (
    <div style={{display: 'flex', alignItems: 'center', gap: 16}}>
      <FaChalkboardTeacher size={40} color="#0072ff" />
      <h1>Clases y Actividades</h1>
    </div>
  );
}