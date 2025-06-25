const swiper = new Swiper('.multiple-slide-carousel', {
  slidesPerView: 5,
  spaceBetween: 20,
  loop: true,
  autoHeight: true,        // <--- que ajuste altura al contenido
  navigation: {
    nextEl: '.custom-button-next',
    prevEl: '.custom-button-prev',
  },
  breakpoints: {
    240:  { slidesPerView: 2, spaceBetween: 10 },
    700:  { slidesPerView: 3, spaceBetween: 15 },
    900:  { slidesPerView: 4, spaceBetween: 20 },
    1100: { slidesPerView: 5, spaceBetween: 20 },
  },
});

document.addEventListener('DOMContentLoaded', () => {
	// --- Lógica del Carrito y Wishlist (EXISTENTE) ---
	let cart = JSON.parse(localStorage.getItem('cart') || '{}');
	function saveCart() {
		localStorage.setItem('cart', JSON.stringify(cart));
		window.dispatchEvent(new CustomEvent('cartUpdated'));
	}

	let wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
	function saveWishlist() {
		localStorage.setItem('wishlist', JSON.stringify(wishlist));
		window.dispatchEvent(new CustomEvent('wishlistUpdated'));
	}

	// --- Lógica para los controles del producto principal (NUEVA) ---
	function initializeProductDetailControls() {
		const container = document.getElementById('product-detail-container');
		if (!container) return; // Salir si no estamos en la página de un producto

		const productId = container.dataset.productId;
		const stock = parseInt(container.dataset.productStock, 10);

		const qtyInput = document.getElementById('qty-input');
		const btnMinus = document.getElementById('qty-minus');
		const btnPlus = document.getElementById('qty-plus');
		const addToCartBtn = container.querySelector('button.bg-pistachio');
		const addToCartBtnText = addToCartBtn.querySelector('span');
		const addToCartBtnIcon = addToCartBtn.querySelector('img');

		const iconCartPath = `${window.contextPath}/images/shopping-cart-white.svg`;
		const iconCheckPath = `${window.contextPath}/images/check.svg`; // Asegúrate de que este icono exista

		let currentQuantity = cart[productId] ? parseInt(cart[productId], 10) : 1;

		function updateButtonStates() {
			qtyInput.value = currentQuantity;

			// Habilitar/deshabilitar botones de cantidad
			btnMinus.disabled = currentQuantity <= 0;
			btnPlus.disabled = currentQuantity >= stock;
			
			// Si no hay stock, deshabilitar todo
			if (stock === 0) {
				btnMinus.disabled = true;
				btnPlus.disabled = true;
				addToCartBtn.disabled = true;
				addToCartBtnText.textContent = 'SIN STOCK';
				addToCartBtn.classList.add('bg-gray-400', 'cursor-not-allowed');
				addToCartBtn.classList.remove('bg-pistachio', 'hover:bg-pistachio-dark');
				addToCartBtnIcon.style.display = 'none';
				return;
			}

			if (cart[productId]) {
				// El producto está en el carrito
				addToCartBtnText.textContent = 'AÑADIDO';
				addToCartBtnIcon.src = iconCheckPath;
				
				addToCartBtn.onmouseover = () => {
					addToCartBtnText.textContent = 'ACTUALIZAR';
					addToCartBtnIcon.src = iconCartPath;
				};
				addToCartBtn.onmouseout = () => {
					addToCartBtnText.textContent = 'AÑADIDO';
					addToCartBtnIcon.src = iconCheckPath;
				};

			} else {
				// El producto no está en el carrito
				addToCartBtnText.textContent = 'AÑADIR';
				addToCartBtnIcon.src = iconCartPath;
				addToCartBtn.onmouseover = null;
				addToCartBtn.onmouseout = null;
			}
		}

		btnMinus.addEventListener('click', () => {
			if (currentQuantity > 1) {
				currentQuantity--;
				updateButtonStates();
			}
		});

		btnPlus.addEventListener('click', () => {
			if (currentQuantity < stock) {
				currentQuantity++;
				updateButtonStates();
			}
		});

		addToCartBtn.addEventListener('click', () => {
			if (stock > 0) {
				cart[productId] = currentQuantity;
				saveCart();
				updateButtonStates(); // Actualiza el botón para mostrar "Añadido" y el check
			}
		});

		// Estado inicial
		updateButtonStates();
	}

	// --- Lógica para las tarjetas de producto en carruseles (EXISTENTE) ---
	function renderCartControls(container, product) {
		container.innerHTML = '';
		const qty = cart[product.id] || 0;

		if (qty === 0) {
			const btn = document.createElement('button');

			if (product.stock === 0) {
				btn.disabled = true;
				btn.className = 'ml-2 bg-gray-200 text-gray-400 px-3 py-1 text-sm rounded cursor-not-allowed';
			} else {
				btn.className = 'ml-2 bg-pistachio text-white px-2 py-2 text-sm rounded hover:bg-pistachio-dark transition flex items-center gap-1';

				const imgIcon = document.createElement('img');
				imgIcon.src = `${window.contextPath}/images/shopping-cart-white.svg`;
				imgIcon.className = 'h-5 w-5';
				imgIcon.alt = 'Carrito de compras';

				btn.appendChild(imgIcon);

				btn.addEventListener('click', () => {
					cart[product.id] = 1;
					saveCart();
					renderCartControls(container, product);
				});
			}
			container.appendChild(btn);
		} else {
			const wrap = document.createElement('div');
			wrap.className = 'flex items-center border border-gray-200 rounded';

			const btnMinus = document.createElement('button');
			btnMinus.className = 'px-2 py-1 text-sm text-gray-600 hover:bg-gray-100';
			btnMinus.textContent = '−';
			btnMinus.addEventListener('click', () => {
				const newQty = (cart[product.id] || 0) - 1;
				if (newQty > 0) {
					cart[product.id] = newQty;
				} else {
					delete cart[product.id];
				}
				saveCart();
				renderCartControls(container, product);
			});

			const input = document.createElement('input');
			input.type = 'text';
			input.value = qty;
			input.className = 'w-7 text-center text-sm py-1 border-0 focus:ring-0 focus:outline-none';
			input.readOnly = true;

			const btnPlus = document.createElement('button');
			btnPlus.className = 'px-2 py-1 text-sm text-gray-600 hover:bg-gray-100';
			btnPlus.textContent = '+';
			if (qty >= product.stock) {
				btnPlus.disabled = true;
				btnPlus.classList.add("cursor-not-allowed", "text-gray-300");
			}
			btnPlus.addEventListener('click', () => {
				cart[product.id] = (cart[product.id] || 0) + 1;
				saveCart();
				renderCartControls(container, product);
			});

			wrap.appendChild(btnMinus);
			wrap.appendChild(input);
			wrap.appendChild(btnPlus);
			container.appendChild(wrap);
		}
	}

	function renderWishlistStatus(button, productId) {
		const svg = button.querySelector('svg');
		if (wishlist.includes(productId.toString())) {
			svg.setAttribute('fill', 'currentColor');
			svg.classList.remove('text-gray-400');
			svg.classList.add('text-red-500');
		} else {
			svg.setAttribute('fill', 'none');
			svg.classList.remove('text-red-500');
			svg.classList.add('text-gray-400');
		}
	}

	// --- Inicialización de los componentes ---

	// 1. Controles del producto principal
	initializeProductDetailControls();

	// 2. Tarjetas de producto en los carruseles
	const productCards = document.querySelectorAll('.product-card');
	productCards.forEach(card => {
		const productData = JSON.parse(card.dataset.product);
		const cartContainer = card.querySelector('.cart-controls-container');
		const wishlistBtn = card.querySelector('.wishlist-btn');

		if (cartContainer) {
			renderCartControls(cartContainer, productData);
		}

		if (wishlistBtn) {
			renderWishlistStatus(wishlistBtn, productData.id);

			wishlistBtn.addEventListener('click', () => {
				const productIdStr = productData.id.toString();
				const index = wishlist.indexOf(productIdStr);

				if (index > -1) {
					wishlist.splice(index, 1);
				} else {
					wishlist.push(productIdStr);
				}
				saveWishlist();
				renderWishlistStatus(wishlistBtn, productData.id);
			});
		}
	});

});