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
        tbody.innerHTML = ''; // Limpiamos el cuerpo de la tabla

        // Si la wishlist está vacía, muestra un mensaje
        if (!wishlistData.items || wishlistData.items.length === 0) {
            tbody.innerHTML = `<tr><td colspan="3" class="text-center p-12"><p class="text-gray-600 text-2xl font-bold mb-4">Tu lista de deseos está vacía.</p><a href="${contextPath}/" class="inline-block bg-pistachio text-white px-6 py-2 rounded hover:bg-dark-pistachio">Descubrir productos</a></td></tr>`;
            return;
        }

        // Si hay items, crea una fila para cada uno
        wishlistData.items.forEach(item => {
            const product = item; // En la wishlist, el item es directamente el producto
            const rowHTML = `
                <tr class="wishlist-item-row border-b" data-product-id="${product.id}">
                    <td class="py-4 px-2 md:px-4">
                        <div class="flex items-center space-x-4">
                            <img src="${contextPath}${product.imagenPath}" alt="${product.nombre}" class="h-20 w-20 object-cover rounded-lg">
                            <div>
                                <p class="font-bold text-base">${product.nombre}</p>
                                <p class="text-sm text-gray-500">${product.laboratorioNombre}</p>
                            </div>
                        </div>
                    </td>
                    <td class="py-4 px-2 md:px-4 text-center">
                        <span class="font-semibold text-lg">${new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(product.price)}</span>
                    </td>
                    <td class="py-4 px-2 md:px-4">
                        <div class="flex flex-col md:flex-row items-center justify-center gap-2">
                            <div class="cart-button-container"></div>
                            <div class="wishlist-button-container"></div>
                        </div>
                    </td>
                </tr>`;
            tbody.insertAdjacentHTML('beforeend', rowHTML);
        });

        // IMPORTANTE: Una vez renderizadas las filas, inicializa sus controles
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
                window.dispatchEvent(new CustomEvent('cartUpdated'));
            }).fail(() => {
                alert('Error al añadir el producto al carrito.');
            });
        } else {
            cartState[productId] = (cartState[productId] || 0) + 1;
            localStorage.setItem('cart', JSON.stringify(cartState));
            renderRowControls(productId);
            window.dispatchEvent(new CustomEvent('cartUpdated'));
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
                window.dispatchEvent(new CustomEvent('cartUpdated'));
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
    }

    // Arrancamos la aplicación
    initializeApp();
});