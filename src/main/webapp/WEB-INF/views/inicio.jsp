<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>Lista de usuarios</title>
</head>
<body>
    <h1>Lista de usuarios</h1>
    
    <ul style="color: black;">
        <c:forEach items="${usuarios}" var="usuario">
            <li>
                <span>${usuario.nombre}</span>
            </li>
        </c:forEach>
        <li>
            ${prueba}
        </li>
        <li>
            ${usuario1.nombre}
        </li>
        <li>
            ${usuario2.nombre}
        </li>
        <li>
            ${usuario3.nombre}
        </li>
        <li>
            ${usuario4.nombre}
        </li>
    </ul>
</body>
</html>
