<div class="bg-white min-h-screen space-y-6">

    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <div class="bg-white p-6 rounded-2xl shadow border">
        <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold mb-0">Listado de Empleados</h2>
            <a href="${pageContext.request.contextPath}/admin/register"
               class="flex items-center bg-pistachio text-white font-medium px-4 py-1.5 rounded-md hover:bg-dark-pistachio transition">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mr-1" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" clip-rule="evenodd"
                          d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" />
                </svg>
                Nuevo Empleado
            </a>
        </div>
        <div class="overflow-auto">
            <table id="tablaEmpleados" class="min-w-full text-sm">
                <thead class="text-gray-500 border-b">
                    <tr>
                        <th class="px-4 py-2 text-left">ID</th>
                        <th class="px-4 py-2 text-left">Nombre</th>
                        <th class="px-4 py-2 text-left">Rol</th>
                        <th class="px-4 py-2 text-left">Activo</th>
                    </tr>
                </thead>
                <tbody id="empleadosBody" class="text-gray-700 divide-y">
                    </tbody>
            </table>
        </div>

        <div id="paginationEmp" class="flex justify-between items-center mt-4">
            <button id="prevEmpBtn" class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition" disabled>Anterior</button>
            <span>Página <span id="pageEmpNum">1</span> de <span id="totalEmpPages">1</span></span>
            <button id="nextEmpBtn" class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition">Siguiente</button>
        </div>
    </div>

    <div id="empleadoDetailModal" class="fixed inset-0 z-50 hidden flex items-center justify-center">
        <div id="empleadoModalOverlay" class="fixed inset-0 bg-black opacity-50"></div>
        <div class="relative bg-white rounded-lg shadow-xl w-11/12 md:w-1/2 max-h-[90vh] overflow-y-auto m-4">
            <div class="p-6">
                <div class="flex justify-between items-start">
                    <h3 id="modalTitle" class="text-2xl font-semibold text-gray-900">Detalles del Empleado</h3>
                    <button id="closeModalBtn" type="button" class="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center">
                        <svg class="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/></svg>
                    </button>
                </div>
                <div id="modalBody" class="mt-4 space-y-3 text-sm"></div>
            </div>
        </div>
    </div>

</div>