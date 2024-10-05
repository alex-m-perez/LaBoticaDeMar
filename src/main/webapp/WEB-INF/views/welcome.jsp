<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% String pageTitle = "La Botica de Mar"; %>
<!DOCTYPE html>




<html lang="es">
    <!-- Incluir el head -->
    <%@ include file="includes/head.jsp" %>
    
    <!-- Incluir el navbar -->
    <%@ include file="includes/navbar.jsp" %>
    
    <!-- Carrusel de imágenes -->
    <section id="carouselHero" class="relative bg-cover bg-center h-72">
        <!-- Contenedor del carrusel -->
        <div id="carousel" class="w-full h-full overflow-hidden relative">
            <!-- Imágenes del carrusel -->
            <div class="carousel-images flex transition-transform duration-700 ease-in-out" style="width: 600%;">

                <div class="w-full h-full bg-cover bg-center overflow-hidden">
                    <!-- Imagen que se ajusta al ancho y se recorta -->
                    <img src="${pageContext.request.contextPath}/images/carrusel.jpg" alt="Logo" class="w-full h-full object-cover">
                </div>

                <div class="w-full h-full bg-cover bg-center overflow-hidden">
                    <img src="${pageContext.request.contextPath}/images/carrusel1.jpg" alt="Logo" class="w-full h-full object-cover">
                </div>

                <div class="w-full h-full bg-cover bg-center overflow-hidden">
                    <img src="${pageContext.request.contextPath}/images/carrusel2.jpg" alt="Logo" class="w-full h-full object-cover">
                </div>

                <div class="w-full h-full bg-cover bg-center overflow-hidden">
                    <img src="${pageContext.request.contextPath}/images/carrusel3.jpg" alt="Logo" class="w-full h-full object-cover">
                </div>

                <div class="w-full h-full bg-cover bg-center overflow-hidden">
                    <img src="${pageContext.request.contextPath}/images/carrusel4.jpg" alt="Logo" class="w-full h-full object-cover">
                </div>
            </div>
        </div>

        <!-- Botones de navegación -->
        <button id="prevBtn" aria-label="Previous Slide" class="absolute left-4 top-1/2 -translate-y-1/2 bg-gray-600 text-white p-2 rounded-full">
            &#10094;
        </button>
        <button id="nextBtn" aria-label="Next Slide" class="absolute right-4 top-1/2 -translate-y-1/2 bg-gray-600 text-white p-2 rounded-full">
            &#10095;
        </button>

        <!-- Indicadores de "bullets" -->
        <div class="absolute bottom-4 left-1/2 transform -translate-x-1/2 flex space-x-2">
            <button class="bullet h-4 w-4 bg-gray-400 rounded-full"></button>
            <button class="bullet h-4 w-4 bg-gray-400 rounded-full"></button>
            <button class="bullet h-4 w-4 bg-gray-400 rounded-full"></button>
            <button class="bullet h-4 w-4 bg-gray-400 rounded-full"></button>
            <button class="bullet h-4 w-4 bg-gray-400 rounded-full"></button>
        </div>
    </section>

    <!-- Script para controlar el carrusel -->
    <script>
        const carousel = document.querySelector('.carousel-images');
        const bullets = document.querySelectorAll('.bullet');
        const totalSlides = 5; // Número total de slides
        let currentIndex = 0;

        // Función para actualizar el carrusel
        function updateCarousel(index) {
            // Cambiar la posición de las imágenes
            carousel.style.transform = `translateX(-${index * 100}%)`;

            // Actualizar los indicadores de "bullets"
            bullets.forEach((bullet, i) => {
                bullet.style.backgroundColor = i === index ? 'gray' : 'lightgray';
            });
        }

        // Función para avanzar al siguiente slide
        function nextSlide() {
            currentIndex = (currentIndex + 1) % totalSlides;
            updateCarousel(currentIndex);
        }

        // Función para retroceder al slide anterior
        function prevSlide() {
            currentIndex = (currentIndex - 1 + totalSlides) % totalSlides;
            updateCarousel(currentIndex);
        }

        // Mover automáticamente cada 3 segundos
        let autoSlide = setInterval(nextSlide, 3000);

        // Navegación con los botones
        document.getElementById('nextBtn').addEventListener('click', () => {
            clearInterval(autoSlide); // Parar el auto-slide al interactuar
            nextSlide();
            autoSlide = setInterval(nextSlide, 3000); // Reiniciar el auto-slide
        });

        document.getElementById('prevBtn').addEventListener('click', () => {
            clearInterval(autoSlide); // Parar el auto-slide al interactuar
            prevSlide();
            autoSlide = setInterval(nextSlide, 3000); // Reiniciar el auto-slide
        });

        // Navegación con los "bullets"
        bullets.forEach((bullet, index) => {
            bullet.addEventListener('click', () => {
                clearInterval(autoSlide); // Parar el auto-slide al interactuar
                currentIndex = index;
                updateCarousel(currentIndex);
                autoSlide = setInterval(nextSlide, 3000); // Reiniciar el auto-slide
            });
        });
    </script>



    <!-- Sección de Productos Destacados -->
    <section class="container mx-auto px-4 py-12">
        <h2 class="text-3xl font-semibold text-gray-800 text-center mb-8">Productos Destacados</h2>
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
            <%-- Listado dinámico de productos --%>
            <%
                String[] productos = {"Producto 1", "Producto 2", "Producto 3", "Producto 4"};
                for (int i = 0; i < productos.length; i++) {
            %>
            <div class="bg-white shadow-md rounded-lg overflow-hidden">
                <img src="https://via.placeholder.com/300x200" alt="Producto" class="w-full h-48 object-cover">
                <div class="p-4">
                    <h3 class="text-lg font-semibold text-gray-800"><%= productos[i] %></h3>
                    <p class="text-gray-600 mt-2">Descripción breve del producto.</p>
                    <div class="mt-4">
                        <span class="text-gray-800 font-bold">$99.99</span>
                        <a href="#" class="text-indigo-600 hover:text-indigo-400 ml-4">Agregar al carrito</a>
                    </div>
                </div>
            </div>
            <div class="bg-white shadow-md rounded-lg overflow-hidden">
                <img src="https://via.placeholder.com/300x200" alt="Producto" class="w-full h-48 object-cover">
                <div class="p-4">
                    <h3 class="text-lg font-semibold text-gray-800"><%= productos[i] %></h3>
                    <p class="text-gray-600 mt-2">Descripción breve del producto.</p>
                    <div class="mt-4">
                        <span class="text-gray-800 font-bold">$99.99</span>
                        <a href="#" class="text-indigo-600 hover:text-indigo-400 ml-4">Agregar al carrito</a>
                    </div>
                </div>
            </div>
            <div class="bg-white shadow-md rounded-lg overflow-hidden">
                <img src="https://via.placeholder.com/300x200" alt="Producto" class="w-full h-48 object-cover">
                <div class="p-4">
                    <h3 class="text-lg font-semibold text-gray-800"><%= productos[i] %></h3>
                    <p class="text-gray-600 mt-2">Descripción breve del producto.</p>
                    <div class="mt-4">
                        <span class="text-gray-800 font-bold">$99.99</span>
                        <a href="#" class="text-indigo-600 hover:text-indigo-400 ml-4">Agregar al carrito</a>
                    </div>
                </div>
            </div>
            <% } %>
        </div>
    </section>

    <!-- Incluir el footer -->
    <%@ include file="includes/footer.jsp" %>

</html>
