import React from "react";
import { FaUsersCog } from "react-icons/fa";

export default function EntrenadoresPersonal() {
  return (
    <div style={{display: 'flex', alignItems: 'center', gap: 16}}>
      <FaUsersCog size={40} color="#0072ff" />
      <h1>Entrenadores y Personal</h1>
    </div>
  );
}