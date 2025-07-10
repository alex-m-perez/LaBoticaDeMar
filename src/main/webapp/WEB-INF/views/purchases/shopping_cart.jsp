<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Mi carrito</title>
        <link rel="icon" href="/images/icono_tab2.png" type="image/png">

        <link rel="stylesheet" href="/css/output.css"/>
        <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css"/>
        <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
    </head>

    <script>
        window.contextPath = '<%= request.getContextPath() %>';
    </script>

    <%-- El body ya tenía las clases correctas, lo cual es perfecto --%>
    <body class="bg-gray-100 min-h-screen flex flex-col" data-authenticated="${not empty pageContext.request.userPrincipal}">

        <header>
            <%@ include file="../includes/navbar.jsp" %>
        </header>

    <main class="flex-grow bg-gray-50 py-10">
        <div class="container mx-auto px-4">
        <h1 class="text-3xl font-bold mb-6 text-left text-gray-800">Tu Cesta de la Compra</h1> 
            <%-- 1. CÁLCULOS INICIALES --%>
            <c:set var="totalSavings" value="${0}" />
            <c:set var="totalItemsPrice" value="${0}" />

            <c:forEach var="item" items="${shoppingCart.items}">
                <%-- Acumula el precio total de los productos (con descuento aplicado) --%>
                <c:set var="effectivePrice" value="${item.producto.price * (1 - item.producto.discount / 100)}" />
                <c:set var="totalItemsPrice" value="${totalItemsPrice + (effectivePrice * item.cantidad)}" />

                <%-- Acumula el ahorro total --%>
                <c:if test="${item.producto.discount > 0}">
                    <c:set var="itemSavings" value="${(item.producto.price * (item.producto.discount / 100)) * item.cantidad}" />
                    <c:set var="totalSavings" value="${totalSavings + itemSavings}" />
                </c:if>
            </c:forEach>

            <%-- Calcula los puntos a generar (total del pedido sin envío * 5, redondeado hacia abajo) --%>
            <c:set var="pointsToGenerate" value="${totalItemsPrice * 5}" />

            <div class="flex flex-col lg:flex-row gap-8 lg:items-start">
                <div class="lg:w-2/3">
                    <%-- Solo muestra el banner si hay productos en el carrito --%>
                    <div id="savings-banner" class="bg-super-light-pistachio text-green-800 rounded-lg border p-1 mb-2 text-center ${empty shoppingCart.items and empty guestCart ? 'hidden' : ''}">
                        ...
                    </div>

                    <div class=" bg-white border border-gray-200 rounded-lg overflow-hidden">
                        <table class="min-w-full text-gray-700 text-sm">
                            <c:if test="${not empty shoppingCart.items}">
                                <thead class="bg-white">
                                    <tr class="border-b border-gray-200">
                                        <th class="py-3 px-4 text-left">Producto</th>
                                        <th class="py-3 px-4 text-center">Cantidad</th>
                                        <th class="py-3 px-4 text-center">Precio</th>
                                        <th class="py-3 px-4 text-center">Total</th>
                                    </tr>
                                </thead>
                            </c:if>

                            <tbody id="cart-items-body" class="divide-y divide-gray-100">
                                <c:choose>
                                    <%-- CASO 1: El carrito tiene productos --%>
                                    <c:when test="${not empty shoppingCart.items}">
                                        <c:forEach var="item" items="${shoppingCart.items}">
                                            <tr class="cart-item-row" 
                                                data-price="${item.producto.price}" 
                                                data-product-id="${item.producto.id}"
                                                data-discount="${item.producto.discount}"
                                                data-stock="${item.producto.stock}">

                                                <td class="py-4 px-4">
                                                    <div class="flex items-center">
                                                        <c:choose>
                                                            <%-- Si el producto tiene datos de imagen... --%>
                                                            <c:when test="${not empty item.producto.imagenData}">
                                                                <%-- ...mostramos la imagen llamando a nuestra API --%>
                                                                <img src="<c:url value='/api/images/${item.producto.id}'/>"
                                                                     alt="${item.producto.nombre}"
                                                                     class="h-16 w-16 object-contain rounded mr-4" />
                                                            </c:when>
                                                            <%-- Si no tiene imagen... --%>
                                                            <c:otherwise>
                                                                <%-- ...mostramos un placeholder --%>
                                                                <div class="h-16 w-16 bg-gray-200 flex items-center justify-center rounded mr-4">
                                                                    <svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7h4l2-3h6l2 3h4v13H3V7z"/>
                                                                        <circle cx="12" cy="13" r="4" stroke="currentColor" stroke-width="2"/>
                                                                    </svg>
                                                                </div>
                                                            </c:otherwise>
                                                        </c:choose>
                                                        <div class="flex-grow">
                                                            <p class="font-semibold">${item.producto.nombre}</p>
                                                            <p class="text-xs text-gray-500">${item.producto.laboratorio.nombre}</p>
                                                        </div>
                                                        <button type="button" class="delete-btn p-2 ml-4 hover:bg-red-100 rounded">
                                                            <img src="${pageContext.request.contextPath}/images/bin.svg" alt="Eliminar" class="h-5 w-5"/>
                                                        </button>
                                                    </div>
                                                </td>
                                                <td class="py-4 px-4 text-center">
                                                    <div class="quantity-control inline-flex border border-gray-200 rounded overflow-hidden">
                                                        <button type="button" class="minus-btn px-3 py-1 bg-gray-100 hover:bg-gray-200">−</button>
                                                        <input type="number" min="1" value="${item.cantidad}" class="qty-input w-12 text-center no-spinner focus:outline-none" readonly/>
                                                        <button type="button" class="plus-btn px-3 py-1 bg-gray-100 hover:bg-gray-200">+</button>
                                                    </div>
                                                </td>
                                                <td class="py-4 px-4 text-center">
                                                    <c:choose>
                                                        <c:when test="${item.producto.discount > 0}">
                                                            <div class="flex flex-col items-center">
                                                                <span class="text-xs text-gray-400 line-through">
                                                                    <fmt:formatNumber value="${item.producto.price}" type="currency" currencySymbol="€" />
                                                                </span>
                                                                <span class="text-red-600 font-semibold">
                                                                    <fmt:formatNumber value="${item.producto.price * (1 - item.producto.discount / 100)}" type="currency" currencySymbol="€" />
                                                                </span>
                                                            </div>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <fmt:formatNumber value="${item.producto.price}" type="currency" currencySymbol="€" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="py-4 px-4 font-semibold text-center line-total">
                                                    <c:set var="effectivePrice" value="${item.producto.price * (1 - item.producto.discount / 100)}" />
                                                    <fmt:formatNumber value="${effectivePrice * item.cantidad}" type="currency" currencySymbol="€" />
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <%-- CASO 2: El carrito está vacío --%>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="4" class="text-center p-12">
                                                <p class="text-gray-600 text-2xl font-bold mb-4">Tu carrito está vacío.</p>
                                                <a href="${pageContext.request.contextPath}/" class="inline-block bg-pistachio text-white px-6 py-2 rounded hover:bg-dark-pistachio">
                                                    Continuar Comprando
                                                </a>
                                            </td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <%-- Resumen del Pedido --%>
                <div class="lg:w-1/3">
                    <div class="bg-white border border-gray-200 rounded-lg p-6 flex flex-col">
                        <h2 class="text-2xl font-semibold text-gray-800 mb-4">Resumen del Pedido</h2>

                        <%-- Detalles del precio --%>
                        <div id="price-breakdown-container" class="space-y-2 mb-6 text-gray-600 text-sm">
                            <div class="flex justify-between">
                                <span>Subtotal</span>
                                <span id="cart-subtotal">--,-- €</span>
                            </div>
                            <div class="flex justify-between">
                                <span>IVA (21%)</span>
                                <span id="cart-iva">--,-- €</span>
                            </div>
                            <div id="shipping-row" class="flex justify-between">
                                <span>Envío</span>
                                <span id="cart-shipping">--,-- €</span>
                            </div>
                            <div class="flex justify-between pb-2 border-b border-gray-200 font-bold text-base text-gray-800">
                                <span>Total</span>
                                <span id="cart-total">--,-- €</span>
                            </div>
                        </div>

                        <%-- ====================================================================== --%>
                        <%-- NUEVO: Método de Pago y Puntos (Solo para usuarios autenticados) --%>
                        <%-- ====================================================================== --%>
                        <c:if test="${not empty shoppingCart.items}">
                            <sec:authorize access="isAuthenticated()">
                                <div class="mb-6">
                                    <%-- Título de la sección --%>
                                    <form id="payment-form" class="space-y-4">
                                        <%-- Número de Tarjeta --%>
                                        <div>
                                            <label for="cardNumber" class="block text-sm font-medium text-gray-600">Número de Tarjeta</label>
                                            <input type="text" id="cardNumber" placeholder="0000 0000 0000 0000" class="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-pistachio focus:border-pistachio">
                                        </div>

                                        <%-- Fila para Fecha de Caducidad y CVC --%>
                                        <div class="flex items-start space-x-4">
                                            <%-- Fecha de Caducidad --%>
                                            <div class="w-2/3">
                                                <label for="expiryDate" class="block text-sm font-medium text-gray-600">Fecha de Caducidad</label>
                                                <input 
                                                    type="tel" 
                                                    id="expiryDate" 
                                                    placeholder="MM/AAAA" 
                                                    maxlength="7"
                                                    
                                                    class="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm 
                                                        focus:ring-pistachio focus:border-pistachio 
                                                        transition-colors duration-200">
                                                <p id="expiry-error" class="text-red-600 text-xs mt-1 h-4"></p> <%-- Mensaje de error oculto --%>
                                            </div>

                                            <%-- CVC --%>
                                            <div class="w-1/3">
                                                <label for="cvc" class="block text-sm font-medium text-gray-600">CVC</label>
                                                <input type="text" id="cvc" placeholder="123" class="mt-1 w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:ring-pistachio focus:border-pistachio">
                                            </div>
                                        </div>
                                    </form>

                                    <%-- Sistema de Puntos --%>
                                    <div class="flex justify-between items-center mt-6 border-t pt-4">
                                        <div class="flex items-center">
                                            <input id="use-points" name="use-points" data-user-points="${puntos}" type="checkbox" class="h-4 w-4 text-pistachio rounded border-gray-300 focus:ring-pistachio">
                                            <label for="use-points" class="ml-2 block text-sm font-medium text-gray-700">Utilizar mis puntos</label>
                                        </div>
                                        <span class="text-sm font-medium text-gray-800">
                                            Tienes: ${puntos} puntos <%-- Asume que el objeto de usuario tiene una propiedad "puntos" --%>
                                        </span>
                                    </div>
                                </div>
                            </sec:authorize>
                            
                        </c:if>

                        <%-- Botón para Proceder al Pago --%>
                        <div class="mt-auto"> <%-- Se añade 'mt-auto' para empujar el botón siempre al fondo --%>
                            <sec:authorize access="isAuthenticated()">
                                <c:choose>
                                    <c:when test="${not empty shoppingCart.items}">
                                        <button id="checkout" type="button" onclick="procesarPago()" class="w-full block bg-pistachio text-white text-center py-3 rounded-lg hover:bg-dark-pistachio transition cursor-pointer">
                                            <span class="text-xl font-bold">Proceder al Pago</span>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="block bg-gray-300 text-white text-center py-3 rounded-lg cursor-not-allowed pointer-events-none">
                                            <span class="text-xl font-bold">Proceder al Pago</span>
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </sec:authorize>
                            <sec:authorize access="!isAuthenticated()">
                                <a href="/login" class="w-full block bg-pistachio text-white text-center py-3 rounded-lg hover:bg-dark-pistachio transition cursor-pointer">
                                    <span class="text-xl font-bold">Iniciar sesión para realizar compra</span>
                                </a>
                            </sec:authorize>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </main>

    <footer class="bg-gray-800 text-white py-4">
        <div class="container mx-auto text-center">
            <%@ include file="../includes/footer.jsp" %>
        </div>
    </footer>

    <script src="${pageContext.request.contextPath}/js/purchases/shopping_cart.js" defer></script>
</body>
</html>