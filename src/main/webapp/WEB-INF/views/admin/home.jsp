<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Administrador</title>
        <link rel="icon" href="/images/icono_tab2.png" type="image/png">

        <link rel="stylesheet" href="/css/output.css">
        <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" rel="stylesheet"/>
        <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>
    </head>
    
    <body class="flex flex-col min-h-screen">

        <header>
            <%@ include file="../includes/navbar.jsp" %>
        </header>
        
        <div id="modalOverlay" class="fixed top-0 left-0 w-screen h-screen bg-black bg-opacity-50 hidden z-[9999]"></div>
    
        <script src="${pageContext.request.contextPath}/js/admin/home.js" defer></script>
        
        <main class="flex-grow bg-white">
            <div class="container mx-auto p-4">
                <div class="w-full overflow-x-auto">
                    <nav id="section-nav"
                         class="flex w-max space-x-6 px-4 py-3 border-b-2 border-gray-300 mx-auto min-w-fit">
                        <a href="/admin/ventas" class="text-xl text-gray-500 transition-colors whitespace-nowrap" data-section="Ventas">Ventas</a>
                        <a href="/admin/devoluciones" class="text-xl text-gray-500 transition-colors whitespace-nowrap" data-section="Devoluciones">Devoluciones</a>
                        <a href="/admin/products" class="text-xl text-gray-500 transition-colors whitespace-nowrap" data-section="Productos">Productos</a>
                        <a href="/admin/empleados" class="text-xl text-gray-500 transition-colors whitespace-nowrap" data-section="Empleados">Empleados</a>
                        <a href="/admin/usuarios" class="text-xl text-gray-500 transition-colors whitespace-nowrap" data-section="Usuarios">Usuarios</a>
                    </nav>
                </div>

                <div id="content-area" class="p-4"></div>

            </div>
        </main>
        
        <footer class="bg-gray-800 text-white py-4">
            <div class="container mx-auto text-center">
                <%@ include file="../includes/footer.jsp" %>
            </div>
        </footer>

    </body>
</html>