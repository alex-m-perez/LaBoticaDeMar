<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "Inicio - Tienda Online";
%>
<!DOCTYPE html>
<html lang="es">
    <!-- Incluir el head -->
    <%@ include file="includes/head.jsp" %>
<body class="bg-gray-100">
    
    <!-- Incluir el navbar -->
    <%@ include file="includes/navbar.jsp" %>
    
    <!-- Hero Section -->
    <section class="bg-cover bg-center h-96" style="background-image: url('https://example.com/banner.jpg');">
        <div class="flex items-center justify-center h-full bg-black bg-opacity-50">
            <h1 class="text-white text-5xl font-bold">Bienvenido a Nuestra Tienda</h1>
        </div>
    </section>

    <!-- Sección de Productos Destacados -->
    <section class="container mx-auto px-4 py-12">
        <h2 class="text-3xl font-semibold text-gray-800 text-center mb-8">Productos Destacados</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            <%-- Listado dinámico de productos --%>
            <%
                String[] productos = {"Producto 1", "Producto 2", "Producto 3", "Producto 4"};
                for (int i = 0; i < productos.length; i++) {
            %>
            <div class="bg-white shadow-md rounded-lg overflow-hidden">
                <img src="https://via.placeholder.com/300x200" alt="Producto" class="w-full h-48 object-cover">
                <div class="p-4">
                    <h3 class="text-lg font-semibold text-gray-800"><%= productos[i] %></h3>
                    <p class="text-gray-600 mt-2">Descripción breve del producto.</p>
                    <div class="mt-4">
                        <span class="text-gray-800 font-bold">$99.99</span>
                        <a href="#" class="text-indigo-600 hover:text-indigo-400 ml-4">Agregar al carrito</a>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </section>

    <!-- Incluir el footer -->
    <%@ include file="includes/footer.jsp" %>

</body>
</html>
