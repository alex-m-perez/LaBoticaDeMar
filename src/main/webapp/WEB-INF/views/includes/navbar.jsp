<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script>
    window.contextPath = '<%= request.getContextPath() %>';
    
    const userCartState = JSON.parse('${not empty userCartJson ? userCartJson : "{}"}');
    const userWishlistState = JSON.parse('${not empty userWishlistJson ? userWishlistJson : "[]"}');  
    
    const laboratoriosAgrupados = JSON.parse('${not empty laboratoriosAgrupadosJson ? laboratoriosAgrupadosJson : "{}"}');
</script>

<script src="${pageContext.request.contextPath}/js/navbar/navbar.js" defer></script>

<nav id="navbar" class="bg-white shadow-md sticky top-0 w-full z-50 transition-all duration-300 ease-in-out">
    <div class="container mx-auto p-4 h-full">
        <div class="flex justify-between items-center">
            <div class="flex-1 flex justify-start items-center">
                <a href="${pageContext.request.contextPath}/">
                    <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo" class="h-14 mr-2 cursor-pointer">
                </a>
                <a href="/" class="text-3xl font-bold hidden lg:block" style="font-family: 'Satisfy', cursive; color: #86207e;">
                    La Botica de Mar
                </a>
            </div>

            <div class="flex-1 flex justify-center px-4">
                <form action="/search" method="get" class="relative w-full max-w-lg">
                    <input
                        type="text"
                        id="navbarSearchInput"
                        name="query"
                        placeholder="Buscar productos..."
                        class="w-full py-2 px-4 rounded-lg border border-gray-300 focus:outline-none focus:border-blue-500 pl-10"
                        autocomplete="off"
                    />
                    <img
                        src="${pageContext.request.contextPath}/images/search.svg"
                        class="absolute left-3 top-1/2 transform -translate-y-1/2 h-5 w-5 text-gray-500"
                        style="fill: currentColor;"
                        alt="Buscar"
                    />
                    <ul
                        id="navbarSuggestions"
                        class="
                            absolute
                            top-full left-0
                            w-full
                            bg-white border border-gray-300 rounded-md shadow mt-1 max-h-60 overflow-auto
                            hidden z-40
                        "
                    ></ul>
                </form>
            </div>

            <div class="flex-1 flex justify-end space-x-4 items-center">
                <sec:authorize access="isAuthenticated()">
                    <p class="hidden lg:block whitespace-nowrap">Bienvenido, ${currentUserName}</p>
                    <div id="profileMenuContainer" class="relative flex-none inline-flex items-center">
                        <img
                            id="profileIcon"
                            src="${pageContext.request.contextPath}/images/user-circle.svg"
                            class="h-6 w-6 flex-shrink-0 cursor-pointer"
                            alt="Mi perfil"
                        />
                        <div id="profileMenu" class="hidden absolute top-full left-1/2 transform -translate-x-1/2 mt-1 w-48 bg-white shadow-lg rounded-md z-50">
                            <ul class="py-2">
                                <li><a href="${pageContext.request.contextPath}/profile/datos_personales"  class="block px-4 py-2 hover:bg-gray-100">Mi perfil</a></li>
                                <li><a href="${pageContext.request.contextPath}/profile/mis_compras"   class="block px-4 py-2 hover:bg-gray-100">Mis pedidos</a></li>
                                <li><a href="${pageContext.request.contextPath}/profile/mis_devoluciones"  class="block px-4 py-2 hover:bg-gray-100">Mis devoluciones</a></li>
                                <li><hr class="my-2 border-gray-200"/></li>
                                <li><a href="${pageContext.request.contextPath}/auth/logout"  class="block px-4 py-2 text-red-600 hover:bg-gray-100">Cerrar sesión</a></li>
                            </ul>
                        </div>
                    </div>
                </sec:authorize>

                <sec:authorize access="!isAuthenticated()">
                    <div class="hidden lg:block whitespace-nowrap">
                        <a
                            href="${pageContext.request.contextPath}/login"
                            class="text-gray-800 hover:text-gray-600"
                        >Iniciar Sesión</a>
                        <span class="mx-1 text-gray-500">|</span>
                        <a
                            href="${pageContext.request.contextPath}/register"
                            class="text-gray-800 hover:text-gray-600"
                        >Registrarse</a>
                    </div>
                </sec:authorize>

                <img
                    src="${pageContext.request.contextPath}/images/heart.svg"
                    class="h-6 w-6 flex-shrink-0 cursor-pointer"
                    alt="Artículos que me gustan"
                    onclick="window.location.href='/wishlist'"
                />
                
                <div class="relative flex-shrink-0">
                    <img
                        src="${pageContext.request.contextPath}/images/shopping-cart.svg"
                        class="h-6 w-6 flex-shrink-0 cursor-pointer"
                        alt="Mi carrito"
                        onclick="window.location.href='/cart'"
                    />
                    <span id="cart-item-count" onclick="window.location.href='/cart'"
                          class="absolute -top-2 -right-2 bg-light-pistachio text-gray-800 text-xs font-semibold rounded-full h-5 w-5 flex items-center justify-center hidden cursor-pointer">
                    </span>
                </div>

                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <img
                        src="${pageContext.request.contextPath}/images/settings.svg"
                        class="h-6 w-6 flex-shrink-0 cursor-pointer"
                        alt="Administracion"
                        onclick="window.location.href='/admin/home'"
                    />
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
                <a href="${pageContext.request.contextPath}/" class="text-xl navbar-item text-red-500 font-bold hover:text-red-600 whitespace-nowrap">Ofertas</a>
            </div>
        </div>

        <div id="categoriesList"
        class="flex justify-start overflow-hidden transition-[max-height,opacity] duration-500 ease-in-out max-h-0 opacity-0 menu-with-line">
            <ul id="familiesList" class="w-1/4 list-none space-y-2 border-r border-gray-200 pr-4">
                <c:forEach var="entry" items="${familiaCategorias}">
                    <li class="family-item cursor-pointer p-2 rounded hover:bg-gray-100" data-familia-id="${entry.key.id}">
                        ${entry.key.nombre}
                        
                        <ul class="hidden">
                            <li>
                                <a href="product?familia=${entry.key.id}"
                                    class="block py-1 hover:text-gray-700">
                                    Ver todo
                                </a>
                            </li>
                            <c:forEach var="catMap" items="${entry.value}">
                                <c:forEach var="catEntry" items="${catMap}">
                                    <li>
                                        <a href="/product?familia=${entry.key.id}&categoria=${catEntry.key.id}" class="block py-1 hover:text-gray-700">
                                            ${catEntry.key.nombre}
                                        </a>
                                    </li>
                                </c:forEach>
                            </c:forEach>
                        </ul>
                    </li>
                </c:forEach>
            </ul>
            <ul id="familyCategoriesList" class="w-3/4 list-none grid grid-flow-col grid-rows-6 gap-x-2 gap-y-1 pl-4"></ul>
        </div>

        <div id="brandsList"
        class="flex flex-col justify-start overflow-hidden transition-[max-height,opacity] duration-500 ease-in-out max-h-0 opacity-0 menu-with-line">
            <div id="alphabetButtons" class="flex justify-center flex-wrap mx-6 mb-1 gap-2"></div>
            <div id="brandsByLetterContainer"
                 class="w-full mt-1 p-4 border-t border-gray-100 flex justify-center flex-wrap gap-x-8 gap-y-2">
            </div>
        </div>
    </div>
</nav>