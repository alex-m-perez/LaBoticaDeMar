<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>La Botica de Mar</title>
        <link rel="icon" href="/images/icono_tab2.png" type="image/png">

        <link rel="stylesheet" href="/css/output.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
    </head>

    <body class="bg-gray-100 min-h-screen flex flex-col" data-authenticated="${not empty pageContext.request.userPrincipal}">

        <header>
            <%@ include file="../includes/navbar.jsp" %>
        </header>

        <main class="flex-grow bg-white">
            <section id="carouselHero" class="relative bg-cover bg-center h-72">
                <div id="carouselExample" class="relative w-full h-full" data-carousel="slide">
                    <div class="relative h-full overflow-hidden rounded-lg">
                        <div class="hidden duration-700 ease-in-out" data-carousel-item><img src="${pageContext.request.contextPath}/images/carrusel.jpg" class="absolute block w-full h-full object-cover" alt="..."></div>
                        <div class="hidden duration-700 ease-in-out" data-carousel-item><img src="${pageContext.request.contextPath}/images/carrusel1.jpg" class="absolute block w-full h-full object-cover" alt="..."></div>
                        <div class="hidden duration-700 ease-in-out" data-carousel-item><img src="${pageContext.request.contextPath}/images/carrusel2.jpg" class="absolute block w-full h-full object-cover" alt="..."></div>
                        <div class="hidden duration-700 ease-in-out" data-carousel-item><img src="${pageContext.request.contextPath}/images/carrusel3.jpg" class="absolute block w-full h-full object-cover" alt="..."></div>
                        <div class="hidden duration-700 ease-in-out" data-carousel-item><img src="${pageContext.request.contextPath}/images/carrusel4.jpg" class="absolute block w-full h-full object-cover" alt="..."></div>
                    </div>
                    <div class="absolute z-30 flex space-x-3 bottom-5 left-1/2 -translate-x-1/2">
                        <button data-carousel-slide-to="0" class="w-3 h-3 rounded-full" aria-label="Slide 1"></button>
                        <button data-carousel-slide-to="1" class="w-3 h-3 rounded-full" aria-label="Slide 2"></button>
                        <button data-carousel-slide-to="2" class="w-3 h-3 rounded-full" aria-label="Slide 3"></button>
                        <button data-carousel-slide-to="3" class="w-3 h-3 rounded-full" aria-label="Slide 4"></button>
                        <button data-carousel-slide-to="4" class="w-3 h-3 rounded-full" aria-label="Slide 5"></button>
                    </div>
                </div>
            </section>
            
            <section class="container mx-auto px-4 py-8 mb-4">
                <h2 class="text-2xl font-bold text-gray-800 text-start mb-4">Productos destacados</h2>
                <div class="flex items-center space-x-2">
                    <button id="product-carousel-prev" class="p-1 bg-white hover:bg-gray-100 text-gray-700 rounded-full shadow-md">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/></svg>
                    </button>

                    <div class="swiper multiple-slide-carousel w-full">
                        <div class="swiper-wrapper">
                            <c:forEach var="producto" items="${destacados}">
                                <div class="swiper-slide !h-auto">
                                    <%-- Plantilla de tarjeta de producto --%>
                                    <div class="product-card relative flex flex-col p-2 border border-gray-200 rounded-lg h-full bg-white w-full" data-product='{"id": "${producto.id}", "stock": ${producto.stock}}'>
                                        <div class="relative mb-4">
                                            <c:if test="${producto.discount > 0}">
                                                <span class="absolute top-0 left-0 bg-red-600 text-white text-sm font-bold px-2 py-1 rounded-md z-10">
                                                    -<fmt:formatNumber value="${producto.discount}" maxFractionDigits="0"/>%
                                                </span>
                                            </c:if>
                                            <button class="wishlist-btn absolute top-2 right-2 text-gray-400 hover:text-red-500 z-10" data-id="${producto.id}">
                                                <svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/></svg>
                                            </button>
                                            <a href="${pageContext.request.contextPath}/product/${producto.id}">
                                                <c:choose>
                                                    <c:when test="${not empty producto.imagenData}">
                                                        <img src="<c:url value='/api/images/${producto.id}'/>" alt="${producto.nombre}" class="h-40 w-full object-contain rounded"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="h-40 w-full bg-gray-200 flex items-center justify-center rounded">
                                                            <svg class="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7h4l2-3h6l2 3h4v13H3V7z"/><circle cx="12" cy="13" r="4" stroke="currentColor" stroke-width="2"/></svg>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </a>
                                        </div>
                                        <h3 onclick="window.location.href='${pageContext.request.contextPath}/product/${producto.id}'" class="text-sm font-medium mb-2 cursor-pointer hover:text-pistachio flex-grow">${producto.nombre}</h3>
                                        <div class="flex items-center space-x-1 mb-2">
                                            <c:forEach var="i" begin="1" end="5"><svg class="w-4 h-4 ${i <= producto.rating ? 'text-yellow-400' : 'text-gray-300'}" fill="currentColor" viewBox="0 0 20 20"><path fill-rule="evenodd" d="M10 15l-5.09 2.676 0.974-5.678L1.364 8.324l5.72-0.832L10 2l2.916 5.492 5.72 0.832-4.52 3.674 0.974 5.678z" clip-rule="evenodd"/></svg></c:forEach>
                                            <span class="text-xs text-gray-500">(${producto.ratingCount})</span>
                                        </div>
                                        <span class="text-xs font-semibold mb-4 ${producto.stock > 0 ? 'text-green-600' : 'text-red-600'}">${producto.stock > 0 ? 'En stock' : 'No disponible'}</span>
                                        <div class="mt-auto flex justify-between items-end">
                                            <div class="flex flex-col">
                                                <c:choose>
                                                    <c:when test="${producto.discount > 0}"><span class="text-sm text-gray-400 line-through"><fmt:formatNumber value="${producto.price}" type="currency" currencySymbol="€ " minFractionDigits="2"/></span><span class="text-lg font-bold text-gray-800"><fmt:formatNumber value="${producto.price * (1 - producto.discount / 100)}" type="currency" currencySymbol="€ " minFractionDigits="2" maxFractionDigits="2"/></span></c:when>
                                                    <c:otherwise><span class="text-lg font-bold text-gray-800"><fmt:formatNumber value="${producto.price}" type="currency" currencySymbol="€ " minFractionDigits="2"/></span></c:otherwise>
                                                </c:choose>
                                            </div>
                                            <div class="cart-controls-container"></div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <button id="product-carousel-next" class="p-1 bg-white hover:bg-gray-100 text-gray-700 rounded-full shadow-md">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/></svg>
                    </button>
                </div>
            </section>
        </main>

        <footer class="bg-gray-800 text-white py-4">
             <div class="container mx-auto text-center">
                <%@ include file="/WEB-INF/views/includes/footer.jsp" %>
             </div>
        </footer>
        
        <script>
            new Swiper('.multiple-slide-carousel', {
                loop: true,
                navigation: { nextEl: '#product-carousel-next', prevEl: '#product-carousel-prev' },
                breakpoints: { 240: { slidesPerView: 2, spaceBetween: 10 }, 700: { slidesPerView: 3, spaceBetween: 15 }, 900: { slidesPerView: 4, spaceBetween: 20 }, 1100: { slidesPerView: 5, spaceBetween: 20 }, }
            });
        </script>
        
        <script src="${pageContext.request.contextPath}/js/main/welcome.js" defer></script>
    </body>
</html>