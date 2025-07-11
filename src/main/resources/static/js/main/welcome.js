
// --- 1. CONFIGURACIÓN INICIAL ---
const GUEST_CART_KEY = 'cart';
const GUEST_WISHLIST_KEY = 'wishlist';

const isAuthenticated = document.body.dataset.authenticated === 'true';
const contextPath = document.body.dataset.contextPath || '';

// El estado se carga desde el servidor (si está autenticado) o localStorage (si es invitado)
let cartState = isAuthenticated ? (userCartState || {}) : JSON.parse(localStorage.getItem(GUEST_CART_KEY) || '{}');
let wishlistState = isAuthenticated ? (userWishlistState || []) : JSON.parse(localStorage.getItem(GUEST_WISHLIST_KEY) || '[]');

// --- 2. MANEJO DEL ESTADO (GUARDADO) ---

function saveCartState() {
    if (!isAuthenticated) {
        localStorage.setItem(GUEST_CART_KEY, JSON.stringify(cartState));
    }
    // ¡LA MAGIA ESTÁ AQUÍ! Enviamos el estado dentro del evento.
    window.dispatchEvent(new CustomEvent('cartUpdated', { detail: { newState: cartState } }));
}

function saveWishlistState() {
    if (!isAuthenticated) {
        localStorage.setItem(GUEST_WISHLIST_KEY, JSON.stringify(wishlistState));
    }
    window.dispatchEvent(new CustomEvent('wishlistUpdated'));
}

// --- 3. COMUNICACIÓN CON LA API ---

// ... (las funciones de API para el carrito no cambian)
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

/**
 * **NUEVO:** Envía los datos locales (carrito y wishlist) al servidor para fusionarlos.
 */
function mergeGuestDataOnLogin() {
    const guestCart = JSON.parse(localStorage.getItem(GUEST_CART_KEY));
    const guestWishlist = JSON.parse(localStorage.getItem(GUEST_WISHLIST_KEY));

    const promises = []; // Un array para guardar las llamadas asíncronas

    // 1. Preparamos la promesa para fusionar el carrito (si existe)
    if (guestCart && Object.keys(guestCart).length > 0) {
        const cartPromise = $.ajax({
            url: `${contextPath}/api/cart/merge`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(guestCart)
        }).done(() => localStorage.removeItem(GUEST_CART_KEY));
        promises.push(cartPromise);
    }

    // 2. Preparamos la promesa para fusionar la wishlist (si existe)
    if (guestWishlist && guestWishlist.length > 0) {
        const wishlistPromise = $.ajax({
            url: `${contextPath}/api/wishlist/merge`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(guestWishlist)
        }).done(() => localStorage.removeItem(GUEST_WISHLIST_KEY));
        promises.push(wishlistPromise);
    }

    // 3. Si hay alguna operación pendiente, esperamos a que todas terminen
    if (promises.length > 0) {
        Promise.all(promises)
            .then(() => {
                console.log('¡Sincronización completada! Refrescando la página...');
                // Una vez todo se ha fusionado, recargamos para que la UI se actualice
                location.reload(); 
            })
            .catch((error) => {
                console.error('Ocurrió un error durante la sincronización:', error);
            });
    }
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
    const productIdStr = product.id.toString(); // Clave: siempre manejar el ID como string
    const isAdding = !wishlistState.includes(productIdStr);

    if (isAuthenticated) {
        updateServerWishlist(product.id, isAdding).done(() => {
            if (isAdding) {
                wishlistState.push(productIdStr);
            } else {
                wishlistState.splice(wishlistState.indexOf(productIdStr), 1);
            }
            renderWishlistStatus(button, product.id);
            saveWishlistState();
        }).fail(() => alert('No se pudo actualizar tu lista de deseos.'));
    } else {
        if (isAdding) {
            wishlistState.push(productIdStr);
        } else {
            wishlistState.splice(wishlistState.indexOf(productIdStr), 1);
        }
        saveWishlistState();
        renderWishlistStatus(button, product.id);
    }
}

// --- 6. INICIALIZACIÓN DE LA PÁGINA ---
function syncUI() {
    cartState = isAuthenticated ? userCartState : JSON.parse(localStorage.getItem('cart') || '{}');
    wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
    
    // El resto es la lógica que ya tenías
    if (isAuthenticated) {
        mergeGuestDataOnLogin();
    }

    window.dispatchEvent(new CustomEvent('cartUpdated', { detail: { newState: cartState } }));

    document.querySelectorAll('.product-card').forEach(card => {
        // El ID del producto SIEMPRE debe ser un string para evitar problemas con BigDecimal (e.g., 123.0 vs 123)
        const productData = JSON.parse(card.dataset.product, (key, value) => {
            if (key === 'id') return value.toString();
            return value;
        });

        const cartContainer = card.querySelector('.cart-controls-container');
        const wishlistBtn = card.querySelector('.wishlist-btn');

        if (cartContainer) renderCartControls(cartContainer, productData);
        if (wishlistBtn) {
            renderWishlistStatus(wishlistBtn, productData.id);
            wishlistBtn.onclick = () => handleWishlistToggle(productData, wishlistBtn);
        }
    });
}

// Llamamos a la función cuando el DOM está listo (carga inicial)
document.addEventListener('DOMContentLoaded', syncUI);

// Y la volvemos a llamar si la página se restaura desde la caché del navegador (al usar el botón "Atrás")
window.addEventListener('pageshow', (event) => {
    if (event.persisted) {
        console.log("Página restaurada desde bfcache. Sincronizando UI...");
        syncUI();
    }
});



/*
function initializeApp() {
    // **NUEVO**: Si el usuario está autenticado, intentar fusionar los datos de invitado.
    if (isAuthenticated) {
        mergeGuestDataOnLogin();
    }

    window.dispatchEvent(new CustomEvent('cartUpdated', { detail: { newState: cartState } }));

    document.querySelectorAll('.product-card').forEach(card => {
        // El ID del producto SIEMPRE debe ser un string para evitar problemas con BigDecimal (e.g., 123.0 vs 123)
        const productData = JSON.parse(card.dataset.product, (key, value) => {
            if (key === 'id') return value.toString();
            return value;
        });

        const cartContainer = card.querySelector('.cart-controls-container');
        const wishlistBtn = card.querySelector('.wishlist-btn');

        if (cartContainer) renderCartControls(cartContainer, productData);
        if (wishlistBtn) {
            renderWishlistStatus(wishlistBtn, productData.id);
            wishlistBtn.onclick = () => handleWishlistToggle(productData, wishlistBtn);
        }
    });
}

document.addEventListener('DOMContentLoaded', initializeApp);
*/