/**
 * Inicializa la funcionalidad de la sección "Mis Devoluciones".
 * Esta función es llamada por perfil.js después de cargar dinámicamente el HTML y este script.
 */
function initializePage() {
    const tableBody = document.getElementById('returns-table-body');
    const paginationControls = document.getElementById('pagination-controls');
    const modal = document.getElementById('return-detail-modal');

    if (!tableBody || !paginationControls || !modal) {
        console.error('Faltan elementos del DOM para inicializar la sección de Mis Devoluciones.');
        return;
    }

    const modalOverlay = modal.querySelector('#modal-overlay');
    const modalCloseBtn = modal.querySelector('#modal-close-btn');
    const modalTitle = modal.querySelector('#modal-title');
    const modalBody = modal.querySelector('#modal-body');
    const API_DEVOLUCIONES_URL = '/api/devoluciones';

    const formatDate = dateString => {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
    };

    function fetchReturns(page = 0) {
        tableBody.innerHTML = `<tr><td colspan="5" class="text-center p-10">Cargando devoluciones...</td></tr>`;

        fetch(`${API_DEVOLUCIONES_URL}?page=${page}&size=10`)
            .then(response => {
                if (!response.ok) throw new Error('La respuesta de la red no fue correcta.');
                return response.json();
            })
            .then(data => {
                renderReturns(data.devolucionesPage.content);
                renderPagination(data.devolucionesPage);
            })
            .catch(error => {
                console.error('Error al cargar las devoluciones:', error);
                tableBody.innerHTML = `<tr><td colspan="5" class="text-center text-red-500 p-10">Error al cargar el historial de devoluciones.</td></tr>`;
            });
    }

    function renderReturns(returnsList) {
        tableBody.innerHTML = '';
        if (!returnsList || returnsList.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="5" class="text-center p-10">No has solicitado ninguna devolución todavía.</td></tr>`;
            return;
        }

        returnsList.forEach(ret => {
            const row = document.createElement('tr');
            row.className = 'hover:bg-gray-50 cursor-pointer';
            row.dataset.returnId = ret.id;
            row.innerHTML = `
                <td class="px-4 py-3 font-medium">#${String(ret.id).padStart(6, '0')}</td>
                <td class="px-4 py-3">${formatDate(ret.fechaSolicitud)}</td>
                <td class="px-4 py-3">#${String(ret.ventaId).padStart(6, '0')}</td>
                <td class="px-4 py-3">${ret.motivo || 'N/A'}</td>
                <td class="px-4 py-3 text-center">
                    <button data-return-id="${ret.id}" class="text-blue-600 hover:underline details-btn">Detalles</button>
                </td>
            `;
            tableBody.appendChild(row);
        });
    }

    function renderPagination(pageData) {
        paginationControls.innerHTML = '';
        const { totalPages, number: currentPage } = pageData;
        if (totalPages <= 1) return;

        for (let i = 0; i < totalPages; i++) {
            const btn = document.createElement('button');
            btn.textContent = i + 1;
            btn.className = `px-4 py-2 mx-1 rounded-md text-sm font-medium ${
                i === currentPage ? 'bg-pistachio text-white' : 'bg-white text-gray-700 hover:bg-gray-100'
            }`;
            btn.addEventListener('click', () => fetchReturns(i));
            paginationControls.appendChild(btn);
        }
    }

    function fetchReturnDetails(returnId) {
        fetch(`${API_DEVOLUCIONES_URL}/${returnId}`)
            .then(response => {
                if (!response.ok) throw new Error('No se pudieron cargar los detalles.');
                return response.json();
            })
            .then(details => showDetailModal(details))
            .catch(error => {
                console.error('Error al cargar detalles de la devolución:', error);
                alert('No se pudieron cargar los detalles de la devolución.');
            });
    }

    function showDetailModal(details) {
        modalTitle.textContent = `Detalles de la Devolución #${String(details.id).padStart(6, '0')}`;
        modalBody.innerHTML = `
            <div class="space-y-2 text-sm">
                <p><strong>Fecha de solicitud:</strong> ${formatDate(details.fechaSolicitud)}</p>
                <p><strong>Pedido Original:</strong> #${String(details.ventaId).padStart(6, '0')}</p>
                <p><strong>Motivo:</strong> ${details.motivo}</p>
                <div class="pt-2">
                    <p class="font-semibold">Comentarios:</p>
                    <p class="p-2 bg-gray-50 rounded border mt-1">${details.comentarios || '<em>Sin comentarios.</em>'}</p>
                </div>
            </div>
        `;
        modal.classList.remove('hidden');
        modal.classList.add('flex');
    }

    function hideDetailModal() {
        modal.classList.add('hidden');
        modal.classList.remove('flex');
    }

    // --- EVENT LISTENERS ---
    tableBody.addEventListener('click', event => {
        const btn = event.target.closest('.details-btn');
        const row = event.target.closest('tr[data-return-id]');
        const id = btn ? btn.dataset.returnId : row ? row.dataset.returnId : null;
        if (id) fetchReturnDetails(id);
    });

    modalCloseBtn.addEventListener('click', hideDetailModal);
    modalOverlay.addEventListener('click', hideDetailModal);

    // --- CARGA INICIAL ---
    fetchReturns();
}

document.addEventListener('DOMContentLoaded', initializePage);