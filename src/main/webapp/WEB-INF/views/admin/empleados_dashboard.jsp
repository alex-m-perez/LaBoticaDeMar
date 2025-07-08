<div class="bg-white min-h-screen space-y-6">

    <%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
    <!-- Tabla de Empleados -->
    <div class="bg-white p-6 rounded-2xl shadow border">
        <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold mb-0">Listado de Empleados</h2>
            <button id="nuevoEmpBtn"
                    class="flex items-center bg-pistachio text-white font-medium px-4 py-1.5 rounded-md hover:bg-dark-pistachio transition">
                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mr-1" fill="currentColor" viewBox="0 0 20 20">
                    <path fill-rule="evenodd" clip-rule="evenodd"
                          d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" />
                </svg>
                Nuevo Empleado
            </button>
        </div>
        <div class="overflow-auto">
          <table id="tablaEmpleados" class="min-w-full text-sm">
            <thead class="text-gray-500 border-b">
              <tr>
                <th class="px-4 py-2 text-left">ID</th>
                <th class="px-4 py-2 text-left">Nombre</th>
                <th class="px-4 py-2 text-left">Rol</th>
              </tr>
            </thead>
            <tbody id="empleadosBody" class="text-gray-700 divide-y">
              <tr>
                <td class="px-4 py-2">1</td>
                <td class="px-4 py-2">Lucía Fernández</td>
                <td class="px-4 py-2">EMPLEADO</td>
              </tr>
              <tr>
                <td class="px-4 py-2">2</td>
                <td class="px-4 py-2">Carlos Gómez</td>
                <td class="px-4 py-2">EMPLEADO</td>
              </tr>
              <tr>
                <td class="px-4 py-2">3</td>
                <td class="px-4 py-2">Marta Ruiz</td>
                <td class="px-4 py-2">EMPLEADO</td>
              </tr>
            </tbody>
          </table>
        </div>


        <!-- Paginación -->
        <div id="paginationEmp" class="flex justify-between items-center mt-4">
            <button id="prevEmpBtn" class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition" disabled>
                Anterior
            </button>
            <span>Página <span id="pageEmpNum">1</span> de <span id="totalEmpPages">1</span></span>
            <button id="nextEmpBtn" class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition">
                Siguiente
            </button>
        </div>
    </div>

    <!-- Modal Nuevo/Editar Empleado -->
    <div id="empleadoModal" class="fixed inset-0 flex items-center justify-center hidden z-[10000]">
        <div class="bg-white rounded-2xl shadow-lg w-full max-w-lg p-6 relative">
            <button id="closeEmpModal" class="absolute right-4 top-4 text-2xl text-gray-500 hover:text-red-500 transition">&times;</button>
            <h3 class="text-xl font-semibold text-center mb-4" id="empleadoModalTitle">Crear Empleado</h3>
            <form id="empleadoForm" class="space-y-4">
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label for="empNombre" class="block text-sm text-gray-600">Nombre</label>
                        <input type="text" id="empNombre" name="nombre" required class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio" />
                    </div>
                    <div>
                        <label for="empDepto" class="block text-sm text-gray-600">Departamento</label>
                        <select id="empDepto" name="departamentoId" required class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio">
                            <option value="">Selecciona</option>
                            <c:forEach var="dep" items="${departamentos}"><option value="${dep.id}">${dep.nombre}</option></c:forEach>
                        </select>
                    </div>
                    <div>
                        <label for="empRol" class="block text-sm text-gray-600">Rol</label>
                        <select id="empRol" name="rolId" required class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-p pistachio focus:border-pistachio">
                            <option value="">Selecciona</option>
                            <c:forEach var="rol" items="${roles}"><option value="${rol.id}">${rol.nombre}</option></c:forEach>
                        </select>
                    </div>
                    <div>
                        <label for="empActivo" class="block text-sm text-gray-600">Activo</label>
                        <select id="empActivo" name="activo" required class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio">
                            <option value="true">Sí</option>
                            <option value="false">No</option>
                        </select>
                    </div>
                    <div>
                        <label for="empFechaIngreso" class="block text-sm text-gray-600">Fecha Ingreso</label>
                        <input type="date" id="empFechaIngreso" name="fechaIngreso" required class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-p pistachio focus:border-p pistachio" />
                    </div>
                </div>
                <div class="text-center">
                    <button type="submit" class="bg-pistachio text-white font-medium px-6 py-2 rounded-md hover:bg-dark-pistachio transition">Guardar</button>
                </div>
            </form>
        </div>
    </div>

</div>
