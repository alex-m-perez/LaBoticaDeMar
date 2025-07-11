<div class="bg-white p-6 rounded-2xl shadow border">
    <h2 class="text-lg font-semibold mb-4">Historial de devoluciones</h2>
    <div class="overflow-auto">
        <table class="min-w-full text-sm">
            <thead class="text-gray-500 border-b">
                <tr>
                    <th class="px-4 py-2 text-left">Devolución ID</th>
                    <th class="px-4 py-2 text-left">Fecha Solicitud</th>
                    <th class="px-4 py-2 text-left">Pedido Original</th>
                    <th class="px-4 py-2 text-left">Motivo</th>
                    <th class="px-4 py-2 text-center">Acciones</th>
                </tr>
            </thead>
            <tbody id="returns-table-body" class="text-gray-700 divide-y">
                </tbody>
        </table>
    </div>
    <div id="pagination-controls" class="flex justify-center mt-6"></div>
</div>

<div id="return-detail-modal" class="fixed inset-0 z-50 hidden items-center justify-center">
    <div id="modal-overlay" class="fixed inset-0 bg-black opacity-50"></div>
    <div class="relative bg-white rounded-lg shadow-xl w-11/12 md:w-1/2 lg:w-1/3 max-h-[90vh] overflow-y-auto m-4">
        <div class="p-6">
            <div class="flex justify-between items-start">
                <h3 id="modal-title" class="text-2xl font-semibold text-gray-900">Detalles de la Devolución</h3>
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