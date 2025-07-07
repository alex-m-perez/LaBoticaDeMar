/**
 * Gestiona la navegación modular y la carga dinámica de contenido y scripts 
 * para la sección de perfil de usuario.
 */
document.addEventListener('DOMContentLoaded', () => {

    const links       = document.querySelectorAll('#section-nav a');
    const contentArea = document.getElementById('content-area');
    const contextPath = document.body.dataset.contextPath || '';

    /**
     * Carga un fragmento de HTML y su script asociado.
     * @param {string} url La URL de la sección a cargar.
     * @param {boolean} addToHistory Si se debe añadir al historial del navegador.
     */
    function loadSection(url, addToHistory = true) {
        contentArea.innerHTML = '<p class="text-center p-10">Cargando...</p>';

        fetch(url, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
        .then(res => {
            if (!res.ok) throw new Error(`Error al cargar la vista desde ${url}`);
            return res.text();
        })
        .then(html => {
            // 1) Inserta el fragmento HTML
            contentArea.innerHTML = html;

            // 2) Actualiza las clases activas en la navegación
            updateActiveLink(url);

            // 3) Carga dinámicamente el script de la sección
            loadSectionScript(url);

            // 4) Actualiza la URL en el historial del navegador
            if (addToHistory) {
                history.pushState({ sectionUrl: url }, '', url);
            }
        })
        .catch(err => {
            contentArea.innerHTML = `<p class="text-red-500 text-center">Error: No se pudo cargar el contenido.</p>`;
            console.error(err);
        });
    }

    /**
     * Resalta el enlace de navegación activo.
     * @param {string} url La URL activa.
     */
    function updateActiveLink(url) {
        links.forEach(l => {
            l.classList.remove('text-pistachio', 'font-bold');
            l.classList.add('text-gray-500');
        });
        const active = Array.from(links).find(l => l.getAttribute('href') === url);
        if (active) {
            active.classList.remove('text-gray-500');
            active.classList.add('text-pistachio', 'font-bold');
        }
    }

    /**
     * Carga el JS específico para la sección recién inyectada.
     * @param {string} url La URL de la sección.
     */
    function loadSectionScript(url) {
        const section = url.split('/').pop(); // e.g., "datos" de "/profile/datos_personales"
        const scriptId = 'section-script';

        // Elimina el script de la sección anterior para evitar conflictos
        document.getElementById(scriptId)?.remove();
        
        // Crea el nuevo tag <script>
        const script = document.createElement('script');
        // **NUEVA CONVENCIÓN DE NOMBRES PARA SCRIPTS DE PERFIL**
        script.src = `${contextPath}/js/user/${section}.js`; 
        script.id = scriptId;
        script.defer = true;
        
        // Opcional: Ejecuta una función de inicialización cuando el script cargue
        script.onload = () => {
            // Para "datos", buscará la función global `initDatosPage()`
            const initFunctionName = `init${section.charAt(0).toUpperCase() + section.slice(1)}Page`;
            if (typeof window[initFunctionName] === 'function') {
                window[initFunctionName]();
            }
        };

        document.body.appendChild(script);
    }


    // A) Interceptar clics para la navegación interna
    links.forEach(link => {
        link.addEventListener('click', e => {
            e.preventDefault();
            const url = link.getAttribute('href');
            if (window.location.pathname !== url) {
                loadSection(url, true);
            }
        });
    });

    // B) Manejar los botones "atrás" y "adelante" del navegador
    window.addEventListener('popstate', (e) => {
        const defaultUrl = '/profile/datos_personales';
        if (e.state && e.state.sectionUrl) {
            loadSection(e.state.sectionUrl, false);
        } else {
            // Si no hay estado, volvemos a la sección por defecto del perfil
            loadSection(defaultUrl, false);
        }
    });

    // C) Carga inicial de la página según la URL
    const validSections = ['/profile/datos_personales', '/profile/pedidos', '/profile/devoluciones'];
    const currentPath = window.location.pathname;
    const defaultSection = '/profile/datos_personales';

    if (validSections.includes(currentPath)) {
        // Si se accede directamente a una URL de sección válida, cárgala
        loadSection(currentPath, false);
    } else {
        // Si se accede a "/profile" o una URL no válida, carga la sección por defecto
        loadSection(defaultSection, true); // true para actualizar la URL a /profile/datos_personales
    }
});