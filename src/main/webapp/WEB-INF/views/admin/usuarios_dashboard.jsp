<%-- Archivo: /WEB-INF/views/admin/sections/usuarios.jsp --%>
<div class="bg-white min-h-screen space-y-6">

    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div class="bg-white p-6 rounded-2xl shadow border flex flex-col">
            <h2 class="text-lg font-semibold mb-4">Usuarios con más compras</h2>
            <div class="overflow-auto flex-grow">
                <table class="min-w-full text-sm">
                    <thead class="text-gray-500 border-b"><tr><th class="px-4 py-2 text-left">ID</th><th class="px-4 py-2 text-center">Cantidad</th><th class="px-4 py-2 text-right">Promedio</th></tr></thead>
                    <tbody id="kpi-compradores-tbody"></tbody>
                </table>
            </div>
            <div id="kpi-compradores-pagination" class="flex justify-between items-center mt-3 text-xs"></div>
        </div>

        <div class="bg-white p-6 rounded-2xl shadow border flex flex-col">
            <h2 class="text-lg font-semibold mb-4">Usuarios con más gasto</h2>
            <div class="overflow-auto flex-grow">
                <table class="min-w-full text-sm">
                    <thead class="text-gray-500 border-b"><tr><th class="px-4 py-2 text-left">ID</th><th class="px-4 py-2 text-center">Pedidos</th><th class="px-4 py-2 text-right">Total</th></tr></thead>
                    <tbody id="kpi-gastadores-tbody"></tbody>
                </table>
            </div>
            <div id="kpi-gastadores-pagination" class="flex justify-between items-center mt-3 text-xs"></div>
        </div>

        <div class="bg-white p-6 rounded-2xl shadow border flex flex-col">
            <h2 class="text-lg font-semibold mb-4">Usuarios con más devoluciones</h2>
            <div class="overflow-auto flex-grow">
                <table class="min-w-full text-sm">
                    <thead class="text-gray-500 border-b"><tr><th class="px-4 py-2 text-left">ID</th><th class="px-4 py-2 text-center">Cantidad</th><th class="px-4 py-2 text-right">Valor</th></tr></thead>
                    <tbody id="kpi-devoluciones-tbody"></tbody>
                </table>
            </div>
            <div id="kpi-devoluciones-pagination" class="flex justify-between items-center mt-3 text-xs"></div>
        </div>
    </div>

    <div class="bg-white p-6 rounded-2xl shadow border">
        <h2 class="text-lg font-semibold mb-4">Listado Completo de Clientes</h2>
        <div class="overflow-auto">
            <table class="min-w-full text-sm">
                <thead class="text-gray-500 border-b">
                    <tr>
                        <th class="px-4 py-2 text-left">ID</th><th class="px-4 py-2 text-left">Nombre completo</th><th class="px-4 py-2 text-left">Fecha nacimiento</th><th class="px-4 py-2 text-left">Dirección</th><th class="px-4 py-2 text-left">Correo</th><th class="px-4 py-2 text-left">Teléfono</th>
                    </tr>
                </thead>
                <tbody id="usuariosTbody" class="divide-y"></tbody>
            </table>
        </div>
        <div class="flex justify-between items-center mt-4 text-sm">
            <button id="main-prevBtn" class="bg-gray-200 text-gray-700 px-3 py-1 rounded-md hover:bg-gray-300">Anterior</button>
            <span id="main-pageInfo">Página 1 de 1</span>
            <button id="main-nextBtn" class="bg-gray-200 text-gray-700 px-3 py-1 rounded-md hover:bg-gray-300">Siguiente</button>
        </div>
    </div>
</div>

<div id="userDetailModal" class="fixed inset-0 z-50 hidden flex items-center justify-center">
    <div id="modalOverlay" class="fixed inset-0 bg-black opacity-50"></div>
    <div class="relative bg-white rounded-lg shadow-xl w-11/12 md:w-2/3 max-h-[90vh] overflow-y-auto m-4">
        <div class="p-6">
            <div class="flex justify-between items-start">
                <h3 id="modalTitle" class="text-2xl font-semibold text-gray-900">Ficha del Cliente</h3>
                <button id="closeModalBtn" type="button" class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center">
                    <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/></svg>
                </button>
            </div>
            <div id="modalBody" class="mt-4 space-y-4 text-sm"></div>
        </div>
    </div>
</div>