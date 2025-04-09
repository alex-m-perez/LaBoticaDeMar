<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String pageTitle = "La Botica de Mar";
%>
<!DOCTYPE html>
<html lang="es">
    <%@ include file="/WEB-INF/views/includes/head.jsp" %>
    <%@ include file="/WEB-INF/views/includes/navbar.jsp" %>

    <main class="flex-grow bg-white">
        <section class="container mx-0 lg:mx-auto px-4 py-8">
            <!-- Contenedor Principal -->
            <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                <!-- Columna Izquierda: Imagen del Producto -->
                <div class="rounded-lg p-4">
                    <h1 class="text-2xl font-bold mb-2">${producto.nombre}</h1>
                    <div class="flex items-center mb-4">
                        <!-- Valoración -->
                        <span class="text-yellow-400">
                            <c:forEach var="i" begin="1" end="5">
                                <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" viewBox="0 0 24 24" fill="${i <= producto.rating ? '#FFD700' : '#D3D3D3'}">
                                    <path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/>
                                </svg>
                            </c:forEach>
                        </span>
                        <span class="ml-2">(${producto.ratingCount})</span>
                    </div>
                    <!-- Imagen Principal -->
                    <img src="${producto.imagenPath != null ? producto.imagenPath : 'https://via.placeholder.com/400x400'}" 
                        alt="Producto" class="w-full rounded-lg shadow-md">
                    <!-- Carousel de Imágenes -->
                    <div class="flex justify-center mt-4 space-x-2">
                        <button class="p-2 border rounded">&#9664;</button>
                        <div class="w-12 h-12 border rounded"></div>
                        <div class="w-12 h-12 border rounded"></div>
                        <div class="w-12 h-12 border rounded"></div>
                        <button class="p-2 border rounded">&#9654;</button>
                    </div>
                </div>

                <!-- Columna Derecha: Precio y Acciones -->
                <div class="col-span-2 rounded-lg p-4">
                    <!-- Precio y Stock -->
                    <div class="flex justify-between items-center mb-4">
                        <div>
                            <span class="text-3xl font-bold">${producto.price} €</span>
                            <span class="block text-green-600">
                                ${producto.stock > 0 ? 'En stock' : 'Agotado'}
                            </span>
                        </div>
                        <!-- Selector de Cantidad -->
                        <div class="flex items-center space-x-2">
                            <button class="px-4 py-2 bg-gray-300 rounded">-</button>
                            <input type="number" value="1" class="w-12 text-center border">
                            <button class="px-4 py-2 bg-gray-300 rounded">+</button>
                        </div>
                    </div>
                    <!-- Métodos de Pago -->
                    <div class="mb-6">
                        <h2 class="text-lg font-semibold mb-2">Métodos de pago:</h2>
                        <div class="flex space-x-4">
                            <div class="w-10 h-10 bg-gray-200 rounded"></div>
                            <div class="w-10 h-10 bg-gray-200 rounded"></div>
                            <div class="w-10 h-10 bg-gray-200 rounded"></div>
                            <div class="w-10 h-10 bg-gray-200 rounded"></div>
                        </div>
                    </div>
                    <!-- Botones de Acción -->
                    <div class="space-y-4">
                        <button class="w-full py-3 bg-blue-600 text-white rounded">Añadir al carrito</button>
                        <button class="w-full py-3 bg-green-600 text-white rounded">Comprar ahora</button>
                    </div>
                </div>
            </div>

            <!-- Artículos Similares -->
            <div class="mt-10 rounded-lg p-4">
                <h2 class="text-2xl font-bold mb-4">Artículos similares:</h2>
                <div class="flex items-center">
                    <button class="p-2 border rounded">&#9664;</button>
                    <!-- Productos -->
                    <div class="flex space-x-4 overflow-x-auto">
                        <c:forEach var="productoSimilar" items="${productosSimilares}">
                            <div class="w-40 h-40 bg-gray-200 rounded"></div>
                        </c:forEach>
                    </div>
                    <button class="p-2 border rounded">&#9654;</button>
                </div>
            </div>
        </section>
    </main>

    <%@ include file="/WEB-INF/views/includes/footer.jsp" %>
</html>
