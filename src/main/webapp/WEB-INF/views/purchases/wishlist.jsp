<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Mis favoritos</title>
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

    <main class="flex-grow bg-gray-50 py-10">
        <div class="container mx-auto px-4">
            <h1 class="text-3xl font-bold mb-6 text-left text-gray-800">Mi Lista de Deseos</h1>
            <div class="flex">
                <div class="w-full bg-white border border-gray-200 rounded-lg overflow-hidden">
                    <table class="min-w-full text-gray-700 text-sm">
                        <c:if test="${not empty likedProducts}">
                            <thead class="bg-white">
                                <tr class="border-b border-gray-200">
                                    <th class="py-3 px-4 text-left">Producto</th>
                                    <th class="py-3 px-4 text-center">Precio</th>
                                    <th class="py-3 px-4 text-center">Acciones</th>
                                </tr>
                            </thead>
                        </c:if>

                        <tbody class="divide-y divide-gray-100">
                            <c:choose>
                                <c:when test="${not empty likedProducts}">
                                    <c:forEach var="producto" items="${likedProducts}">
                                        <tr class="wishlist-item-row" data-product-id="${producto.id}">
                                            <td class="py-4 px-4">
                                                <div class="flex items-center">
                                                    <img src="${pageContext.request.contextPath}${producto.imagenPath}" alt="${producto.nombre}" class="h-16 w-16 object-cover rounded mr-4" />
                                                    <div>
                                                        <p class="font-semibold">${producto.nombre}</p>
                                                        <c:if test="${not empty producto.laboratorio}">
                                                          <p class="text-xs text-gray-500">${producto.laboratorio.nombre}</p>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </td>
                                            
                                            <td class="py-4 px-4 text-center font-semibold">
                                                 <c:choose>
                                                    <c:when test="${producto.discount > 0}">
                                                        <div class="flex flex-col items-center">
                                                            <span class="text-xs text-gray-400 line-through"><fmt:formatNumber value="${producto.price}" type="currency" currencySymbol="€"/></span>
                                                            <span class="text-red-600"><fmt:formatNumber value="${producto.price * (1 - producto.discount / 100)}" type="currency" currencySymbol="€"/></span>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${producto.price}" type="currency" currencySymbol="€"/>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            
                                            <td class="py-4 px-4 text-center">
                                                <div class="flex justify-center items-center gap-3">
                                                    <div class="cart-button-container"></div>
                                                    <div class="wishlist-button-container"></div>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="3" class="text-center p-12">
                                            <p class="text-gray-600 text-2xl font-bold mb-4">Tu lista de deseos está vacía.</p>
                                            <a href="${pageContext.request.contextPath}/" class="inline-block bg-pistachio text-white px-6 py-2 rounded hover:bg-dark-pistachio">
                                                Descubrir Productos
                                            </a>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>
    
    <%@ include file="../includes/footer.jsp" %>
    
    <script src="${pageContext.request.contextPath}/js/purchases/wishlist.js" defer></script>
</body>
</html>