// src/main/resources/static/js/admin-dashboard.js

document.addEventListener('DOMContentLoaded', function() {
    const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
    const dashboardMainArea = document.querySelector('.dashboard-main-area');

    navLinks.forEach(link => {
        link.addEventListener('click', function(event) {
            // Eliminar la clase 'active' de todos los elementos del menú
            navLinks.forEach(nav => nav.parentElement.classList.remove('active'));
            // Añadir la clase 'active' al elemento del menú clicado
            this.parentElement.classList.add('active');

            // Prevenir la navegación por defecto si se va a cargar contenido dinámico
            // event.preventDefault(); // Descomenta si usas carga AJAX de contenido

            // Lógica para cargar contenido dinámico si usas AJAX
            // const targetUrl = this.getAttribute('href');
            // if (targetUrl.startsWith('/admin/') && targetUrl !== '/admin/dashboard' && targetUrl !== '/admin/logout') {
            //     loadContent(targetUrl);
            // } else if (targetUrl === '/admin/dashboard') {
            //     // Mostrar la sección principal del dashboard
            //     document.querySelectorAll('.dashboard-section').forEach(section => {
            //         section.classList.remove('active');
            //     });
            //     document.getElementById('main-dashboard').classList.add('active');
            // }
        });
    });

    // Función de ejemplo para cargar contenido con AJAX (futuro)
    // async function loadContent(url) {
    //     try {
    //         const response = await fetch(url);
    //         if (!response.ok) {
    //             throw new Error(`HTTP error! status: ${response.status}`);
    //         }
    //         const content = await response.text();
    //         dashboardMainArea.innerHTML = content; // Carga el HTML en el área principal
    //
    //         // Oculta todas las secciones predefinidas y muestra solo el contenido cargado
    //         document.querySelectorAll('.dashboard-section').forEach(section => {
    //             section.classList.remove('active');
    //         });
    //
    //         // Puedes necesitar reinicializar scripts para el nuevo contenido cargado
    //         // Por ejemplo, si los gráficos Chart.js se cargan dinámicamente
    //         // initializeCharts();
    //
    //     } catch (error) {
    //         console.error("Error al cargar el contenido:", error);
    //         dashboardMainArea.innerHTML = "<p>Error al cargar la sección. Por favor, inténtelo de nuevo.</p>";
    //     }
    // }
});