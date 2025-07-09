<div class="bg-white min-h-screen space-y-6">

    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!-- Dashboard de Usuarios -->
    <!-- Primera fila: 3 tablas pequeñas -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <!-- Usuarios con más compras -->
        <div class="bg-white p-6 rounded-2xl shadow border">
            <h2 class="text-lg font-semibold mb-4">Usuarios con más compras</h2>
            <div class="overflow-auto">
                <table id="tablaTopCompradores" class="min-w-full text-sm">
                    <thead class="text-gray-500 border-b">
                        <tr>
                            <th class="px-4 py-2 text-left">ID</th>
                            <th class="px-4 py-2 text-left">Cantidad</th>
                            <th class="px-4 py-2 text-left">Promedio</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="px-4 py-2">1</td>
                            <td class="px-4 py-2">8</td>
                            <td class="px-4 py-2">8.10 €</td>
                        </tr>
                        <tr>
                            <td class="px-4 py-2">2</td>
                            <td class="px-4 py-2">6</td>
                            <td class="px-4 py-2">7.95 €</td>
                        </tr>
                        <tr>
                            <td class="px-4 py-2">3</td>
                            <td class="px-4 py-2">4</td>
                            <td class="px-4 py-2">8.00 €</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Últimos registrados -->
        <div class="bg-white p-6 rounded-2xl shadow border">
            <h2 class="text-lg font-semibold mb-4">Usuarios con más gasto</h2>
            <div class="overflow-auto">
                <table id="tablaUltimosUsuarios" class="min-w-full text-sm">
                    <thead class="text-gray-500 border-b">
                        <tr>
                            <th class="px-4 py-2 text-left">ID</th>
                            <th class="px-4 py-2 text-left">Pedidos</th>
                            <th class="px-4 py-2 text-left">Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="px-4 py-2">1</td>
                            <td class="px-4 py-2">8</td>
                            <td class="px-4 py-2">8.10 €</td>
                        </tr>
                        <tr>
                            <td class="px-4 py-2">2</td>
                            <td class="px-4 py-2">6</td>
                            <td class="px-4 py-2">7.95 €</td>
                        </tr>
                        <tr>
                            <td class="px-4 py-2">3</td>
                            <td class="px-4 py-2">4</td>
                            <td class="px-4 py-2">8.00 €</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- Usuarios con más devoluciones -->
        <div class="bg-white p-6 rounded-2xl shadow border">
            <h2 class="text-lg font-semibold mb-4">Usuarios con más devoluciones</h2>
            <div class="overflow-auto">
                <table id="tablaTopDevoluciones" class="min-w-full text-sm">
                    <thead class="text-gray-500 border-b">
                        <tr>
                            <th class="px-4 py-2 text-left">Id</th>
                            <th class="px-4 py-2 text-left">Cantidad</th>
                            <th class="px-4 py-2 text-left">Promedio</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td class="px-4 py-2">1</td>
                            <td class="px-4 py-2">7</td>
                            <td class="px-4 py-2">7.80</td>
                        </tr>
                        <tr>
                            <td class="px-4 py-2">2</td>
                            <td class="px-4 py-2">5</td>
                            <td class="px-4 py-2">8.20</td>
                        </tr>
                        <tr>
                            <td class="px-4 py-2">3</td>
                            <td class="px-4 py-2">3</td>
                            <td class="px-4 py-2">8.00</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Segunda fila: tabla completa de usuarios -->
    <div class="bg-white p-6 rounded-2xl shadow border">
        <h2 class="text-lg font-semibold mb-4">Listado Completo de Clientes</h2>
        <div class="overflow-auto">
            <table id="tablaUsuarios" class="min-w-full text-sm">
                <thead class="text-gray-500 border-b">
                    <tr>
                        <th class="px-4 py-2 text-left">ID</th>
                        <th class="px-4 py-2 text-left">Nombre completo</th>
                        <th class="px-4 py-2 text-left">Fecha nacimiento</th>
                        <th class="px-4 py-2 text-left">Dirección</th>
                        <th class="px-4 py-2 text-left">Correo</th>
                        <th class="px-4 py-2 text-left">Teléfono</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="px-4 py-2">U1001</td>
                        <td class="px-4 py-2">Carlos Gomez Martinez</td>
                        <td class="px-4 py-2">DD/MM/AAAA</td>
                        <td class="px-4 py-2">Calle Ejmplo 1</td>
                        <td class="px-4 py-2">carlos89@mail.com</td>
                        <td class="px-4 py-2">612345678</td>
                    </tr>
                    <tr>
                        <td class="px-4 py-2">U1002</td>
                        <td class="px-4 py-2">Lucía</td>
                        <td class="px-4 py-2">DD/MM/AAAA</td>
                        <td class="px-4 py-2">Calle Ejmplo 1</td>
                        <td class="px-4 py-2">lucia.g@mail.com</td>
                        <td class="px-4 py-2">622334455</td>
                    </tr>
                    <tr>
                        <td class="px-4 py-2">U1003</td>
                        <td class="px-4 py-2">Marta</td>
                        <td class="px-4 py-2">DD/MM/AAAA</td>
                        <td class="px-4 py-2">Calle Ejmplo 1</td>
                        <td class="px-4 py-2">martaruiz@mail.com</td>
                        <td class="px-4 py-2">600112233</td>
                    </tr>
                </tbody>
            </table>
        </div>

        <div id="pagination" class="flex justify-between items-center mt-4">
            <button id="prevBtn"
                    class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition"
                    disabled>
                Anterior
            </button>
            <span>Página <span id="pageNum">1</span> de <span id="totalPages">1</span></span>
            <button id="nextBtn"
                    class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition">
                Siguiente
            </button>
        </div>
    </div>

</div>
