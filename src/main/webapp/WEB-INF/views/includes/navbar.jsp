<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav class="bg-white shadow-2xl sticky top-0 w-full z-50">
    <!-- Contenedor principal del navbar -->
    <div class="container mx-auto px-4 py-4 h-full">
        <!-- Primera fila: Logo, Barra de búsqueda, Iconos -->
        <div class="flex justify-between items-center">
            <!-- Logo a la izquierda -->
            <div class="flex-1 flex justify-start items-center">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo" class="h-14 mr-2">
                <a href="#" class="text-3xl font-bold" style="font-family: 'Satisfy', cursive; color: #86207e;">
                    La Botica de Mar
                </a>
            </div>


            <!-- Barra de búsqueda con icono en el centro -->
            <div class="flex-1 flex justify-center">
                <form action="/home/search" method="get" class="relative w-full max-w-lg">
                    <!-- Input de búsqueda -->
                    <input type="text" name="query" placeholder="Buscar productos..." 
                        class="w-full py-2 px-4 rounded-lg border border-gray-300 focus:outline-none focus:border-blue-500 pl-10">
                    
                    <!-- Icono de lupa usando tu SVG -->
                    <img src="${pageContext.request.contextPath}/images/search.svg" 
                        class="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-500" 
                        style="fill: currentColor;" 
                        alt="Buscar">  
                </form>
            </div>

            <!-- Iconos a la derecha -->
            <div class="flex-1 flex justify-end space-x-4 items-center">
                <img src="${pageContext.request.contextPath}/images/heart.svg" class="h-6 cursor-pointer" alt="Artículos que me gustan"
                    onclick="window.location.href='/home/wishlist'">
                <img src="${pageContext.request.contextPath}/images/shopping-cart.svg" class="h-6 cursor-pointer" alt="Mi carrito"
                    onclick="window.location.href='/home/cart'">
                <img src="${pageContext.request.contextPath}/images/user-circle.svg" class="h-6 cursor-pointer" alt="Mi perfil"
                    onclick="window.location.href='/home/profile'">
            </div>
        </div>

        <!-- Segunda fila: Enlaces de navegación -->
        <div class="flex justify-start mt-3">
            <a href="/home/welcome.jsp" class="text-gray-800 font-bold hover:text-gray-600 px-3">Categorias</a>
            <a href="${pageContext.request.contextPath}/productos.jsp" class="text-gray-800 font-bold hover:text-gray-600 px-3">Marcas</a>
            <a href="${pageContext.request.contextPath}/productos.jsp" class="text-red-600 font-bold hover:text-red-400 px-3">Ofertas</a>
            <a href="${pageContext.request.contextPath}/productos.jsp" class="text-gray-800 font-bold hover:text-gray-600 px-3">Packs Ahorro</a>
            <a href="${pageContext.request.contextPath}/productos.jsp" class="text-gray-800 font-bold hover:text-gray-600 px-3">Ortopedia</a>
            <a href="${pageContext.request.contextPath}/productos.jsp" class="text-gray-800 font-bold hover:text-gray-600 px-3">SPD</a>
        </div>
    </div>
</nav>
