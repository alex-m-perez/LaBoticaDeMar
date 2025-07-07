document.addEventListener('DOMContentLoaded', () => {
    // --- 1. CONFIGURACIÓN INICIAL ---
    const GUEST_CART_KEY = 'cart';
    const GUEST_WISHLIST_KEY = 'wishlist';
    
    const isAuthenticated = document.body.dataset.authenticated === 'true';
    const contextPath = document.body.dataset.contextPath || '';

    // El estado del carrito y wishlist se carga desde el servidor o localStorage
    let cartState = isAuthenticated ? (userCartState || {}) : JSON.parse(localStorage.getItem(GUEST_CART_KEY) || '{}');
    let wishlistState = isAuthenticated ? (userWishlistState || []) : JSON.parse(localStorage.getItem(GUEST_WISHLIST_KEY) || '[]');
    
    // --- 2. MANEJO DEL ESTADO (GUARDADO) ---

    function saveCartState() {
        debugger
        if (!isAuthenticated) {
            localStorage.setItem(GUEST_CART_KEY, JSON.stringify(cartState));
        }
        window.dispatchEvent(new CustomEvent('cartUpdated'));
    }

    function saveWishlistState() {
        if (!isAuthenticated) {
            localStorage.setItem(GUEST_WISHLIST_KEY, JSON.stringify(wishlistState));
        }
        window.dispatchEvent(new CustomEvent('wishlistUpdated'));
    }

    // --- 3. COMUNICACIÓN CON LA API (para usuarios autenticados) ---

    function updateServerCart(productId, isAdding) {
        return $.ajax({
            url: `${contextPath}/api/cart/add_item`,
            type: 'POST',
            data: { itemId: productId, add: isAdding }
        });
    }

    function deleteFromServerCart(productId) {
        return $.ajax({
            url: `${contextPath}/api/cart/delete_item`,
            type: 'POST',
            data: { itemId: productId }
        });
    }

    function updateServerWishlist(productId, isAdding) {
        const url = isAdding ? `${contextPath}/api/wishlist/add_item` : `${contextPath}/api/wishlist/delete_item`;
        return $.ajax({ url: url, type: 'POST', data: { productId: productId } });
    }

    // --- 4. RENDERIZADO DE CONTROLES (MODIFICACIÓN DEL DOM) ---

    function renderCartControls(container, product) {
        container.innerHTML = '';
        const qty = cartState[product.id] || 0;

        if (qty === 0) {
            // Botón "Añadir"
            const btn = document.createElement('button');
            btn.className = 'ml-2 bg-pistachio text-white px-2 py-2 text-sm rounded hover:bg-dark-pistachio transition flex items-center gap-1';
            btn.innerHTML = `<img src="${contextPath}/images/shopping-cart-white.svg" class="h-5 w-5" alt="Añadir al carrito">`;
            
            if (product.stock === 0) {
                btn.disabled = true;
                btn.className = 'ml-2 bg-gray-200 text-gray-400 px-3 py-1 text-sm rounded cursor-not-allowed';
                btn.textContent = 'Añadir';
            } else {
                btn.onclick = () => handleAddToCart(product, container);
            }
            container.appendChild(btn);
        } else {
            // Controles de cantidad (+/-)
            const wrap = document.createElement('div');
            wrap.className = 'flex items-center border border-gray-200 rounded';
            wrap.innerHTML = `
                <button class="btn-minus px-2 py-1 text-sm text-gray-600 hover:bg-gray-100">−</button>
                <input type="text" value="${qty}" class="w-7 text-center text-sm py-1 border-0 focus:ring-0" readonly>
                <button class="btn-plus px-2 py-1 text-sm text-gray-600 hover:bg-gray-100">+</button>
            `;
            wrap.querySelector('.btn-minus').onclick = () => handleQuantityChange(product, -1, container);
            const plusBtn = wrap.querySelector('.btn-plus');
            plusBtn.onclick = () => handleQuantityChange(product, 1, container);
            if (qty >= product.stock) {
                 plusBtn.disabled = true;
                 plusBtn.classList.add("cursor-not-allowed", "text-gray-300");
            }
            container.appendChild(wrap);
        }
    }

    function renderWishlistStatus(button, productId) {
        const svg = button.querySelector('svg');
        if (wishlistState.includes(productId.toString())) {
            svg.setAttribute('fill', 'currentColor');
            svg.classList.add('text-red-500');
            svg.classList.remove('text-gray-400');
        } else {
            svg.setAttribute('fill', 'none');
            svg.classList.add('text-gray-400');
            svg.classList.remove('text-red-500');
        }
    }

    // --- 5. MANEJADORES DE ACCIONES DEL USUARIO ---

    function handleAddToCart(product, container) {
        if (isAuthenticated) {
            updateServerCart(product.id, true).done(() => {
                cartState[product.id] = 1;
                renderCartControls(container, product);
                saveCartState();
            }).fail(() => alert('No se pudo añadir al carrito.'));
        } else {
            cartState[product.id] = 1;
            saveCartState();
            renderCartControls(container, product);
        }
    }
    
    function handleQuantityChange(product, delta, container) {
        const currentQty = cartState[product.id] || 0;
        const newQty = currentQty + delta;

        if (isAuthenticated) {
            const apiCall = newQty > currentQty ? updateServerCart(product.id, true) : (newQty > 0 ? updateServerCart(product.id, false) : deleteFromServerCart(product.id));
            apiCall.done(() => {
                if (newQty > 0) cartState[product.id] = newQty;
                else delete cartState[product.id];
                renderCartControls(container, product);
                saveCartState();
            }).fail(() => alert('No se pudo actualizar el carrito.'));
        } else {
            if (newQty > 0) cartState[product.id] = newQty;
            else delete cartState[product.id];
            saveCartState();
            renderCartControls(container, product);
        }
    }

    function handleWishlistToggle(product, button) {
        const productIdStr = product.id.toString();
        const isAdding = !wishlistState.includes(productIdStr);

        if (isAuthenticated) {
            updateServerWishlist(product.id, isAdding).done(() => {
                isAdding ? wishlistState.push(productIdStr) : wishlistState.splice(wishlistState.indexOf(productIdStr), 1);
                renderWishlistStatus(button, product.id);
                saveWishlistState();
            }).fail(() => alert('No se pudo actualizar tu lista de deseos.'));
        } else {
            isAdding ? wishlistState.push(productIdStr) : wishlistState.splice(wishlistState.indexOf(productIdStr), 1);
            saveWishlistState();
            renderWishlistStatus(button, product.id);
        }
    }

    // --- 6. INICIALIZACIÓN DE LA PÁGINA ---

    function initializeApp() {
        document.querySelectorAll('.product-card').forEach(card => {
            const productData = JSON.parse(card.dataset.product);
            const cartContainer = card.querySelector('.cart-controls-container');
            const wishlistBtn = card.querySelector('.wishlist-btn');

            if (cartContainer) renderCartControls(cartContainer, productData);
            if (wishlistBtn) {
                renderWishlistStatus(wishlistBtn, productData.id);
                wishlistBtn.onclick = () => handleWishlistToggle(productData, wishlistBtn);
            }
        });
    }

    initializeApp();
});