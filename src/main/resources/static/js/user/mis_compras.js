/**
 * Manages the "Mis Compras" section of the webstore.
 * Fetches and displays a paginated list of sales and shows details in a modal.
 */
function initializePage() {
    // --- Referencias a elementos del DOM ---
    const tableBody = document.getElementById('sales-table-body');
    const paginationControls = document.getElementById('pagination-controls');
    const modal = document.getElementById('sale-detail-modal');
    const modalOverlay = document.getElementById('modal-overlay');
    const modalCloseBtn = document.getElementById('modal-close-btn');
    const modalTitle = document.getElementById('modal-title');
    const modalBody = document.getElementById('modal-body');
    
    // --- Referencias a elementos del DOM del modal de devolución ---
    const returnModal = document.getElementById('return-modal');
    const returnModalOverlay = document.getElementById('return-modal-overlay');
    const returnModalCloseBtn = document.getElementById('return-modal-close-btn');
    const returnReasonSelect = document.getElementById('return-reason-select');
    const returnCommentsTextarea = document.getElementById('return-comments-textarea');
    const charCounter = document.getElementById('char-counter');
    const submitReturnBtn = returnModal.querySelector('button.bg-blue-600'); // Botón de Enviar Solicitud

    const API_VENTAS_URL = '/api/ventas/mis_compras';
    const API_DEVOLUCIONES_URL = '/api/devoluciones'; // --- NUEVO: URL base para devoluciones ---

    // --- NUEVO: Variable para almacenar el ID de la venta seleccionada ---
    let currentSaleId = null; 

    if (!tableBody || !paginationControls || !modal || !modalOverlay || !modalCloseBtn || !modalTitle || !modalBody ||
        !returnModal || !returnModalOverlay || !returnModalCloseBtn || !returnReasonSelect || !returnCommentsTextarea || !charCounter || !submitReturnBtn) {
        console.error('Faltan elementos del DOM para inicializar la página de Mis Compras.');
        return;
    }

    const DevolucionEnum = [
        { id: 1, etiqueta: "Producto dañado en envío" },
        { id: 2, etiqueta: "Producto defectuoso" },
        { id: 3, etiqueta: "Producto equivocado" },
        { id: 4, etiqueta: "Cantidad incorrecta" },
        { id: 5, etiqueta: "Producto caducado" },
        { id: 6, etiqueta: "Embalaje abierto" },
        { id: 7, etiqueta: "Faltan componentes" },
        { id: 8, etiqueta: "Error en descripción" },
        { id: 9, etiqueta: "Producto repetido" },
        { id: 10, etiqueta: "Entrega inadecuada" }
    ];

    function populateReturnReasons() {
        returnReasonSelect.innerHTML = '<option value="" selected disabled>Elige un motivo...</option>';
        DevolucionEnum.forEach(motivo => {
            const option = document.createElement('option');
            option.value = motivo.id;
            option.textContent = motivo.etiqueta;
            returnReasonSelect.appendChild(option);
        });
    }

    // --- (resto de funciones como formatDate, formatCurrency, fetchSales, etc. se mantienen igual) ---
    const formatDate = dateString => {
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' });
    };

    function formatCurrency(value) {
        return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(value);
    }

    function fetchSales(page = 0) {
        tableBody.innerHTML = `<tr><td colspan="5" class="text-center p-10">Cargando compras...</td></tr>`;
        fetch(`${API_VENTAS_URL}?page=${page}&size=10`)
            .then(response => response.ok ? response.json() : Promise.reject(response))
            .then(data => {
                renderSales(data.salesPage.content);
                renderPagination(data.salesPage);
            })
            .catch(() => {
                tableBody.innerHTML = `<tr><td colspan="5" class="text-center text-red-500 p-10">Error al cargar el historial de compras.</td></tr>`;
            });
    }
    
    function renderSales(sales) {
        tableBody.innerHTML = '';
        if (!sales || sales.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="5" class="text-center p-10">No has realizado ninguna compra todavía.</td></tr>`;
            return;
        }
        sales.forEach(sale => {
            const row = document.createElement('tr');
            row.className = 'hover:bg-gray-50 cursor-pointer';
            row.dataset.saleId = sale.id;
            row.innerHTML = `
                <td class="px-4 py-3 font-medium">#${String(sale.id).padStart(6, '0')}</td>
                <td class="px-4 py-3">${formatDate(sale.fechaVenta)}</td>
                <td class="px-4 py-3">${sale.totalItems}</td>
                <td class="px-4 py-3 text-right">${formatCurrency(sale.montoTotal)}</td>
                <td class="px-4 py-3 text-center"><span class="text-blue-600 hover:underline">Detalles</span></td>
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
            btn.textContent = (i + 1).toString();
            btn.className = `px-4 py-2 mx-1 rounded-md text-sm font-medium ${i === currentPage ? 'bg-pistachio text-white' : 'bg-white text-gray-700 hover:bg-gray-100'}`;
            btn.addEventListener('click', () => fetchSales(i));
            paginationControls.appendChild(btn);
        }
    }
    
    function fetchSaleDetails(saleId) {
        fetch(`${API_VENTAS_URL}/${saleId}`)
            .then(response => response.ok ? response.json() : Promise.reject(response))
            .then(details => showModal(details))
            .catch(() => alert('No se pudieron cargar los detalles del pedido.'));
    }

    function showModal(details) {
        // --- NUEVO: Guardar el ID de la venta actual ---
        currentSaleId = details.id; 

        modalTitle.textContent = `Detalles del Pedido #${String(details.id).padStart(6, '0')}`;
        const calcUnitPrice = price => Math.round(price * 100) / 100;
        
        const tableRows = details.productos.map(p => {
            const qty = p.cantidad;
            const unit = calcUnitPrice(p.precioUnitario);
            const lineTotal = calcUnitPrice(unit * qty);
            return `
                <tr class="border-b">
                    <td class="px-4 py-2 flex items-center">
                        <img src="${p.imagenPath || '/images/placeholder.png'}" alt="${p.nombre}" class="w-12 h-12 object-cover rounded mr-3">
                        ${p.nombre}
                    </td>
                    <td class="px-4 py-2 text-center">${qty}</td>
                    <td class="px-4 py-2 text-center">${formatCurrency(unit)}</td>
                    <td class="px-4 py-2 text-center">${formatCurrency(lineTotal)}</td>
                </tr>`;
        }).join('');

        modalBody.innerHTML = `
            <div class="grid grid-cols-2 gap-4 mb-4 text-sm">
                <div><strong>Fecha del pedido:</strong> ${formatDate(details.fechaVenta)}</div>
                <div class="text-right"><strong>Puntos utilizados:</strong> ${details.puntosUtilizados || 0}</div>
            </div>
            <table class="w-full text-sm text-left">
                <thead class="bg-gray-100"><tr class="rounded-md"><th class="px-4 py-2">Producto</th><th class="px-4 py-2">Cantidad</th><th class="px-4 py-2">Precio</th><th class="px-4 py-2">Total</th></tr></thead>
                <tbody>${tableRows}</tbody>
            </table>
            <div class="flex justify-between items-center mt-4 pr-2">
                <button id="open-return-modal-btn" class="bg-red-500 text-white px-4 py-2 rounded-md hover:bg-red-600 text-sm">Realizar devolución</button>
                <strong class="text-lg">Total del Pedido: ${formatCurrency(details.montoTotal)}</strong>
            </div>
        `;

        document.getElementById('open-return-modal-btn').addEventListener('click', () => {
            returnReasonSelect.value = '';
            returnCommentsTextarea.value = '';
            charCounter.textContent = '0/650';
            showReturnModal();
        });

        modal.classList.remove('hidden');
        modal.classList.add('flex');
    }

    function hideModal() {
        modal.classList.add('hidden');
        modal.classList.remove('flex');
    }

    function showReturnModal() {
        returnModal.classList.remove('hidden');
        returnModal.classList.add('flex');
    }

    function hideReturnModal() {
        returnModal.classList.add('hidden');
        returnModal.classList.remove('flex');
    }

    // --- NUEVO: Función para enviar el formulario de devolución ---
    function handleReturnSubmit() {
        const motivoId = returnReasonSelect.value;
        const comentarios = returnCommentsTextarea.value;

        if (!motivoId) {
            alert('Por favor, selecciona un motivo para la devolución.');
            return;
        }

        const returnData = {
            motivoId: parseInt(motivoId, 10),
            comentarios: comentarios
        };

        // Deshabilitar botón para evitar envíos múltiples
        submitReturnBtn.disabled = true;
        submitReturnBtn.textContent = 'Enviando...';

        fetch(`${API_DEVOLUCIONES_URL}/new/${currentSaleId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                // Incluir token CSRF si es necesario
            },
            body: JSON.stringify(returnData)
        })
        .then(response => {
            if (!response.ok) {
                // Si la respuesta no es OK, lanza un error para que lo capture el .catch()
                return response.json().then(err => Promise.reject(err));
            }
            return response.json(); 
        })
        .then(() => {
            hideReturnModal();
            hideModal();
            alert('Solicitud de devolución registrada con éxito.');
            fetchSales(); // Opcional: Recargar la lista de ventas
        })
        .catch(error => {
            console.error('Error al registrar la devolución:', error);
            alert(`No se pudo registrar la devolución. Motivo: ${error.message || 'Error desconocido'}`);
        })
        .finally(() => {
            // Volver a habilitar el botón
            submitReturnBtn.disabled = false;
            submitReturnBtn.textContent = 'Enviar Solicitud';
        });
    }

    tableBody.addEventListener('click', event => {
        const row = event.target.closest('tr[data-sale-id]');
        if (row) {
            fetchSaleDetails(row.dataset.saleId);
        }
    });

    modalCloseBtn.addEventListener('click', hideModal);
    modalOverlay.addEventListener('click', hideModal);
    returnModalCloseBtn.addEventListener('click', hideReturnModal);
    returnModalOverlay.addEventListener('click', hideReturnModal);
    returnCommentsTextarea.addEventListener('input', () => {
        charCounter.textContent = `${returnCommentsTextarea.value.length}/650`;
    });
    
    // --- MODIFICADO: Añadido listener para el botón de envío ---
    submitReturnBtn.addEventListener('click', handleReturnSubmit);

    populateReturnReasons();
    fetchSales();
}

document.addEventListener('DOMContentLoaded', initializePage);