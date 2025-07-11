<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar sesión</title>
    
    <!-- Tailwind CSS -->
    <link rel="stylesheet"  href="/css/output.css">   
    <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
 
</head>

<script>
    window.contextPath = '<%= request.getContextPath() %>';
    
    const userCartState = JSON.parse('${not empty userCartJson ? userCartJson : "{}"}');
    const userWishlistState = JSON.parse('${not empty userWishlistJson ? userWishlistJson : "[]"}');  
    
    const laboratoriosAgrupados = JSON.parse('${not empty laboratoriosAgrupadosJson ? laboratoriosAgrupadosJson : "{}"}');
</script>

<script src="${pageContext.request.contextPath}/js/main/login.js" defer></script>

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

    <div class="flex-grow flex items-center justify-center">
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
                    class="mt-1 w-full px-4 py-2 border border-gray-300 rounded-lg shadow-sm focus:ring-2 focus:ring-dark-pistachio focus:border-blue-500"
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

                <button onclick="login(event)" class="w-full bg-pistachio text-white py-2 px-4 rounded-lg hover:bg-dark-pistachio transition duration-300 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2">
                    Iniciar Sesión
                </button>
            </form>

            <p class="text-center text-sm text-gray-600 mt-4">
                ¿No tienes una cuenta?
                <a href="/register" class="text-blue-600 hover:underline">Regístrate aquí</a>
            </p>
        </div>
    </div>

</body>
</html>
