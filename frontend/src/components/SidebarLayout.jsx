import React from "react";
import SidebarMenu from "./SidebarMenu";
import "./SidebarLayout.css";

export default function SidebarLayout({ children }) {
  return (
    <div className="layout">
      <aside className="sidebar">
        <SidebarMenu />
      </aside>
      <main className="main-content">{children}</main>
    </div>
  );
}