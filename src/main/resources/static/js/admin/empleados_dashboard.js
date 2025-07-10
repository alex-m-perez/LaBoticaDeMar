(function() {
    let isInitialized = false;

    function initializePage() {
        if (isInitialized) return;

        // --- Elementos del DOM ---
        const tbody = document.getElementById('empleadosBody');
        const prevBtn = document.getElementById('prevEmpBtn');
        const nextBtn = document.getElementById('nextEmpBtn');
        const pageNumEl = document.getElementById('pageEmpNum');
        const totalPagesEl = document.getElementById('totalEmpPages');

        const modal = document.getElementById('empleadoDetailModal');
        const modalOverlay = document.getElementById('empleadoModalOverlay');
        const closeModalBtn = document.getElementById('closeModalBtn');
        const modalTitle = document.getElementById('modalTitle');
        const modalBody = document.getElementById('modalBody');

        if (!tbody || !modal || !prevBtn || !nextBtn) {
            console.error("Faltan elementos de la tabla, paginación o el modal en el DOM.");
            return;
        }

        // --- Estado de la Página ---
        let currentPage = 0;
        let totalPages = 1;
        const size = 15; // Número de empleados por página

        // --- Funciones de Utilidad ---
        const formatDate = dateStr => dateStr ? new Date(dateStr).toLocaleDateString('es-ES') : 'N/A';

        function toggleBtn(btn, isDisabled) {
            btn.disabled = isDisabled;
            btn.classList.toggle('opacity-50', isDisabled);
            btn.classList.toggle('cursor-not-allowed', isDisabled);
            btn.classList.toggle('hover:bg-gray-300', !isDisabled);
        }

        // --- Lógica de Renderizado ---
        function renderToggle(id, isActivo) {
            const baseClasses = 'relative inline-flex items-center h-6 rounded-full w-11 cursor-pointer transition-colors focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-pistachio';
            const circleClasses = 'inline-block w-4 h-4 transform bg-white rounded-full transition-transform';
            const bgColor = isActivo ? 'bg-pistachio' : 'bg-gray-300';
            const circlePos = isActivo ? 'translate-x-6' : 'translate-x-1';

            return `
                <button class="toggle-activo-btn ${baseClasses} ${bgColor}" data-id="${id}" data-status="${isActivo}" aria-label="Cambiar estado">
                    <span class="${circleClasses} ${circlePos}"></span>
                </button>
            `;
        }

        function renderTable(empleados) {
            tbody.innerHTML = '';
            if (empleados.length === 0) {
                tbody.innerHTML = `<tr><td colspan="4" class="text-center italic text-xl py-6">No se encontraron empleados.</td></tr>`;
                return;
            }
            empleados.forEach(emp => {
                const tr = document.createElement('tr');
                tr.className = 'hover:bg-gray-50 cursor-pointer';
                tr.dataset.empleadoId = emp.id;

                const rolPrincipal = emp.roles && emp.roles.length > 0 ? emp.roles[0].replace('ROLE_', '') : 'N/A';
                
                tr.innerHTML = `
                    <td class="px-4 py-2 font-medium">#${emp.id}</td>
                    <td class="px-4 py-2">${emp.nombreCompleto}</td>
                    <td class="px-4 py-2">${rolPrincipal}</td>
                    <td class="px-4 py-2">${renderToggle(emp.id, emp.activo)}</td>
                `;
                tbody.appendChild(tr);
            });
        }

        function updateControls() {
            pageNumEl.textContent    = totalPages > 0 ? currentPage + 1 : 0;
            totalPagesEl.textContent = totalPages > 0 ? totalPages : 1;
            toggleBtn(prevBtn, currentPage === 0);
            toggleBtn(nextBtn, currentPage + 1 >= totalPages);
        }

        function loadPage(page = 0) {
            const url = `${window.contextPath}/admin/api/empleados/get_pagable_list?page=${page}&size=${size}`;
            
            fetch(url)
                .then(res => {
                    if (!res.ok) {
                        return Promise.reject(`Error del servidor: ${res.status}`);
                    }
                    return res.json();
                })
                .then(pageData => {
                    currentPage = pageData.number;
                    totalPages = pageData.totalPages;
                    renderTable(pageData.content);
                    updateControls();
                })
                .catch(err => {
                    console.error("Error al cargar la página de empleados:", err);
                    tbody.innerHTML = `<tr><td colspan="4" class="text-center text-red-500 py-6">No se pudo cargar la información.</td></tr>`;
                });
        }

        // --- Lógica de Interacción ---
        function handleToggleClick(toggle) {
            const id = toggle.dataset.id;
            const newStatus = !(toggle.dataset.status === 'true');
            const url = `${window.contextPath}/admin/api/empleados/${id}/activate?activar=${newStatus}`;

            fetch(url, { method: 'PUT' })
                .then(response => {
                    if (!response.ok) throw new Error('No se pudo actualizar el estado.');
                    
                    toggle.dataset.status = newStatus;
                    toggle.classList.toggle('bg-pistachio', newStatus);
                    toggle.classList.toggle('bg-gray-300', !newStatus);
                    const circle = toggle.querySelector('span');
                    circle.classList.toggle('translate-x-6', newStatus);
                    circle.classList.toggle('translate-x-1', !newStatus);
                })
                .catch(error => {
                    console.error('Error al cambiar estado:', error);
                    alert('Hubo un error al cambiar el estado del empleado.');
                });
        }

        function openDetailsModal(empleadoId) {
            fetch(`${window.contextPath}/admin/api/empleados/${empleadoId}`)
                .then(res => res.ok ? res.json() : Promise.reject('Error al cargar datos'))
                .then(data => {
                    modalTitle.textContent = `Detalles de ${data.nombre} ${data.apellido1}`;
                    
                    const generoStr = data.genero === 1 ? 'Masculino' : (data.genero === 2 ? 'Femenino' : 'Otro');
                    
                    modalBody.innerHTML = `
                        <div class="p-4 bg-gray-50 rounded-lg border">
                            <h4 class="font-semibold text-gray-800 mb-2">Información Personal</h4>
                            <div class="grid grid-cols-1 sm:grid-cols-2 gap-x-4 gap-y-1">
                                <div><strong>Nombre:</strong> ${data.nombre || ''}</div>
                                <div><strong>Primer Apellido:</strong> ${data.apellido1 || ''}</div>
                                <div><strong>Segundo Apellido:</strong> ${data.apellido2 || 'N/A'}</div>
                                <div><strong>Fecha Nacimiento:</strong> ${formatDate(data.fechaNac)}</div>
                                <div><strong>Género:</strong> ${generoStr}</div>
                            </div>
                        </div>
                        <div class="p-4 bg-gray-50 rounded-lg border">
                            <h4 class="font-semibold text-gray-800 mb-2">Contacto y Cuenta</h4>
                            <div class="grid grid-cols-1 sm:grid-cols-2 gap-x-4 gap-y-1">
                                <div><strong>Correo:</strong> ${data.correo || ''}</div>
                                <div><strong>Teléfono:</strong> ${data.telefono || 'N/A'}</div>
                                <div class="col-span-1 sm:col-span-2"><strong>Dirección:</strong> ${data.direccionPostal || 'N/A'}</div>
                            </div>
                        </div>
                    `;
                    showModal();
                })
                .catch(err => alert('No se pudieron cargar los datos del empleado.'));
        }

        function showModal() { modal.classList.remove('hidden'); }
        function hideModal() { modal.classList.add('hidden'); }

        // --- Listeners de Eventos ---
        prevBtn.addEventListener('click', () => { if (currentPage > 0) loadPage(currentPage - 1); });
        nextBtn.addEventListener('click', () => { if (currentPage + 1 < totalPages) loadPage(currentPage + 1); });

        tbody.addEventListener('click', (e) => {
            const toggle = e.target.closest('.toggle-activo-btn');
            if (toggle) {
                e.stopPropagation();
                handleToggleClick(toggle);
            } else {
                const row = e.target.closest('tr[data-empleado-id]');
                if (row) {
                    openDetailsModal(row.dataset.empleadoId);
                }
            }
        });

        closeModalBtn.addEventListener('click', hideModal);
        modalOverlay.addEventListener('click', hideModal);
        
        // --- Carga Inicial ---
        loadPage(0);
        isInitialized = true;
    }

    window.initializePage = initializePage;
})();