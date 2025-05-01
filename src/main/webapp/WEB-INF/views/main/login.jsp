<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión</title>
    
    <!-- Tailwind CSS -->
    <link rel="stylesheet"  href="/css/output.css">   
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
 
</head>

<script>
    async function login(event) {
        event.preventDefault();

        const miForm = document.querySelector('form');
        const formData = new FormData(miForm);

        const formObject = {};
        formData.forEach((value, key) => {
            formObject[key] = value;
        });

        try {
            const response = await axios.post('/auth/authenticate', formObject, {
                headers: {
                    'Content-Type': 'application/json',
                },
            });

            console.log('Autenticación exitosa:', response.data);
            window.location.href = "/";
        } catch (error) {
            console.error('Error:', error.response ? error.response.data : error.message);
            alert('Dirección de correo o contraseña incorrectos.');
        }
    }
</script>


<body class="bg-gray-100">

  <div class="min-h-screen flex items-center justify-center">
    <div class="bg-white p-6 rounded-lg shadow-md w-full max-w-md">
        <div class="text-center mb-6">
            <h2 class="text-2xl font-bold text-gray-800">Iniciar Sesión</h2>
            <p class="text-gray-600">Bienvenido de nuevo, por favor ingrese sus datos.</p>
        </div>

        <form class="space-y-4">
        <div>
            <label for="email" class="block text-sm font-medium text-gray-700">Correo Electrónico</label>
            <input
            type="email"
            id="email"
            name="correo"
            required
            placeholder="ejemplo@correo.com"
            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
        </div>

        <div>
            <label for="password" class="block text-sm font-medium text-gray-700">Contraseña</label>
            <input
            type="password"
            id="password"
            name="password"
            required
            placeholder="••••••••"
            class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            >
        </div>

        <div class="flex items-center justify-between">
            <label class="inline-flex items-center">
            <input type="checkbox" class="rounded text-blue-600 focus:ring-blue-500">
            <span class="ml-2 text-sm text-gray-600">Recordar sesión</span>
            </label>
            <a href="/auth/forgot-password" class="text-sm text-blue-600 hover:underline">¿Olvidaste tu contraseña?</a>
        </div>

        <button onclick="login(event)" class="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition duration-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
            Iniciar Sesión
        </button>
        </form>

        <p class="text-center text-sm text-gray-600 mt-4">
            ¿No tienes una cuenta?
            <a href="/auth/register" class="text-blue-600 hover:underline">Regístrate aquí</a>
        </p>
    </div>
  </div>

</body>
</html>
