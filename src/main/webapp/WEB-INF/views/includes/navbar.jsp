<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<nav id="navbar" class="bg-white shadow-l sticky top-0 w-full z-50 transition-all duration-300 ease-in-out">
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
                <form action="/search" method="get" class="relative w-full max-w-lg">
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
                    onclick="window.location.href='/wishlist'">
                <img src="${pageContext.request.contextPath}/images/shopping-cart.svg" class="h-6 cursor-pointer" alt="Mi carrito"
                    onclick="window.location.href='/cart'">
                <img src="${pageContext.request.contextPath}/images/user-circle.svg" class="h-6 cursor-pointer" alt="Mi perfil"
                    onclick="window.location.href='/profile'">
            </div>
        </div>

        <!-- Segunda fila: Enlaces de navegación -->
        <div class="flex justify-start mt-3">
            <!-- Enlace Categorías con funcionalidad para abrir el menú lateral -->
            <button id="toggleCategoriesBtn" class="text-gray-800 font-bold hover:text-gray-600 px-3 flex items-center space-x-0.5 focus:outline-none">
                <span>Categorias</span>
                <img id="categories_arrowDown" src="${pageContext.request.contextPath}/images/arrow-down.svg" class="mt-1 h-4 cursor-pointer" alt="Flecha hacia abajo">
                <img id="categories_arrowUp" src="${pageContext.request.contextPath}/images/arrow-up.svg" class="mt-1 h-4 cursor-pointer hidden" alt="Flecha hacia arriba">
            </button>
            <button id="toggleBrandsBtn" class="text-gray-800 font-bold hover:text-gray-600 px-3 flex items-center space-x-0.5 focus:outline-none">
                <span>Marcas</span>
                <img id="brands_arrowDown" src="${pageContext.request.contextPath}/images/arrow-down.svg" class="mt-1 h-4 cursor-pointer" alt="Flecha hacia abajo">
                <img id="brands_arrowUp" src="${pageContext.request.contextPath}/images/arrow-up.svg" class="mt-1 h-4 cursor-pointer hidden" alt="Flecha hacia arriba">
            </button>
            <button class="text-red-600 font-bold hover:text-red-400 px-3" onclick="window.location.href='/product/profile'">Ofertas</button>
            <a href="${pageContext.request.contextPath}/productos.jsp" class="text-gray-800 font-bold hover:text-gray-600 px-3">Packs Ahorro</a>
            <a href="${pageContext.request.contextPath}/productos.jsp" class="text-gray-800 font-bold hover:text-gray-600 px-3">SPD</a>
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

        <script>
            // Generar los botones del abecedario dinámicamente
            const alphabetContainer = document.getElementById('alphabetButtons');
            const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');
            
            alphabet.forEach(letter => {
                const button = document.createElement('button');
                button.textContent = letter;
                button.classList.add('px-3', 'py-1', 'bg-gray-200', 'hover:bg-gray-300', 'text-gray-800', 'font-bold', 'rounded');
                alphabetContainer.appendChild(button);
            });
        </script>

    </div>
</nav>

<script>
    const toggleCategoriesBtn = document.getElementById('toggleCategoriesBtn');
    const toggleBrandsBtn = document.getElementById('toggleBrandsBtn');
    
    const categoriesList = document.getElementById('categoriesList');
    const brandsList = document.getElementById('brandsList');
    
    const categoriesArrowDown = document.getElementById('categories_arrowDown');
    const categoriesArrowUp = document.getElementById('categories_arrowUp');
    const brandsArrowDown = document.getElementById('brands_arrowDown');
    const brandsArrowUp = document.getElementById('brands_arrowUp');

    let isCategoriesVisible = false;
    let isBrandsVisible = false;

    function toggleMenu(menuToShow, menuToHide, arrowDownToShow, arrowUpToShow, arrowDownToHide, arrowUpToHide, visibilityFlagToShow, visibilityFlagToHide) {
        // Si el menú que se intenta mostrar está abierto, lo cerramos
        if (visibilityFlagToShow) {
            menuToShow.classList.add('hidden');
            menuToShow.classList.remove('block');
            arrowDownToShow.classList.remove('hidden');
            arrowUpToShow.classList.add('hidden');
            visibilityFlagToShow = false;
        } else {
            // Mostramos el menú deseado
            menuToShow.classList.remove('hidden');
            menuToShow.classList.add('block');
            arrowDownToShow.classList.add('hidden');
            arrowUpToShow.classList.remove('hidden');
            visibilityFlagToShow = true;

            // Ocultamos el otro menú si está visible
            if (visibilityFlagToHide) {
                menuToHide.classList.add('hidden');
                menuToHide.classList.remove('block');
                arrowDownToHide.classList.remove('hidden');
                arrowUpToHide.classList.add('hidden');
                visibilityFlagToHide = false;
            }
        }

        // Actualizamos las variables globales directamente
        isCategoriesVisible = (menuToShow === categoriesList) ? visibilityFlagToShow : visibilityFlagToHide;
        isBrandsVisible = (menuToShow === brandsList) ? visibilityFlagToShow : visibilityFlagToHide;
    }

    // Evento de clic en Categorías
    toggleCategoriesBtn.addEventListener('click', function() {
        toggleMenu(categoriesList, brandsList, categoriesArrowDown, categoriesArrowUp, brandsArrowDown, brandsArrowUp, isCategoriesVisible, isBrandsVisible);
    });

    // Evento de clic en Marcas
    toggleBrandsBtn.addEventListener('click', function() {
        toggleMenu(brandsList, categoriesList, brandsArrowDown, brandsArrowUp, categoriesArrowDown, categoriesArrowUp, isBrandsVisible, isCategoriesVisible);
    });
</script>



