<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "La Botica de Mar";
%>
<!DOCTYPE html>
<html lang="es">

    <!-- Incluir el head -->
    <%@ include file="includes/head.jsp" %>

    <!-- Incluir el navbar -->
    <%@ include file="includes/navbar.jsp" %>
    
    <div class="flex-grow">
        <div class="container mx-auto my-10 flex-grow">
        <h1 class="text-3xl font-bold mb-6 text-center text-gray-800">Tu Carrito de Compras</h1>

        <!-- Tabla de productos -->
        <div class="bg-white shadow-md rounded-lg p-6">
            <table class="min-w-full bg-white">
                <thead>
                    <tr>
                        <th class="py-2 px-4 text-left">Producto</th>
                        <th class="py-2 px-4 text-left">Cantidad</th>
                        <th class="py-2 px-4 text-left">Precio</th>
                        <th class="py-2 px-4 text-left">Total</th>
                        <th class="py-2 px-4 text-left"></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="producto" items="${carrito}">
                        <tr>
                            <td class="py-4 px-4 border-b">
                                <div class="flex items-center">
                                    <img src="${producto.imagen}" alt="${producto.nombre}" class="h-12 w-12 mr-4">
                                    <span>${producto.nombre}</span>
                                </div>
                            </td>
                            <td class="py-4 px-4 border-b">
                                <input type="number" value="${producto.cantidad}" class="w-16 py-1 px-2 border rounded-lg text-center">
                            </td>
                            <td class="py-4 px-4 border-b">${producto.precio} €</td>
                            <td class="py-4 px-4 border-b">${producto.precio * producto.cantidad} €</td>
                            <td class="py-4 px-4 border-b">
                                <a href="eliminarProducto?productoId=${producto.id}" class="text-red-600 hover:text-red-800">Eliminar</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <!-- Resumen del carrito -->
        <div class="flex justify-end mt-6">
            <div class="w-1/3 bg-white p-6 rounded-lg shadow-md">
                <h2 class="text-2xl font-bold mb-4 text-gray-800">Resumen del Pedido</h2>
                <div class="flex justify-between mb-2">
                    <span class="text-gray-700">Subtotal</span>
                    <span class="text-gray-700">${subtotal} €</span>
                </div>
                <div class="flex justify-between mb-2">
                    <span class="text-gray-700">Envío</span>
                    <span class="text-gray-700">${envio} €</span>
                </div>
                <div class="flex justify-between font-bold text-xl mb-4">
                    <span>Total</span>
                    <span>${total} €</span>
                </div>
                <a href="checkout.jsp" class="bg-blue-500 text-white py-2 px-4 rounded-lg hover:bg-blue-700 w-full text-center block">
                    Proceder al Pago
                </a>
            </div>
        </div>
    </div>

    </div>
    
    <!-- Incluir el footer -->
    <%@ include file="includes/footer.jsp" %>

</html>
