<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${producto.nombre}</title>
    <link rel="icon" href="/images/icono_tab2.png" type="image/png"/>

    <link rel="stylesheet" href="/css/output.css"/>
    <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"/>
    <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
</head>

	<script src="${pageContext.request.contextPath}/js/products/producto_info.js" defer></script>

	<style>
        .swiper-slide { flex-shrink: 0; margin-right: 10px !important; height: auto !important; }
        #accordion-product-details .accordion-content { max-height: 12rem; /* equivale a max-h-48 */ overflow-y: auto; }
        .scrollbar-invisible::-webkit-scrollbar {
            display: none; /* Oculta la barra de scroll */
            width: 0px; /* Asegura que no ocupe espacio */
        }

        /* Para Firefox */
        .scrollbar-invisible {
            scrollbar-width: none; /* Oculta la barra de scroll */
            -ms-overflow-style: none;  /* Para IE/Edge */
        }
    </style>

	<script> window.contextPath = '<%= request.getContextPath() %>'; </script>
	<%@ include file="../includes/navbar.jsp" %>

	<main class="flex-grow bg-white py-8">
		<div class="container mx-auto px-4">

			<!-- Breadcrumbs dinámicas -->
            <nav class="text-sm mb-6" aria-label="Breadcrumb">
                <ol class="list-reset flex text-gray-600">
                    <c:forEach var="crumb" items="${breadcrumbs}" varStatus="st">
                        <li>
                            <c:choose>
                                <c:when test="${not empty crumb.href}">
                                    <a href="${crumb.href}" class="hover:underline">
                                        ${crumb.label}
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-gray-800">${crumb.label}</span>
                                </c:otherwise>
                            </c:choose>
                        </li>
                        <c:if test="${!st.last}">
                            <li class="mx-2">/</li>
                        </c:if>
                    </c:forEach>
                </ol>
            </nav>


            <div class="flex gap-8 h-min-96">
                <!-- Imágen del producto (placeholder si no hay imagen) -->
                <div class="w-full lg:w-2/5 p-2">
                    <div class="h-128 w-128 w-full bg-gray-200 flex items-center justify-center rounded-lg">
                        <c:choose>
                            <c:when test="${not empty producto.imagenPath}"><img src="${pageContext.request.contextPath}${producto.imagenPath}" alt="${producto.nombre}" class="w-full h-full object-cover rounded-lg"/></c:when>
                            <c:otherwise><svg class="w-16 h-16 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7h4l2-3h6l2 3h4v13H3V7z"/><circle cx="12" cy="13" r="4" stroke="currentColor" stroke-width="2"/></svg></c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Detalle del producto -->
                <div class="w-full lg:w-3/5 flex flex-col justify-between">
                    <div>
                        <h1 class="text-3xl font-bold text-gray-800 mb-4">${producto.nombre}</h1>

                        <div class="flex items-center gap-x-4 mb-4">
                            <div class="flex items-center space-x-1">
                                <c:forEach var="i" begin="1" end="5">
                                    <svg class="w-5 h-5 ${i <= producto.rating ? 'text-yellow-400' : 'text-gray-300'}" fill="currentColor" viewBox="0 0 20 20">
                                        <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.97a1 1 0 00.95.69h4.178c.969 0 1.371 1.24.588 1.81l-3.385 2.46a1 1 0 00-.364 1.118l1.286 3.97c.3.921-.755 1.688-1.54 1.118l-3.385-2.46a1 1 0 00-1.175 0l-3.385 2.46c-.785.57-1.84-.197-1.54-1.118l1.286-3.97a1 1 0 00-.364-1.118L2.047 9.397c-.783-.57-.38-1.81.588-1.81h4.178a1 1 0 00.95-.69l1.286-3.97z"/>
                                    </svg>
                                </c:forEach>
                                <span class="text-gray-600">(${producto.ratingCount})</span>
                            </div>

                            <c:if test="${producto.discount > 0}">
                                <span class="inline-block bg-red-500 text-white text-xs font-semibold px-2 py-1 rounded">
                                    Descuento: <fmt:formatNumber value="${producto.discount}" maxFractionDigits="0" />%
                                </span>
                            </c:if>
                        </div>

                        <div id="accordion-product-details" data-accordion="collapse" class="mb-6">
                            <h2 id="accordion-heading-1">
                                <button type="button" class="group flex items-center justify-between w-full p-2 text-left hover:bg-gray-50 [&[aria-expanded='true']]:bg-white" data-accordion-target="#accordion-body-1" aria-expanded="true" aria-controls="accordion-body-1">
                                    <span class="text-base font-medium text-gray-800 group-aria-expanded:font-bold group-aria-expanded:text-lg">Descripción</span>
                                    <svg data-accordion-icon class="w-3 h-3 rotate-180 shrink-0" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 6"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5 5 1 1 5"/></svg>
                                </button>
                            </h2>
                            <div id="accordion-body-1" class="accordion-content pb-2" aria-labelledby="accordion-heading-1">
                                <div class="p-2">
                                    <p class="text-gray-600 whitespace-pre-wrap pr-2 scrollbar-invisible leading-relaxed">${producto.descripcion}</p>
                                </div>
                            </div>

                            <c:if test="${not empty producto.use}">
                                <h2 id="accordion-heading-2">
                                    <button type="button" class="group flex items-center justify-between w-full p-2 text-left hover:bg-gray-50 [&[aria-expanded='true']]:bg-white" data-accordion-target="#accordion-body-2" aria-expanded="false" aria-controls="accordion-body-2">
                                        <span class="text-base font-medium text-gray-800 group-aria-expanded:font-bold group-aria-expanded:text-lg">Uso</span>
                                        <svg data-accordion-icon class="w-3 h-3 shrink-0" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 6"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5 5 1 1 5"/></svg>
                                    </button>
                                </h2>
                                <div id="accordion-body-2" class="hidden accordion-content pb-2" aria-labelledby="accordion-heading-2">
                                    <div class="p-2">
                                        <p class="text-gray-600 whitespace-pre-wrap pr-2 scrollbar-invisible leading-relaxed">${producto.use}</p>
                                    </div>
                                </div>
                            </c:if>

                            <c:if test="${not empty producto.composition}">
                                <h2 id="accordion-heading-3">
                                    <button type="button" class="group flex items-center justify-between w-full p-2 text-left hover:bg-gray-50 [&[aria-expanded='true']]:bg-white" data-accordion-target="#accordion-body-3" aria-expanded="false" aria-controls="accordion-body-3">
                                        <span class="text-base font-medium text-gray-800 group-aria-expanded:font-bold group-aria-expanded:text-lg">Composición</span>
                                        <svg data-accordion-icon class="w-3 h-3 shrink-0" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 6"><path stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5 5 1 1 5"/></svg>
                                    </button>
                                </h2>
                                <div id="accordion-body-3" class="hidden accordion-content pb-2" aria-labelledby="accordion-heading-3">
                                    <div class="p-2">
                                        <p class="text-gray-600 whitespace-pre-wrap pr-2 scrollbar-invisible leading-relaxed">${producto.composition}</p>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>

                    <div class="flex items-end justify-between mt-auto" 
                            id="product-detail-container" 
                            data-product-id="${producto.id}" 
                            data-product-stock="${producto.stock}">
                        <div class="flex flex-col">
                            <div class="flex items-center gap-3 items-baseline">
                                <c:if test="${producto.discount == 0 || producto.discount == null}">
                                    <span class="text-2xl font-bold text-gray-800">
                                        <fmt:formatNumber value="${producto.price}" pattern="#,##0.00"/> €
                                    </span>
                                </c:if>

                                <c:if test="${producto.discount > 0}">
                                    <span class="text-2xl font-bold text-red-600">
                                        <fmt:formatNumber value="${producto.price * (1 - producto.discount / 100)}" pattern="#,##0.00"/> €
                                    </span>
                                    <span class="text-md text-gray-500 line-through">
                                        <fmt:formatNumber value="${producto.price}" pattern="#,##0.00"/> €
                                    </span>
                                </c:if>
                            </div>

                            <c:choose>
                                <c:when test="${producto.stock > 0}">
                                    <span class="text-green-600 font-semibold mt-1">Disponible</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="text-red-600 font-semibold mt-1">Sin stock</span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <div class="flex items-center space-x-3">
                            <div class="flex items-center border border-gray-200 rounded">
                                <button id="qty-minus" class="px-3 py-1 text-lg text-gray-600 hover:bg-gray-100 rounded-l transition">
                                    −
                                </button>
                                <input id="qty-input" type="text" value="1" class="w-12 text-center text-md font-bold border-0 focus:ring-0" readonly>
                                <button id="qty-plus" class="px-3 py-1 text-lg text-gray-600 hover:bg-gray-100 rounded-r transition">
                                    +
                                </button>
                            </div>
                            <button class="ml-4 px-4 py-2 flex gap-2 items-center bg-pistachio text-white font-semibold rounded hover:bg-pistachio-dark transition">
                                <span>AÑADIR</span>
                                <img
                                    src="${pageContext.request.contextPath}/images/shopping-cart-white.svg"
                                    class="h-5"
                                    alt="Administracion"
                                    onclick="window.location.href='/admin/home'"
                                />
                            </button>
                        </div>
                    </div>
                </div>
            </div>

			<!-- Sección de Productos Destacados -->
			<section class="container mx-0 lg:mx-auto px-4 py-8 mb-4">
				<h2 class="text-2xl font-bold text-gray-800 text-start mb-4">Productos relacionados</h2>
				<div class="w-full h-90 flex items-center space-x-2">
					<button id="product-carousel-prev-relacionados" class="p-1 bg-white hover:bg-gray-100 text-gray-700 rounded-full">
						<svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
							<path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
						</svg>
					</button>

					<div class="swiper multiple-slide-carousel-relacionados swiper-container h-full w-full relative">
						<div class="swiper-wrapper">
							<c:forEach var="producto" items="${destacados}">
								<div class="swiper-slide flex justify-center">
									<!-- tarjeta de producto idéntica a la anterior, ya dinámica -->
                                    <div class="product-card relative flex flex-col overflow-hidden p-2 border border-gray-200 rounded-lg bg-white w-64! h-full"
                                        data-product='{"id": "${producto.id}", "stock": ${producto.stock}}'>

                                        <button class="wishlist-btn absolute top-2 right-2 text-gray-400 hover:text-red-500 z-10" data-id="${producto.id}">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                                            </svg>
                                        </button>

                                        <div class="relative mb-4">
                                            <c:if test="${producto.discount > 0}">
                                                <span class="absolute top-0 left-0 bg-red-600 text-white text-sm font-bold px-2 py-1 rounded-md z-10">
                                                    -<fmt:formatNumber value="${producto.discount}" maxFractionDigits="0" />%
                                                </span>
                                            </c:if>

                                            <c:choose>
                                                <c:when test="${not empty producto.imagenPath}">
                                                    <img src="${pageContext.request.contextPath}${producto.imagenPath}"
                                                        alt="${producto.nombre}"
                                                        class="h-40 w-full object-cover rounded"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="h-40 w-full bg-gray-200 flex items-center justify-center rounded">
                                                        <svg xmlns="http://www.w3.org/2000/svg" class="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7h4l2-3h6l2 3h4v13H3V7z"/>
                                                            <circle cx="12" cy="13" r="4" stroke="currentColor" stroke-width="2"/>
                                                        </svg>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <h3 onclick="window.location.href='${pageContext.request.contextPath}/product/${producto.id}'"
                                            class="text-sm font-medium mb-2 cursor-pointer hover:text-pistachio">
                                            ${producto.nombre}
                                        </h3>

                                        <div class="flex items-center space-x-1 mb-2">
                                            <c:forEach var="i" begin="1" end="5">
                                                <svg class="w-4 h-4 ${i <= producto.rating ? 'text-yellow-400' : 'text-gray-300'}" fill="currentColor" viewBox="0 0 20 20">
                                                    <path fill-rule="evenodd" d="M10 15l-5.09 2.676 0.974-5.678L1.364 8.324l5.72-0.832L10 2l2.916 5.492 5.72 0.832-4.52 3.674 0.974 5.678z" clip-rule="evenodd"/>
                                                </svg>
                                            </c:forEach>
                                            <span class="text-xs text-gray-500">(${producto.ratingCount})</span>
                                        </div>

                                        <span class="text-xs font-semibold mb-4 ${producto.stock > 0 ? 'text-green-600' : 'text-red-600'}">
                                            ${producto.stock > 0 ? 'Disponible' : 'No disponible'}
                                        </span>

                                        <div class="mt-auto flex justify-between items-end">
                                            <div class="flex flex-col">
                                                <c:if test="${producto.discount > 0}">
                                                    <span class="text-sm text-gray-400 line-through">
                                                        <fmt:formatNumber value="${producto.price}" type="currency" currencySymbol="€ "/>
                                                    </span>
                                                    <span class="text-lg font-bold text-gray-800">
                                                        <fmt:formatNumber value="${producto.price * (1 - producto.discount / 100)}" type="currency" currencySymbol="€ " minFractionDigits="2" maxFractionDigits="2"/>
                                                    </span>
                                                </c:if>
                                                <c:if test="${producto.discount == 0 || producto.discount == null}">
                                                    <span class="text-lg font-bold text-gray-800">
                                                        <fmt:formatNumber value="${producto.price}" type="currency" currencySymbol="€ "/>
                                                    </span>
                                                </c:if>
                                            </div>
                                            <div class="cart-controls-container"></div>
                                        </div>
                                    </div>
								</div>
							</c:forEach>
						</div>
					</div>

					<button id="product-carousel-next-relacionados" class="p-1 bg-white hover:bg-gray-100 text-gray-700 rounded-full">
						<svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
							<path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
						</svg>
					</button>
				</div>
			</section>

            <!-- Sección de Productos Destacados -->
            <section class="container mx-0 lg:mx-auto px-4 py-8 mb-4">
                <h2 class="text-2xl font-bold text-gray-800 text-start mb-4">Productos relacionados</h2>
                <div class="w-full h-90 flex items-center space-x-2">
                    <button id="product-carousel-prev-destacados" class="p-1 bg-white hover:bg-gray-100 text-gray-700 rounded-full">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7"/>
                        </svg>
                    </button>

                    <div class="swiper multiple-slide-carousel-destacados swiper-container h-full w-full relative">
                        <div class="swiper-wrapper">
                            <c:forEach var="producto" items="${destacados}">
                                <div class="swiper-slide flex justify-center">
                                    <!-- tarjeta de producto idéntica a la anterior, ya dinámica -->
                                    <div class="product-card relative flex flex-col overflow-hidden p-2 border border-gray-200 rounded-lg bg-white w-64! h-full"
                                        data-product='{"id": "${producto.id}", "stock": ${producto.stock}}'>

                                        <button class="wishlist-btn absolute top-2 right-2 text-gray-400 hover:text-red-500 z-10" data-id="${producto.id}">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/>
                                            </svg>
                                        </button>

                                        <div class="relative mb-4">
                                            <c:if test="${producto.discount > 0}">
                                                <span class="absolute top-0 left-0 bg-red-600 text-white text-sm font-bold px-2 py-1 rounded-md z-10">
                                                    -<fmt:formatNumber value="${producto.discount}" maxFractionDigits="0" />%
                                                </span>
                                            </c:if>

                                            <c:choose>
                                                <c:when test="${not empty producto.imagenPath}">
                                                    <img src="${pageContext.request.contextPath}${producto.imagenPath}"
                                                        alt="${producto.nombre}"
                                                        class="h-40 w-full object-cover rounded"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <div class="h-40 w-full bg-gray-200 flex items-center justify-center rounded">
                                                        <svg xmlns="http://www.w3.org/2000/svg" class="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7h4l2-3h6l2 3h4v13H3V7z"/>
                                                            <circle cx="12" cy="13" r="4" stroke="currentColor" stroke-width="2"/>
                                                        </svg>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>

                                        <h3 onclick="window.location.href='${pageContext.request.contextPath}/product/${producto.id}'"
                                            class="text-sm font-medium mb-2 cursor-pointer hover:text-pistachio">
                                            ${producto.nombre}
                                        </h3>

                                        <div class="flex items-center space-x-1 mb-2">
                                            <c:forEach var="i" begin="1" end="5">
                                                <svg class="w-4 h-4 ${i <= producto.rating ? 'text-yellow-400' : 'text-gray-300'}" fill="currentColor" viewBox="0 0 20 20">
                                                    <path fill-rule="evenodd" d="M10 15l-5.09 2.676 0.974-5.678L1.364 8.324l5.72-0.832L10 2l2.916 5.492 5.72 0.832-4.52 3.674 0.974 5.678z" clip-rule="evenodd"/>
                                                </svg>
                                            </c:forEach>
                                            <span class="text-xs text-gray-500">(${producto.ratingCount})</span>
                                        </div>

                                        <span class="text-xs font-semibold mb-4 ${producto.stock > 0 ? 'text-green-600' : 'text-red-600'}">
                                            ${producto.stock > 0 ? 'Disponible' : 'No disponible'}
                                        </span>

                                        <div class="mt-auto flex justify-between items-end">
                                            <div class="flex flex-col">
                                                <c:if test="${producto.discount > 0}">
                                                    <span class="text-sm text-gray-400 line-through">
                                                        <fmt:formatNumber value="${producto.price}" type="currency" currencySymbol="€ "/>
                                                    </span>
                                                    <span class="text-lg font-bold text-gray-800">
                                                        <fmt:formatNumber value="${producto.price * (1 - producto.discount / 100)}" type="currency" currencySymbol="€ " minFractionDigits="2" maxFractionDigits="2"/>
                                                    </span>
                                                </c:if>
                                                <c:if test="${producto.discount == 0 || producto.discount == null}">
                                                    <span class="text-lg font-bold text-gray-800">
                                                        <fmt:formatNumber value="${producto.price}" type="currency" currencySymbol="€ "/>
                                                    </span>
                                                </c:if>
                                            </div>
                                            <div class="cart-controls-container"></div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                    <button id="product-carousel-next-destacados" class="p-1 bg-white hover:bg-gray-100 text-gray-700 rounded-full">
                        <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7"/>
                        </svg>
                    </button>
                </div>
            </section>

		</div>
	</main>

	<%@ include file="../includes/footer.jsp" %>

	<script>
		new Swiper('.multiple-slide-carousel-relacionados', {
			loop: true,
			navigation: {
				prevEl: '#product-carousel-prev-relacionados',
				nextEl: '#product-carousel-next-relacionados',
			},
			breakpoints: {
				0:    { slidesPerView: 2, spaceBetween: 8 },
				700:  { slidesPerView: 3, spaceBetween: 12 },
				900:  { slidesPerView: 4, spaceBetween: 16 },
				1280: { slidesPerView: 5, spaceBetween: 20 },
			},
		});

		new Swiper('.multiple-slide-carousel-destacados', {
			loop: true,
			navigation: {
				prevEl: '#product-carousel-prev-destacados',
				nextEl: '#product-carousel-next-destacados',
			},
			breakpoints: {
				0:    { slidesPerView: 2, spaceBetween: 8 },
				700:  { slidesPerView: 3, spaceBetween: 12 },
				900:  { slidesPerView: 4, spaceBetween: 16 },
				1280: { slidesPerView: 5, spaceBetween: 20 },
			},
		});
	</script>
</html>
