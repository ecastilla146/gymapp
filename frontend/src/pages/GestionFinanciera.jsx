import React from "react";
import { FaChartLine } from "react-icons/fa";

export default function GestionFinanciera() {
  return (
    <div style={{display: 'flex', alignItems: 'center', gap: 16}}>
      <FaChartLine size={40} color="#0072ff" />
      <h1>Gestión Financiera</h1>
    </div>
  );
}