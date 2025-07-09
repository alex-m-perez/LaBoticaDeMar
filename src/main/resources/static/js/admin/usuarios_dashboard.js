(function() {
    let isInitialized = false;

    // Estado de la paginación
    let mainState = { currentPage: 0, totalPages: 1 };
    let kpiStates = {
        compradores: { currentPage: 0, totalPages: 1 },
        gastadores: { currentPage: 0, totalPages: 1 },
        devoluciones: { currentPage: 0, totalPages: 1 }
    };

    // --- Funciones de Utilidad ---
    const formatDate = dateStr => dateStr ? new Date(dateStr).toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' }) : 'N/A';
    const formatCurrency = (value, currency = 'EUR') => new Intl.NumberFormat('es-ES', { style: 'currency', currency }).format(value || 0);
    const get = (id) => document.getElementById(id); // Alias corto

    // --- Lógica de Renderizado ---
    function renderMainTable(pageData) { // 1. Cambiamos el nombre del parámetro para más claridad
        const tbody = get('usuariosTbody');
        tbody.innerHTML = '';

        const users = pageData.content; // 2. Extraemos la lista de usuarios del objeto Page

        if (!users || users.length === 0) {
            tbody.innerHTML = `<tr><td colspan="6" class="text-center italic py-6">No se encontraron clientes.</td></tr>`;
            return;
        }
        users.forEach(user => {
            const tr = document.createElement('tr');
            tr.className = 'hover:bg-gray-50 cursor-pointer';
            tr.dataset.usuarioId = user.id;
            tr.innerHTML = `
                <td class="px-4 py-2 font-medium">#${user.id}</td>
                <td class="px-4 py-2">${user.nombreCompleto || 'N/A'}</td>
                <td class="px-4 py-2">${formatDate(user.fechaNac)}</td>
                <td class="px-4 py-2">${user.direccionPostal || 'N/A'}</td>
                <td class="px-4 py-2">${user.correo || 'N/A'}</td>
                <td class="px-4 py-2">${user.telefono || 'N/A'}</td>
            `;
            tbody.appendChild(tr);
        });
    }

    function renderKpiTable(kpiType, pageData) {
        const tbody = get(`kpi-${kpiType}-tbody`);
        tbody.innerHTML = '';
        if (!pageData.content || pageData.content.length === 0) {
            tbody.innerHTML = `<tr><td colspan="3" class="text-center italic py-4">Sin datos.</td></tr>`;
            return;
        }
        pageData.content.forEach(item => {
            const tr = document.createElement('tr');
            let cells = '';
            switch (kpiType) {
                case 'compradores':
                    cells = `<td class="px-4 py-2">#${item.usuarioId}</td><td class="px-4 py-2 text-center">${item.cantidadCompras}</td><td class="px-4 py-2 text-right">${formatCurrency(item.promedioCompra)}</td>`;
                    break;
                case 'gastadores':
                    cells = `<td class="px-4 py-2">#${item.usuarioId}</td><td class="px-4 py-2 text-center">${item.totalPedidos}</td><td class="px-4 py-2 text-right">${formatCurrency(item.totalGastado)}</td>`;
                    break;
                case 'devoluciones':
                     cells = `<td class="px-4 py-2">#${item.usuarioId}</td><td class="px-4 py-2 text-center">${item.cantidadDevoluciones}</td><td class="px-4 py-2 text-right">${formatCurrency(item.valorDevoluciones)}</td>`;
                    break;
            }
            tr.innerHTML = cells;
            tbody.appendChild(tr);
        });
    }

    function renderModal(user) {
        const generoStr = {1: 'Masculino', 2: 'Femenino'}[user.genero] || 'Otro/No especificado';
        const boolStr = val => val ? '<span class="text-green-600 font-semibold">Sí</span>' : '<span class="text-red-600 font-semibold">No</span>';

        get('modalTitle').textContent = `Ficha de Cliente: ${user.nombreCompleto}`;
        get('modalBody').innerHTML = `
            <div class="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-4">
                <div class="space-y-4">
                    <div class="p-4 bg-gray-50 rounded-lg border">
                        <h4 class="font-semibold text-gray-800 mb-2 border-b pb-1">Información Personal</h4>
                        <div class="grid grid-cols-2 gap-1">
                            <div><strong>ID:</strong> #${user.id}</div>
                            <div><strong>Activo:</strong> ${boolStr(user.activo)}</div>
                            <div class="col-span-2"><strong>Nombre:</strong> ${user.nombreCompleto}</div>
                            <div><strong>Fecha Nac.:</strong> ${formatDate(user.fechaNac)}</div>
                            <div><strong>Género:</strong> ${generoStr}</div>
                        </div>
                    </div>
                    <div class="p-4 bg-gray-50 rounded-lg border">
                        <h4 class="font-semibold text-gray-800 mb-2 border-b pb-1">Contacto</h4>
                        <div class="grid grid-cols-1 gap-1">
                            <div><strong>Correo:</strong> ${user.correo}</div>
                            <div><strong>Teléfono:</strong> ${user.telefono || 'N/A'}</div>
                            <div class="col-span-2"><strong>Dirección:</strong> ${user.direccionPostal || 'N/A'}</div>
                        </div>
                    </div>
                </div>
                <div class="space-y-4">
                     <div class="p-4 bg-gray-50 rounded-lg border">
                        <h4 class="font-semibold text-gray-800 mb-2 border-b pb-1">Consentimientos</h4>
                        <div class="grid grid-cols-2 gap-1">
                            <div><strong>Promociones:</strong> ${boolStr(user.aceptaPromociones)}</div>
                            <div><strong>Términos:</strong> ${boolStr(user.aceptaTerminos)}</div>
                            <div class="col-span-2"><strong>Privacidad:</strong> ${boolStr(user.aceptaPrivacidad)}</div>
                        </div>
                    </div>
                    <div class="p-4 bg-gray-50 rounded-lg border">
                        <h4 class="font-semibold text-gray-800 mb-2 border-b pb-1">Actividad y Fidelización</h4>
                        <div class="grid grid-cols-1 gap-1">
                            <div><strong>Puntos Acumulados:</strong> <span class="font-bold text-pistachio">${user.puntos || 0}</span></div>
                            <div><strong>Preferencias:</strong> ${user.preferencias && user.preferencias.length > 0 ? user.preferencias.join(', ') : 'Ninguna'}</div>
                            <div><strong>Roles:</strong> ${user.roles && user.roles.length > 0 ? user.roles.join(', ').replace(/ROLE_/g, '') : 'N/A'}</div>
                        </div>
                    </div>
                </div>
            </div>
        `;
        get('userDetailModal').classList.remove('hidden');
        document.body.classList.add('overflow-hidden');
    }

    // --- Lógica de Paginación y Carga ---
    function updatePaginationControls(type, state) {
        const isMain = type === 'main';
        const prefix = isMain ? 'main-' : `kpi-${type}-`;
        const container = isMain ? document : get(`kpi-${type}-pagination`);
        
        if (isMain) {
            get('main-pageInfo').textContent = `Página ${state.totalPages > 0 ? state.currentPage + 1 : 0} de ${state.totalPages || 1}`;
            get('main-prevBtn').disabled = state.currentPage === 0;
            get('main-nextBtn').disabled = state.currentPage + 1 >= state.totalPages;
        } else {
             container.innerHTML = `
                <button data-type="${type}" data-action="prev" class="bg-gray-200 px-2 py-0.5 rounded hover:bg-gray-300" ${state.currentPage === 0 ? 'disabled' : ''}>‹</button>
                <span>${state.totalPages > 0 ? state.currentPage + 1 : 0} / ${state.totalPages || 1}</span>
                <button data-type="${type}" data-action="next" class="bg-gray-200 px-2 py-0.5 rounded hover:bg-gray-300" ${state.currentPage + 1 >= state.totalPages ? 'disabled' : ''}>›</button>
            `;
        }
    }

    async function loadPage(url, state, renderFn, ...renderArgs) {
        try {
            const res = await fetch(url);
            if (!res.ok) throw new Error(`Error en la petición: ${res.status}`);
            const pageData = await res.json();
            
            state.currentPage = pageData.number;
            state.totalPages = pageData.totalPages;
            
            renderFn( ...renderArgs, pageData);
            return pageData;
        } catch (err) {
            console.error(`Error al cargar datos desde ${url}:`, err);
        }
    }

    function loadMainPage(page = 0) {
        const url = `${window.contextPath}/admin/api/usuarios/get_pagable_list?page=${page}&size=10`;
        // Simplemente eliminamos el argumento 'null'
        loadPage(url, mainState, renderMainTable).then(() => updatePaginationControls('main', mainState));
    }

    function loadKpiPage(kpiType, page = 0) {
        const url = `${window.contextPath}/admin/api/usuarios/kpis/top-${kpiType}?page=${page}&size=5`;
        loadPage(url, kpiStates[kpiType], renderKpiTable, kpiType).then(() => updatePaginationControls(kpiType, kpiStates[kpiType]));
    }
    
    async function openDetailsModal(usuarioId) {
        try {
            const res = await fetch(`${window.contextPath}/admin/api/usuarios/${usuarioId}`);
            if (!res.ok) throw new Error('No se pudo cargar la ficha del cliente.');
            const userData = await res.json();
            renderModal(userData);
        } catch (err) {
            alert(err.message);
            console.error(err);
        }
    }

    function hideModal() {
        get('userDetailModal').classList.add('hidden');
        document.body.classList.remove('overflow-hidden');
    }

    // --- Inicialización y Listeners ---
    function initializePage() {
        if (isInitialized) return;

        // Listeners principales
        get('main-prevBtn').addEventListener('click', () => loadMainPage(mainState.currentPage - 1));
        get('main-nextBtn').addEventListener('click', () => loadMainPage(mainState.currentPage + 1));
        get('usuariosTbody').addEventListener('click', (e) => {
            const row = e.target.closest('tr[data-usuario-id]');
            if (row) openDetailsModal(row.dataset.usuarioId);
        });

        // Listeners para KPIs (usando delegación de eventos)
        ['compradores', 'gastadores', 'devoluciones'].forEach(type => {
            get(`kpi-${type}-pagination`).addEventListener('click', e => {
                const button = e.target.closest('button');
                if (!button) return;
                const action = button.dataset.action;
                let page = kpiStates[type].currentPage;
                if (action === 'prev') page--;
                if (action === 'next') page++;
                loadKpiPage(type, page);
            });
        });

        // Listeners del modal
        get('closeModalBtn').addEventListener('click', hideModal);
        get('modalOverlay').addEventListener('click', hideModal);

        // Carga inicial de datos
        loadMainPage(0);
        loadKpiPage('compradores', 0);
        loadKpiPage('gastadores', 0);
        loadKpiPage('devoluciones', 0);
        
        isInitialized = true;
    }

    window.initializePage = initializePage;
})();