<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "La Botica de Mar";
%>
<!DOCTYPE html>
<html lang="es">
    <%@ include file="/WEB-INF/views/includes/head.jsp" %>
    <%@ include file="/WEB-INF/views/includes/navbar.jsp" %>
    
    <main class="flex-grow">
        <div class="container mx-auto mt-10">
            <div class="bg-white p-8 rounded-lg shadow-md flex">

                <!-- Columna izquierda: Foto del producto -->
                <div class="w-1/2">
                    <img src="${producto.imagenPath != null ? producto.imagenPath : 'https://via.placeholder.com/300x200'}" alt="Producto" class="w-full h-auto rounded-lg">
                </div>

                <!-- Columna central: Descripción y desplegables -->
                <div class="w-1/2 pl-8">
                    <!-- Título del producto -->
                    <h1 class="text-3xl font-bold text-gray-800 mb-4">${producto.nombre}</h1>
                    
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

            <!-- Sección de Productos Destacados -->
            <section class="container mx-auto px-4 py-8">
                <h2 class="text-2xl font-bold text-gray-800 text-start mb-8">PRODUCTOS DESTACADOS</h2>
                <!-- Carousel Container -->
                <div class="w-full h-96 relative">
                    <!-- Swiper Carousel -->
                    <div class="swiper multiple-slide-carousel swiper-container h-full w-full relative">
                        <div class="swiper-wrapper">
                            <!-- Reemplazamos las diapositivas estándar con tus tarjetas de producto -->
                            <c:forEach var="producto" items="${destacados}">
                                    <div class="swiper-slide bg-white shadow-md rounded-lg overflow-hidden flex flex-col w-full h-full mx-2">
                                        <img src="${producto.imagenPath != null ? producto.imagenPath : 'https://via.placeholder.com/300x200'}" 
                                            href="${pageContext.request.contextPath}/product/${producto.id}" alt="Imagen del Producto" class="w-full h-3/5 object-cover">
                                        <div class="p-4 flex-grow flex h-2/5 flex-col justify-between">
                                            <div class="h-3/6">
                                                <span onclick="window.location.href='/product/${producto.id}'" class="text-sm font-semibold cursor-pointer text-gray-800">${producto.nombre}</span>
                                            </div>
                                            <div class="flex items-center h-1/6">
                                                <c:forEach var="i" begin="1" end="5">
                                                    <svg xmlns="http://www.w3.org/2000/svg" 
                                                        fill="${i <= producto.rating ? '#FFD700' : '#D3D3D3'}" 
                                                        viewBox="0 0 24 24" 
                                                        class="w-5 h-5 ${i <= producto.rating ? 'text-yellow-400' : 'text-gray-300'}">
                                                        <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                                                    </svg>
                                                </c:forEach>
                                            </div>
                                            <div class="mt-2 flex justify-between items-center h-2/6">
                                                <div class="flex flex-col">
                                                    <span class="text-sm ${producto.stock > 0 ? 'text-green-600' : 'text-red-600'}">
                                                        ${producto.stock > 0 ? 'En stock' : 'No disponible'}
                                                    </span>
                                                    <!-- Stock information -->
                                                    <div class="flex justify-between items-center">
                                                        <span class="text-gray-800 font-bold">${producto.price} €</span>
                                                    </div>
                                                </div>
                                                <div class="h-8">
                                                    <button class="bg-gray-300 text-gray-800 p-2 h-full rounded-lg hover:bg-red-300">
                                                        <img src="${pageContext.request.contextPath}/images/heart.svg" class="h-full cursor-pointer" alt="Mi carrito">
                                                    </button>
                                                    <button class="bg-gray-300 text-gray-800 p-2 h-full rounded-lg hover:bg-green-300">
                                                        <img src="${pageContext.request.contextPath}/images/shopping-cart.svg" class="h-full cursor-pointer" alt="Mi carrito">
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                            </c:forEach>
                        </div>

                        <!-- Botones funcionales (invisibles) -->
                        <button id="slider-button-left" class="invisible swiper-button-prev group !p-2 flex justify-center items-center border border-solid border-indigo-600 !w-12 !h-12 transition-all duration-500 rounded-full  hover:bg-indigo-600 !-translate-x-16" data-carousel-prev>
                            <svg class="h-5 w-5 text-indigo-600 group-hover:text-white" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                                <path d="M10.0002 11.9999L6 7.99971L10.0025 3.99719" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                        </button>
                        <button id="slider-button-right" class="invisible swiper-button-next group !p-2 flex justify-center items-center border border-solid border-indigo-600 !w-12 !h-12 transition-all duration-500 rounded-full hover:bg-indigo-600 !translate-x-16" data-carousel-next>
                            <svg class="h-5 w-5 text-indigo-600 group-hover:text-white" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                                <path d="M5.99984 4.00012L10 8.00029L5.99748 12.0028" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" />
                            </svg>
                        </button>

                    </div>

                    <!-- Botones estilizados (visibles) -->
                    <div>
                        <button type="button" id="product-carousel-prev" class="absolute top-1/2 left-0 z-30 transform -translate-y-1/2 bg-gray-700 text-white rounded-full p-2 hover:bg-gray-500 focus:outline-none" data-carousel-prev>
                            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                            </svg>
                        </button>

                        <button type="button" id="product-carousel-next" class="absolute top-1/2 right-0 z-30 transform -translate-y-1/2 bg-gray-700 text-white rounded-full p-2 hover:bg-gray-500 focus:outline-none" data-carousel-next>
                            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
                            </svg>
                        </button>
                    </div>

                    <script>
                        const swiper = new Swiper('.multiple-slide-carousel', {
                            slidesPerView: 5,  // Valor máximo de elementos por vista
                            spaceBetween: 5,  // Espacio entre productos
                            loop: true, 
                            navigation: {
                                nextEl: '.swiper-button-next',
                                prevEl: '.swiper-button-prev',
                            },
                            breakpoints: {
                                // Cuando la pantalla es menor a 640px de ancho, mostrar 1 elemento
                                240: {
                                    slidesPerView: 2,
                                    spaceBetween: 10,
                                },
                                // Cuando la pantalla es menor a 768px, mostrar 2 elementos
                                700: {
                                    slidesPerView: 3,
                                    spaceBetween: 15,
                                },
                                // Cuando la pantalla es menor a 1024px, mostrar 3 elementos
                                900: {
                                    slidesPerView: 4,
                                    spaceBetween: 20,
                                },
                                // Cuando la pantalla es mayor a 1024px, mostrar 5 elementos
                                1100: {
                                    slidesPerView: 5,
                                    spaceBetween: 20,
                                },
                                // A partir de 1440px en adelante, mostrar 5 elementos
                                1440: {
                                    slidesPerView: 6,
                                    spaceBetween: 20,
                                }
                            }
                        });

                        // Para el botón "anterior" estilizado
                        document.getElementById('product-carousel-prev').addEventListener('click', function() {
                            document.getElementById('slider-button-left').click();
                        });

                        // Para el botón "siguiente" estilizado
                        document.getElementById('product-carousel-next').addEventListener('click', function() {
                            document.getElementById('slider-button-right').click();
                        });
                    </script>
                </div>
            </section>
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
    </main>
    
    <%@ include file="/WEB-INF/views/includes/footer.jsp" %>
</html>
