<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<% String pageTitle = "La Botica de Mar – Productos"; %>
<!DOCTYPE html>
<html lang="es">
	<head>
		<meta charset="UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
		<title><%= pageTitle %></title>
		<link rel="icon" href="${pageContext.request.contextPath}/images/icono_tab2.png" type="image/png"/>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/output.css"/>
		<link href="https://fonts.googleapis.com/css2?family=Satisfy&display=swap" rel="stylesheet"/>
	</head>

    <script> window.contextPath = '<%= request.getContextPath() %>'; </script>
    <script src="${pageContext.request.contextPath}/js/products/productos.js" defer></script>

	<body class="flex flex-col min-h-screen bg-gray-50">
		<%@ include file="/WEB-INF/views/includes/navbar.jsp" %>

		<main class="flex-grow container mx-auto px-4 py-8">
			<h1 class="text-3xl font-bold text-center text-gray-800 mb-8">Nuestros Productos</h1>
			<div class="flex flex-col lg:flex-row gap-8 items-start">
				<!-- SIDEBAR -->
				<aside class="lg:w-1/4 self-start bg-white border border-gray-200 rounded-lg p-6 shadow-sm">
					<form id="filterForm" class="space-y-6">
						<!-- FAMILIA -->
						<div>
							<h2 class="font-semibold mb-2">Familia</h2>
							<select name="familia" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
								<option value="">Todas las familias</option>
								<c:if test="${not empty todasLasFamilias}">
									<c:forEach var="fam" items="${todasLasFamilias}">
										<option value="${fam.id}" <c:if test="${fam.id == filtroFamilia}">selected</c:if>>
											${fam.nombre}
										</option>
									</c:forEach>
								</c:if>
							</select>
						</div>

						<!-- CATEGORÍA -->
						<div>
							<h2 class="font-semibold mb-2">Categoría</h2>
							<select name="categoria" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
								<option value="">Todas las categorías</option>

                                <c:if test="${not empty categoriasPorFamilia}">
                                    <c:forEach var="cat" items="${categoriasPorFamilia}">
                                        <c:if test="${cat.familia.id == filtroFamilia}">
                                            <option value="${cat.id}"
                                                    <c:if test="${cat.id == filtroCategoria}">selected</c:if>>
                                                ${cat.nombre}
                                            </option>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
							</select>
						</div>

						<!-- SUBCATEGORÍA -->
						<div>
							<h2 class="font-semibold mb-2">Subcategoría</h2>
							<select name="subCategoria" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
								<option value="">Todas las subcategorías</option>
								<c:if test="${not empty todasLasSubcategorias}">
									<c:forEach var="sub" items="${todasLasSubcategorias}">
										<option value="${sub.id}" <c:if test="${sub.id == filtroSubCategoria}">selected</c:if>>
											${sub.nombre}
										</option>
									</c:forEach>
								</c:if>
							</select>
						</div>

						<!-- TIPO -->
						<div>
							<h2 class="font-semibold mb-2">Tipo</h2>
							<select name="tipo" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
								<option value="">Todos los tipos</option>
								<c:if test="${not empty todosLosTipos}">
									<c:forEach var="t" items="${todosLosTipos}">
										<option value="${t.id}" <c:if test="${t.id == filtroTipo}">selected</c:if>>
											${t.nombre}
										</option>
									</c:forEach>
								</c:if>
							</select>
						</div>

						<!-- LABORATORIO -->
						<div>
							<h2 class="font-semibold mb-2">Laboratorio</h2>
							<select name="laboratorio" class="w-full border rounded p-2 focus:ring-pistachio focus:outline-none">
								<option value="">Todos los laboratorios</option>
								<c:if test="${not empty todosLosLaboratorios}">
									<c:forEach var="lab" items="${todosLosLaboratorios}">
										<option value="${lab.id}" <c:if test="${lab.id == filtroLaboratorio}">selected</c:if>>
											${lab.nombre}
										</option>
									</c:forEach>
								</c:if>
							</select>
						</div>

						<!-- DISPONIBILIDAD -->
						<div class="space-y-2">
							<h2 class="font-semibold mb-2">Disponibilidad</h2>
							<label class="flex items-center">
								<input type="checkbox" name="activo" value="true"
								       class="form-checkbox h-5 w-5 text-pistachio"
								       <c:if test="${filtroActivo}">checked</c:if>/>
								<span class="ml-2">Sólo activos</span>
							</label>
							<label class="flex items-center">
								<input type="checkbox" name="stock" value="true"
								       class="form-checkbox h-5 w-5 text-pistachio"
								       <c:if test="${filtroStock}">checked</c:if>/>
								<span class="ml-2">Sólo en stock</span>
							</label>
						</div>

						<!-- PRECIO -->
						<div>
							<h2 class="font-semibold mb-2">Precio (€)</h2>
							<div class="flex space-x-2">
								<input type="number" name="precioMin" placeholder="Mín" min="0"
								       value="${filtroPrecioMin}"
								       class="w-1/2 border rounded p-2 focus:ring-pistachio focus:outline-none"/>
								<input type="number" name="precioMax" placeholder="Máx" min="0"
								       value="${filtroPrecioMax}"
								       class="w-1/2 border rounded p-2 focus:ring-pistachio focus:outline-none"/>
							</div>
						</div>

						<button type="submit"
								class="w-full py-2 bg-pistachio text-white font-medium rounded hover:bg-dark-pistachio transition">
							Filtrar
						</button>
					</form>
				</aside>

				<!-- GRID DE PRODUCTOS -->
				<section class="lg:w-3/4 flex flex-col">
					<div id="productsGrid"
                        class="grid 
                                bg-white            <!-- fondo único -->
                                divide-x divide-y   <!-- líneas entre celdas -->
                                divide-gray-200     <!-- color de las líneas -->
                                grid-cols-4         <!-- esto lo sigues calculando dinámico -->
                                ">
                    </div>

					<!-- PAGINACIÓN -->
					<div class="mt-6 flex justify-between items-center">
						<button id="prevBtn"
								class="px-4 py-2 bg-gray-200 rounded disabled:opacity-50"
								disabled>
							Anterior
						</button>
						<span>Página <span id="pageNum">1</span> de <span id="totalPages">1</span></span>
						<button id="nextBtn" class="px-4 py-2 bg-gray-200 rounded">
							Siguiente
						</button>
					</div>
				</section>
			</div>
		</main>

		<%@ include file="/WEB-INF/views/includes/footer.jsp" %>
	</body>
</html>
