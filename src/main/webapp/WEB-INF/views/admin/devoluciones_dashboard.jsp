<div class="bg-white min-h-screen space-y-6">

	<!-- Filtros de Devolución -->
	<div class="flex justify-center mb-10">
		<div class="p-4 bg-gray-100 border border-gray-200 rounded-xl shadow-sm inline-block">
			<form class="flex flex-wrap items-end justify-center gap-4 text-sm">

				<!-- Campo: Desde -->
				<div class="flex flex-col">
					<label for="fechaInicio" class="text-gray-600">Desde</label>
					<input type="date" id="fechaInicio" name="fechaInicio"
						class="w-36 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
				</div>

				<!-- Campo: Hasta -->
				<div class="flex flex-col">
					<label for="fechaFin" class="text-gray-600">Hasta</label>
					<input type="date" id="fechaFin" name="fechaFin"
						class="w-36 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
				</div>

				<!-- Precio Min -->
				<div class="flex flex-col">
					<label for="precioMin" class="text-gray-600">Precio Mín.</label>
					<input type="number" id="precioMin" step="0.01" placeholder="Ej: 5.00"
						class="w-28 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
				</div>

				<!-- Precio Max -->
				<div class="flex flex-col">
					<label for="precioMax" class="text-gray-600">Precio Máx.</label>
					<input type="number" id="precioMax" step="0.01" placeholder="Ej: 50.00"
						class="w-28 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
				</div>

				<!-- Producto -->
				<div class="flex flex-col">
					<label for="producto" class="text-gray-600">Producto</label>
					<input type="text" id="producto" placeholder="Ej: Ibuprofeno"
						class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
				</div>

				<!-- Usuario -->
				<div class="flex flex-col">
					<label for="usuario" class="text-gray-600">Usuario</label>
					<input type="text" id="usuario" placeholder="Ej: Ana P."
						class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
				</div>

				<!-- Motivo -->
				<div class="flex flex-col">
					<label for="motivo" class="text-gray-600">Motivo</label>
					<select id="motivo"
						class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
						<option value="">Todos</option>
						<option value="danado">Producto dañado</option>
						<option value="error">Error en el pedido</option>
						<option value="otros">Otros</option>
					</select>
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

	<!-- KPIs Section -->
	<div class="grid grid-cols-1 md:grid-cols-4 gap-4">
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Total Devoluciones (Hoy)</h2>
			<p class="text-2xl font-bold">15</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Importe Total Devuelto</h2>
			<p class="text-2xl font-bold">320.00 €</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">% De Devoluciones (Mes)</h2>
			<p class="text-2xl font-bold">2.4%</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Media Días Hasta Devolver</h2>
			<p class="text-2xl font-bold">3.7 días</p>
		</div>
	</div>

	<!-- Chart Section -->
	<div class="bg-white p-6 rounded-2xl shadow border">
		<h2 class="text-lg font-semibold mb-4">Evolución de Devoluciones</h2>
		<div id="returns-line-chart" class="w-full h-64 bg-gray-100 flex items-center justify-center text-gray-400">
			[Gráfico de Línea de Devoluciones]
		</div>
	</div>

	<!-- Top Returned Products & Motivos -->
	<div class="grid grid-cols-1 md:grid-cols-2 gap-4">
		<div class="bg-white p-6 rounded-2xl shadow border">
			<h2 class="text-lg font-semibold mb-4">Productos Más Devueltos</h2>
			<ul class="space-y-2">
				<li class="flex justify-between text-sm">
					<span>Crema Hidratante Aloe</span>
					<span class="font-medium">30 unidades</span>
				</li>
				<li class="flex justify-between text-sm">
					<span>Multivitaminas Pack</span>
					<span class="font-medium">22 unidades</span>
				</li>
				<li class="flex justify-between text-sm">
					<span>Gel Antibacteriano</span>
					<span class="font-medium">18 unidades</span>
				</li>
			</ul>
		</div>
		<div class="bg-white p-6 rounded-2xl shadow border">
			<h2 class="text-lg font-semibold mb-4">Motivos de Devolución</h2>
			<div id="reason-pie-chart" class="w-full h-64 bg-gray-100 flex items-center justify-center text-gray-400">
				[Gráfico de Motivos de Devolución]
			</div>
		</div>
	</div>

	<!-- Return Records Table -->
	<div class="bg-white p-6 rounded-2xl shadow border">
		<h2 class="text-lg font-semibold mb-4">Historial de Devoluciones</h2>
		<div class="overflow-auto">
			<table class="min-w-full text-sm">
				<thead class="text-gray-500 border-b">
					<tr>
						<th class="px-4 py-2 text-left">ID</th>
						<th class="px-4 py-2 text-left">Fecha</th>
						<th class="px-4 py-2 text-left">Producto</th>
						<th class="px-4 py-2 text-left">Cantidad</th>
						<th class="px-4 py-2 text-left">Importe</th>
						<th class="px-4 py-2 text-left">Motivo</th>
						<th class="px-4 py-2 text-left">Cliente</th>
					</tr>
				</thead>
				<tbody class="text-gray-700 divide-y">
					<tr>
						<td class="px-4 py-2">#R0342</td>
						<td class="px-4 py-2">02/05/2025</td>
						<td class="px-4 py-2">Crema Hidratante</td>
						<td class="px-4 py-2">1</td>
						<td class="px-4 py-2">12.50 €</td>
						<td class="px-4 py-2">Producto dañado</td>
						<td class="px-4 py-2">Ana P.</td>
					</tr>
					<tr>
						<td class="px-4 py-2">#R0343</td>
						<td class="px-4 py-2">02/05/2025</td>
						<td class="px-4 py-2">Vitaminas Pack</td>
						<td class="px-4 py-2">2</td>
						<td class="px-4 py-2">25.00 €</td>
						<td class="px-4 py-2">Error en el pedido</td>
						<td class="px-4 py-2">Luis G.</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
