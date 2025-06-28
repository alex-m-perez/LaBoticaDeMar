<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% String pageTitle = "La Botica de Mar"; %>

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
</head>

<%-- El script ahora se carga con 'defer' para asegurar que el DOM esté listo --%>
<script src="${pageContext.request.contextPath}/js/purchases/shopping_cart.js" defer></script>

<body class="flex flex-col min-h-screen">
    <%@ include file="../includes/navbar.jsp" %>

    <main class="flex-grow bg-gray-50 py-10">
        <div class="container mx-auto px-4">
            <div class="flex flex-col lg:flex-row gap-8">

                <div class="lg:w-2/3 bg-white border border-gray-200 rounded-lg overflow-hidden">
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
                                        <%-- CAMBIO: Añadimos data-discount y data-stock para que JS los use --%>
                                        <tr class="cart-item-row" 
                                            data-price="${item.producto.price}" 
                                            data-product-id="${item.producto.id}"
                                            data-discount="${item.producto.discount}"
                                            data-stock="${item.producto.stock}">

                                            <td class="py-4 px-4">
                                                <div class="flex items-center">
                                                    <img src="${pageContext.request.contextPath}${item.producto.imagenPath}" alt="${item.producto.nombre}" class="h-16 w-16 object-cover rounded mr-4" />
                                                    <div class="flex-grow">
                                                        <p class="font-semibold">${item.producto.nombre}</p>
                                                        <p class="text-xs text-gray-500">${item.producto.laboratorio.nombre}</p>
                                                    </div>
                                                    <button type="button" class="p-2 ml-4 hover:bg-red-100 rounded" onclick="eliminarDelCarrito('${item.producto.id}', this)">
                                                        <img src="${pageContext.request.contextPath}/images/bin.svg" alt="Eliminar" class="h-5 w-5"/>
                                                    </button>
                                                </div>
                                            </td>
                                            <td class="py-4 px-4 text-center">
                                                <div class="quantity-control inline-flex border border-gray-200 rounded overflow-hidden">
                                                    <%-- CAMBIO: La función ahora solo necesita 'this' y el delta (-1 o 1) --%>
                                                    <button type="button" class="minus-btn px-3 py-1 bg-gray-100 hover:bg-gray-200" onclick="updateQuantity(this, -1)">−</button>
                                                    <input type="number" min="1" value="${item.cantidad}" class="qty-input w-12 text-center no-spinner focus:outline-none" readonly/>
                                                    <button type="button" class="plus-btn px-3 py-1 bg-gray-100 hover:bg-gray-200" onclick="updateQuantity(this, 1)">+</button>
                                                </div>
                                            </td>
                                            <td class="py-4 px-4 text-center">
                                                <%-- CAMBIO: Lógica para mostrar precio con descuento --%>
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
                                                 <%-- CAMBIO: El total de línea también considera el descuento --%>
                                                <c:set var="effectivePrice" value="${item.producto.price * (1 - item.producto.discount / 100)}" />
                                                <fmt:formatNumber value="${effectivePrice * item.cantidad}" type="currency" currencySymbol="€" />
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <%-- CASO 2: El carrito está vacío (sin cambios) --%>
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

                <%-- El resto del JSP (Resumen del Pedido y Footer) permanece igual --%>
                <div class="lg:w-1/3">
                    <div class="bg-white border border-gray-200 rounded-lg p-6 flex flex-col">
                        <h2 class="text-2xl font-semibold text-gray-800 mb-4">Resumen del Pedido</h2>
                        <div class="space-y-2 mb-6 text-gray-600 text-sm">
                            <div class="flex justify-between">
                                <span>Subtotal</span>
                                <span id="cart-subtotal">--,-- €</span>
                            </div>
                            <div class="flex justify-between">
                                <span>IVA (21%)</span>
                                <span id="cart-iva">--,-- €</span>
                            </div>
                            <div class="flex justify-between">
                                <span>Envío</span>
                                <span id="cart-shipping">--,-- €</span>
                            </div>
                            <div class="flex justify-between pt-2 border-t border-gray-200 font-bold text-base text-gray-800">
                                <span>Total</span>
                                <span id="cart-total">--,-- €</span>
                            </div>
                        </div>
                        <c:choose>
                            <c:when test="${not empty shoppingCart.items}">
                                <a href="${pageContext.request.contextPath}/checkout" class="mt-auto block bg-pistachio text-white text-center py-3 rounded hover:bg-dark-pistachio transition">
                                    <span class="text-xl font-bold">Proceder al Pago</span>
                                </a>
                            </c:when>
                            <c:otherwise>
                                <a class="mt-auto block bg-gray-300 text-white text-center py-3 rounded cursor-not-allowed pointer-events-none">
                                    <span class="text-xl font-bold">Proceder al Pago</span>
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <%@ include file="../includes/footer.jsp" %>
</body>
</html>