(function() {
    let isInitialized = false;

    function initializePage() {
        if (isInitialized) return;

        // --- Elementos del DOM ---
        const filterForm = document.getElementById('salesFilterForm');
        const tbody = document.getElementById('salesTbody');
        const prevBtn = document.getElementById('prevBtn');
        const nextBtn = document.getElementById('nextBtn');
        const pageNumEl = document.getElementById('pageNum');
        const totalPagesEl = document.getElementById('totalPages');
        const userInput = document.getElementById('nombreUsuario');
        const clienteIdInput = document.getElementById('clienteId'); // <-- NUEVO: Campo oculto para el ID
        const userSuggestions = document.getElementById('userSuggestions');
        const fechaInicioInput = document.getElementById('fechaInicio');
        const fechaFinInput = document.getElementById('fechaFin');

        // --- Elementos del Modal de Detalles ---
        const modal = document.getElementById('saleDetailModal');
        const modalOverlay = document.getElementById('modalOverlay');
        const closeModalBtn = document.getElementById('closeModalBtn');
        const modalTitle = document.getElementById('modalTitle');
        const modalBody = document.getElementById('modalBody');

        // --- Elementos del Modal de Actualizar Estado ---
        const updateStatusModal = document.getElementById('updateStatusModal');
        const updateStatusOverlay = document.getElementById('updateStatusOverlay');
        const closeUpdateStatusBtn = document.getElementById('closeUpdateStatusBtn');
        const saveStatusBtn = document.getElementById('saveStatusBtn');
        const statusSelect = document.getElementById('statusSelect');

        // --- Constantes para los elementos de KPIs ---
        const kpiVentasHoy = document.getElementById('kpi-ventas-hoy');
        const kpiIngresosHoy = document.getElementById('kpi-ingresos-hoy');
        const kpiVentasRango = document.getElementById('kpi-ventas-rango');
        const kpiIngresosRango = document.getElementById('kpi-ingresos-rango');

        if (!filterForm || !tbody || !modal || !updateStatusModal || !clienteIdInput) {
            console.error("Faltan elementos del DOM para inicializar la página.");
            return;
        }

        // --- Estado de la Página ---
        let currentPage = 0;
        let totalPages = 1;
        let size = 15;
        let searchTimer;
        let currentSaleId = null;

        // --- Funciones de Utilidad ---
        const formatDate = dateStr => new Date(dateStr).toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
        const formatDateTime = dateStr => new Date(dateStr).toLocaleString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
        const formatCurrency = value => new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(value);

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

            fetch(`${window.contextPath}/admin/api/ventas/kpis?${params.toString()}`)
                .then(res => res.json())
                .then(kpis => {
                    kpiVentasHoy.textContent = kpis.totalVentasHoy || 0;
                    kpiIngresosHoy.textContent = formatCurrency(kpis.ingresosHoy);
                    kpiVentasRango.textContent = kpis.totalVentasRango || 0;
                    kpiIngresosRango.textContent = formatCurrency(kpis.ingresosRango);
                })
                .catch(err => console.error("Error al cargar KPIs:", err));
        }

        // --- Lógica de Renderizado ---
        function renderTable(sales) {
            tbody.innerHTML = '';
            if (sales.length === 0) {
                tbody.innerHTML = `<tr><td colspan="8" class="text-center italic text-xl py-6">No hay resultados que coincidan con los filtros.</td></tr>`;
                return;
            }
            sales.forEach(sale => {
                const tr = document.createElement('tr');
                tr.className = 'cursor-pointer hover:bg-gray-50';
                tr.dataset.saleId = sale.id;
                tr.innerHTML = `
                    <td class="px-4 py-2 font-medium">#${String(sale.id).padStart(6, '0')}</td>
                    <td class="px-4 py-2 text-center">${sale.clienteId}</td>
                    <td class="px-4 py-2">${sale.clienteNombre || 'N/A'}</td>
                    <td class="px-4 py-2">${formatDate(sale.fechaVenta)}</td>
                    <td class="px-4 py-2 text-center">${sale.totalItems || 0}</td>
                    <td class="px-4 py-2 text-right font-semibold">${formatCurrency(sale.montoTotal)}</td>
                    <td class="px-4 py-2">Online</td>
                    <td class="px-4 py-2 text-center"><span class="text-pistachio font-bold hover:underline">Ver Detalles</span></td>
                `;
                tr.addEventListener('click', () => openDetailsModal(sale.id));
                tbody.appendChild(tr);
            });
        }

        function toggleBtn(btn, isDisabled) {
            btn.disabled = isDisabled;
            btn.classList.toggle('opacity-50', isDisabled);
            btn.classList.toggle('cursor-not-allowed', isDisabled);
        }
        
        function updateControls() {
            pageNumEl.textContent = totalPages > 0 ? currentPage + 1 : 0;
            totalPagesEl.textContent = totalPages > 0 ? totalPages : 1;
            toggleBtn(prevBtn, currentPage === 0);
            toggleBtn(nextBtn, currentPage + 1 >= totalPages);
        }

        function loadPage(page = 0) {
            const params = new URLSearchParams(new FormData(filterForm));
            params.append('page', page);
            params.append('size', size);
            fetch(`${window.contextPath}/admin/api/ventas/get_pagable_list?${params.toString()}`)
                .then(res => res.json())
                .then(pageData => {
                    currentPage = pageData.number;
                    totalPages = pageData.totalPages;
                    renderTable(pageData.content);
                    updateControls();
                })
                .catch(err => {
                    tbody.innerHTML = `<tr><td colspan="8" class="text-center text-red-500 py-6">Error al cargar los datos.</td></tr>`;
                    console.error(err);
                });
        }

        // --- Lógica de Modales ---
        function openDetailsModal(saleId) {
            currentSaleId = saleId;
            fetch(`${window.contextPath}/admin/api/ventas/${saleId}`)
                .then(res => res.ok ? res.json() : Promise.reject('Error al cargar detalles'))
                .then(details => {
                    const puntos = details.puntosUtilizados || 0;
                    const fechaHora = formatDateTime(details.fechaVenta);
                    modalTitle.textContent = `Detalles del Pedido #${String(details.id).padStart(6, '0')}`;
                    const productRows = details.productos.map(p => `
                        <tr class="border-b last:border-b-0">
                            <td class="py-2 px-4 flex items-center gap-3">
                                <img src="${p.imagenPath || `${window.contextPath}/images/placeholder.png`}" alt="${p.nombre}" class="w-12 h-12 object-cover rounded">
                                <div>
                                    <p class="font-semibold">${p.nombre}</p>
                                    <p class="text-xs text-gray-500">${p.laboratorioNombre || ''}</p>
                                </div>
                            </td>
                            <td class="py-2 px-4 text-center">${p.cantidad}</td>
                            <td class="py-2 px-4 text-right">${formatCurrency(p.precioUnitario)}</td>
                        </tr>
                    `).join('');

                    modalBody.innerHTML = `
                        <div class="grid grid-cols-3 gap-4 mb-4 text-sm">
                            <div><strong>Fecha:</strong> ${fechaHora}</div>
                            <div><strong>Estado:</strong> ${details.estado || 'Procesando'}</div>
                            <div class="text-right"><strong>Puntos utilizados:</strong> ${puntos}</div>
                        </div>
                        <table class="w-full text-sm mt-4">
                            <thead class="bg-gray-100"><tr>
                                <th class="py-2 px-4 text-left">Producto</th>
                                <th class="py-2 px-4 text-center">Cantidad</th>
                                <th class="py-2 px-4 text-right">Precio Unit.</th>
                            </tr></thead>
                            <tbody>${productRows}</tbody>
                        </table>
                        <div class="flex justify-between items-center mt-6">
                            <button id="open-update-status-btn" class="bg-blue-600 text-white font-medium px-4 py-2 rounded-md hover:bg-blue-700">Actualizar Estado</button>
                            <div class="text-right">
                                <p class="text-lg font-bold">Total: ${formatCurrency(details.montoTotal)}</p>
                            </div>
                        </div>
                    `;
                    
                    document.getElementById('open-update-status-btn')
                        .addEventListener('click', () => {
                            document.getElementById('updateOrderDate').textContent = `Fecha del pedido: ${fechaHora}`;
                            document.getElementById('updatePointsDisplay').textContent = `Puntos utilizados: ${puntos}`;
                            showUpdateStatusModal();
                        });
                    showModal();
                })
                .catch(err => {
                    alert('No se pudieron cargar los detalles del pedido.');
                    console.error(err);
                });
        }
        
        function showModal() { modal.classList.remove('hidden'); document.body.classList.add('overflow-hidden'); }
        function hideModal() { modal.classList.add('hidden'); document.body.classList.remove('overflow-hidden'); }
        
        function showUpdateStatusModal() { updateStatusModal.classList.remove('hidden'); }
        function hideUpdateStatusModal() { updateStatusModal.classList.add('hidden'); }
        
        function handleSaveStatus() {
            // Lógica para guardar el estado (sin cambios)
        }
        
        // --- Listeners de Eventos ---
        filterForm.addEventListener('submit', e => {
            e.preventDefault();
            loadPage(0);
            loadKpis(); // <-- Actualizar KPIs al buscar
        });
        
        filterForm.addEventListener('reset', () => {
            clienteIdInput.value = ''; 
            setDefaultDateRange(); // <-- Restablecer fechas por defecto
            setTimeout(() => {
                loadPage(0);
                loadKpis(); // <-- Actualizar KPIs al limpiar
            }, 0);
        });
        
        prevBtn.addEventListener('click', () => { if (currentPage > 0) loadPage(currentPage - 1) });
        nextBtn.addEventListener('click', () => { if (currentPage + 1 < totalPages) loadPage(currentPage + 1) });
        
        closeModalBtn.addEventListener('click', hideModal);
        modalOverlay.addEventListener('click', hideModal);
        
        closeUpdateStatusBtn.addEventListener('click', hideUpdateStatusModal);
        updateStatusOverlay.addEventListener('click', hideUpdateStatusModal);
        saveStatusBtn.addEventListener('click', handleSaveStatus);
        
        tbody.addEventListener('click', (e) => {
            const row = e.target.closest('tr[data-sale-id]');
            if (row) { openDetailsModal(row.dataset.saleId); }
        });

        // --- MODIFICADO ---
        // Adaptado para manejar la nueva respuesta de la API y actualizar el campo oculto.
        userInput.addEventListener('input', () => {
            clearTimeout(searchTimer);
            clienteIdInput.value = ''; // Limpia el ID si el usuario escribe de nuevo
            
            const query = userInput.value.trim();
            if (query.length < 2) {
                userSuggestions.classList.add('hidden');
                return;
            }

            searchTimer = setTimeout(() => {
                fetch(`${window.contextPath}/admin/api/usuarios/search_names?q=${encodeURIComponent(query)}`)
                    .then(res => res.json())
                    .then(users => { // 'users' es ahora un array de objetos [{id, nombreCompleto}]
                        userSuggestions.innerHTML = '';
                        if (users.length) {
                            users.forEach(user => { // Iterar sobre los objetos de usuario
                                const item = document.createElement('div');
                                item.textContent = user.nombreCompleto; // Mostrar el nombre completo
                                item.className = 'p-2 hover:bg-gray-100 cursor-pointer';
                                
                                item.addEventListener('mousedown', () => {
                                    userInput.value = user.nombreCompleto; // Poner el nombre en el input visible
                                    clienteIdInput.value = user.id;        // Poner el ID en el input oculto
                                    userSuggestions.classList.add('hidden');
                                });
                                userSuggestions.appendChild(item);
                            });
                            userSuggestions.classList.remove('hidden');
                        } else {
                            userSuggestions.classList.add('hidden');
                        }
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