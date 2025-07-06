//wishlist.js
// Se ejecuta cuando el DOM está completamente cargado
document.addEventListener('DOMContentLoaded', () => {

    // --- 1. CONFIGURACIÓN Y ESTADO ---
    const isAuthenticated = document.body.dataset.authenticated === 'true';
    const contextPath = window.contextPath || '';
    
    let cartState = isAuthenticated ? userCartState : JSON.parse(localStorage.getItem('cart') || '{}');
    let wishlistState = isAuthenticated ? userWishlistState : JSON.parse(localStorage.getItem('wishlist') || '[]');

    const iconCartPath = `${contextPath}/images/shopping-cart-white.svg`;
    const iconCheckPath = `${contextPath}/images/check.svg`;

    // --- 2. FUNCIONES DE RENDERIZADO DE BOTONES ---

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
    // (Sin cambios)
    document.querySelectorAll('.wishlist-item-row').forEach(row => {
        const productId = row.dataset.productId;
        if (productId) {
            renderRowControls(productId);
        }
    });
});