<div class="bg-white min-h-screen space-y-6">

	<!-- Filtros de Empleados -->
	<div class="flex justify-center mb-10">
		<div class="p-4 bg-gray-100 border border-gray-200 rounded-xl shadow-sm inline-block">
			<form class="flex flex-wrap items-end justify-center gap-4 text-sm">

				<!-- Nombre -->
				<div class="flex flex-col">
					<label for="nombreEmpleado" class="text-gray-600">Nombre</label>
					<input type="text" id="nombreEmpleado" placeholder="Ej: Laura M."
						class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio" />
				</div>

				<!-- Rol -->
				<div class="flex flex-col">
					<label for="rol" class="text-gray-600">Rol</label>
					<select id="rol"
						class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
						<option value="">Todos</option>
						<option value="admin">Administrador</option>
						<option value="vendedor">Vendedor</option>
						<option value="almacen">Almacén</option>
					</select>
				</div>

				<!-- Estado -->
				<div class="flex flex-col">
					<label for="estadoEmpleado" class="text-gray-600">Estado</label>
					<select id="estadoEmpleado"
						class="w-40 border border-gray-300 rounded-md px-2 py-1 focus:outline-none focus:ring-pistachio focus:border-pistachio">
						<option value="">Todos</option>
						<option value="activo">Activo</option>
						<option value="inactivo">Inactivo</option>
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

	<!-- KPIs de Empleados -->
	<div class="grid grid-cols-1 md:grid-cols-4 gap-4">
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Total Empleados</h2>
			<p class="text-2xl font-bold">45</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Empleados Activos</h2>
			<p class="text-2xl font-bold">38</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Inactivos</h2>
			<p class="text-2xl font-bold">7</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Nuevos este mes</h2>
			<p class="text-2xl font-bold">3</p>
		</div>
	</div>

	<!-- Roles Chart Section -->
	<div class="bg-white p-6 rounded-2xl shadow border">
		<h2 class="text-lg font-semibold mb-4">Distribución por Rol</h2>
		<div id="employee-roles-pie-chart" class="w-full h-64 bg-gray-100 flex items-center justify-center text-gray-400">
			[Gráfico de Roles de Empleados]
		</div>
	</div>

	<!-- Últimos Empleados Añadidos -->
	<div class="bg-white p-6 rounded-2xl shadow border">
		<h2 class="text-lg font-semibold mb-4">Últimos Empleados Añadidos</h2>
		<ul class="space-y-2 text-sm">
			<li class="flex justify-between">
				<span>María López</span>
				<span class="text-gray-500">15/05/2025</span>
			</li>
			<li class="flex justify-between">
				<span>Iván Ruiz</span>
				<span class="text-gray-500">13/05/2025</span>
			</li>
			<li class="flex justify-between">
				<span>Daniela Pérez</span>
				<span class="text-gray-500">10/05/2025</span>
			</li>
		</ul>
	</div>

	<!-- Tabla de Empleados -->
	<div class="bg-white p-6 rounded-2xl shadow border">
		<h2 class="text-lg font-semibold mb-4">Listado de Empleados</h2>
		<div class="overflow-auto">
			<table class="min-w-full text-sm">
				<thead class="text-gray-500 border-b">
					<tr>
						<th class="px-4 py-2 text-left">ID</th>
						<th class="px-4 py-2 text-left">Nombre</th>
						<th class="px-4 py-2 text-left">Correo</th>
						<th class="px-4 py-2 text-left">Rol</th>
						<th class="px-4 py-2 text-left">Estado</th>
						<th class="px-4 py-2 text-left">Fecha Alta</th>
					</tr>
				</thead>
				<tbody class="text-gray-700 divide-y">
					<tr>
						<td class="px-4 py-2">#E032</td>
						<td class="px-4 py-2">Laura Martínez</td>
						<td class="px-4 py-2">laura@empresa.com</td>
						<td class="px-4 py-2">Vendedor</td>
						<td class="px-4 py-2 text-green-600">Activo</td>
						<td class="px-4 py-2">05/03/2024</td>
					</tr>
					<tr>
						<td class="px-4 py-2">#E033</td>
						<td class="px-4 py-2">Carlos Gutiérrez</td>
						<td class="px-4 py-2">carlos@empresa.com</td>
						<td class="px-4 py-2">Almacén</td>
						<td class="px-4 py-2 text-red-500">Inactivo</td>
						<td class="px-4 py-2">21/01/2023</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
