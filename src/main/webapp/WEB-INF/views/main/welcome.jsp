<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String pageTitle = "La Botica de Mar"; %>
<!DOCTYPE html>

<html lang="es">
    <!-- Incluir el head -->
    <%@ include file="/WEB-INF/views/includes/head.jsp" %>
    
    <!-- Incluir el navbar -->
    <%@ include file="/WEB-INF/views/includes/navbar.jsp" %>
    
    <!-- Carrusel de imágenes -->
    <section id="carouselHero" class="relative bg-cover bg-center h-72">
        <!-- Contenedor del carrusel -->
        <div id="carouselExample" class="relative w-full h-full" data-carousel="slide">
            <div class="relative h-full overflow-hidden rounded-lg">
                <!-- Reemplaza con tus imágenes -->
                <div class="hidden duration-700 ease-in-out" data-carousel-item>
                    <img src="${pageContext.request.contextPath}/images/carrusel.jpg" class="absolute block w-full h-full object-cover" alt="...">
                </div>
                <div class="hidden duration-700 ease-in-out" data-carousel-item>
                    <img src="${pageContext.request.contextPath}/images/carrusel1.jpg" class="absolute block w-full h-full object-cover" alt="...">
                </div>
                <div class="hidden duration-700 ease-in-out" data-carousel-item>
                    <img src="${pageContext.request.contextPath}/images/carrusel2.jpg" class="absolute block w-full h-full object-cover" alt="...">
                </div>
                <div class="hidden duration-700 ease-in-out" data-carousel-item>
                    <img src="${pageContext.request.contextPath}/images/carrusel3.jpg" class="absolute block w-full h-full object-cover" alt="...">
                </div>
                <div class="hidden duration-700 ease-in-out" data-carousel-item>
                    <img src="${pageContext.request.contextPath}/images/carrusel4.jpg" class="absolute block w-full h-full object-cover" alt="...">
                </div>
            </div>
            <!-- Indicadores opcionales -->
            <div class="absolute z-30 flex space-x-3 bottom-5 left-1/2 -translate-x-1/2">
                <button data-carousel-slide-to="0" class="w-3 h-3 rounded-full" aria-label="Slide 1"></button>
                <button data-carousel-slide-to="1" class="w-3 h-3 rounded-full" aria-label="Slide 2"></button>
                <button data-carousel-slide-to="2" class="w-3 h-3 rounded-full" aria-label="Slide 3"></button>
                <button data-carousel-slide-to="3" class="w-3 h-3 rounded-full" aria-label="Slide 4"></button>
                <button data-carousel-slide-to="4" class="w-3 h-3 rounded-full" aria-label="Slide 5"></button>
            </div>
        </div>
    </section>


    <!-- Script para controlar el carrusel -->
    <script>
        const carousel = new Carousel(document.getElementById('carouselExample'), {
            interval: 7000, // Cambia de imagen cada 3 segundos
        });     
    </script>


    <!-- Sección de Productos Destacados -->
    <section class="container mx-auto px-4 py-8">
        <h2 class="text-2xl font-bold text-gray-800 text-start mb-8">PRODUCTOS DESTACADOS</h2>

        <!-- Carousel Container -->
        <div class="w-full relative">
            <!-- Swiper Carousel -->
            <div class="swiper multiple-slide-carousel swiper-container relative">
                <div class="swiper-wrapper mb-16">
                    <!-- Reemplazamos las diapositivas estándar con tus tarjetas de producto -->
                    <c:forEach var="producto" items="${destacados}">
                        <div class="swiper-slide">
                            <div class="bg-white shadow-md rounded-lg overflow-hidden flex flex-col w-full mx-2">
                                <img src="${producto.imagenPath != null ? producto.imagenPath : 'https://via.placeholder.com/300x200'}" alt="Imagen del Producto" class="w-full h-48 object-cover">
                                <div class="p-4 flex-grow flex flex-col justify-between">
                                    <div class="h-3/6">
                                        <h3 class="text-lg font-semibold text-gray-800">${producto.nombre}</h3>
                                    </div>
                                    <div class="flex items-center h-1/6">
                                        <c:forEach var="i" begin="1" end="5">
                                            <svg xmlns="http://www.w3.org/2000/svg" fill="${i <= producto.rating ? 'currentColor' : 'none'}" viewBox="0 0 24 24" stroke="currentColor" class="w-5 h-5 text-yellow-500">
                                                <path stroke-linecap="round" stroke-linejoin="round" d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                                            </svg>
                                        </c:forEach>
                                    </div>
                                    <div class="h-2/6">
                                        <span class="mt-4 text-sm ${producto.stock > 0 ? 'text-green-600' : 'text-red-600'}">
                                            ${producto.stock > 0 ? 'En stock' : 'No disponible'}
                                        </span>
                                        <!-- Stock information -->
                                        <div class="flex justify-between items-center">
                                            <span class="text-gray-800 font-bold">$${producto.price}</span>
                                            <div class="h-8">
                                                <button class="bg-gray-300 text-gray-800 p-2 h-full rounded-lg hover:bg-red-400">
                                                    <img src="${pageContext.request.contextPath}/images/heart.svg" class="h-full cursor-pointer" alt="Mi carrito">
                                                </button>
                                                <button class="bg-gray-300 text-gray-800 p-2 h-full rounded-lg hover:bg-red-400">
                                                    <img src="${pageContext.request.contextPath}/images/shopping-cart.svg" class="h-full cursor-pointer" alt="Mi carrito">
                                                </button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <!-- Botones estilizados (visibles) -->
                <button id="slider-button-left" class="invisible swiper-button-prev group absolute top-1/2 left-2 transform -translate-y-1/2 bg-gray-200 hover:bg-gray-300 rounded-full p-3">
                    <svg class="h-5 w-5 text-gray-600 group-hover:text-gray-800" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                        <path d="M10.0002 11.9999L6 7.99971L10.0025 3.99719" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                </button>

                <button id="slider-button-right" class="invisible swiper-button-next group absolute top-1/2 right-2 transform -translate-y-1/2 bg-gray-200 hover:bg-gray-300 rounded-full p-3">
                    <svg class="h-5 w-5 text-gray-600 group-hover:text-gray-800" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                        <path d="M5.99984 4.00012L10 8.00029L5.99748 12.0028" stroke="currentColor" stroke-width="1.6" stroke-linecap="round" stroke-linejoin="round" />
                    </svg>
                </button>

                <!-- Botones funcionales (invisibles) -->
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

                <script>
                    // Delegar clic de botones estilizados a los botones funcionales

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
        </div>

        <!-- Swiper JS (configuración) -->
        <script>
        const swiper = new Swiper('.multiple-slide-carousel', {
            slidesPerView: 5,  // Muestra 5 productos por vista
            spaceBetween: 20,  // Espacio entre productos
            navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
            },
        });
        </script>

    </section>

    <!-- Incluir el footer -->
    <%@ include file="/WEB-INF/views/includes/footer.jsp" %>

</html>
