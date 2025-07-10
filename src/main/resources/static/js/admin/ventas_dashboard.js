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
        const fechaFinInput = document.getElementById('fechaFin');
        const estadoFiltroSelect = document.getElementById('estadoFiltro');
        const clienteIdInput = document.getElementById('clienteId');
        const userSuggestions = document.getElementById('userSuggestions');
        const fechaInicioInput = document.getElementById('fechaInicio');

        const modal = document.getElementById('saleDetailModal');
        const modalOverlay = document.getElementById('modalOverlay');
        const closeModalBtn = document.getElementById('closeModalBtn');
        const modalTitle = document.getElementById('modalTitle');
        const modalBody = document.getElementById('modalBody');

        const updateStatusModal = document.getElementById('updateStatusModal');
        const updateStatusOverlay = document.getElementById('updateStatusOverlay');
        const closeUpdateStatusBtn = document.getElementById('closeUpdateStatusBtn');
        const saveStatusBtn = document.getElementById('saveStatusBtn');
        const statusSelect = document.getElementById('statusSelect');

        const kpiVentasHoy = document.getElementById('kpi-ventas-hoy');
        const kpiIngresosHoy = document.getElementById('kpi-ingresos-hoy');
        const kpiVentasRango = document.getElementById('kpi-ventas-rango');
        const kpiIngresosRango = document.getElementById('kpi-ingresos-rango');

        // --- Estado de la Página ---
        let currentPage = 0;
        let totalPages = 1;
        let size = 15;
        let searchTimer;
        let currentSaleId = null;

        // --- Funciones de Utilidad ---
        const formatDate = dateStr => new Date(dateStr).toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
        const formatDateTime = dateStr => new Date(dateStr).toLocaleString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
        const formatCurrency = value => new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(value || 0);

        function toggleBtn(btn, isDisabled) {
            btn.disabled = isDisabled;
            btn.classList.toggle('opacity-50', isDisabled);
            btn.classList.toggle('cursor-not-allowed', isDisabled);
        }

        // --- Lógica de la Aplicación ---
        function updateControls() {
            pageNumEl.textContent = totalPages > 0 ? currentPage + 1 : 0;
            totalPagesEl.textContent = totalPages > 0 ? totalPages : 1;
            toggleBtn(prevBtn, currentPage === 0);
            toggleBtn(nextBtn, currentPage + 1 >= totalPages);
        }

        function populateFilterStatusSelect() {
            fetch(`${window.contextPath}/admin/api/ventas/estados`)
                .then(res => res.ok ? res.json() : Promise.reject('Error al cargar estados'))
                .then(estados => {
                    estadoFiltroSelect.innerHTML = '<option value="">Todos</option>'; // Opción por defecto
                    estados.forEach(estado => {
                        const option = document.createElement('option');
                        option.value = estado.id;
                        option.textContent = estado.label;
                        estadoFiltroSelect.appendChild(option);
                    });
                })
                .catch(err => console.error(err));
        }

        function populateStatusSelect() {
            fetch(`${window.contextPath}/admin/api/ventas/estados`)
                .then(res => res.ok ? res.json() : Promise.reject('Error al cargar estados'))
                .then(estados => {
                    statusSelect.innerHTML = '';
                    estados.forEach(estado => {
                        const option = document.createElement('option');
                        option.value = estado.id; // El ID del estado
                        option.textContent = estado.label; // La etiqueta
                        statusSelect.appendChild(option);
                    });
                })
                .catch(err => console.error(err));
        }

        function renderTable(sales) {
            tbody.innerHTML = '';
            if (sales.length === 0) {
                // El colspan ahora es 7 porque hemos quitado una columna
                tbody.innerHTML = `<tr><td colspan="7" class="text-center italic text-xl py-6">No hay resultados que coincidan con los filtros.</td></tr>`;
                return;
            }
            sales.forEach(sale => {
                const tr = document.createElement('tr');
                tr.className = 'cursor-pointer hover:bg-gray-50';
                tr.dataset.saleId = sale.id;
                // HTML de la fila actualizado para coincidir con la nueva cabecera
                tr.innerHTML = `
                    <td class="px-4 py-2 font-medium">#${String(sale.id).padStart(6, '0')}</td>
                    <td class="px-4 py-2 text-center">${sale.clienteId}</td>
                    <td class="px-4 py-2">${sale.clienteNombre || 'N/A'}</td>
                    <td class="px-4 py-2">${formatDate(sale.fechaVenta)}</td>
                    <td class="px-4 py-2 text-center">${sale.estado || 'N/A'}</td>
                    <td class="px-4 py-2 text-right font-semibold">${formatCurrency(sale.montoTotal)}</td>
                    <td class="px-4 py-2 text-center"><span class="text-pistachio font-bold hover:underline">Ver Detalles</span></td>
                `;
                tbody.appendChild(tr);
            });
        }

        function loadPage(page = 0) {
            const params = new URLSearchParams(new FormData(filterForm));
            params.append('page', page);
            params.append('size', size);
            fetch(`${window.contextPath}/admin/api/ventas/get_pagable_list?${params.toString()}`)
                .then(res => res.ok ? res.json() : Promise.reject('Error al cargar la página'))
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
        
        function loadKpis() {
            const params = new URLSearchParams(new FormData(filterForm));
            fetch(`${window.contextPath}/admin/api/ventas/kpis?${params.toString()}`)
                .then(res => res.ok ? res.json() : Promise.reject('Error al cargar KPIs'))
                .then(kpis => {
                    kpiVentasHoy.textContent = kpis.totalVentasHoy || 0;
                    kpiIngresosHoy.textContent = formatCurrency(kpis.ingresosHoy);
                    kpiVentasRango.textContent = kpis.totalVentasRango || 0;
                    kpiIngresosRango.textContent = formatCurrency(kpis.ingresosRango);
                })
                .catch(err => console.error("Error al cargar KPIs:", err));
        }

        // --- Lógica de Modales ---
        function openDetailsModal(saleId) {
            currentSaleId = saleId;
            fetch(`${window.contextPath}/admin/api/ventas/${saleId}`)
                .then(res => res.ok ? res.json() : Promise.reject('Error al cargar detalles'))
                .then(details => {
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
                        </tr>`).join('');

                    // --- 1. Lógica para deshabilitar el botón ---
                    const esDevolucion = details.estadoId === 5;

                    const clasesBoton = esDevolucion 
                        ? 'bg-gray-400 text-white font-medium px-4 py-2 rounded-md opacity-50 cursor-not-allowed'
                        : 'bg-blue-600 text-white font-medium px-4 py-2 rounded-md hover:bg-blue-700';

                    const atributoDisabled = esDevolucion ? 'disabled' : '';

                    // --- 2. Construcción del HTML del modal ---
                    modalBody.innerHTML = `
                        <div class="grid grid-cols-3 gap-4 mb-4 text-sm">
                            <div><strong>Fecha:</strong> ${formatDateTime(details.fechaVenta)}</div>
                            <div><strong>Estado:</strong> ${details.estado || 'Procesando'}</div>
                            <div class="text-right"><strong>Puntos utilizados:</strong> ${details.puntosUtilizados || 0}</div>
                        </div>
                        <table class="w-full text-sm mt-4">
                            <thead class="bg-gray-100"><tr><th class="py-2 px-4 text-left">Producto</th><th class="py-2 px-4 text-center">Cantidad</th><th class="py-2 px-4 text-right">Precio Unit.</th></tr></thead>
                            <tbody>${productRows}</tbody>
                        </table>
                        <div class="flex justify-between items-center mt-6">
                            <button id="open-update-status-btn" class="${clasesBoton}" ${atributoDisabled}>Actualizar Estado</button>
                            <div class="text-right"><p class="text-lg font-bold">Total: ${formatCurrency(details.montoTotal)}</p></div>
                        </div>
                    `;

                    // --- 3. Añadir el listener solo si el botón NO está deshabilitado ---
                    if (!esDevolucion) {
                        document.getElementById('open-update-status-btn').addEventListener('click', () => showUpdateStatusModal(details.estadoId));
                    }

                    showModal();
                })
                .catch(err => alert('No se pudieron cargar los detalles del pedido.'));
        }

        function showModal() { modal.classList.remove('hidden'); document.body.classList.add('overflow-hidden'); }
        function hideModal() { modal.classList.add('hidden'); document.body.classList.remove('overflow-hidden'); }

        function showUpdateStatusModal(currentStatusId) {
            if (currentStatusId !== undefined && currentStatusId !== null) {
                statusSelect.value = currentStatusId;
            }
            updateStatusModal.classList.remove('hidden');
        }
        
        function hideUpdateStatusModal() { updateStatusModal.classList.add('hidden'); }
        
        function handleSaveStatus() {
            const nuevoEstadoId = statusSelect.value;
            if (!currentSaleId || nuevoEstadoId === '') return;
            
            saveStatusBtn.disabled = true;
            saveStatusBtn.textContent = 'Guardando...';

            fetch(`${window.contextPath}/admin/api/ventas/${currentSaleId}/estado`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ nuevoEstadoId: parseInt(nuevoEstadoId, 10) })
            })
            .then(res => {
                if (!res.ok) throw new Error('No se pudo actualizar el estado.');
                hideUpdateStatusModal();
                hideModal();
                alert('Estado del pedido actualizado con éxito.');
                loadPage(currentPage);
            })
            .catch(err => {
                alert(err.message);
                console.error(err);
            })
            .finally(() => {
                saveStatusBtn.disabled = false;
                saveStatusBtn.textContent = 'Guardar Cambios';
            });
        }
        
        // --- Listeners ---
        filterForm.addEventListener('submit', e => { e.preventDefault(); loadPage(0); loadKpis(); });
        filterForm.addEventListener('reset', () => { setTimeout(() => { loadPage(0); loadKpis(); }, 0); });
        
        prevBtn.addEventListener('click', () => { if (currentPage > 0) loadPage(currentPage - 1); });
        nextBtn.addEventListener('click', () => { if (currentPage + 1 < totalPages) loadPage(currentPage + 1); });
        
        closeModalBtn.addEventListener('click', hideModal);
        modalOverlay.addEventListener('click', hideModal);
        closeUpdateStatusBtn.addEventListener('click', hideUpdateStatusModal);
        updateStatusOverlay.addEventListener('click', hideUpdateStatusModal);
        
        saveStatusBtn.addEventListener('click', handleSaveStatus);
        
        tbody.addEventListener('click', (e) => {
            const row = e.target.closest('tr[data-sale-id]');
            if (row) openDetailsModal(row.dataset.saleId);
        });

        userInput.addEventListener('input', () => {
             clearTimeout(searchTimer);
             clienteIdInput.value = '';
             const query = userInput.value.trim();
             if (query.length < 2) {
                 userSuggestions.classList.add('hidden');
                 return;
             }
             searchTimer = setTimeout(() => {
                 fetch(`${window.contextPath}/admin/api/usuarios/search_names?q=${encodeURIComponent(query)}`)
                     .then(res => res.json())
                     .then(users => {
                         userSuggestions.innerHTML = '';
                         if (users.length) {
                             users.forEach(user => {
                                 const item = document.createElement('div');
                                 item.textContent = user.nombreCompleto;
                                 item.className = 'p-2 hover:bg-gray-100 cursor-pointer';
                                 item.addEventListener('mousedown', () => {
                                     userInput.value = user.nombreCompleto;
                                     clienteIdInput.value = user.id;
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

        userInput.addEventListener('blur', () => setTimeout(() => userSuggestions.classList.add('hidden'), 200));
        
        // --- Carga Inicial ---
        loadPage(0);
        loadKpis();
        populateStatusSelect();
        populateFilterStatusSelect();
        isInitialized = true;
    }

    window.initializePage = initializePage;
})();