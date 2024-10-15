<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "La Botica de Mar";
%>
<!DOCTYPE html>
<html lang="es">

    <<!-- Incluir el head -->
    <%@ include file="/WEB-INF/views/includes/head.jsp" %>
    
    <!-- Incluir el navbar -->
    <%@ include file="/WEB-INF/views/includes/navbar.jsp" %>
    
    <div class="flex-grow">
        <div class="container mx-auto mt-10">
            <div class="bg-white p-8 rounded-lg shadow-md flex">

                <!-- Columna izquierda: Foto del producto -->
                <div class="w-1/2">
                    <img src="ruta/foto-del-producto.jpg" alt="Producto" class="w-full h-auto rounded-lg">
                </div>

                <!-- Columna central: Descripción y desplegables -->
                <div class="w-1/2 pl-8">
                    <!-- Título del producto -->
                    <h1 class="text-3xl font-bold text-gray-800 mb-4">Nombre del Producto</h1>
                    
                    <!-- Descripción breve -->
                    <p class="text-gray-600 mb-4">
                        Una breve descripción del producto va aquí. Esta sección puede contener detalles generales sobre el producto para captar la atención del usuario.
                    </p>

                    <!-- Sección de desplegables -->
                    <div class="mb-4">
                        <button class="w-full text-left p-3 bg-gray-100 space-x-2 flex justify-start align-center border border-gray-200 rounded-lg mb-2 hover:bg-gray-200" onclick="toggleDropdown('detailsDropdown')">
                            <img id="categories_arrowDown" src="${pageContext.request.contextPath}/images/arrow-down.svg" class="mt-1 h-4 cursor-pointer" alt="Flecha hacia abajo">
                            <span>Detalles del producto</span>
                        </button>
                        <div id="detailsDropdown" class="hidden p-3 border border-gray-200 rounded-lg">
                            <p class="text-gray-600">Aquí puedes incluir información más detallada del producto, como dimensiones, materiales, cuidados, etc.</p>
                        </div>

                        <button class="w-full text-left p-3 bg-gray-100 border border-gray-200 rounded-lg mb-2 hover:bg-gray-200" onclick="toggleDropdown('specificationsDropdown')">Especificaciones</button>
                        <div id="specificationsDropdown" class="hidden p-3 border border-gray-200 rounded-lg">
                            <p class="text-gray-600">Sección para especificaciones técnicas como el peso, tamaño, componentes y más detalles.</p>
                        </div>

                        <button class="w-full text-left p-3 bg-gray-100 border border-gray-200 rounded-lg hover:bg-gray-200" onclick="toggleDropdown('reviewsDropdown')">Opiniones de clientes</button>
                        <div id="reviewsDropdown" class="hidden p-3 border border-gray-200 rounded-lg">
                            <p class="text-gray-600">Aquí puedes mostrar las opiniones y valoraciones de otros usuarios sobre este producto.</p>
                        </div>
                    </div>
                </div>

                <!-- Columna derecha: Opciones de compra -->
                <div class="w-1/3 pl-8">
                    <!-- Precio del producto -->
                    <div class="text-2xl font-bold text-gray-800 mb-4">
                        $29.99
                    </div>

                    <!-- Sección de control de cantidad -->
                    <div class="flex items-center mb-4">
                        <!-- Botón de restar unidades -->
                        <button class="bg-gray-300 text-gray-800 py-2 px-4 rounded-l-lg hover:bg-gray-400" onclick="decreaseQuantity()">
                            -
                        </button>
                        <!-- Indicador de cantidad -->
                        <input id="quantity" type="text" value="1" class="w-12 text-center border border-gray-300 py-2 px-2" readonly>
                        <!-- Botón de aumentar unidades -->
                        <button class="bg-gray-300 text-gray-800 py-2 px-4 rounded-r-lg hover:bg-gray-400" onclick="increaseQuantity()">
                            +
                        </button>
                    </div>

                    <!-- Botón para añadir al carrito -->
                    <div class="flex items-center space-x-2 mb-4 h-1/5">
                        <!-- Botón para añadir al carrito -->
                        <button class="w-full bg-blue-600 text-white h-1/2 py-3 px-6 flex justify-center items-center rounded-lg hover:bg-blue-500">
                            <span class="mx-2">Agregar al carrito</span>
                            <img src="${pageContext.request.contextPath}/images/shopping-cart.svg" class="h-full cursor-pointer" alt="Mi carrito">
                        </button>

                        <!-- Botón de "Me gusta" con solo el ícono del corazón -->
                        <button class="bg-gray-300 text-gray-800 p-2 h-1/2 rounded-lg hover:bg-red-400">
                            <img src="${pageContext.request.contextPath}/images/heart.svg" class="h-full cursor-pointer" alt="Mi carrito">
                        </button>
                    </div>

                    <!-- Botón de compra directa -->
                    <button class="w-full bg-green-600 text-white py-3 px-6 rounded-lg hover:bg-green-500">
                        Comprar ahora
                    </button>
                </div>

                <script>
                    // Función para aumentar la cantidad
                    function increaseQuantity() {
                        let quantityInput = document.getElementById('quantity');
                        let currentQuantity = parseInt(quantityInput.value);
                        quantityInput.value = currentQuantity + 1;
                    }

                    // Función para disminuir la cantidad
                    function decreaseQuantity() {
                        let quantityInput = document.getElementById('quantity');
                        let currentQuantity = parseInt(quantityInput.value);
                        if (currentQuantity > 1) { // Para evitar que la cantidad sea menor que 1
                            quantityInput.value = currentQuantity - 1;
                        }
                    }
                </script>

            </div>

            <!-- Carousel de productos recomendados usando Flowbite -->
            <div class="mt-10 mb-10">
                <h2 class="text-2xl font-bold mb-6">Productos recomendados</h2>
                <!-- Carousel container -->
                <div id="carouselExample" class="relative w-full items-center flex justify-between" data-carousel="static">
                    <button type="button" class="top-0 left-0 z-30 flex items-center justify-center h-full px-4 cursor-pointer group focus:outline-none" data-carousel-prev>
                        <span class="inline-flex items-center justify-center w-10 h-10 rounded-full bg-gray-500 group-hover:bg-gray-600 dark:bg-gray-800 dark:group-hover:bg-gray-700">
                            <svg aria-hidden="true" class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"></path>
                            </svg>
                            <span class="sr-only">Previous</span>
                        </span>
                    </button>
                    <div class="flex justify-between items-center w-full overflow-hidden rounded-lg">
                        <!-- Slide 1 -->
                        <div class="w-1/5 p-4 flex flex-col justify-center items-center">
                            <img src="ruta/producto1.jpg" class="w-24 h-24 rounded-lg" alt="Producto 1">
                            <h3 class="text-lg font-bold">Producto 1</h3>
                            <p class="text-gray-600">$19.99</p>
                        </div>
                        <!-- Slide 2 -->
                        <div class="w-1/5 p-4 flex flex-col justify-center items-center">
                            <img src="ruta/producto1.jpg" class="w-24 h-24 rounded-lg" alt="Producto 1">
                            <h3 class="text-lg font-bold">Producto 1</h3>
                            <p class="text-gray-600">$19.99</p>
                        </div>
                        <!-- Slide 3 -->
                        <div class="w-1/5 p-4 flex flex-col justify-center items-center">
                            <img src="ruta/producto1.jpg" class="w-24 h-24 rounded-lg" alt="Producto 1">
                            <h3 class="text-lg font-bold">Producto 1</h3>
                            <p class="text-gray-600">$19.99</p>
                        </div>
                        <!-- Slide 4 -->
                        <div class="w-1/5 p-4 flex flex-col justify-center items-center">
                            <img src="ruta/producto1.jpg" class="w-24 h-24 rounded-lg" alt="Producto 1">
                            <h3 class="text-lg font-bold">Producto 1</h3>
                            <p class="text-gray-600">$19.99</p>
                        </div>
                        <!-- Slide 5 -->
                        <div class="w-1/5 p-4 flex flex-col justify-center items-center">
                            <img src="ruta/producto1.jpg" class="w-24 h-24 rounded-lg" alt="Producto 1">
                            <h3 class="text-lg font-bold">Producto 1</h3>
                            <p class="text-gray-600">$19.99</p>
                        </div>
                        <!-- Slide 5 -->
                        <div class="w-1/5 p-4 flex flex-col justify-center items-center">
                            <img src="ruta/producto1.jpg" class="w-24 h-24 rounded-lg" alt="Producto 1">
                            <h3 class="text-lg font-bold">Producto 1</h3>
                            <p class="text-gray-600">$19.99</p>
                        </div>
                    </div>
                    <button type="button" class="top-0 right-0 z-30 flex items-center justify-center h-full px-4 cursor-pointer group focus:outline-none" data-carousel-next>
                        <span class="inline-flex items-center justify-center w-10 h-10 rounded-full bg-gray-500 group-hover:bg-gray-600 dark:bg-gray-800 dark:group-hover:bg-gray-700">
                            <svg aria-hidden="true" class="w-6 h-6 text-white" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7"></path>
                            </svg>
                            <span class="sr-only">Next</span>
                        </span>
                    </button>
                </div>
            </div>
        </div>

        <!-- Scripts para manejar los desplegables -->
        <script>
            function toggleDropdown(id) {
                const dropdown = document.getElementById(id);
                if (dropdown.classList.contains('hidden')) {
                    dropdown.classList.remove('hidden');
                } else {
                    dropdown.classList.add('hidden');
                }
            }
        </script>
    </div>
    
    <!-- Incluir el footer -->
    <%@ include file="/WEB-INF/views/includes/footer.jsp" %>

</html>
