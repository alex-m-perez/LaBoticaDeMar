<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "La Botica de Mar";
%>
<!DOCTYPE html>
<html lang="es">

    <%@ include file="../includes/head.jsp" %>
    
    <div class="flex-grow">
        <!-- Incluir el navbar -->
        <%@ include file="../includes/navbar.jsp" %>

        <div class="container mx-auto mt-10 flex justify-center items-center">
            <div class="container w-2/5 mx-5 mt-10 justify-start">
                <h2 class="text-2xl font-bold w-auto text-gray-800">Datos personales</h2>
                <div class="w-auto bg-white p-8 my-2 rounded-lg shadow-md">
                    <form action="updateUser" method="post">
                        <div class="mb-4 flex items-center">
                            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                                Apellidos:
                            </label>
                            <input type="text" id="name" name="name" value="${user.name}" 
                                class="shadow appearance-none border rounded w-full mx-4 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-4 flex items-center">
                            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                                DNI/NIF:
                            </label>
                            <input type="text" id="name" name="name" value="${user.name}" 
                                class="shadow appearance-none border rounded w-full mx-4 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-4 flex items-center">
                            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                                Edad:
                            </label>
                            <input type="text" id="name" name="name" value="${user.name}" 
                                class="shadow appearance-none border rounded w-full mx-4 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-4 flex items-center">
                            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                                Dirección:
                            </label>
                            <input type="text" id="name" name="name" value="${user.name}" 
                                class="shadow appearance-none border rounded w-full mx-4 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="flex items-center justify-start mt-8">
                            <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold mx-2 py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit">
                                Editar
                            </button>
                            <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold mx-2 py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit">
                                Guardar cambios
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <div class="container w-2/5 mx-5 mt-10 justify-start">
                <h2 class="text-2xl font-bold w-auto text-gray-800">Datos personales</h2>
                <div class="w-auto bg-white p-8 rounded-lg shadow-md">
                    <form action="updateUser" method="post">
                        <div class="mb-4 flex items-center">
                            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                                Apellidos:
                            </label>
                            <input type="text" id="name" name="name" value="${user.name}" 
                                class="shadow appearance-none border rounded w-full mx-4 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-4 flex items-center">
                            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                                DNI/NIF:
                            </label>
                            <input type="text" id="name" name="name" value="${user.name}" 
                                class="shadow appearance-none border rounded w-full mx-4 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-4 flex items-center">
                            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                                Edad:
                            </label>
                            <input type="text" id="name" name="name" value="${user.name}" 
                                class="shadow appearance-none border rounded w-full mx-4 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="mb-4 flex items-center">
                            <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                                Dirección:
                            </label>
                            <input type="text" id="name" name="name" value="${user.name}" 
                                class="shadow appearance-none border rounded w-full mx-4 py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                        </div>

                        <div class="flex items-center justify-start mt-8">
                            <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold mx-2 py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit">
                                Editar
                            </button>
                            <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold mx-2 py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit">
                                Guardar cambios
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>


        <div class="container mx-auto mt-10 flex justify-center items-center">
            <div class="mx-5 w-2/5 bg-white p-8 rounded-lg shadow-md">
                <h2 class="text-2xl font-bold mb-6 text-gray-800">Compras</h2>

                <form action="updateUser" method="post">
                    <!-- Nombre -->
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                            Nombre
                        </label>
                        <input type="text" id="name" name="name" value="${user.name}" 
                            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <!-- Correo Electrónico -->
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
                            Correo Electrónico
                        </label>
                        <input type="email" id="email" name="email" value="${user.email}"
                            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <!-- Teléfono -->
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="phone">
                            Teléfono
                        </label>
                        <input type="tel" id="phone" name="phone" value="${user.phone}"
                            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <!-- Botón para guardar -->
                    <div class="flex items-center justify-between">
                        <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit">
                            Guardar Cambios
                        </button>
                    </div>
                </form>
            </div>

            <div class="mx-5 w-2/5 bg-white p-8 rounded-lg shadow-md">
                <h2 class="text-2xl font-bold mb-6 text-gray-800">Otros</h2>

                <form action="updateUser" method="post">
                    <!-- Nombre -->
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="name">
                            Nombre
                        </label>
                        <input type="text" id="name" name="name" value="${user.name}" 
                            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <!-- Correo Electrónico -->
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
                            Correo Electrónico
                        </label>
                        <input type="email" id="email" name="email" value="${user.email}"
                            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <!-- Teléfono -->
                    <div class="mb-4">
                        <label class="block text-gray-700 text-sm font-bold mb-2" for="phone">
                            Teléfono
                        </label>
                        <input type="tel" id="phone" name="phone" value="${user.phone}"
                            class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline">
                    </div>

                    <!-- Botón para guardar -->
                    <div class="flex items-center justify-between">
                        <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit">
                            Guardar Cambios
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Incluir el footer -->
    <%@ include file="../includes/footer.jsp" %>

</html>
