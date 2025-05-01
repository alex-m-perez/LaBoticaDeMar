<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "La Botica de Mar";
%>
<!DOCTYPE html>
<html lang="es">
    <%@ include file="../includes/head.jsp" %>
    <%@ include file="../includes/navbar.jsp" %>

    <script src="${pageContext.request.contextPath}/js/admin/home.js" defer></script>
    
    <main class="flex-grow bg-white">

        <!-- Barra de navegación -->
        <div class="w-full border-b border-gray-200">
            <nav id="section-nav" class="flex space-x-6 px-4 py-3">
                <a href="#" class="text-gray-500 cursor-pointer transition-colors" data-section="Ventas">Ventas</a>
                <a href="#" class="text-gray-500 cursor-pointer transition-colors" data-section="Devoluciones">Devoluciones</a>
                <a href="#" class="text-gray-500 cursor-pointer transition-colors" data-section="Productos">Productos</a>
                <a href="#" class="text-gray-500 cursor-pointer transition-colors" data-section="Ofertas">Ofertas</a>
                <a href="#" class="text-gray-500 cursor-pointer transition-colors" data-section="Empleados">Empleados</a>
                <a href="#" class="text-gray-500 cursor-pointer transition-colors" data-section="Usuarios">Usuarios</a>
            </nav>
        </div>

        <!-- Contenido dinámico -->
        <div id="content-area" class="p-4"></div>

    </main>
    
    <%@ include file="../includes/footer.jsp" %>
</html>
