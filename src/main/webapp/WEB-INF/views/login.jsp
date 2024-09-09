<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Iniciar sesión - [Nombre de la tienda online]</title>
<!-- AJAX -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://unpkg.com/jspdf@latest/dist/jspdf.umd.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.3.4/jspdf.min.js"></script>
<!-- JAVASCRIPT -->
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.5.3/jspdf.min.js"></script>
<script type="text/javascript" src="https://html2canvas.hertzen.com/dist/html2canvas.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/1.5.3/jspdf.min.js"></script>
<script type="text/javascript" src="https://html2canvas.hertzen.com/dist/html2canvas.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
<script>
function login(event) {
    event.preventDefault(); // Prevenir la recarga de la página

    var miForm = document.querySelector('form');
    const formData = new FormData(miForm);

    // Convertir FormData a un objeto JSON
    const formObject = {};
    formData.forEach((value, key) => {
        formObject[key] = value;
    });

    $.ajax({
        url: 'http://localhost:8080/auth/authenticate', 
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify(formObject),
        success: function(response) {
            if (response !== null && response !== "") {
                window.location.href = "http://localhost:8080/home/welcome";
            } else {
                alert('Error: Autenticación fallida.');
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert('Error: Autenticación fallida. Verifica tus credenciales.');
            console.log(errorThrown);
        }
    });
}
</script>


<header>
</header>

<main>
    <h1>Iniciar sesión</h1>
    <form id="user_pwd">
        <label>Correo electrónico:</label>
        <input type="correo" id="correo" name="correo" required>
        <label>Contraseña:</label>
        <input type="password" id="password" name="password" required>
        <button onclick="login(event)">Iniciar sesión</button>
    </form>

    <!-- Aquí insertamos el select que muestra los usuarios -->
    <select name="usuarioSeleccionado">
        <c:forEach var="usuario" items="${usuarios}">
            <option value="${usuario.id}">${usuario.nombre}</option>
        </c:forEach>
    </select>

    <p><a href="recuperar-contrasena.jsp">¿Olvidaste tu contraseña?</a></p>
    <p><a href="registro.jsp">Crear una nueva cuenta</a></p>
</main>

<footer>
    <p>Contacto: [Información de contacto]</p>
    <p><a href="#">Redes sociales</a></p>
    <p><a href="#">Aviso legal</a></p>
</footer>

</body>
</html>
