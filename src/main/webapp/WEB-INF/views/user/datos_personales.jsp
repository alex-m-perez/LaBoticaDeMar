<%-- Importamos el Enum para poder usarlo directamente en el JSP --%>
<%@ page import="es.laboticademar.webstore.enumerations.PreferenciaEnum" %>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="max-w-4xl mx-auto">
    <form id="profileForm" class="space-y-8">
        <div class="bg-white border border-gray-200 rounded-2xl shadow-sm p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-5">
            
                <div class="md:col-span-2 text-lg font-semibold text-gray-800 border-b border-gray-200 pb-2 mb-2">Información Personal</div>
                <div>
                    <label for="nombre" class="block text-sm font-medium text-gray-700">Nombre</label>
                    <input type="text" id="nombre" name="nombre" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
                <div>
                    <label for="apellido1" class="block text-sm font-medium text-gray-700">Primer Apellido</label>
                    <input type="text" id="apellido1" name="apellido1" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
                <div>
                    <label for="apellido2" class="block text-sm font-medium text-gray-700">Segundo Apellido</label>
                    <input type="text" id="apellido2" name="apellido2" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
                <div>
                    <label for="fechaNac" class="block text-sm font-medium text-gray-700">Fecha de Nacimiento</label>
                    <input type="date" id="fechaNac" name="fechaNac" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
                <div>
                    <label for="genero" class="block text-sm font-medium text-gray-700">Género</label>
                    <select id="genero" name="genero" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio">
                        <option value="1">Masculino</option>
                        <option value="2">Femenino</option>
                        <option value="3">Otro</option>
                        <option value="0">Prefiero no decirlo</option>
                    </select>
                </div>
                <div>
                    <label for="telefono" class="block text-sm font-medium text-gray-700">Teléfono</label>
                    <input type="tel" id="telefono" name="telefono" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
            </div>
        </div>

        <div class="bg-white border border-gray-200 rounded-2xl shadow-sm p-6">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-x-6 gap-y-5">
                <div class="md:col-span-2 text-lg font-semibold text-gray-800 border-b border-gray-200 pb-2 mb-2">Dirección de Envío</div>
                <div class="md:col-span-2">
                    <label for="calle" class="block text-sm font-medium text-gray-700">Calle y número</label>
                    <div class="flex gap-4 mt-1">
                        <input type="text" id="calle" name="calle" placeholder="Nombre de la calle" class="flex-grow w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        <input type="text" id="numero" name="numero" placeholder="Nº" class="w-24 px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                </div>
                
                <div>
                    <label for="piso" class="block text-sm font-medium text-gray-700">Piso</label>
                    <input type="text" id="piso" name="piso" placeholder="Ej: 3º" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
                <div>
                    <label for="puerta" class="block text-sm font-medium text-gray-700">Puerta</label>
                    <input type="text" id="puerta" name="puerta" placeholder="Ej: Izda." class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
                <div>
                    <label for="localidad" class="block text-sm font-medium text-gray-700">Localidad</label>
                    <input type="text" id="localidad" name="localidad" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
                <div>
                    <label for="codigoPostal" class="block text-sm font-medium text-gray-700">Código Postal</label>
                    <input type="text" id="codigoPostal" name="codigoPostal" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
                 <div>
                    <label for="provincia" class="block text-sm font-medium text-gray-700">Provincia</label>
                    <input type="text" id="provincia" name="provincia" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
                 <div>
                    <label for="pais" class="block text-sm font-medium text-gray-700">País</label>
                    <input type="text" id="pais" name="pais" class="mt-1 w-full px-4 py-2.5 border border-gray-300 rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                </div>
            </div>
        </div>

        <div class="bg-white border border-gray-200 rounded-2xl shadow-sm p-6">

            <div class="flex flex-col md:flex-row gap-8">

                <div class="w-full md:w-48 flex-shrink-0">
                    <span class="block text-lg font-semibold text-gray-800 mb-4 text-center">Tus puntos</span>
                    <div class="flex flex-col items-center justify-center gap-2 bg-pistachio/10 border border-pistachio/50 text-pistachio p-4 rounded-xl shadow-sm h-40">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-auto w-10" viewBox="0 0 20 20" fill="currentColor">
                            <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                        </svg>
                        <span id="user-points" class="text-4xl font-bold">0</span>
                    </div>
                </div>

                <div class="flex-grow">
                    <span class="block text-lg font-semibold text-gray-800 mb-4 text-center">Tus intereses</span>
                    <div class="flex flex-wrap gap-3 justify-center">
                        <c:forEach var="pref" items="<%= PreferenciaEnum.all() %>">
                            <div data-id="${pref.id}" class="select-interes cursor-pointer rounded-3xl px-5 py-2.5 border border-gray-300 text-sm font-medium whitespace-nowrap transition-colors duration-200 ease-in-out hover:border-pistachio">
                                ${pref.label}
                            </div>
                        </c:forEach>
                    </div>
                    <input type="hidden" name="preferencias" id="preferenciasInput">
                </div>

            </div>
        </div>
        
        <div class="text-center pt-4">
            <button type="submit" class="bg-pistachio text-white font-bold px-8 py-3 rounded-lg hover:bg-dark-pistachio transition-colors duration-300 shadow-lg hover:shadow-xl focus:outline-none focus:ring-4 focus:ring-pistachio/50">
                Guardar Cambios
            </button>
        </div>
    </form>
</div>