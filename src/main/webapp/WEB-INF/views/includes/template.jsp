<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<% String pageTitle = "La Botica de Mar";%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title><%= pageTitle != null ? pageTitle : "Tienda Online" %></title>
        <link rel="icon" href="/images/icono_tab2.png" type="image/png">

        <link rel="stylesheet" href="/css/output.css">
        <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/flowbite@2.5.2/dist/flowbite.min.js"></script>
        <link href="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.css" rel="stylesheet"/>
        <script src="https://cdn.jsdelivr.net/npm/swiper@11/swiper-bundle.min.js"></script>

        <script> window.contextPath = '<%= request.getContextPath() %>'; </script>
    </head>

    <body class="flex flex-col min-h-screen">

        <header>
            <%@ include file="../includes/navbar.jsp" %>
        </header>
        
        <main class="flex-grow bg-white">

            </main>
        
        <footer class="bg-gray-800 text-white py-4">
             <%@ include file="../includes/footer.jsp" %>
        </footer>

    </body>
</html>