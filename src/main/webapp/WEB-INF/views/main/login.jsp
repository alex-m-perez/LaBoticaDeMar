<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión</title>
    <link rel="stylesheet"  href="/css/output.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>

<body>
<script>
function login(event) {
    event.preventDefault(); // Prevenir la recarga de la página

    var miForm = document.querySelector('form');
    const formData = new FormData(miForm);

    // Convertir FormData a un objeto JSON
    const formObject = {};
    formData.forEach((value, key) => {
        formObject[key] = value;
    });

    $.ajax({
        url: '/auth/authenticate',
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(formObject),
        success: function(response, textStatus, jqXHR) {
            window.location.href = "/welcome";
        },
        error: function(jqXHR, textStatus, errorThrown) {
            console.log(errorThrown);
            alert('Dirección de correo o contraseña incorrectos.');
        }
    });
}

</script>


<header class="bg-pink-600 text-white p-4 h-16">
    <div class="container mx-auto flex items-center">
        <!-- Imagen del logo, ajustada al tamaño del header -->
        <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo" class="h-10 mr-4">
        
        <!-- Texto del encabezado -->
        <h1 class="text-2xl font-bold">La Botica de Mar</h1>
    </div>
</header>



<main class="min-h-screen bg-gray-100 flex flex-col justify-center items-center">
    <div class="bg-white p-8 shadow-lg rounded-lg w-full max-w-md">
        <h1 class="text-3xl font-semibold mb-6 text-center text-blue-600">Iniciar sesión</h1>
        <form id="user_pwd" class="space-y-6">
            <div>
                <label for="correo" class="block text-sm font-medium text-gray-700">Correo electrónico</label>
                <input type="email" id="correo" name="correo" required
                    class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:ring-blue-500 focus:border-blue-500">
            </div>

            <div>
                <label for="password" class="block text-sm font-medium text-gray-700">Contraseña</label>
                <input type="password" id="password" name="password" required
                    class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:ring-blue-500 focus:border-blue-500">
            </div>


            <div class="flex justify-between items-center">
                <button type="submit" onclick="login(event)"
                    class="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-500 focus:outline-none focus:ring-2 focus:ring-blue-400 focus:ring-opacity-75">Iniciar sesión</button>
                <a href="recuperar-contrasena.jsp" class="text-blue-600 hover:underline text-sm">¿Olvidaste tu contraseña?</a>
            </div>
        </form>

        <div class="mt-6 text-center">
            <p class="text-sm text-gray-600">¿No tienes una cuenta? <a href="registro.jsp" class="text-blue-600 hover:underline">Crear una nueva cuenta</a></p>
        </div>
    </div>
</main>

<footer class="bg-gray-800 text-white py-4 mt-8">
    <div class="container mx-auto text-center">
        <p>Contacto: [Información de contacto]</p>
        <p><a href="#" class="hover:underline">Redes sociales</a></p>
        <p><a href="#" class="hover:underline">Aviso legal</a></p>
    </div>
</footer>


</body>
</html>
