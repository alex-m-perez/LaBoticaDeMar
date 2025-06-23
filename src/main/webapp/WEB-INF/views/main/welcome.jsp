<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<% String pageTitle = "La Botica de Mar"; %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><%= pageTitle != null ? pageTitle : "Tienda Online" %></title>
        <link rel="icon" href="/images/icono_tab2.png" type="image/png">

        <!-- Tailwind CSS -->
        <link rel="stylesheet" href="/css/output.css">    
        <!-- Fuentes -->
        <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet">
        <!-- AJAX -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <!-- Flowbite CDN -->
        <script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
        <!-- Pagedone CDN -->
        <link href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" rel="stylesheet"/>
        <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
    </head>

    <body class="bg-gray-100 min-h-screen flex flex-col">
        <%@ include file="/WEB-INF/views/includes/navbar.jsp" %>

        <main class="flex-grow bg-white">
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
            <script>
                const carousel = new Carousel(document.getElementById('carouselExample'), {
                    interval: 7000, // Cambia de imagen cada 3 segundos
                });     
            </script>

            <!-- Sección de Productos Destacados -->
            <section class="container mx-0 lg:mx-auto px-4 py-8">
                <h2 class="text-2xl font-bold text-gray-800 text-start mb-8">PRODUCTOS DESTACADOS</h2>
                <!-- Carousel Container -->
                <div class="w-full h-96 relative">
                    <!-- Swiper Carousel -->
                    <div class="swiper multiple-slide-carousel swiper-container h-full w-full relative">
                        <div class="swiper-wrapper">
                            <!-- Reemplazamos las diapositivas estándar con tus tarjetas de producto -->
                            <c:forEach var="producto" items="${destacados}">
                                <div class="swiper-slide bg-white shadow-md rounded-lg overflow-hidden flex flex-col w-full h-full">
                                    <img src="${producto.imagenPath != null ? producto.imagenPath : 'https://via.placeholder.com/300x200'}" 
                                        href="${pageContext.request.contextPath}/product/${producto.id}" alt="Imagen del Producto" class="w-full h-3/5 object-cover">
                                    <div class="p-4 flex-grow flex h-2/5 flex-col justify-between">
                                        <div class="h-3/6">
                                            <span onclick="window.location.href='/product/${producto.id}'" class="text-sm font-semibold cursor-pointer text-gray-800">${producto.nombre}</span>
                                        </div>
                                        <div class="flex items-center justify-start h-1/6">
                                            <div class="flex ">
                                                <c:forEach var="i" begin="1" end="5">
                                                    <svg xmlns="http://www.w3.org/2000/svg" 
                                                        fill="${i <= producto.rating ? '#FFD700' : '#D3D3D3'}" 
                                                        viewBox="0 0 24 24" 
                                                        class="w-5 h-5 ${i <= producto.rating ? 'text-yellow-400' : 'text-gray-300'}">
                                                        <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                                                    </svg>

                                                </c:forEach>
                                            </div>
                                            <span class="text-sm">(${producto.ratingCount})</span>
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
                    </div>
                    <div>
                        <button type="button" id="product-carousel-prev" class="custom-button-prev absolute top-1/2 left-0 z-30 transform -translate-y-1/2 bg-gray-700 text-white rounded-full p-2 hover:bg-gray-500 focus:outline-none" data-carousel-prev>
                            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                            </svg>
                        </button>
                        <button type="button" id="product-carousel-next" class="custom-button-next absolute top-1/2 right-0 z-30 transform -translate-y-1/2 bg-gray-700 text-white rounded-full p-2 hover:bg-gray-500 focus:outline-none" data-carousel-next>
                            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
                            </svg>
                        </button>
                    </div>

                    <script>
                        const swiper = new Swiper('.multiple-slide-carousel', {
                            slidesPerView: 5,  // Valor máximo de elementos por vista
                            spaceBetween: 50,  // Espacio entre productos
                            loop: true, 
                            navigation: {
                                nextEl: '.custom-button-next',
                                prevEl: '.custom-button-prev',
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
                                    slidesPerView: 5,
                                    spaceBetween: 20,
                                }
                            }
                        });
                    </script>
                </div>
            </section>

        </main>

        <%@ include file="/WEB-INF/views/includes/footer.jsp" %>
    </body>
</html>
