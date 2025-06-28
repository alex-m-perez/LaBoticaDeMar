<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
    <link rel="icon" href="${pageContext.request.contextPath}/images/icono_tab2.png" type="image/png">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/output.css">
    <link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>
<body class="bg-gray-100 flex flex-col justify-center items-center min-h-screen p-4">

    <div class="text-center">
        <div class="flex items-center justify-center mb-14">
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="Logo" class="h-28 mr-2">
            <a href="${pageContext.request.contextPath}/" class="text-5xl font-bold font-[Satisfy] text-logo-purple">
                La Botica de Mar
            </a>
        </div>

        <div>
            <h1 class="text-6xl font-bold text-pistachio">Esto no estaba en el prospecto...</h1>
            <h2 class="text-2xl font-semibold text-gray-800 mt-4">
                Ha ocurrido un error procesando tu solicitud.
                <br>
                <c:choose>
                    <c:when test="${not empty errorDetails}">
                        ${errorDetails}
                    </c:when>
                    <c:otherwise>
                        Nuestro equipo está trabajando para solucionarlo lo antes posible.
                    </c:otherwise>
                </c:choose>
            </h2>
            
            <a href="${pageContext.request.contextPath}/" class="text-xl mt-20 inline-block bg-pistachio text-white font-bold py-4 px-6 rounded-xl hover:bg-pistachio-dark transition">
                Volver al inicio
            </a>
        </div>
    </div>

</body>
</html>