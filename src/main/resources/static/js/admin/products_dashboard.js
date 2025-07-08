// products.js
(function(){
	// ——————————————————————————————
	// 1) Paginación, filtrado y renderizado de productos
	// ——————————————————————————————
	function initializePage() {
		let currentPage = 0,
			totalPages  = 1,
			size        = 25;

		const tbody        = document.getElementById('productosBody'),
			  prevBtn      = document.getElementById('prevBtn'),
			  nextBtn      = document.getElementById('nextBtn'),
			  pageNumEl    = document.getElementById('pageNum'),
			  totalPagesEl = document.getElementById('totalPages');

		function renderTable(content) {
            tbody.innerHTML = '';

            // Si no hay productos, mostramos un mensaje centrado
            if (content.length === 0) {
                const tr = document.createElement('tr');
                const td = document.createElement('td');
                td.colSpan = 6;                              // abarca todas las columnas
                td.className = 'text-center italic text-xl py-4';     // centrado, cursiva y padding vertical
                td.textContent = 'No hay resultados que coincidan con los filtros';
                tr.appendChild(td);
                tbody.appendChild(tr);
                return;
            }

            // Si hay datos, los renderizamos normalmente
            content.forEach(prod => {
                const tr = document.createElement('tr');
                tr.classList.add('cursor-pointer', 'hover:bg-gray-50');
                tr.addEventListener('click', () => openEditModal(prod));
                tbody.appendChild(tr);

                tr.innerHTML = `
                    <td class="px-4 py-2">${prod.id}</td>
                    <td class="px-4 py-2">${prod.nombre}</td>
                    <td class="px-4 py-2">${prod.categoriaNombre || ''}</td>
                    <td class="px-4 py-2 ${prod.stock === 0 ? 'text-red-500 font-bold' : ''}">
                        ${prod.stock || 0}
                    </td>
                    <td class="px-4 py-2">${(prod.price || 0).toFixed(2)} €</td>
                    <td class="px-4 py-2 ${prod.activo ? 'text-pistachio' : 'text-red-500'}">
                        ${prod.activo ? 'Activo' : 'Inactivo'}
                    </td>
                `;
                tbody.appendChild(tr);
            });

        }


		function toggleBtn(btn, isDisabled) {
			btn.disabled = isDisabled;
			btn.classList.toggle('opacity-50', isDisabled);
			btn.classList.toggle('hover:bg-gray-300', !isDisabled);
		}

		function updateControls() {
			pageNumEl.textContent    = currentPage + 1;
			totalPagesEl.textContent = totalPages;
			toggleBtn(prevBtn, currentPage === 0);
			toggleBtn(nextBtn, currentPage + 1 === totalPages);
		}

		function loadPage(page) {
			const fm     = new FormData(document.getElementById('filterForm'));
			const params = new URLSearchParams({ page, size });

			for (const [k, v] of fm.entries()) {
				if (v !== '') params.append(k, v);
			}

			fetch(`${window.contextPath}/admin/api/products/get_pagable_list?` + params)
				.then(res => {
					if (!res.ok) throw new Error('Error al cargar productos');
					return res.json();
				})
				.then(data => {
					currentPage = data.number;
					totalPages  = data.totalPages;
					renderTable(data.content);
					updateControls();
				})
				.catch(err => console.error(err));
		}

		prevBtn.addEventListener('click', () => {
			if (currentPage > 0) loadPage(currentPage - 1);
		});
		nextBtn.addEventListener('click', () => {
			if (currentPage + 1 < totalPages) loadPage(currentPage + 1);
		});

		document.getElementById('filterForm')
			.addEventListener('submit', e => {
				e.preventDefault();
				loadPage(0);
			});

		// carga inicial de la primera página
		loadPage(0);
	}


	// ——————————————————————————————
	// 2) Modal "Nuevo producto"
	// ——————————————————————————————
	const overlay      = document.getElementById('modalOverlay');
	const nuevoBtn     = document.getElementById('nuevoBtn');
	const modal        = document.getElementById('nuevoModal');
	const closeModal   = document.getElementById('closeModal');
	const nuevoForm    = document.getElementById('nuevoForm');

	function openModal() {
		overlay.classList.remove('hidden');
		modal.classList.remove('hidden');
		document.body.classList.add('overflow-hidden');
	}
	function hideModal() {
		overlay.classList.add('hidden');
		modal.classList.add('hidden');
		document.body.classList.remove('overflow-hidden');
		nuevoForm.reset();
	}

	nuevoBtn.addEventListener('click', openModal);
	closeModal.addEventListener('click', hideModal);
	overlay.addEventListener('click', hideModal);

	nuevoForm.addEventListener('submit', e => {
		e.preventDefault();
		const formData = new FormData(nuevoForm);
		fetch(`${window.contextPath}/admin/products`, {
			method: 'POST',
			body: formData,
			headers: { 'X-Requested-With': 'XMLHttpRequest' }
		})
		.then(res => {
			if (!res.ok) throw new Error('Error al guardar');
			return res.json();
		})
		.then(() => {
			hideModal();
			loadSection('/admin/products', false);
		})
		.catch(err => {
			alert('No se pudo guardar el producto.');
			console.error(err);
		});
	});

    function openEditModal(prod) {
        // Cambiar título
        document.querySelector('#nuevoModal h3').textContent = 'Editar producto';

        // Campos de texto, número, etc.
        document.getElementById('newCod').value      = prod.id || '';
        document.getElementById('newNombre').value   = prod.nombre || '';
        document.getElementById('newDesc').value     = prod.descripcion || '';
        document.getElementById('newUse').value      = prod.use || '';
        document.getElementById('newComp').value     = prod.composition || '';
        document.getElementById('newStock').value    = prod.stock != null ? prod.stock : '';
        document.getElementById('newPrice').value    = prod.price != null ? prod.price : '';
        document.getElementById('newDiscount').value = prod.discount != null ? prod.discount : '';

        // Selects
        document.getElementById('newFamilia').value       = prod.familiaId       || '';
        document.getElementById('newCategoria').value     = prod.categoriaId     || '';
        document.getElementById('newSubcategoria').value  = prod.subCategoriaId  || '';
        document.getElementById('newLaboratorio').value   = prod.laboratorioId   || '';
        document.getElementById('newTipo').value          = prod.tipoId          || '';

        // Activo / Destacado
        document.getElementById('newActivo').value       = prod.activo != null ? String(prod.activo) : 'true';
        document.getElementById('newDestacado').value    = prod.destacado != null ? String(prod.destacado) : 'false';

        // Mostrar modal
        overlay.classList.remove('hidden');
        modal.classList.remove('hidden');
        document.body.classList.add('overflow-hidden');
    }



	// ——————————————————————————————
	// 3) Modal "Carga masiva"
	// ——————————————————————————————
	const cargaMasivaModal = document.getElementById('cargaMasivaModal');
	const cargaMasivaBtn   = document.getElementById('cargaMasivaBtn');
	const closeCargaMasiva = document.getElementById('closeCargaMasiva');
	const cargaMasivaForm  = document.getElementById('cargaMasivaForm');
	const fileInput        = document.getElementById('fileInput');
	const fileLabel        = document.querySelector('label[for="fileInput"]');

	function openCargaMasiva() {
		overlay.classList.remove('hidden');
		cargaMasivaModal.classList.remove('hidden');
		document.body.classList.add('overflow-hidden');
	}
	function hideCargaMasiva() {
		overlay.classList.add('hidden');
		cargaMasivaModal.classList.add('hidden');
		document.body.classList.remove('overflow-hidden');
		cargaMasivaForm.reset();
		fileLabel.innerHTML = 'Seleccionar archivo';
	}

	cargaMasivaBtn.addEventListener('click', openCargaMasiva);
	closeCargaMasiva.addEventListener('click', hideCargaMasiva);
	overlay.addEventListener('click', hideCargaMasiva);

	fileInput.addEventListener('change', () => {
		if (fileInput.files.length === 0) {
			fileLabel.innerHTML = 'Seleccionar archivo';
			return;
		}
		const file = fileInput.files[0];
		const fileIcon = `
			<svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 mr-2 inline" fill="currentColor" viewBox="0 0 20 20">
				<path d="M4 2a1 1 0 00-1 1v14a1 1 0 001 1h12a1 1 0 001-1V7.414a1 1 0 00-.293-.707L13.414 3A1 1 0 0012.586 3H4z"/>
			</svg>`;
		fileLabel.innerHTML = `${fileIcon}<span class="align-middle">${file.name}</span>`;
	});

	cargaMasivaForm.addEventListener('submit', e => {
		e.preventDefault();
		if (fileInput.files.length === 0) {
			alert('Por favor selecciona un fichero antes de subir.');
			return;
		}
		const formData = new FormData();
		formData.append('file', fileInput.files[0]);

		fetch(`${window.contextPath}/admin/api/products/upload`, {
			method: 'POST',
			body: formData,
			headers: { 'X-Requested-With': 'XMLHttpRequest' }
		})
		.then(res => {
			if (!res.ok) throw new Error(`HTTP ${res.status}`);
			return res.text();
		})
		.then(() => {
			hideCargaMasiva();
			alert('📤 Archivo subido con éxito.');
			loadSection('/admin/products', false);
		})
		.catch(err => {
			console.error(err);
			alert('❌ Error al subir el archivo: ' + err.message);
		});
	});


    // ——————————————————————————————
    // 4) Sugerencias de nombres
    // ——————————————————————————————

    const input     = document.getElementById('nombreProducto');
    const suggBox   = document.getElementById('nombreSuggestions');
    let   timer;

    input.addEventListener('input', e => {
        const q = e.target.value.trim();
        clearTimeout(timer);
        if (q.length < 3) {
            suggBox.innerHTML = '';
            suggBox.classList.add('hidden');
            return;
        }
        timer = setTimeout(() => {
            fetch(`${window.contextPath}/admin/api/products/search_names?q=${encodeURIComponent(q)}`)
                .then(res => {
                    if (!res.ok) throw new Error(`Status ${res.status}`);
                    return res.json();
                })
                .then(list => {
                    if (list.length === 0) {
                        suggBox.classList.add('hidden');
                        return;
                    }
                    suggBox.innerHTML = list
                        .map(name => `<li class="px-2 py-1 hover:bg-gray-100 cursor-pointer">${name}</li>`)
                        .join('');
                    suggBox.classList.remove('hidden');

                    suggBox.querySelectorAll('li').forEach(li => {
                        li.addEventListener('mousedown', () => {
                            input.value = li.textContent;
                            suggBox.classList.add('hidden');
                        });
                    });
                })
                .catch(err => {
                    console.error('Error autocomplete:', err);
                    suggBox.classList.add('hidden');
                });
        }, 300);
    });

    input.addEventListener('blur', () => {
        setTimeout(() => suggBox.classList.add('hidden'), 200);
    });

    window.initializePage = initializePage;

})();
