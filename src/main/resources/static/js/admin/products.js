// products.js
(function(){
	// Esta función se ejecutará cuando home.js cargue dinámicamente este fichero
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
					<td class="px-4 py-2">${prod.categoriaEtiqueta  || ''}</td>
					<td class="px-4 py-2">${prod.stock || 0}</td>
					<td class="px-4 py-2">${(prod.price || 0).toFixed(2)} €</td>
					<td class="px-4 py-2">${prod.activo ? 'Activo' : 'Inactivo'}</td>
				`;
				tbody.appendChild(tr);
			});
		}

		// Helper para togglear el estado de un botón
        function toggleBtn(btn, isDisabled) {
            btn.disabled = isDisabled;
            btn.classList.toggle('opacity-50', isDisabled);
            btn.classList.toggle('hover:bg-gray-300', !isDisabled);
        }

        function updateControls() {
            pageNumEl.textContent    = currentPage + 1;
            totalPagesEl.textContent = totalPages;

            // Prev y Next usando la misma lógica
            toggleBtn(prevBtn, currentPage === 0);
            toggleBtn(nextBtn, currentPage + 1 === totalPages);
        }


		function loadPage(page) {
			fetch(`${window.contextPath}/admin/api/products?page=${page}&size=${size}`)
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
				.catch(err => {
					console.error(err);
					// opcional: mostrar mensaje de error en la UI
				});
		}

		prevBtn.addEventListener('click', () => {
			if (currentPage > 0) loadPage(currentPage - 1);
		});
		nextBtn.addEventListener('click', () => {
			if (currentPage + 1 < totalPages) loadPage(currentPage + 1);
		});

		// Carga inicial
		loadPage(0);
	}

	// Exportamos la función para que home.js la invoque en script.onload
	window.initProductsPage = initProductsPage;

    const overlay    = document.getElementById('modalOverlay');

    const nuevoBtn   = document.getElementById('nuevoBtn');
    const modal      = document.getElementById('nuevoModal');
    const closeModal = document.getElementById('closeModal');
    const nuevoForm  = document.getElementById('nuevoForm');

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
        .then(data => {
            hideModal();
            loadSection('/admin/products', false);
        })
        .catch(err => {
            alert('No se pudo guardar el producto.');
            console.error(err);
        });
    });

    // Referencias al modal de carga masiva
    const cargaMasivaModal = document.getElementById('cargaMasivaModal');
    const cargaMasivaBtn   = document.getElementById('cargaMasivaBtn');
    const closeCargaMasiva = document.getElementById('closeCargaMasiva');
    const cargaMasivaForm  = document.getElementById('cargaMasivaForm');

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
    }

    cargaMasivaBtn.addEventListener('click', openCargaMasiva);
    closeCargaMasiva.addEventListener('click', hideCargaMasiva);
    overlay.addEventListener('click', hideCargaMasiva);

    cargaMasivaForm.addEventListener('submit', e => {
        e.preventDefault();
        const formData = new FormData(cargaMasivaForm);
        fetch(`${window.contextPath}/admin/products/upload`, {
            method: 'POST',
            body: formData,
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
        .then(res => {
            if (!res.ok) throw new Error('Error al subir el archivo');
            return res.json();
        })
        .then(data => {
            hideCargaMasiva();
            // recarga la sección de productos (misma función que usas tras crear uno nuevo)
            loadSection('/admin/products', false);
        })
        .catch(err => {
            alert('No se pudo realizar la carga masiva.');
            console.error(err);
        });
    });
})();
