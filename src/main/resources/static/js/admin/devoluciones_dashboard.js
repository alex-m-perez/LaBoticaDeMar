(function() {
    debugger
    let isInitialized = false;

    function initializePage() {
        if (isInitialized) return;

        // --- Elementos del DOM ---
        const filterForm = document.getElementById('returnsFilterForm');
        const tbody = document.getElementById('returnsTbody');
        const prevBtn = document.getElementById('prevBtn');
        const nextBtn = document.getElementById('nextBtn');
        const pageNumEl = document.getElementById('pageNum');
        const totalPagesEl = document.getElementById('totalPages');
        const userInput = document.getElementById('nombreUsuario');
        const clienteIdInput = document.getElementById('clienteId');
        const userSuggestions = document.getElementById('userSuggestions');
        const fechaInicioInput = document.getElementById('fechaInicio');
        const fechaFinInput = document.getElementById('fechaFin');

        // --- Elementos de KPIs ---
        const kpiDevolucionesHoy = document.getElementById('kpi-devoluciones-hoy');
        const kpiMontoHoy = document.getElementById('kpi-monto-hoy');
        const kpiDevolucionesRango = document.getElementById('kpi-devoluciones-rango');
        const kpiMontoRango = document.getElementById('kpi-monto-rango');
        
        // --- Elementos del Modal (CORREGIDOS) ---
        const modal = document.getElementById('returnDetailModal');
        const modalOverlay = document.getElementById('modalOverlay');
        const closeModalBtn = document.getElementById('closeModalBtn');
        const modalTitle = document.getElementById('modalTitle');
        const modalBody = document.getElementById('modalBody');

        if (!filterForm || !tbody || !modal) {
            console.error("Faltan elementos del DOM para inicializar la página de devoluciones.");
            return;
        }

        // --- Estado y Funciones de Utilidad ---
        let currentPage = 0, totalPages = 1, size = 15, searchTimer;
        const formatCurrency = value => new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(value || 0);
        const formatDateTime = dateStr => new Date(dateStr).toLocaleString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });

        function setDefaultDateRange() {
            const now = new Date();
            const year = now.getFullYear();
            const month = now.getMonth();

            const firstDay = new Date(year, month, 1);
            const lastDay = new Date(year, month + 1, 0);

            // Formato YYYY-MM-DD requerido por <input type="date">
            const toInputFormat = (date) => {
                const y = date.getFullYear();
                const m = String(date.getMonth() + 1).padStart(2, '0');
                const d = String(date.getDate()).padStart(2, '0');
                return `${y}-${m}-${d}`;
            };

            fechaInicioInput.value = toInputFormat(firstDay);
            fechaFinInput.value = toInputFormat(lastDay);
        }

        function loadKpis() {
            const params = new URLSearchParams(new FormData(filterForm));
            fetch(`${window.contextPath}/admin/api/devoluciones/kpis?${params.toString()}`)
                .then(res => res.json())
                .then(kpis => {
                    kpiDevolucionesHoy.textContent = kpis.totalDevolucionesHoy || 0;
                    kpiMontoHoy.textContent = formatCurrency(kpis.montoDevueltoHoy);
                    kpiDevolucionesRango.textContent = kpis.totalDevolucionesRango || 0;
                    kpiMontoRango.textContent = formatCurrency(kpis.montoDevueltoRango);
                })
                .catch(err => console.error("Error al cargar KPIs de devoluciones:", err));
        }

        function renderTable(returns) {
            tbody.innerHTML = '';
            if (returns.length === 0) {
                tbody.innerHTML = `<tr><td colspan="7" class="text-center italic text-xl py-6">No hay devoluciones que coincidan con los filtros.</td></tr>`;
                return;
            }
            returns.forEach(dev => {
                const tr = document.createElement('tr');
                tr.className = 'cursor-pointer hover:bg-gray-50';
                tr.dataset.returnId = dev.id;
                tr.innerHTML = `
                    <td class="px-4 py-2 font-medium">#${String(dev.id).padStart(6, '0')}</td>
                    <td class="px-4 py-2 text-center">${dev.clienteId}</td>
                    <td class="px-4 py-2">${dev.clienteNombre || 'N/A'}</td>
                    <td class="px-4 py-2">${formatDateTime(dev.fechaSolicitud)}</td>
                    <td class="px-4 py-2 text-right font-semibold">${formatCurrency(dev.montoDevuelto)}</td>
                    <td class="px-4 py-2">#${String(dev.ventaId).padStart(6, '0')}</td>
                    <td class="px-4 py-2 text-center"><span class="text-pistachio font-bold hover:underline">Ver Detalles</span></td>
                `;
                tbody.appendChild(tr);
            });
        }
        
        function loadPage(page = 0) {
            const params = new URLSearchParams(new FormData(filterForm));
            params.append('page', page);
            params.append('size', size);
            fetch(`${window.contextPath}/admin/api/devoluciones/get_pagable_list?${params.toString()}`)
                .then(res => res.json())
                .then(pageData => {
                    currentPage = pageData.number;
                    totalPages = pageData.totalPages;
                    renderTable(pageData.content);
                    updateControls();
                })
                .catch(err => {
                    tbody.innerHTML = `<tr><td colspan="7" class="text-center text-red-500 py-6">Error al cargar los datos.</td></tr>`;
                    console.error(err);
                });
        }

        function openDetailsModal(returnId) {
            fetch(`${window.contextPath}/admin/api/devoluciones/${returnId}`)
                .then(res => res.ok ? res.json() : Promise.reject('Error al cargar detalles'))
                .then(details => {
                    modalTitle.textContent = `Detalles de la Devolución #${String(details.id).padStart(6, '0')}`;
                    modalBody.innerHTML = `
                        <div class="space-y-2 text-sm">
                            <p><strong>Cliente:</strong> ${details.clienteNombre}</p>
                            <p><strong>Fecha de solicitud:</strong> ${formatDateTime(details.fechaSolicitud)}</p>
                            <p><strong>Pedido Original:</strong> #${String(details.ventaId).padStart(6, '0')}</p>
                            <p><strong>Monto Devuelto:</strong> ${formatCurrency(details.montoDevuelto)}</p>
                            <div class="pt-2">
                                <p class="font-semibold">Motivo:</p>
                                <p class="p-2 bg-gray-50 rounded border mt-1">${details.motivo || '<em>No especificado.</em>'}</p>
                            </div>
                            <div class="pt-2">
                                <p class="font-semibold">Comentarios:</p>
                                <p class="p-2 bg-gray-50 rounded border mt-1">${details.comentarios || '<em>Sin comentarios.</em>'}</p>
                            </div>
                        </div>`;
                    modal.classList.remove('hidden');
                })
                .catch(err => alert('No se pudieron cargar los detalles de la devolución.'));
        }
        
        // --- Funciones de apoyo y listeners ---
        function updateControls() { pageNumEl.textContent = totalPages > 0 ? currentPage + 1 : 0; totalPagesEl.textContent = totalPages > 0 ? totalPages : 1; prevBtn.disabled = (currentPage === 0); nextBtn.disabled = (currentPage + 1 >= totalPages); }
        function hideModal() { modal.classList.add('hidden'); }
        
        // Al enviar el formulario, previene el comportamiento por defecto y carga la primera página y los KPIs.
        filterForm.addEventListener('submit', e => {
            e.preventDefault();
            loadPage(0);
            loadKpis();
        });

        // Al limpiar (resetear) el formulario, vacía el ID del cliente, restablece las fechas y recarga los datos.
        // Se usa un setTimeout para asegurar que el reset del DOM finalice antes de la recarga.
        filterForm.addEventListener('reset', () => {
            clienteIdInput.value = '';
            setDefaultDateRange();
            setTimeout(() => {
                loadPage(0);
                loadKpis();
            }, 0);
        });


        // --- Eventos de Paginación ---

        // Carga la página anterior si no estamos en la primera.
        prevBtn.addEventListener('click', () => {
            if (currentPage > 0) {
                loadPage(currentPage - 1);
            }
        });

        // Carga la página siguiente si no estamos en la última.
        nextBtn.addEventListener('click', () => {
            if (currentPage + 1 < totalPages) {
                loadPage(currentPage + 1);
            }
        });


        // --- Eventos del Modal de Detalles ---

        // Cierra el modal al hacer clic en el botón de cerrar.
        closeModalBtn.addEventListener('click', hideModal);

        // Cierra el modal al hacer clic en el fondo oscuro (overlay).
        modalOverlay.addEventListener('click', hideModal);


        // --- Eventos de la Tabla ---

        // Abre el modal de detalles al hacer clic en cualquier fila de la tabla que tenga un ID de devolución.
        tbody.addEventListener('click', (e) => {
            const row = e.target.closest('tr[data-return-id]');
            if (row) {
                openDetailsModal(row.dataset.returnId);
            }
        });


        // --- Lógica de Autocompletado para el campo de Usuario ---

        userInput.addEventListener('input', () => {
            // Cancela cualquier temporizador de búsqueda anterior para evitar múltiples peticiones.
            clearTimeout(searchTimer);
            clienteIdInput.value = ''; // Limpia el ID del cliente al modificar el nombre.

            const query = userInput.value.trim();

            // Oculta las sugerencias si el texto es muy corto.
            if (query.length < 2) {
                userSuggestions.classList.add('hidden');
                return;
            }

            // Inicia un temporizador para buscar solo después de que el usuario deje de escribir (300ms).
            searchTimer = setTimeout(() => {
                const searchUrl = `${window.contextPath}/admin/api/usuarios/search_names?q=${encodeURIComponent(query)}`;

                fetch(searchUrl)
                    .then(res => res.json())
                    .then(users => {
                        userSuggestions.innerHTML = ''; // Limpia sugerencias anteriores.

                        if (users.length > 0) {
                            // Crea y añade un elemento por cada usuario encontrado.
                            users.forEach(user => {
                                const item = document.createElement('div');
                                item.textContent = user.nombreCompleto;
                                item.className = 'p-2 hover:bg-gray-100 cursor-pointer';

                                // Al seleccionar (con 'mousedown' para que se dispare antes que el 'blur' del input),
                                // se rellena el input, se guarda el ID y se ocultan las sugerencias.
                                item.addEventListener('mousedown', () => {
                                    userInput.value = user.nombreCompleto;
                                    clienteIdInput.value = user.id;
                                    userSuggestions.classList.add('hidden');
                                });
                                userSuggestions.appendChild(item);
                            });
                            userSuggestions.classList.remove('hidden');
                        } else {
                            // Oculta el contenedor si no hay resultados.
                            userSuggestions.classList.add('hidden');
                        }
                    })
                    .catch(err => {
                        console.error("Error al buscar usuarios:", err);
                        userSuggestions.classList.add('hidden');
                    });
            }, 300);
        });

        userInput.addEventListener('blur', () => {
            setTimeout(() => userSuggestions.classList.add('hidden'), 200);
        });

        // --- Carga Inicial ---
        setDefaultDateRange();
        loadPage(0);
        loadKpis();
        isInitialized = true;
    }

    window.initializePage = initializePage;
})();