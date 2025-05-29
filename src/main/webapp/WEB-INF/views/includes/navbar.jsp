<script src="${pageContext.request.contextPath}/js/navbar/navbar.js" defer></script>

<nav id="navbar" class="bg-white shadow-md sticky top-0 w-full z-50 transition-all duration-300 ease-in-out">
    <div class="container mx-auto p-4 h-full">
        <div class="flex justify-between items-center">
            <div class="flex-1 flex justify-start items-center">
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo" class="h-14 mr-2 ">
                <a href="/" class="text-3xl font-bold hidden lg:block" style="font-family: 'Satisfy', cursive; color: #86207e;">
                    La Botica de Mar
                </a>
            </div>

            <div class="flex-1 flex justify-center">
                <form action="/search" method="get" class="relative w-full max-w-lg">
                    <input type="text" name="query" placeholder="Buscar productos..." 
                        class="w-full py-2 px-4 rounded-lg border border-gray-300 focus:outline-none focus:border-blue-500 pl-10">
                    
                    <img src="${pageContext.request.contextPath}/images/search.svg" 
                        class="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-500" 
                        style="fill: currentColor;" 
                        alt="Buscar">  
                </form>
            </div>

            <div class="flex-1 flex justify-end space-x-4 items-center">
                <sec:authorize access="isAuthenticated()">
                    <p>Bienvenido, ${usuario.nombre}</p>
                </sec:authorize>
                <sec:authorize access="!isAuthenticated()">
                    <p>Iniciar Sesión</p>
                </sec:authorize>
                <img src="${pageContext.request.contextPath}/images/user-circle.svg" class="h-6 cursor-pointer" alt="Mi perfil"
                    onclick="window.location.href='/profile'">
                <img src="${pageContext.request.contextPath}/images/heart.svg" class="h-6 cursor-pointer" alt="Artículos que me gustan"
                    onclick="window.location.href='/wishlist'">
                <img src="${pageContext.request.contextPath}/images/shopping-cart.svg" class="h-6 cursor-pointer" alt="Mi carrito"
                    onclick="window.location.href='/cart'">
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <img src="${pageContext.request.contextPath}/images/settings.svg" class="h-6 cursor-pointer" alt="Administracion"
                        onclick="window.location.href='/admin/home'">
                </sec:authorize>
                
            </div>
        </div>

        <div class="w-full overflow-x-auto">
            <div class="flex w-max space-x-4 px-4 py-2">
                <button id="toggleCategoriesBtn" class="text-xl navbar-item text-gray-500 font-bold hover:text-gray-800 flex items-center whitespace-nowrap">
                    Categorias
                    <img id="categories_arrowDown" src="${pageContext.request.contextPath}/images/arrow-down.svg" class="ml-1 mt-1 h-4" alt="Flecha">
                    <img id="categories_arrowUp" src="${pageContext.request.contextPath}/images/arrow-up.svg" class="ml-1 mt-1 h-4 hidden" alt="Flecha">
                </button>
                <button id="toggleBrandsBtn" class="text-xl navbar-item text-gray-500 font-bold hover:text-gray-800 flex items-center whitespace-nowrap">
                    Marcas
                    <img id="brands_arrowDown" src="${pageContext.request.contextPath}/images/arrow-down.svg" class="ml-1 mt-1 h-4" alt="Flecha">
                    <img id="brands_arrowUp" src="${pageContext.request.contextPath}/images/arrow-up.svg" class="ml-1 mt-1 h-4 hidden" alt="Flecha">
                </button>
                <a href="${pageContext.request.contextPath}/" class="text-xl navbar-item text-gray-500 font-bold hover:text-gray-800 whitespace-nowrap">Packs Ahorro</a>
                <a href="${pageContext.request.contextPath}/" class="text-xl navbar-item text-red-500 font-bold hover:text-red-800 whitespace-nowrap">Ofertas</a>
            </div>
        </div>

        <div id="categoriesList" class="hidden mt-4 border-t flex justify-start border-gray-200 pt-4">
            <ul class="grid grid-cols-1 mx-6 gap-1 text-gray-600">
                <li><a href="#" class="font-bold hover:text-gray-500">Piel</a></li>
                <li><a href="#" class="hover:text-gray-400">Vitaminas y Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-400">Medicamentos</a></li>
            </ul>
            <ul class="grid grid-cols-1 mx-6 gap-1 text-gray-600">
                <li><a href="#" class="font-bold hover:text-gray-500">Suplementacion</a></li>
                <li><a href="#" class="hover:text-gray-400">Vitaminas y Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-400">Medicamentos</a></li>
            </ul>
            <ul class="grid grid-cols-1 mx-6 gap-1 text-gray-600">
                <li><a href="#" class="font-bold hover:text-gray-500">Mamas y bebes</a></li>
                <li><a href="#" class="hover:text-gray-400">Vitaminas y Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-400">Medicamentos</a></li>
            </ul>
            <ul class="grid grid-cols-1 mx-6 gap-1 text-gray-600">
                <li><a href="#" class="font-bold hover:text-gray-500">Medicamentos</a></li>
                <li><a href="#" class="hover:text-gray-400">Vitaminas y Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-400">Medicamentos</a></li>
            </ul>
        </div>

        <div id="brandsList" class="hidden mt-4 border-t flex-col justify-start border-gray-200 pt-4">
            <div id="alphabetButtons" class="flex justify-center mx-6 mb-4 space-x-2"></div>
        </div>

        <!-- Contenedor de la lista de categorías (oculto completamente inicialmente) -->
        <div id="categoriesList" class="hidden mt-4 border-t flex justify-start border-gray-200 pt-4">
            <ul class="grid grid-cols-1 mx-6 gap-1 text-gray-600">
                <li><a href="#" class="font-bold hover:text-gray-500">Piel</a></li>
                <li><a href="#" class="hover:text-gray-400">Vitaminas y Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-400">Medicamentos</a></li>
            </ul>
            <ul class="grid grid-cols-1 mx-6 gap-1 text-gray-600">
                <li><a href="#" class="font-bold hover:text-gray-500">Suplementacion</a></li>
                <li><a href="#" class="hover:text-gray-400">Vitaminas y Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-400">Medicamentos</a></li>
            </ul>
            <ul class="grid grid-cols-1 mx-6 gap-1 text-gray-600">
                <li><a href="#" class="font-bold hover:text-gray-500">Mamas y bebes</a></li>
                <li><a href="#" class="hover:text-gray-400">Vitaminas y Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-400">Medicamentos</a></li>
            </ul>
            <ul class="grid grid-cols-1 mx-6 gap-1 text-gray-600">
                <li><a href="#" class="font-bold hover:text-gray-500">Medicamentos</a></li>
                <li><a href="#" class="hover:text-gray-400">Vitaminas y Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-400">Medicamentos</a></li>
            </ul>
        </div>

        <div id="brandsList" class="hidden mt-4 border-t flex-col justify-start border-gray-200 pt-4">
            <!-- Contenedor para los botones del abecedario -->
            <div id="alphabetButtons" class="flex justify-center mx-6 mb-4 space-x-2"></div>

            <!-- Lista de marcas -->
            <ul class="flex flex-wrap justify-between mx-6 w-auto text-gray-600">
                <li><a href="#" class="hover:text-gray-600">Vitaminas</a></li>
                <li><a href="#" class="hover:text-gray-600">Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-600">Cuidado de la piel</a></li>
                <li><a href="#" class="hover:text-gray-600">Medicamentos</a></li>
                <li><a href="#" class="hover:text-gray-600">Vitaminas y Suplementos</a></li>
                <li><a href="#" class="hover:text-gray-600">Medicamentos</a></li>
            </ul>
        </div>

    </div>
</nav>



