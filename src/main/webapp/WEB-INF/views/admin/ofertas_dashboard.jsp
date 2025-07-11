<div class="bg-white min-h-screen space-y-6">

    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!-- Filtros de Ofertas -->
    <div class="flex justify-center mb-10">
        <div class="p-4 bg-gray-100 border border-gray-200 rounded-xl shadow-sm inline-block">
            <form id="filterFormOffers" class="flex flex-wrap items-start justify-center gap-4 text-sm">
                <!-- Producto (Código / Nombre) -->
                <div class="flex flex-col">
                    <label for="productoId" class="text-gray-600">Cód. Producto</label>
                    <input type="text" id="productoId" name="productoId" placeholder="Ej: 123456"
                        class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                </div>
                <div class="flex flex-col relative">
                    <label for="productoNombre" class="text-gray-600">Producto</label>
                    <input type="text" id="productoNombre" name="productoNombre" placeholder="Ej: Ibuprofeno"
                        class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                    <ul id="nombreSuggestionsOffers" class="absolute top-full left-0 w-40 bg-white border border-gray-300 rounded-md shadow mt-1 max-h-48 overflow-auto hidden z-10"></ul>
                </div>

                <!-- Activo -->
                <div class="flex flex-col">
                    <label for="activo" class="text-gray-600">Activo</label>
                    <select id="activo" name="activo"
                        class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
                        <option value="">Todos</option>
                        <option value="true">Activo</option>
                        <option value="false">Inactivo</option>
                    </select>
                </div>

                <!-- Descuento (min/max) -->
                <div class="flex flex-col">
                    <label class="text-gray-600">Descuento (%)</label>
                    <div class="flex gap-2">
                        <input type="number" step="0.01" id="discountMin" name="discountMin" placeholder="0"
                            class="w-20 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                        <input type="number" step="0.01" id="discountMax" name="discountMax" placeholder="100"
                            class="w-20 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                    </div>
                </div>

                <!-- Fecha de Inicio y Fin -->
                <div class="flex flex-col">
                    <label for="startDate" class="text-gray-600">Inicio</label>
                    <input type="date" id="startDate" name="startDate"
                        class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                </div>
                <div class="flex flex-col">
                    <label for="endDate" class="text-gray-600">Fin</label>
                    <input type="date" id="endDate" name="endDate"
                        class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
                </div>

                <!-- Botones -->
                <div class="flex gap-2 self-end">
                    <button type="submit"
                        class="bg-pistachio text-white font-medium px-4 py-1.5 rounded-md hover:bg-dark-pistachio transition">
                        Buscar
                    </button>
                    <button type="reset"
                        class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition">
                        Limpiar
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- KPIs de Ofertas -->
    <div class="grid grid-cols-1 md:grid-cols-4 gap-4 mb-8">
        <div class="p-4 rounded-2xl shadow border">
            <h2 class="text-sm text-gray-500">Total Ofertas</h2>
            <p class="text-2xl font-bold">${totalOffers}</p>
        </div>
        <div class="p-4 rounded-2xl shadow border">
            <h2 class="text-sm text-gray-500">Ofertas Activas</h2>
            <p class="text-2xl font-bold text-green-600">${activeOffers}</p>
        </div>
        <div class="p-4 rounded-2xl shadow border">
            <h2 class="text-sm text-gray-500">Ofertas Expiradas</h2>
            <p class="text-2xl font-bold text-red-600">${expiredOffers}</p>
        </div>
        <div class="p-4 rounded-2xl shadow border">
            <h2 class="text-sm text-gray-500">Descuento Medio</h2>
            <p class="text-2xl font-bold">${averageDiscount}%</p>
        </div>
    </div>

    <!-- Tabla de Ofertas -->
    <div class="bg-white p-6 rounded-2xl shadow border">
        <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold mb-0">Listado de Ofertas</h2>
            <div class="flex items-center space-x-2">
                <button id="nuevoOfferBtn"
                        class="flex items-center bg-pistachio text-white font-medium px-4 py-1.5 rounded-md hover:bg-dark-pistachio transition">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mr-1" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" clip-rule="evenodd"
                              d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" />
                    </svg>
                    Nueva Oferta
                </button>
            </div>
        </div>
        <div class="overflow-auto">
            <table id="tablaOfertas" class="min-w-full text-sm">
                <thead class="text-gray-500 border-b">
                    <tr>
                        <th class="px-4 py-2 text-left">Producto</th>
                        <th class="px-4 py-2 text-left">Descuento</th>
                        <th class="px-4 py-2 text-left">Inicio</th>
                        <th class="px-4 py-2 text-left">Fin</th>
                        <th class="px-4 py-2 text-left">Estado</th>
                    </tr>
                </thead>
                <tbody id="ofertasBody" class="text-gray-700 divide-y">
                    <c:forEach var="oferta" items="${ofertas}">
                        <tr>
                            <td class="px-4 py-2">${oferta.producto.nombre}</td>
                            <td class="px-4 py-2">${oferta.discount}%</td>
                            <td class="px-4 py-2">${oferta.startDate}</td>
                            <td class="px-4 py-2">${oferta.endDate}</td>
                            <td class="px-4 py-2">
                                <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                                    ${oferta.active ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}">
                                    ${oferta.active ? 'Activa' : 'Inactiva'}
                                </span>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty ofertas}">
                        <tr>
                            <td colspan="5" class="px-4 py-4 text-center text-gray-500">No hay ofertas disponibles.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <!-- Paginación -->
        <div id="paginationOffers" class="flex justify-between items-center mt-4">
            <button id="prevOfferBtn"
                    class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition" disabled>
                Anterior
            </button>
            <span>Página <span id="pageOfferNum">1</span> de <span id="totalOfferPages">1</span></span>
            <button id="nextOfferBtn"
                    class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition">
                Siguiente
            </button>
        </div>
    </div>

    <!-- Modal Nueva/Editar Oferta -->
    <div id="offerModal" class="fixed inset-0 flex items-center justify-center hidden z-[10000]">
        <div class="bg-white rounded-2xl shadow-lg w-full max-w-lg p-6 relative">
            <button
                id="closeOfferModal"
                class="absolute right-4 top-4 text-2xl text-gray-500 hover:text-red-500 transition">&times;</button>
            <h3 class="text-xl font-semibold text-center mb-4" id="offerModalTitle">Crear Oferta</h3>
            <form id="offerForm" class="space-y-4">
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="offerProduct" class="block text-sm text-gray-600">Producto</label>
                        <select id="offerProduct" name="productId" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio">
                            <option value="">Selecciona producto</option>
                            <c:forEach var="producto" items="${productos}">
                                <option value="${producto.id}">${producto.nombre}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div>
                        <label for="offerDiscount" class="block text-sm text-gray-600">Descuento (%)</label>
                        <input type="number" step="0.01" id="offerDiscount" name="discount" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio" />
                    </div>
                    <div>
                        <label for="offerStart" class="block text-sm text-gray-600">Inicio</label>
                        <input type="date" id="offerStart" name="startDate" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio" />
                    </div>
                    <div>
                        <label for="offerEnd" class="block text-sm text-gray-600">Fin</label>
                        <input type="date" id="offerEnd" name="endDate" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio" />
                    </div>
                    <div class="col-span-2">
                        <label for="offerActive" class="block text-sm text-gray-600">Activo</label>
                        <select id="offerActive" name="active"
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio">
                            <option value="true">Sí</option>
                            <option value="false">No</option>
                        </select>
                    </div>
                </div>
                <div class="text-center">
                    <button type="submit"
                        class="bg-pistachio text-white font-medium px-6 py-2 rounded-md hover:bg-dark-pistachio transition">
                        Guardar
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
