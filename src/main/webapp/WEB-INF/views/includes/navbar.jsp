<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="bg-white shadow-md">
    <div class="container mx-auto px-4 py-4 flex justify-between items-center">
        <a href="#" class="text-2xl font-bold text-gray-800">La botica de Mar</a>
        <div>
            <a href="${pageContext.request.contextPath}/index.jsp" class="text-gray-800 hover:text-gray-600 px-3">Inicio</a>
            <a href="${pageContext.request.contextPath}/productos.jsp" class="text-gray-800 hover:text-gray-600 px-3">Productos</a>
            <a href="${pageContext.request.contextPath}/contacto.jsp" class="text-gray-800 hover:text-gray-600 px-3">Contacto</a>
        </div>
        <div style="display: flex">
            <img src="${pageContext.request.contextPath}/images/heart.svg" class="size-16" alt="Mi carrito" style="width: 24px;">
            <img src="${pageContext.request.contextPath}/images/shopping-cart.svg" class="size-16" alt="Mi carrito" style="width: 24px; margin-left: 14px;">
            <img src="${pageContext.request.contextPath}/images/user-circle.svg" class="size-16" alt="Mi carrito" style="width: 24px; margin-left: 14px;">
        </div>
    </div>
</nav>
]