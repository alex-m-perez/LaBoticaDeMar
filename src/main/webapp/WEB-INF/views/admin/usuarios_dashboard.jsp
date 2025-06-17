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
                            <th class="px-4 py-2 text-left">Usuario</th>
                            <th class="px-4 py-2 text-left">Cantidad</th>
                            <th class="px-4 py-2 text-left">Ticket medio (€)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${topBuyers}">
                            <tr>
                                <td class="px-4 py-2">${u.username}</td>
                                <td class="px-4 py-2">${u.totalSales}</td>
                                <td class="px-4 py-2">${u.totalSales}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- Últimos registrados -->
        <div class="bg-white p-6 rounded-2xl shadow border">
            <h2 class="text-lg font-semibold mb-4">Últimos usuarios registrados</h2>
            <div class="overflow-auto">
                <table id="tablaUltimosUsuarios" class="min-w-full text-sm">
                    <thead class="text-gray-500 border-b">
                        <tr>
                            <th class="px-4 py-2 text-left">ID Usuario</th>
                            <th class="px-4 py-2 text-left">Fecha Registro</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${latestUsers}">
                            <tr>
                                <td class="px-4 py-2">${u.id}</td>
                                <td class="px-4 py-2">${u.registerDate}</td>
                            </tr>
                        </c:forEach>
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
                            <th class="px-4 py-2 text-left">Usuario</th>
                            <th class="px-4 py-2 text-left">Cantidad</th>
                            <th class="px-4 py-2 text-left">Ticket medio (€)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${topReturners}">
                            <tr>
                                <td class="px-4 py-2">${u.username}</td>
                                <td class="px-4 py-2">${u.totalReturns}</td>
                                <td class="px-4 py-2">${u.totalReturns}</td>
                            </tr>
                        </c:forEach>
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
                        <th class="px-4 py-2 text-left">Nombre</th>
                        <th class="px-4 py-2 text-left">Apellido1</th>
                        <th class="px-4 py-2 text-left">Apellido2</th>
                        <th class="px-4 py-2 text-left">Puntos</th>
                        <th class="px-4 py-2 text-left">Correo</th>
                        <th class="px-4 py-2 text-left">Dirección</th>
                        <th class="px-4 py-2 text-left">Teléfono</th>
                        <th class="px-4 py-2 text-left">Roles</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="u" items="${allUsers}">
                        <tr>
                            <td class="px-4 py-2">${u.id}</td>
                            <td class="px-4 py-2">${u.nombre}</td>
                            <td class="px-4 py-2">${u.apellido1}</td>
                            <td class="px-4 py-2">${u.apellido2}</td>
                            <td class="px-4 py-2">${u.puntos}</td>
                            <td class="px-4 py-2">${u.correo}</td>
                            <td class="px-4 py-2">${u.direccionPostal}</td>
                            <td class="px-4 py-2">${u.telefono}</td>
                            <td class="px-4 py-2">
                                <c:forEach var="rol" items="${u.roles}">
                                    <span class="inline-block px-2 py-0.5 text-xs bg-gray-100 rounded mr-1">${rol}</span>
                                </c:forEach>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>
