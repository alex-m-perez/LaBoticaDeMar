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
						class="bg-pistachio text-white font-medium px-4 py-1.5 rounded-md hover:bg-dark-pistachio transition">
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
			<p class="text-2xl font-bold">302.57 €</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">% De Devoluciones (Mes)</h2>
			<p class="text-2xl font-bold">83,33%</p>
		</div>
		<div class="p-4 rounded-2xl shadow border">
			<h2 class="text-sm text-gray-500">Media Días Hasta Devolver</h2>
			<p class="text-2xl font-bold">2,4 días</p>
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
						<td class="px-4 py-2">12/06/2025</td>
						<td class="px-4 py-2">Crema Hidratante</td>
						<td class="px-4 py-2">1</td>
						<td class="px-4 py-2">12.50 €</td>
						<td class="px-4 py-2">Producto dañado en envío</td>
						<td class="px-4 py-2">Ana P.</td>
					</tr>
					<tr>
						<td class="px-4 py-2">#R0343</td>
						<td class="px-4 py-2">12/06/2025</td>
						<td class="px-4 py-2">Vitaminas Pack</td>
						<td class="px-4 py-2">2</td>
						<td class="px-4 py-2">25.00 €</td>
						<td class="px-4 py-2">Producto defectuoso</td>
						<td class="px-4 py-2">Luis G.</td>
					</tr>
                    <tr>
                      <td class="px-4 py-2">#R0401</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Gel Antibacterial</td>
                      <td class="px-4 py-2">1</td>
                      <td class="px-4 py-2">3.50 €</td>
                      <td class="px-4 py-2">Cantidad incorrecta</td>
                      <td class="px-4 py-2">Carlos M.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0402</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Protector Solar SPF50</td>
                      <td class="px-4 py-2">1</td>
                      <td class="px-4 py-2">9.90 €</td>
                      <td class="px-4 py-2">Embalaje abierto</td>
                      <td class="px-4 py-2">Marta R.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0403</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Jarabe Tos Infantil</td>
                      <td class="px-4 py-2">2</td>
                      <td class="px-4 py-2">15.00 €</td>
                      <td class="px-4 py-2">Error en descripción</td>
                      <td class="px-4 py-2">Sofía V.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0404</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Vitaminas C 500mg</td>
                      <td class="px-4 py-2">1</td>
                      <td class="px-4 py-2">6.30 €</td>
                      <td class="px-4 py-2">Dañado en transporte</td>
                      <td class="px-4 py-2">Raúl J.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0405</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Colirio Hidratante</td>
                      <td class="px-4 py-2">1</td>
                      <td class="px-4 py-2">4.80 €</td>
                      <td class="px-4 py-2">No era necesario</td>
                      <td class="px-4 py-2">Laura F.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0406</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Termómetro Digital</td>
                      <td class="px-4 py-2">1</td>
                      <td class="px-4 py-2">11.95 €</td>
                      <td class="px-4 py-2">No funciona</td>
                      <td class="px-4 py-2">Ismael L.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0407</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Mascarilla Facial</td>
                      <td class="px-4 py-2">3</td>
                      <td class="px-4 py-2">9.00 €</td>
                      <td class="px-4 py-2">Producto defectuoso</td>
                      <td class="px-4 py-2">Clara T.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0408</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Crema Analgésica</td>
                      <td class="px-4 py-2">1</td>
                      <td class="px-4 py-2">7.40 €</td>
                      <td class="px-4 py-2">Reacción alérgica</td>
                      <td class="px-4 py-2">Diego C.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0409</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Spray Nasal</td>
                      <td class="px-4 py-2">2</td>
                      <td class="px-4 py-2">10.00 €</td>
                      <td class="px-4 py-2">Envase roto</td>
                      <td class="px-4 py-2">Eva B.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0410</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Champú Anticaspa</td>
                      <td class="px-4 py-2">1</td>
                      <td class="px-4 py-2">5.60 €</td>
                      <td class="px-4 py-2">Cambio por otro</td>
                      <td class="px-4 py-2">Sergio E.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0411</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Toallitas Antisépticas</td>
                      <td class="px-4 py-2">2</td>
                      <td class="px-4 py-2">4.20 €</td>
                      <td class="px-4 py-2">Pedido duplicado</td>
                      <td class="px-4 py-2">Patricia Z.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0412</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Aceite Esencial Lavanda</td>
                      <td class="px-4 py-2">1</td>
                      <td class="px-4 py-2">6.75 €</td>
                      <td class="px-4 py-2">Mal olor</td>
                      <td class="px-4 py-2">Beatriz H.</td>
                    </tr>
                    <tr>
                      <td class="px-4 py-2">#R0413</td>
                      <td class="px-4 py-2">12/06/2025</td>
                      <td class="px-4 py-2">Pastillas para Garganta</td>
                      <td class="px-4 py-2">1</td>
                      <td class="px-4 py-2">3.20 €</td>
                      <td class="px-4 py-2">Sabor desagradable</td>
                      <td class="px-4 py-2">Tomás D.</td>
                    </tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
