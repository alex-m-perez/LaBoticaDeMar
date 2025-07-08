<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrarse</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icono_tab2.png" type="image/png">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/output.css">
    <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script>window.contextPath = '<%= request.getContextPath() %>';</script>
    <script src="${pageContext.request.contextPath}/js/auth/registroNuevoEmpleado.js" defer></script>
</head>

<%-- El body ya estaba correcto, se mantiene --%>
<body class="bg-gray-100 flex flex-col min-h-screen">

    <header class="sticky top-0 z-50">
        <nav class="bg-white shadow-md w-full transition-all duration-300 ease-in-out">
            <div class="container mx-auto p-4 flex items-center justify-between">
                <div class="flex items-center">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo" class="h-14 mr-2">
                    <a href="${pageContext.request.contextPath}/" class="text-3xl font-bold hidden lg:block font-[Satisfy] text-logo-purple">
                        La Botica de Mar
                    </a>
                </div>
                <div class="flex items-center space-x-1 text-gray-600">
                    <span class="text-sm">Conexión segura</span>
                    <img src="${pageContext.request.contextPath}/images/locker.svg" alt="Seguro" class="h-6">
                </div>
            </div>
        </nav>
    </header>

    <main class="flex-grow py-8 flex items-center justify-center">
        <div class="relative bg-white p-8 rounded-xl shadow-lg w-full max-w-2xl mx-4">
            <h3 id="step-title"
                class="text-3xl font-bold text-gray-800 text-center mb-4">
                Registro de nuevo empleado
            </h3>
            <div class="mb-6">
                <div class="flex justify-between items-center mb-1">
                    <span id="step-label" class="text-gray-700 font-medium">Paso 1 de 3</span>
                </div>
                <div class="w-full bg-gray-200 rounded-full h-2">
                    <div id="progress-bar" class="bg-pistachio h-2 rounded-full w-1/3 transition-width duration-300"></div>
                </div>
            </div>

            <form id="register-form"
                  action="${pageContext.request.contextPath}/auth/register"
                  method="post"
                  data-success-redirect="/auth/login"
                  class="space-y-8 pb-20">

                <div id="step-1">
                    <div class="space-y-6">
                        <div>
                            <label for="nombre" class="block text-sm font-medium text-gray-700">Nombre *</label>
                            <input name="nombre" id="nombre" required
                                   placeholder="Nombre"
                                   class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="apellido1" class="block text-sm font-medium text-gray-700">Apellido 1 *</label>
                            <input name="apellido1" id="apellido1" required
                                   placeholder="Apellido 1"
                                   class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="apellido2" class="block text-sm font-medium text-gray-700">Apellido 2</label>
                            <input name="apellido2" id="apellido2"
                                   placeholder="Apellido 2"
                                   class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="correo" class="block text-sm font-medium text-gray-700">Email *</label>
                            <input name="correo" id="correo" type="email" required
                                   placeholder="Su correo"
                                   class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="passwd" class="block text-sm font-medium text-gray-700">Contraseña *</label>
                            <div class="relative">
                                <input name="password" id="passwd" type="password" required
                                       placeholder="••••••••"
                                       class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio pr-12"/>
                                <button type="button" id="toggle-password"
                                        class="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-500">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none"
                                         viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M15 12a3 3 0 11-6 0 3 3 0 016 0z"/>
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                              d="M2.458 12C3.732 7.943 7.523 5 12 5c4.477 0 8.268 2.943 9.542 7-1.274 4.057-5.065 7-9.542 7-4.477 0-8.268-2.943-9.542-7z"/>
                                    </svg>
                                </button>
                            </div>
                        </div>
                        <div>
                            <label for="confirm_passwd" class="block text-sm font-medium text-gray-700">Confirmar contraseña *</label>
                            <div class="relative">
                                <input name="confirm_passwd" id="confirm_passwd" type="password" required
                                       placeholder="••••••••"
                                       class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio pr-12"/>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- STEP 2 -->
                <div id="step-2" class="hidden space-y-6">
                    <!-- Fecha de Nacimiento -->
                    <div>
                        <label for="fechaNac" class="block text-sm font-medium text-gray-700">Fecha de Nacimiento *</label>
                        <input name="fechaNac" id="fechaNac" type="date" required
                            class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Género -->
                    <div>
                        <label for="genero" class="block text-sm font-medium text-gray-700">Género *</label>
                        <select name="genero" id="genero" required
                                class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio">
                            <option value=""">Selecciona...</option>
                            <option value=1>Masculino</option>
                            <option value=2>Femenino</option>
                            <option value=0>Otro</option>
                        </select>
                    </div>
                    <!-- Teléfono -->
                    <div>
                        <label for="telefono" class="block text-sm font-medium text-gray-700">Teléfono *</label>
                        <input name="telefono" id="telefono" type="tel" required
                            placeholder="123456789"
                            class="mt-1 w-full px-4 py-3	border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Dirección -->
                    <div class="grid grid-cols-2 gap-6">
                        <div>
                            <label for="calle" class="block text-sm font-medium text-gray-700">Calle *</label>
                            <input name="calle" id="calle" required
                                placeholder="Calle"
                                class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="numero" class="block text-sm font-medium text-gray-700">Número *</label>
                            <input name="numero" id="numero" required
                                placeholder="Número"
                                class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="calle" class="block text-sm font-medium text-gray-700">Piso</label>
                            <input name="piso" id="piso"
                                placeholder="Piso"
                                class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="numero" class="block text-sm font-medium text-gray-700">Puerta</label>
                            <input name="puerta" id="puerta"
                                placeholder="Puerta"
                                class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="localidad" class="block text-sm font-medium text-gray-700">Localidad *</label>
                            <input type="text" id="localidad" name="localidad" required
                                placeholder="Localidad"
                                class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="codigoPostal" class="block text-sm font-medium text-gray-700">Código Postal *</label>
                            <input name="codigoPostal" id="codigoPostal" required
                                placeholder="Código Postal"
                                class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="provincia" class="block text-sm font-medium text-gray-700">Provincia *</label>
                            <input name="provincia" id="provincia" required
                                placeholder="Provincia"
                                class="mt-1 w-full px-4 py-3 border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                        <div>
                            <label for="pais" class="block text-sm font-medium text-gray-700">País *</label>
                            <input name="pais" id="pais" required
                                placeholder="País"
                                class="mt-1 w-full px-4 py-3	border rounded-lg focus:ring-2 focus:ring-pistachio focus:border-pistachio"/>
                        </div>
                    </div>
                </div>

                <div class="absolute bottom-0 left-0 w-full bg-white p-4 flex justify-between rounded-b-xl shadow-inner">
                    <button
                        type="button"
                        id="prev-btn"
                        class="px-6 py-3 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 disabled:opacity-50"
                        disabled
                    >
                        Atrás
                    </button>
                    <button
                        type="button"
                        id="next-btn"
                        class="px-6 py-3 bg-pistachio text-white rounded-lg hover:bg-dark-pistachio"
                    >
                        Siguiente
                    </button>
                </div>
            </form>
        </div>
    </main>

    <footer class="bg-gray-800 text-white py-4">
        <div class="container mx-auto text-center">
            <%@ include file="/WEB-INF/views/includes/footer.jsp" %>
        </div>
    </footer>

</body>
</html>