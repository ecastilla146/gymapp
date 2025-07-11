import React from "react";
import { FaDumbbell } from "react-icons/fa";

export default function Dashboard() {
  return (
    <div style={{display: 'flex', alignItems: 'center', gap: 16}}>
      <FaDumbbell size={40} color="#0072ff" />
      <h1>Dashboard del Gimnasio</h1>
    </div>
  );
}