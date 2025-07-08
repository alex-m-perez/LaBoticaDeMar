    const links      = document.querySelectorAll('#section-nav a');
    const contentArea = document.getElementById('content-area');

    function loadSection(url, addToHistory = true) {
    fetch(url, {
        headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
        .then(res => {
            if (!res.ok) throw new Error('Error al cargar la vista.');
            return res.text();
        })
        .then(html => {
            // 1) Inserta el fragmento
            contentArea.innerHTML = html;

            // 2) Actualiza clases activas
            links.forEach(l => {
                l.classList.remove('text-pistachio', 'font-bold');
                l.classList.add('text-gray-500');
            });
            const active = Array.from(links)
                                .find(l => l.getAttribute('href') === url);
            if (active) {
                active.classList.remove('text-gray-500');
                active.classList.add('text-pistachio', 'font-bold');
            }

            // 3) Cambia la URL sin recargar (si procede)
            if (addToHistory) {
                history.pushState({ sectionUrl: url }, '', url);
            }

            // 4) Cargar dinámicamente el JS de la sección:
            debugger
            const section = url.split('/').pop();          // e.g. "products"
            const scriptId = 'section-script';
            // eliminar posible script anterior
            document.getElementById(scriptId)?.remove();
            // crear nuevo <script>
            const script = document.createElement('script');
            script.src = `${window.contextPath}/js/admin/${section}_dashboard.js`;
            script.id  = scriptId;
            script.defer = true;
            // cuando cargue, ejecutar el inicializador si existe
            script.onload = () => {
                const initFn = window[`initializePage`];
                if (typeof initFn === 'function') initFn();
            };
            document.body.appendChild(script);

            // 5) history
            if (addToHistory) history.pushState({ sectionUrl: url }, '', url);
        })
        .catch(err => {
            contentArea.innerHTML = `<p class="text-red-500">Error al cargar el contenido.</p>`;
            console.error(err);
        });
    }

    //  A) Hijack clicks para navegación interna
    links.forEach(link => {
        link.addEventListener('click', e => {
            e.preventDefault();
            const url = link.getAttribute('href');
            loadSection(url, true);
        });
    });

    //  B) Manejar back/forward
    window.addEventListener('popstate', (e) => {
        const state = e.state;
        if (state && state.sectionUrl) {
            loadSection(state.sectionUrl, false);
        } else {
            // Sin estado, volvemos a home
            loadSection('/admin/home', false);
        }
    });

    // C) Carga inicial según URL
    document.addEventListener('DOMContentLoaded', () => {
        const path = window.location.pathname;
        // definimos la URL por defecto
        const defaultSection = '/admin/ventas';

        if (path !== '/admin/home') {
            // si venimos de otra sección (o acceden directamente) la cargamos
            loadSection(path, false);
        } else {
            // si estamos en home sin selección previa, cargamos ventas
            loadSection(defaultSection, false);
        }
    });

