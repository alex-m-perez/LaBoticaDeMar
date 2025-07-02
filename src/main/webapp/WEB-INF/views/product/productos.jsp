<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Productos</title>
        <link rel="icon" href="/images/icono_tab2.png" type="image/png">

        <link rel="stylesheet" href="/css/output.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>

        <script>
            const urlParams = new URLSearchParams(window.location.search);
            const jerarquiaFiltros = JSON.parse('${not empty jerarquiaFiltrosJson ? jerarquiaFiltrosJson  : "{}"}');
        </script>
    </head>
        
        <style>
            /* --- ESTILOS PARA EL SLIDER DE RANGO DOBLE --- */
            #slider-min, #slider-max {
                -webkit-appearance: none;
                -moz-appearance: none;
                appearance: none;
                width: 100%;
                outline: none;
                position: absolute;
                background-color: transparent;
                pointer-events: none;
            }
            input[type="range"]::-webkit-slider-thumb {
                -webkit-appearance: none;
                appearance: none;
                height: 18px;
                width: 18px;
                background-color: #a8d5c5; /* Color Pistacho */
                border-radius: 50%;
                border: 2px solid white;
                box-shadow: 0 0 0 1px rgba(0,0,0,0.1);
                cursor: pointer;
                pointer-events: auto;
                margin-top: -8px; /* Ajuste vertical */
            }
            .relative > input[type="range"] {
                top: 0;
            }
            input[type="range"]::-moz-range-thumb {
                -moz-appearance: none;
                appearance: none;
                height: 18px;
                width: 18px;
                background-color: #a8d5c5; /* Color Pistacho */
                border-radius: 50%;
                border: 2px solid white;
                box-shadow: 0 0 0 1px rgba(0,0,0,0.1);
                cursor: pointer;
                pointer-events: auto;
            }
        </style>
        
        
    </head>
    
    <body class="flex flex-col min-h-screen bg-gray-50" data-authenticated="${not empty pageContext.request.userPrincipal}">

        <header>
            <%@ include file="/WEB-INF/views/includes/navbar.jsp" %>
        </header>

        <main class="flex-grow container mx-auto px-4 py-8">
            <div class="flex justify-between items-center mb-4">
                <nav class="text-sm text-gray-600" aria-label="Breadcrumb">
                    <ol id="breadcrumb-list" class="list-reset flex">
                        <c:forEach var="crumb" items="${breadcrumbs}" varStatus="st">
                            <li>
                                <a href="${pageContext.request.contextPath}${crumb.href}" class="hover:underline">${crumb.label}</a>
                            </li>
                            <c:if test="${!st.last}">
                                <li class="mx-2">/</li>
                            </c:if>
                        </c:forEach>
                    </ol>
                </nav>

                <span id="totalCount" class="text-gray-700 text-sm">
                    ${totalElements} productos encontrados
                </span>
            </div>

            <div class="flex flex-col lg:flex-row gap-8 items-start">
                <aside class="lg:w-1/4 w-full self-start bg-white border rounded-lg p-6 shadow-sm">
                    <div class="flex justify-center items-baseline gap-2 mb-3">
                        <img src="${pageContext.request.contextPath}/images/filters.svg" class="h-4" alt="Filtros"/>
                        <span class="text-2xl font-bold">Filtros</span>
                    </div>

                    <form id="filterForm" class="space-y-6">
                        <div>
                            <h2 class="font-semibold mb-2">Familia</h2>
                            <select id="familiaSelector" name="familia" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
                                <option value="">Todas las familias</option>
                            </select>
                            <div id="selectedFamiliesContainer" class="mt-2 flex flex-wrap gap-2"></div>
                            <div id="familiaInputsContainer" class="hidden"></div>
                        </div>

                        <div>
                            <h2 class="font-semibold mb-2">Categoría</h2>
                            <select id="categoriaSelector" name="categoria" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
                                <option value="">Todas las categorías</option>
                            </select>
                            <div id="selectedCategoriesContainer" class="mt-2 flex flex-wrap gap-2"></div>
                            <div id="categoriaInputsContainer" class="hidden"></div>
                        </div>

                        <div>
                            <h2 class="font-semibold mb-2">Subcategoría</h2>
                            <select id="subcategoriaSelector" name="subcategoria" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
                                <option value="">Todas las subcategorías</option>
                            </select>
                            <div id="selectedSubcategoriesContainer" class="mt-2 flex flex-wrap gap-2"></div>
                            <div id="subcategoriaInputsContainer" class="hidden"></div>
                        </div>

                        <div>
                            <h2 class="font-semibold mb-2">Tipo</h2>
                            <select id="tipoSelector" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
                                <option value="">Añadir un tipo...</option>
                                <c:if test="${not empty todosLosTipos}">
                                    <c:forEach var="t" items="${todosLosTipos}">
                                        <option value="${t.id}" data-nombre="${t.nombre}">${t.nombre}</option>
                                    </c:forEach>
                                </c:if>
                            </select>
                            <div id="selectedTiposContainer" class="mt-2 flex flex-wrap gap-2"></div>
                            <div id="tipoInputsContainer" class="hidden"></div>
                        </div>

                        <div>
                            <h2 class="font-semibold mb-2">Laboratorio</h2>
                            <select id="laboratorioSelector" name="laboratorio" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
                                <option value="">Todos los laboratorios</option>
                                 <c:if test="${not empty todosLosLaboratorios}">
                                     <c:forEach var="lab" items="${todosLosLaboratorios}">
                                         <option data-nombre="${lab.nombre}" value="${lab.id}">${lab.nombre}</option>
                                     </c:forEach>
                                 </c:if>
                            </select>
                            <div id="selectedLaboratoriosContainer" class="mt-2 flex flex-wrap gap-2"></div>
                            <div id="laboratorioInputsContainer" class="hidden"></div>
                        </div>

                        <div class="space-y-2">
                            <h2 class="font-semibold mb-2">Disponibilidad</h2>
                            <label class="flex items-center">
                                <input type="checkbox" name="activo" value="true"
                                        class="form-checkbox h-5 w-5 text-pistachio"
                                        <c:if test="${filtroActivo}">checked</c:if>/>
                                <span class="ml-2">Sólo activos</span>
                            </label>
                            <label class="flex items-center">
                                <input type="checkbox" name="stock" value="true"
                                        class="form-checkbox h-5 w-5 text-pistachio"
                                        <c:if test="${filtroStock}">checked</c:if>/>
                                <span class="ml-2">Sólo en stock</span>
                            </label>
                        </div>

                        <div>
                            <h2 class="font-semibold mb-2">Precio (€)</h2>
                            <div class="flex space-x-2">
                                <input type="number" name="precioMin" placeholder="Mín" min="0" max="1000"
                                        value="${not empty filtroPrecioMin ? filtroPrecioMin : 0}"
                                        class="w-1/2 border rounded p-2 focus:ring-pistachio focus:outline-none"/>
                                <input type="number" name="precioMax" placeholder="Máx" min="0" max="1000"
                                        value="${not empty filtroPrecioMax ? filtroPrecioMax : 1000}"
                                        class="w-1/2 border rounded p-2 focus:ring-pistachio focus:outline-none"/>
                            </div>

                            <div class="mt-4 relative h-8 flex items-center">
                                <div class="relative w-full h-1.5">
                                    <div class="absolute bg-gray-200 h-1.5 rounded w-full z-10"></div>
                                    <div class="slider-progress absolute bg-pistachio h-1.5 rounded z-20"></div>

                                    <div class="relative z-30">
                                        <input type="range" id="slider-min"
                                                min="0" max="1000" value="${not empty filtroPrecioMin ? filtroPrecioMin : 0}">
                                        <input type="range" id="slider-max"
                                                min="0" max="1000" value="${not empty filtroPrecioMax ? filtroPrecioMax : 1000}">
                                    </div>
                                </div>
                            </div>
                        </div>

                        <button type="submit" class="w-full py-2 bg-pistachio text-white font-medium rounded hover:bg-dark-pistachio transition">
                            Filtrar
                        </button>
                        <button type="button" id="clearFilters" class="w-full mt-1 py-2 border border-gray-300 text-gray-700 font-medium rounded hover:bg-gray-100 transition">
                            Limpiar
                        </button>
                    </form>
                </aside>

                <section class="lg:w-3/4 w-full flex flex-col">
                    <div id="productsGrid" class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 xl:grid-cols-4 gap-4">
                        <%-- El contenido se generará con JS --%>
                    </div>

                    <div id="pagination-controls" class="mt-8 flex justify-between items-center">
                        <button id="prevBtn" class="px-4 py-2 bg-gray-200 rounded disabled:opacity-50" disabled>Anterior</button>
                        <span>Página <span id="pageNum">1</span> de <span id="totalPages">1</span></span>
                        <button id="nextBtn" class="px-4 py-2 bg-gray-200 rounded disabled:opacity-50">Siguiente</button>
                    </div>
                </section>
            </div>
        </main>

        <footer class="bg-gray-800 text-white py-4">
             <div class="container mx-auto text-center">
                <%@ include file="/WEB-INF/views/includes/footer.jsp" %>
             </div>
        </footer>

        <script src="${pageContext.request.contextPath}/js/products/products-grid.js" defer></script>
        <script src="${pageContext.request.contextPath}/js/products/filter-ui.js" defer></script>
    </body>
</html>