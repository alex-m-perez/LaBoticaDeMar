//wishlist.js
document.addEventListener('DOMContentLoaded', () => {

    // --- 1. CONFIGURACIÓN Y ESTADO ---
    const isAuthenticated = document.body.dataset.authenticated === 'true';
    const contextPath = window.contextPath || '';

    let cartState = isAuthenticated ? (userCartState || {}) : JSON.parse(localStorage.getItem('cart') || '{}');
    let wishlistState = isAuthenticated ? (userWishlistState || []) : JSON.parse(localStorage.getItem('wishlist') || '[]');

    const iconCartPath = `${contextPath}/images/shopping-cart-white.svg`;
    const iconCheckPath = `${contextPath}/images/check.svg`;

    // --- 2. FUNCIONES DE RENDERIZADO DE BOTONES ---

    function renderGuestWishlist(wishlistData) {
        const tbody = document.querySelector('#wishlist-items-body');
        if (!tbody) return;

        // Limpiamos el contenido anterior y aplicamos la clase de división
        tbody.innerHTML = '';
        tbody.classList.add('divide-y', 'divide-gray-100');

        // Si la wishlist está vacía, muestra el mensaje y termina
        if (!wishlistData.items || wishlistData.items.length === 0) {
            // Nos aseguramos de quitar la cabecera si la lista se vacía
            const thead = tbody.previousElementSibling;
            if (thead && thead.tagName === 'THEAD') {
                thead.remove();
            }
            tbody.innerHTML = `<tr><td colspan="3" class="text-center p-12"><p class="text-gray-600 text-2xl font-bold mb-4">Tu lista de deseos está vacía.</p><a href="${contextPath}/" class="inline-block bg-pistachio text-white px-6 py-2 rounded hover:bg-dark-pistachio">Descubrir productos</a></td></tr>`;
            return;
        }

        // CREA LA CABECERA SI NO EXISTE (clave para el invitado)
        if (!tbody.previousElementSibling || tbody.previousElementSibling.tagName !== 'THEAD') {
            const theadHtml = `
                <thead class="bg-white">
                    <tr class="border-b border-gray-200">
                        <th class="py-3 px-4 text-left">Producto</th>
                        <th class="py-3 px-4 text-center">Precio</th>
                        <th class="py-3 px-4 text-center">Acciones</th>
                    </tr>
                </thead>
            `;
            tbody.insertAdjacentHTML('beforebegin', theadHtml);
        }

        // Itera y crea cada fila con las clases y estructura idénticas al JSP
        wishlistData.items.forEach(product => {
            // Lógica para la imagen (replicando clases del JSP)
            let imageHtml;
            if (product.imagenData) {
                imageHtml = `<img src="${contextPath}/api/images/${product.id}" alt="${product.nombre}" class="h-16 w-16 object-contain rounded mr-4" />`;
            } else {
                imageHtml = `<div class="h-16 w-16 bg-gray-200 flex items-center justify-center rounded mr-4"><svg class="w-8 h-8 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7h4l2-3h6l2 3h4v13H3V7z"/><circle cx="12" cy="13" r="4" stroke="currentColor" stroke-width="2"/></svg></div>`;
            }

            // Lógica para el precio (incluyendo descuentos)
            let priceCellHtml;
            if (product.discount > 0) {
                const effectivePrice = product.price * (1 - product.discount / 100);
                priceCellHtml = `<div class="flex flex-col items-center"><span class="text-xs text-gray-400 line-through">${new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(product.price)}</span><span class="text-red-600">${new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(effectivePrice)}</span></div>`;
            } else {
                priceCellHtml = new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(product.price);
            }

            // Plantilla de la fila, AHORA CON LAS CLASES IDÉNTICAS AL JSP
            const rowHTML = `
                <tr class="wishlist-item-row" data-product-id="${product.id}">
                    <td class="py-4 px-4">
                        <div class="flex items-center">
                            ${imageHtml}
                            <div>
                                <p class="font-semibold">${product.nombre}</p>
                                <p class="text-xs text-gray-500">${product.laboratorioNombre || ''}</p>
                            </div>
                        </div>
                    </td>
                    <td class="py-4 px-4 text-center font-semibold">
                        ${priceCellHtml}
                    </td>
                    <td class="py-4 px-4 text-center">
                        <div class="flex justify-center items-center gap-3">
                            <div class="cart-button-container"></div>
                            <div class="wishlist-button-container"></div>
                        </div>
                    </td>
                </tr>`;
            tbody.insertAdjacentHTML('beforeend', rowHTML);
        });

        // IMPORTANTE: Tu lógica para inicializar los botones se mantiene
        wishlistData.items.forEach(item => {
            renderRowControls(item.id);
        });
    }

    /**
     * Renderiza los botones para una fila específica.
     */
    function renderRowControls(productId) {
        const row = document.querySelector(`.wishlist-item-row[data-product-id="${productId}"]`);
        if (!row) return;

        const cartContainer = row.querySelector('.cart-button-container');
        const wishlistContainer = row.querySelector('.wishlist-button-container');

        cartContainer.innerHTML = '';
        wishlistContainer.innerHTML = '';

        renderCartButton(cartContainer, productId);
        renderWishlistButton(wishlistContainer, productId);
    }

    /**
     * MODIFICADO: Cambia el color del botón "AÑADIDO" a 'bg-light-pistachio'.
     */
    function renderCartButton(container, productId) {
        const qtyInCart = cartState[productId] || 0;
        const btn = document.createElement('button');
        btn.className = 'h-10 px-4 flex gap-2 items-center font-semibold rounded-lg transition text-sm';

        if (qtyInCart === 0) {
            // Estado "AÑADIR": El hover ya es 'dark-pistachio', lo cual es correcto.
            btn.classList.add('bg-pistachio', 'text-white', 'hover:bg-dark-pistachio');
            btn.innerHTML = `<span>AÑADIR</span><img src="${iconCartPath}" class="h-4" alt="Añadir"/>`;
            btn.onclick = () => handleAddToCart(productId);
        } else {
            // Estado "AÑADIDO":
            // =====================================================================
            // CAMBIO AQUÍ: Usamos 'bg-light-pistachio' en lugar de 'bg-pistachio'
            // =====================================================================
            btn.classList.add('bg-light-pistachio', 'text-white', 'cursor-default');
            btn.innerHTML = `<span>AÑADIDO</span><img src="${iconCheckPath}" class="h-4" alt="Añadido"/>`;
            btn.disabled = true;
        }
        container.appendChild(btn);
    }

    /**
     * Renderiza el botón del corazón (sin cambios).
     */
    function renderWishlistButton(container, productId) {
        const btnWish = document.createElement('button');
        btnWish.className = 'group h-10 w-10 flex items-center justify-center border-2 border-gray-200 rounded-lg hover:bg-red-50 transition';
        btnWish.title = 'Eliminar de la lista de deseos';
        btnWish.innerHTML = `<svg class="w-6 h-6 text-red-500 transition-colors" fill="currentColor" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/></svg>`;
        btnWish.onclick = () => handleRemoveFromWishlist(productId, btnWish);
        container.appendChild(btnWish);
    }

    // --- 3. FUNCIONES DE MANEJO DE ACCIONES (HANDLERS) ---
    // (Estas funciones no necesitan cambios)

    function handleAddToCart(productId) {
        if (isAuthenticated) {
            $.ajax({
                url: `${contextPath}/api/cart/add_item`,
                type: 'POST',
                data: { itemId: productId, add: true },
            }).done(() => {
                cartState[productId] = (cartState[productId] || 0) + 1;
                renderRowControls(productId);
                window.dispatchEvent(new CustomEvent('cartUpdated', { detail: { newState: cartState } }));
            }).fail(() => {
                alert('Error al añadir el producto al carrito.');
            });
        } else {
            cartState[productId] = (cartState[productId] || 0) + 1;
            localStorage.setItem('cart', JSON.stringify(cartState));
            renderRowControls(productId);
            window.dispatchEvent(new CustomEvent('cartUpdated', { detail: { newState: cartState } }));
        }
    }
    
    function handleRemoveFromWishlist(productId, buttonElement) {
        if (!confirm('¿Estás seguro de que quieres eliminar este producto de tu lista de deseos?')) {
            return;
        }
        const rowToRemove = $(buttonElement).closest('.wishlist-item-row');
        if (isAuthenticated) {
            $.ajax({
                url: `${contextPath}/api/wishlist/delete_item`,
                type: 'POST',
                data: { productId: productId },
            }).done(() => {
                const index = wishlistState.indexOf(productId.toString());
                if (index > -1) wishlistState.splice(index, 1);
                window.dispatchEvent(new CustomEvent('cartUpdated', { detail: { newState: cartState } }));
                rowToRemove.fadeOut(400, () => {
                    rowToRemove.remove();
                    if ($('.wishlist-item-row').length === 0) window.location.reload();
                });
            }).fail(() => {
                alert('Error al eliminar el producto.');
            });
        } else {
            const index = wishlistState.indexOf(productId.toString());
            if (index > -1) wishlistState.splice(index, 1);
            localStorage.setItem('wishlist', JSON.stringify(wishlistState));
            rowToRemove.fadeOut(400, () => {
                rowToRemove.remove();
                if ($('.wishlist-item-row').length === 0) window.location.reload();
            });
            window.dispatchEvent(new CustomEvent('wishlistUpdated'));
        }
    }

    // --- 4. INICIALIZACIÓN ---
    function initializeApp() {
        if (isAuthenticated) {
            // Si el usuario está autenticado, los elementos ya están en el HTML.
            // Solo necesitamos inicializar los botones.
            document.querySelectorAll('.wishlist-item-row').forEach(row => {
                const productId = row.dataset.productId;
                if (productId) {
                    renderRowControls(productId);
                }
            });
        } else {
            // Si el usuario es un invitado, la lista está en localStorage.
            const guestWishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');

            if (guestWishlist.length > 0) {
                // Hay items, llamamos a la API para obtener sus detalles
                $.ajax({
                    url: `${contextPath}/api/wishlist/guest-details`,
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(guestWishlist),
                    success: (wishlistData) => {
                        console.log("Datos recibidos del servidor:", wishlistData); 
                        renderGuestWishlist(wishlistData);
                    },
                    error: (err) => {
                        console.error("Error al cargar los detalles de la wishlist de invitado.", err);
                        // Opcional: mostrar un error en la UI
                        const tbody = document.querySelector('#wishlist-items-body');
                        if (tbody) tbody.innerHTML = `<tr><td colspan="3" class="text-center p-12 text-red-500">No se pudo cargar tu lista. Inténtalo de nuevo.</td></tr>`;
                    }
                });
            } else {
                // No hay items, simplemente renderizamos el estado vacío
                renderGuestWishlist({ items: [] });
            }
        }
        window.dispatchEvent(new CustomEvent('cartUpdated', { detail: { newState: cartState } }));
    }

    // Arrancamos la aplicación
    initializeApp();
});