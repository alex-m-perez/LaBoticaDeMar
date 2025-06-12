// products.js
(function(){
	// ——————————————————————————————
	// 0) Carga dinámica de <select> desde la API
	// ——————————————————————————————
	function loadOptions(endpoint, selectId, placeholderText) {
        const url = `${window.contextPath}${endpoint}`;
        console.log('⏳ GET', url);
        fetch(url)
            .then(res => {
                console.log('📥', endpoint, 'status:', res.status);
                if (!res.ok) throw new Error(`Status ${res.status}`);
                return res.json();
            })
            .then(list => {
                console.log('📝 datos para', selectId, list);
                const sel = document.getElementById(selectId);
                sel.innerHTML = `<option value="">${placeholderText}</option>`;
                list.forEach(item => {
                    const opt = document.createElement('option');
                    opt.value = item.id;
                    opt.textContent = item.nombre;
                    sel.appendChild(opt);
                });
            })
            .catch(err => console.error(`❌ fallo en ${selectId}:`, err));
        }

	// carga las categorías, familias y laboratorios
	loadOptions('/api/categoria/get_categorias',  'categoria_select',   'Todas');
	loadOptions('/api/familia/get_familias',      'familia_select',     'Todas');
    loadOptions('/api/laboratorio/get_labs',      'laboratorio_select', 'Todos');


	// ——————————————————————————————
	// 1) Paginación, filtrado y renderizado de productos
	// ——————————————————————————————
	function initProductsPage() {
		let currentPage = 0,
			totalPages  = 1,
			size        = 20;

		const tbody        = document.getElementById('productosBody'),
			  prevBtn      = document.getElementById('prevBtn'),
			  nextBtn      = document.getElementById('nextBtn'),
			  pageNumEl    = document.getElementById('pageNum'),
			  totalPagesEl = document.getElementById('totalPages');

		function renderTable(content) {
			tbody.innerHTML = '';
			content.forEach(prod => {
				const tr = document.createElement('tr');
				tr.innerHTML = `
					<td class="px-4 py-2">${prod.id}</td>
					<td class="px-4 py-2">${prod.nombre}</td>
					<td class="px-4 py-2">${prod.categoriaEtiqueta || ''}</td>
					<td class="px-4 py-2">${prod.stock || 0}</td>
					<td class="px-4 py-2">${(prod.price || 0).toFixed(2)} €</td>
					<td class="px-4 py-2">${prod.activo ? 'Activo' : 'Inactivo'}</td>
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

	// exponemos la función para que pueda llamarla home.js
	window.initProductsPage = initProductsPage;


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


})();
