import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import SidebarLayout from "./components/SidebarLayout";
import { AnimatePresence, motion } from "framer-motion";
import Dashboard from "./pages/Dashboard";
import GestionUsuarios from "./pages/GestionUsuarios";
import GestionMembresias from "./pages/GestionMembresias";
import ControlAsistencia from "./pages/ControlAsistencia";
import ClasesActividades from "./pages/ClasesActividades";
import EntrenadoresPersonal from "./pages/EntrenadoresPersonal";
import ControlInventario from "./pages/ControlInventario";
import GestionFinanciera from "./pages/GestionFinanciera";
import SoporteComunicacion from "./pages/SoporteComunicacion";
import "./App.css";

const pageVariants = {
  initial: { opacity: 0, x: 50 },
  in: { opacity: 1, x: 0 },
  out: { opacity: 0, x: -50 },
};

const pageTransition = {
  type: "spring",
  stiffness: 300,
  damping: 30,
};

function AnimatedPage({ children }) {
  return (
    <motion.div
      initial="initial"
      animate="in"
      exit="out"
      variants={pageVariants}
      transition={pageTransition}
      style={{ height: "100%" }}
    >
      {children}
    </motion.div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <SidebarLayout>
        <AnimatePresence mode="wait">
          <Routes>
            <Route path="/" element={<AnimatedPage><Dashboard /></AnimatedPage>} />
            <Route path="/usuarios" element={<AnimatedPage><GestionUsuarios /></AnimatedPage>} />
            <Route path="/membresias" element={<AnimatedPage><GestionMembresias /></AnimatedPage>} />
            <Route path="/asistencia" element={<AnimatedPage><ControlAsistencia /></AnimatedPage>} />
            <Route path="/clases" element={<AnimatedPage><ClasesActividades /></AnimatedPage>} />
            <Route path="/entrenadores" element={<AnimatedPage><EntrenadoresPersonal /></AnimatedPage>} />
            <Route path="/inventario" element={<AnimatedPage><ControlInventario /></AnimatedPage>} />
            <Route path="/finanzas" element={<AnimatedPage><GestionFinanciera /></AnimatedPage>} />
            <Route path="/soporte" element={<AnimatedPage><SoporteComunicacion /></AnimatedPage>} />
          </Routes>
        </AnimatePresence>
      </SidebarLayout>
    </BrowserRouter>
  );
}
