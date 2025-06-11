<div class="bg-white min-h-screen space-y-6">

	<!-- Filtros de Productos -->
    <div class="flex justify-center mb-10">
        <div class="p-4 bg-gray-100 border border-gray-200 rounded-xl shadow-sm inline-block">
            <form class="flex flex-wrap items-start justify-center gap-4 text-sm">
                <!-- Código Nacional -->
                <div class="flex flex-col">
                    <label for="codNacional" class="text-gray-600">Cód. Nacional</label>
                    <input type="text" id="codNacional" placeholder="Ej: 123456"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio"/>
                </div>

                <!-- Nombre del Producto -->
                <div class="flex flex-col">
                    <label for="nombreProducto" class="text-gray-600">Nombre</label>
                    <input type="text" id="nombreProducto" placeholder="Ej: Ibuprofeno"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio"/>
                </div>

                <!-- Activo -->
                <div class="flex flex-col">
                    <label for="activo" class="text-gray-600">Activo</label>
                    <select id="activo"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
                        <option value="">Todos</option>
                        <option value="true">Activo</option>
                        <option value="false">Inactivo</option>
                    </select>
                </div>

                <!-- Categoría -->
                <div class="flex flex-col">
                    <label for="categoria" class="text-gray-600">Categoría</label>
                    <select id="categoria"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
                        <option value="">Todas</option>
                        <option value="analgesicos">Analgésicos</option>
                        <option value="vitaminas">Vitaminas</option>
                        <option value="dermocosmetica">Dermocosmética</option>
                    </select>
                </div>

                <!-- Subcategoría -->
                <div class="flex flex-col">
                    <label for="subCategoria" class="text-gray-600">Subcategoría</label>
                    <select id="subCategoria"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
                        <option value="">Todas</option>
                        <!-- más opciones -->
                    </select>
                </div>

                <!-- Tipo -->
                <div class="flex flex-col">
                    <label for="tipo" class="text-gray-600">Tipo</label>
                    <select id="tipo"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
                        <option value="">Todos</option>
                        <!-- más opciones -->
                    </select>
                </div>

                <!-- Familia -->
                <div class="flex flex-col">
                    <label for="familia" class="text-gray-600">Familia</label>
                    <select id="familia"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
                        <option value="">Todas</option>
                        <!-- más opciones -->
                    </select>
                </div>

                <!-- Laboratorio -->
                <div class="flex flex-col">
                    <label for="laboratorio" class="text-gray-600">Laboratorio</label>
                    <input type="text" id="laboratorio" placeholder="Ej: FarmaLab"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio"/>
                </div>

                <!-- Presentación -->
                <div class="flex flex-col">
                    <label for="presentacion" class="text-gray-600">Presentación</label>
                    <select id="presentacion"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
                        <option value="">Todas</option>
                        <!-- más opciones -->
                    </select>
                </div>

                <!-- Stock -->
                <div class="flex flex-col">
                    <label for="stock" class="text-gray-600">Stock</label>
                    <select id="stock"
                            class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
                        <option value="">Todos</option>
                        <option value=true>Si</option>
                        <option value=false>No</option>
                    </select>
                </div>

                <!-- Precio (min/max) -->
                <div class="flex flex-col">
                    <label class="text-gray-600">Precio (min/max)</label>
                    <div class="flex gap-2">
                        <input type="number" step="0.01" id="precioMin" placeholder="0.00"
                                class="w-20 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio"/>
                        <input type="number" step="0.01" id="precioMax" placeholder="100.00"
                                class="w-20 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                </div>

                <!-- Botones -->
                <div class="flex gap-2 self-end">
                    <button type="submit"
                            class="bg-pistachio text-white font-medium px-4 py-1.5 rounded-md hover:bg-pistachio-dark transition">
                        Buscar
                    </button>
                    <button type="reset"
                            class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition">
                        Limpiar
                    </button>
                </div>

            </form>
        </div>
    </div>


	<!-- KPIs de Productos -->
	<div class="grid grid-cols-1 md:grid-cols-4 gap-4">
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Total Productos</h2>
			<p class="text-2xl font-bold">320</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Productos Activos</h2>
			<p class="text-2xl font-bold">280</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Productos Inactivos</h2>
			<p class="text-2xl font-bold">40</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Stock Total</h2>
			<p class="text-2xl font-bold">15,750 uds.</p>
		</div>
	</div>

    <!-- Tabla de Productos -->
    <div class="bg-white p-6 rounded-2xl shadow border">
        <div class="flex items-center justify-between mb-4">
            <h2 class="text-lg font-semibold mb-0">Listado de Productos</h2>
            <div class="flex items-center space-x-2">
                <button id="nuevoBtn"
                        class="flex items-center bg-pistachio text-white font-medium px-4 py-1.5 rounded-md hover:bg-pistachio-dark transition">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mr-1" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" clip-rule="evenodd"
                              d="M10 5a1 1 0 011 1v3h3a1 1 0 110 2h-3v3a1 1 0 11-2 0v-3H6a1 1 0 110-2h3V6a1 1 0 011-1z" />
                    </svg>
                    Nuevo
                </button>
                <button id="cargaMasivaBtn"
                        class="flex items-center bg-pistachio text-white font-medium px-4 py-1.5 rounded-md hover:bg-pistachio-dark transition">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                        <path fill-rule="evenodd" clip-rule="evenodd"
                              d="M4 2a1 1 0 00-1 1v14a1 1 0 001 1h12a1 1 0 001-1V7.414a1 1 0 00-.293-.707l-3.414-3.414A1 1 0 0012.586 2H4zm8 1.414L16.586 8H12a1 1 0 01-1-1V3.414z" />
                    </svg>
                    Carga masiva
                </button>
            </div>
        </div>
        <div class="overflow-auto">
            <table id="tablaProductos" class="min-w-full text-sm">
                <thead class="text-gray-500 border-b">
                    <tr>
                        <th class="px-4 py-2 text-left">Cód. Nacional</th>
                        <th class="px-4 py-2 text-left">Nombre</th>
                        <th class="px-4 py-2 text-left">Categoría</th>
                        <th class="px-4 py-2 text-left">Stock</th>
                        <th class="px-4 py-2 text-left">Precio</th>
                        <th class="px-4 py-2 text-left">Estado</th>
                    </tr>
                </thead>
                <tbody id="productosBody" class="text-gray-700 divide-y">
                </tbody>
            </table>
        </div>

        <!-- Paginación -->
        <div id="pagination" class="flex justify-between items-center mt-4">
            <button id="prevBtn"
                    class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition"
                    disabled>
                Anterior
            </button>
            <span>Página <span id="pageNum">1</span> de <span id="totalPages">1</span></span>
            <button id="nextBtn"
                    class="bg-gray-200 text-gray-700 font-medium px-4 py-1.5 rounded-md hover:bg-gray-300 transition">
                Siguiente
            </button>
        </div>
    </div>

	<!-- Inventario Chart Section -->
	<div class="bg-white p-6 rounded-2xl shadow border">
		<h2 class="text-lg font-semibold mb-4">Distribución de Inventario</h2>
		<div id="inventory-bar-chart" class="w-full h-64 bg-gray-100 flex items-center justify-center text-gray-400">
			[Gráfico de Barras por Categoría]
		</div>
	</div>

	<!-- Top Stock & Bajo Stock -->
	<div class="grid grid-cols-1 md:grid-cols-2 gap-4">
		<div class="bg-white p-6 rounded-2xl shadow border">
			<h2 class="text-lg font-semibold mb-4">Productos con Más Stock</h2>
			<ul class="space-y-2">
				<li class="flex justify-between text-sm">
					<span>Ibuprofeno 400mg</span>
					<span class="font-medium">3,200 uds.</span>
				</li>
				<li class="flex justify-between text-sm">
					<span>Vitamina C 1g</span>
					<span class="font-medium">2,850 uds.</span>
				</li>
				<li class="flex justify-between text-sm">
					<span>Gel Antibacteriano</span>
					<span class="font-medium">2,400 uds.</span>
				</li>
			</ul>
		</div>
		<div class="bg-white p-6 rounded-2xl shadow border">
			<h2 class="text-lg font-semibold mb-4">Productos con Bajo Stock</h2>
			<ul class="space-y-2">
				<li class="flex justify-between text-sm">
					<span>Paracetamol 500mg</span>
					<span class="font-medium">35 uds.</span>
				</li>
				<li class="flex justify-between text-sm">
					<span>Colirio Ocular</span>
					<span class="font-medium">20 uds.</span>
				</li>
				<li class="flex justify-between text-sm">
					<span>Cremas para Afeitar</span>
					<span class="font-medium">15 uds.</span>
				</li>
			</ul>
		</div>
	</div>

	<!-- Overlay y Modal (oculto por defecto) -->
    <div id="nuevoModal" class="fixed inset-0 flex items-center justify-center hidden z-[10000]">
        <div class="bg-white rounded-2xl shadow-lg w-full max-w-2xl p-6 relative">
            <div class="relative mb-4">
                <button
                    id="closeModal"
                    class="absolute right-4 top-1/2 transform -translate-y-1/2
                           w-8 h-8 text-2xl flex items-center justify-center rounded
                           hover:bg-red-500 hover:text-white text-gray-500
                           transition-colors">
                    &times;
                </button>
                <h3 class="text-xl font-semibold text-center">
                    Crear nuevo producto
                </h3>
            </div>

            <form id="nuevoForm" class="space-y-4 overflow-y-auto max-h-[80vh]">
                <div class="grid grid-cols-2 gap-4 pr-4">
                    <!-- Código Nacional -->
                    <div>
                        <label for="newCod" class="block text-sm text-gray-600">Cód. Nacional</label>
                        <input type="text" id="newCod" name="codNacional" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Nombre -->
                    <div>
                        <label for="newNombre" class="block text-sm text-gray-600">Nombre</label>
                        <input type="text" id="newNombre" name="nombre" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Categoría -->
                    <div>
                        <label for="newCategoria" class="block text-sm text-gray-600">Categoría</label>
                        <select id="newCategoria" name="categoria" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio">
                            <option value="">Selecciona</option>
                            <option value="analgesicos">Analgésicos</option>
                            <option value="vitaminas">Vitaminas</option>
                            <option value="dermocosmetica">Dermocosmética</option>
                        </select>
                    </div>
                    <!-- Subcategoría -->
                    <div>
                        <label for="newSub" class="block text-sm text-gray-600">Subcategoría</label>
                        <input type="number" id="newSub" name="subCategoria"
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Tipo -->
                    <div>
                        <label for="newTipo" class="block text-sm text-gray-600">Tipo</label>
                        <input type="number" id="newTipo" name="tipo"
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Familia -->
                    <div>
                        <label for="newFamilia" class="block text-sm text-gray-600">Familia</label>
                        <input type="text" id="newFamilia" name="familia" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Laboratorio -->
                    <div class="col-span-2">
                        <label for="newLab" class="block text-sm text-gray-600">Laboratorio</label>
                        <input type="text" id="newLab" name="laboratorio"
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Descripción -->
                    <div class="col-span-2">
                        <label for="newDesc" class="block text-sm text-gray-600">Descripción</label>
                        <textarea id="newDesc" name="descripcion" rows="3"
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"></textarea>
                    </div>
                    <!-- Stock -->
                    <div>
                        <label for="newStock" class="block text-sm text-gray-600">Stock</label>
                        <input type="number" id="newStock" name="stock" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Precio -->
                    <div>
                        <label for="newPrice" class="block text-sm text-gray-600">Precio</label>
                        <input type="number" step="0.01" id="newPrice" name="price" required
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Descuento -->
                    <div>
                        <label for="newDiscount" class="block text-sm text-gray-600">Descuento (%)</label>
                        <input type="number" step="0.01" id="newDiscount" name="discount"
                            class="w-full border border-gray-300 rounded-md px-2 py-1 focus:ring-pistachio focus:border-pistachio"/>
                    </div>
                    <!-- Activo -->
                    <div class="flex items-center space-x-2 pt-6">
                        <input type="checkbox" id="newActivo" name="activo" class="h-4 w-4 text-pistachio focus:ring-pistachio"/>
                        <label for="newActivo" class="text-gray-600">Activo</label>
                    </div>
                    <!-- Imagen -->
                    <div class="col-span-2">
                        <label class="block text-sm text-gray-600 mb-1">Imagen</label>
                        <input type="file" id="imagenInput" name="imagen" accept=".png,.jpg" class="hidden"/>
                        <label for="imagenInput"
                            class="w-full h-32 border-2 border-dashed border-gray-300 rounded-md flex items-center justify-center text-gray-500 hover:border-pistachio hover:text-pistachio cursor-pointer">
                            Añadir imagen
                        </label>
                    </div>
                </div>

                <!-- Botón Guardar -->
                <div class="mt-6 text-center">
                    <button type="submit"
                        class="bg-pistachio text-white font-medium px-6 py-2 rounded-md hover:bg-pistachio-dark transition">
                        Guardar
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Modal Carga Masiva -->
    <div id="cargaMasivaModal" class="fixed inset-0 flex items-center justify-center hidden z-[10000]">
        <div class="bg-white rounded-2xl shadow-lg w-full max-w-md p-6 relative">
            <div class="relative mb-4">
                <button
                    id="closeCargaMasiva"
                    class="absolute right-4 top-1/2 transform -translate-y-1/2
                           w-8 h-8 text-2xl flex items-center justify-center rounded
                           hover:bg-red-500 hover:text-white text-gray-500
                           transition-colors">&times;</button>
                <h3 class="text-xl font-semibold text-center">
                    Carga masiva de productos
                </h3>
            </div>
            <form id="cargaMasivaForm" class="space-y-4">
                <div>
                    <input type="file" id="fileInput" name="file" accept=".csv, .xls, .xlsx" class="hidden"/>
                    <label for="fileInput"
                           class="w-full h-32 border-2 border-dashed border-gray-300 rounded-md
                                  flex items-center justify-center text-gray-500
                                  hover:border-pistachio hover:text-pistachio cursor-pointer">
                        Seleccionar archivo
                    </label>
                </div>
                <div class="mt-6 text-center">
                    <button type="submit"
                            class="bg-pistachio text-white font-medium px-6 py-2 rounded-md
                                   hover:bg-pistachio-dark transition">
                        Subir
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
