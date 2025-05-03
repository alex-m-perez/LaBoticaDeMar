// secciones.js

document.addEventListener("DOMContentLoaded", function () {
    const links = document.querySelectorAll('#section-nav a');
    const contentArea = document.getElementById('content-area');

    const sectionRoutes = {
        "Ventas": "/admin/ventas",
        "Devoluciones": "views/admin/returns/sample.jsp",
        "Productos": "views/admin/products/sample.jsp",
        "Ofertas": "views/admin/offers/sample.jsp",
        "Empleados": "views/admin/employees/sample.jsp",
        "Usuarios": "views/admin/users/sample.jsp"
    };

    links.forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();

            // Estilos activos
            links.forEach(l => l.classList.remove("text-black"));
            links.forEach(l => l.classList.add("text-gray-500"));

            this.classList.remove("text-gray-500");
            this.classList.add("text-black");

            const section = this.getAttribute("data-section");
            const url = sectionRoutes[section];

            if (url) {
                fetch(url)
                    .then(response => {
                        if (!response.ok) throw new Error("Error al cargar contenido");
                        return response.text();
                    })
                    .then(html => {
                        contentArea.innerHTML = html;
                    })
                    .catch(err => {
                        contentArea.innerHTML = "<p class='text-red-500'>No se pudo cargar el contenido.</p>";
                        console.error(err);
                    });
            }
        });
    });
});
