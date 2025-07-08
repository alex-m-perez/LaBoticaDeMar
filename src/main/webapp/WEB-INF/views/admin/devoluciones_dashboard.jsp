<%-- Archivo: /WEB-INF/views/admin/sections/devoluciones.jsp --%>
<div class="bg-white space-y-6">
    

    <div class="flex justify-center mb-10">
        <div class="p-4 bg-gray-100 border border-gray-200 rounded-xl shadow-sm inline-block">
            <form id="returnsFilterForm" class="flex flex-wrap items-end justify-center gap-4 text-sm">
                <div class="flex flex-col relative">
                    <label for="nombreUsuario" class="text-gray-600">Nombre Usuario</label>
                    <input type="text" id="nombreUsuario" placeholder="Buscar por nombre..." class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" autocomplete="off" />
                    <input type="hidden" id="clienteId" name="clienteId" />
                    <div id="userSuggestions" class="absolute top-full left-0 w-full bg-white border mt-1 rounded-md shadow-lg z-20 hidden max-h-48 overflow-y-auto"></div>
                </div>
                <div class="flex flex-col">
                    <label for="idUsuario" class="text-gray-600">ID Usuario</label>
                    <input type="text" id="idUsuario" name="idUsuario" placeholder="Ej: 123" class="w-28 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                </div>
                <div class="flex flex-col">
                    <label for="fechaInicio" class="text-gray-600">Desde</label>
                    <input type="date" id="fechaInicio" name="fechaInicio" class="w-36 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                </div>
                <div class="flex flex-col">
                    <label for="fechaFin" class="text-gray-600">Hasta</label>
                    <input type="date" id="fechaFin" name="fechaFin" class="w-36 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                </div>
                <div class="flex flex-col">
                    <label for="montoMin" class="text-gray-600">Monto Mín.</label>
                    <input type="number" id="montoMin" name="montoMin" step="0.01" placeholder="Ej: 5.00" class="w-28 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                </div>
                <div class="flex flex-col">
                    <label for="montoMax" class="text-gray-600">Monto Máx.</label>
                    <input type="number" id="montoMax" name="montoMax" step="0.01" placeholder="Ej: 50.00" class="w-28 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                </div>
                <div class="flex gap-2 self-end">
                    <button type="submit" class="bg-pistachio text-white font-medium px-4 py-1.5 rounded-md hover:bg-dark-pistachio transition">Buscar</button>
                    <button type="reset" class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition">Limpiar</button>
                </div>
            </form>
        </div>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div class="p-4 rounded-2xl shadow border">
            <h2 class="text-sm text-gray-500">Total Devoluciones (Hoy)</h2>
            <p id="kpi-devoluciones-hoy" class="text-2xl font-bold">0</p>
        </div>
        <div class="p-4 rounded-2xl shadow border">
            <h2 class="text-sm text-gray-500">Monto Devuelto (Hoy)</h2>
            <p id="kpi-monto-hoy" class="text-2xl font-bold">0,00 €</p>
        </div>
        <div class="p-4 rounded-2xl shadow border">
            <h2 class="text-sm text-gray-500">Total Devoluciones (Rango)</h2>
            <p id="kpi-devoluciones-rango" class="text-2xl font-bold">0</p>
        </div>
        <div class="p-4 rounded-2xl shadow border">
            <h2 class="text-sm text-gray-500">Monto Devuelto (Rango)</h2>
            <p id="kpi-monto-rango" class="text-2xl font-bold">0,00 €</p>
        </div>
    </div>

    <div class="bg-white p-6 rounded-2xl shadow border">
        <h2 class="text-lg font-semibold mb-4">Historial de Devoluciones</h2>
        <div class="overflow-auto">
            <table class="min-w-full text-sm">
                <thead class="text-gray-500 border-b">
                    <tr>
                        <th class="px-4 py-2 text-left">ID Devolución</th>
                        <th class="px-4 py-2 text-left">ID Cliente</th>
                        <th class="px-4 py-2 text-left">Nombre Cliente</th>
                        <th class="px-4 py-2 text-left">Fecha</th>
                        <th class="px-4 py-2 text-right">Monto</th>
                        <th class="px-4 py-2 text-left">Venta Original</th>
                        <th class="px-4 py-2 text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody id="returnsTbody" class="text-gray-700 divide-y"></tbody>
            </table>
        </div>
        <div id="pagination-controls" class="flex justify-between items-center mt-4 text-sm">
            <button id="prevBtn" class="bg-gray-200 text-gray-700 px-3 py-1 rounded-md hover:bg-gray-300 transition-colors">Anterior</button>
            <span class="px-4 text-gray-600">Página <span id="pageNum" class="font-semibold">1</span> de <span id="totalPages" class="font-semibold">1</span></span>
            <button id="nextBtn" class="bg-gray-200 text-gray-700 px-3 py-1 rounded-md hover:bg-gray-300 transition-colors">Siguiente</button>
        </div>
    </div>
</div>

<div id="returnDetailModal" class="fixed inset-0 z-50 hidden flex items-center justify-center">
    <div id="modalOverlay" class="fixed inset-0 bg-black opacity-50"></div>
    <div class="relative bg-white rounded-lg shadow-xl w-11/12 md:w-1/2 lg:w-1/3 max-h-[90vh] overflow-y-auto m-4">
        <div class="p-6">
            <div class="flex justify-between items-start">
                <h3 id="modalTitle" class="text-2xl font-semibold text-gray-900">Detalles de la Devolución</h3>
                <button id="closeModalBtn" type="button" class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center">
                     <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/></svg>
                </button>
            </div>
            <div id="modalBody" class="mt-4 space-y-4"></div>
        </div>
    </div>
</div>