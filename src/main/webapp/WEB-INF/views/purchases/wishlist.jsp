<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "La Botica de Mar";
%>
<!DOCTYPE html>
<html lang="es">

    <%@ include file="../includes/head.jsp" %>
    <%@ include file="../includes/navbar.jsp" %>
    
    <div class="flex-grow">
        <div class="container mx-auto my-10 flex-grow">
            <h1 class="text-3xl font-bold mb-6 text-center text-gray-800">Tus Artículos Favoritos</h1>

            <!-- Lista de artículos que te gustan -->
            <div class="bg-white shadow-md rounded-lg p-6">
                <c:if test="${not empty likedProducts}">
                    <table class="min-w-full bg-white">
                        <thead>
                            <tr>
                                <th class="py-2 px-4 text-left">Producto</th>
                                <th class="py-2 px-4 text-left">Precio</th>
                                <th class="py-2 px-4 text-left"></th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Repite este bloque por cada producto en la lista de productos que te gustan -->
                            <c:forEach var="producto" items="${likedProducts}">
                                <tr>
                                    <td class="py-4 px-4 border-b">
                                        <div class="flex items-center">
                                            <img src="${producto.imagen}" alt="${producto.nombre}" class="h-12 w-12 mr-4">
                                            <span>${producto.nombre}</span>
                                        </div>
                                    </td>
                                    <td class="py-4 px-4 border-b">${producto.precio} €</td>
                                    <td class="py-4 px-4 border-b flex space-x-4">
                                        <!-- Añadir al carrito -->
                                        <form action="addToCart" method="post">
                                            <input type="hidden" name="productoId" value="${producto.id}">
                                            <button type="submit" class="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-700">
                                                Añadir al Carrito
                                            </button>
                                        </form>

                                        <!-- Eliminar de favoritos -->
                                        <form action="removeFromLiked" method="post">
                                            <input type="hidden" name="productoId" value="${producto.id}">
                                            <button type="submit" class="bg-red-500 text-white py-2 px-4 rounded-lg hover:bg-red-700">
                                                Eliminar
                                            </button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:if>

                <!-- Mensaje si no hay productos en la lista de favoritos -->
                <c:if test="${empty likedProducts}">
                    <p class="text-center text-gray-600">No tienes artículos en tu lista de favoritos.</p>
                </c:if>
            </div>
        </div>
    </div>
    
    <!-- Incluir el footer -->
    <%@ include file="../includes/footer.jsp" %>

</html>
