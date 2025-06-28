document.addEventListener('DOMContentLoaded', () => {
// --- 1. CONFIGURACIÓN INICIAL ---
const isAuthenticated = document.body.dataset.authenticated === 'true';
const contextPath = document.body.dataset.contextPath || (document.querySelector('script[src*="/js/welcome/welcome.js"]') ? document.querySelector('script[src*="/js/welcome/welcome.js"]').src.split('/js/')[0] : '');
if(contextPath) document.body.dataset.contextPath = contextPath;

// Unificamos el estado del carrito. Si está autenticado, usa el objeto del servidor. Si no, el de localStorage.
let cartState = isAuthenticated ? userCartState : JSON.parse(localStorage.getItem('cart') || '{}');
let wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');

function saveCartState() {
    if (!isAuthenticated) {
        localStorage.setItem('cart', JSON.stringify(cartState));
    }
    // Para usuarios autenticados, el estado se guarda en el servidor, no hace falta hacer nada aquí.
    window.dispatchEvent(new CustomEvent('cartUpdated'));
}

function saveWishlist() {
    localStorage.setItem('wishlist', JSON.stringify(wishlist));
    window.dispatchEvent(new CustomEvent('wishlistUpdated'));
}

// --- 2. LÓGICA DE ACTUALIZACIÓN DEL CARRITO (CON AJAX PARA USUARIOS AUTENTICADOS) ---

function updateServerCart(productId, isAdding, container, product) {
    $.ajax({
        url: `${contextPath}/api/cart/add_item`,
        type: 'POST',
        data: { itemId: productId, add: isAdding },
        success: function() {
            // Actualiza el estado local y re-renderiza los controles
            const currentQty = cartState[productId] || 0;
            cartState[productId] = isAdding ? currentQty + 1 : currentQty - 1;
            renderCartControls(container, product);
            window.dispatchEvent(new CustomEvent('cartUpdated')); // Notifica al navbar
        },
        error: function() {
            alert('No se pudo actualizar el carrito.');
        }
    });
}

function deleteFromServerCart(productId, container, product) {
    $.ajax({
        url: `${contextPath}/api/cart/delete_item`,
        type: 'POST',
        data: { itemId: productId },
        success: function() {
            delete cartState[productId];
            renderCartControls(container, product);
            window.dispatchEvent(new CustomEvent('cartUpdated')); // Notifica al navbar
        },
        error: function() {
            alert('No se pudo eliminar el producto del carrito.');
        }
    });
}

// --- 3. RENDERIZADO DE CONTROLES (AHORA CON LÓGICA CONDICIONAL) ---

function renderCartControls(container, product) {
    container.innerHTML = '';
    const qty = cartState[product.id] || 0;

    if (qty === 0) {
        // Botón "Añadir"
        const btn = document.createElement('button');
        if (product.stock === 0) {
            btn.disabled = true;
            btn.className = 'ml-2 bg-gray-200 text-gray-400 px-3 py-1 text-sm rounded cursor-not-allowed';
            btn.textContent = 'Añadir';
        } else {
            btn.className = 'ml-2 bg-pistachio text-white px-2 py-2 text-sm rounded hover:bg-pistachio-dark transition flex items-center gap-1';
            const imgIcon = document.createElement('img');
            imgIcon.src = `${contextPath}/images/shopping-cart-white.svg`;
            imgIcon.className = 'h-5 w-5';
            imgIcon.alt = 'Añadir al carrito';
            btn.appendChild(imgIcon);

            btn.addEventListener('click', () => {
                if (isAuthenticated) {
                    updateServerCart(product.id, true, container, product);
                } else {
                    cartState[product.id] = 1;
                    saveCartState();
                    renderCartControls(container, product);
                }
            });
        }
        container.appendChild(btn);
    } else {
        // Controles +/-
        const wrap = document.createElement('div');
        wrap.className = 'flex items-center border border-gray-200 rounded';

        const btnMinus = document.createElement('button');
        btnMinus.className = 'px-2 py-1 text-sm text-gray-600 hover:bg-gray-100';
        btnMinus.textContent = '−';
        btnMinus.addEventListener('click', () => {
            const currentQty = cartState[product.id] || 0;
            if (isAuthenticated) {
                if (currentQty > 1) {
                    updateServerCart(product.id, false, container, product);
                } else {
                    // Si la cantidad es 1 y se resta, se elimina el item
                    deleteFromServerCart(product.id, container, product);
                }
            } else {
                const newQty = currentQty - 1;
                if (newQty > 0) {
                    cartState[product.id] = newQty;
                } else {
                    delete cartState[product.id];
                }
                saveCartState();
                renderCartControls(container, product);
            }
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
            if (isAuthenticated) {
                updateServerCart(product.id, true, container, product);
            } else {
                cartState[product.id] = (cartState[product.id] || 0) + 1;
                saveCartState();
                renderCartControls(container, product);
            }
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