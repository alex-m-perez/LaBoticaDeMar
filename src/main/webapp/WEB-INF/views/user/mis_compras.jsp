<div class="bg-white p-6 rounded-2xl shadow border">
    <h2 class="text-lg font-semibold mb-4">Historial de compras</h2>
    <div class="overflow-auto">
        <table class="min-w-full text-sm">
            <thead class="text-gray-500 border-b">
                <tr>
                    <th class="px-4 py-2 text-left">Pedido ID</th>
                    <th class="px-4 py-2 text-left">Fecha</th>
                    <th class="px-4 py-2 text-left">Nº de productos</th>
                    <th class="px-4 py-2 text-right">Total</th>
                    <th class="px-4 py-2 text-center">Acciones</th>
                </tr>
            </thead>
            <tbody id="sales-table-body" class="text-gray-700 divide-y">
                </tbody>
        </table>
    </div>
    <div id="pagination-controls" class="flex justify-center mt-6"></div>
</div>

<div id="sale-detail-modal" class="fixed inset-0 z-50 hidden items-center justify-center">
    <div id="modal-overlay" class="fixed inset-0 bg-black opacity-50"></div>

    <div class="relative bg-white rounded-lg shadow-xl w-11/12 md:w-3/4 lg:w-1/2 max-h-[90vh] overflow-y-auto m-4">
        <div class="p-6">
            <div class="flex justify-between items-start">
                <h3 id="modal-title" class="text-2xl font-semibold text-gray-900">Detalles del Pedido</h3>
                <button id="modal-close-btn" type="button" class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center">
                    <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                    </svg>
                    <span class="sr-only">Cerrar modal</span>
                </button>
            </div>
            <div id="modal-body" class="mt-4 space-y-4">
                </div>
        </div>
    </div>
</div>

<div id="return-modal" class="fixed inset-0 z-[100] hidden items-center justify-center">
    <div id="return-modal-overlay" class="absolute inset-0 bg-black opacity-50 z-0"></div>

    <div class="relative bg-white rounded-lg shadow-xl w-11/12 md:w-1/2 lg:w-1/3 max-h-[90vh] overflow-y-auto m-4 z-10">
        <div class="p-6">
            <div class="flex justify-between items-start">
                <h3 class="text-2xl font-semibold text-gray-900">Solicitar Devolución</h3>
                <button id="return-modal-close-btn" type="button" class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center">
                    <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
                        <path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
                    </svg>
                    <span class="sr-only">Cerrar modal</span>
                </button>
            </div>
            <div class="mt-4 space-y-4">
                <div>
                    <label for="return-reason-select" class="block mb-2 text-sm font-medium text-gray-900">Seleccionar motivo</label>
                    <select id="return-reason-select" class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5">
                        <option selected disabled>Elige un motivo...</option>
                    </select>
                </div>
                <div>
                    <label for="return-comments-textarea" class="block mb-2 text-sm font-medium text-gray-900">Comentarios</label>
                    <textarea id="return-comments-textarea" rows="4" class="block p-2.5 w-full text-sm text-gray-900 bg-gray-50 rounded-lg border border-gray-300 focus:ring-blue-500 focus:border-blue-500" maxlength="650"></textarea>
                    <div id="char-counter" class="text-sm text-gray-500 text-right mt-1">0/650</div>
                </div>
                <div class="text-right">
                    <button class="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">Enviar Solicitud</button>
                </div>
            </div>
        </div>
    </div>
</div>