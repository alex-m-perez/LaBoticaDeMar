<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Iniciar sesión - [Nombre de la tienda online]</title>
  <link rel="stylesheet" href="css/estilos.css">
</head>
<body>
  <header>
    <a href="index.jsp"><img src="imagenes/logo.png" alt="Logo de la tienda online"></a>
  </header>
  <main>
    <h1>Iniciar sesión</h1>
    <form action="login.do" method="post">
      <label for="email">Correo electrónico:</label>
      <input type="email" id="email" name="email" required>
      <label for="password">Contraseña:</label>
      <input type="password" id="password" name="password" required>
      <input type="submit" value="Iniciar sesión">
    </form>
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