document.addEventListener('DOMContentLoaded', () => {
    // --- 1. CONFIGURACIÓN INICIAL ---
    const isAuthenticated = document.body.dataset.authenticated === 'true';
    const contextPath = document.body.dataset.contextPath || (document.querySelector('script[src*="/js/welcome/welcome.js"]') ? document.querySelector('script[src*="/js/welcome/welcome.js"]').src.split('/js/')[0] : '');
    if (contextPath) document.body.dataset.contextPath = contextPath;

    // MODIFICADO: Unificamos el estado de ambos, carrito y wishlist
    let cartState = isAuthenticated ? userCartState : JSON.parse(localStorage.getItem('cart') || '{}');
    // La variable 'userWishlistState' debe venir del servidor, como 'userCartState'
    let wishlistState = isAuthenticated ? userWishlistState : JSON.parse(localStorage.getItem('wishlist') || '[]');

    function saveCartState() {
        if (!isAuthenticated) {
            localStorage.setItem('cart', JSON.stringify(cartState));
        }
        window.dispatchEvent(new CustomEvent('cartUpdated'));
    }

    // MODIFICADO: Renombrada y ahora usa 'wishlistState'
    function saveWishlistState() {
        if (!isAuthenticated) {
            localStorage.setItem('wishlist', JSON.stringify(wishlistState));
        }
        window.dispatchEvent(new CustomEvent('wishlistUpdated'));
    }

    // --- 2. LÓGICA DE ACTUALIZACIÓN (CARRITO Y WISHLIST) ---

    // Funciones del carrito (sin cambios)
    function updateServerCart(productId, isAdding, container, product) {
        $.ajax({
            url: `${contextPath}/api/cart/add_item`,
            type: 'POST',
            data: { itemId: productId, add: isAdding },
            success: function() {
                const currentQty = cartState[productId] || 0;
                cartState[productId] = isAdding ? currentQty + 1 : currentQty - 1;
                renderCartControls(container, product);
                window.dispatchEvent(new CustomEvent('cartUpdated'));
            },
            error: function() { alert('No se pudo actualizar el carrito.'); }
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
                window.dispatchEvent(new CustomEvent('cartUpdated'));
            },
            error: function() { alert('No se pudo eliminar el producto del carrito.'); }
        });
    }

    // --- NUEVA FUNCIÓN PARA LA WISHLIST CON AJAX ---
    function updateServerWishlist(productId, isAdding, button) {
        const url = isAdding ? `${contextPath}/api/wishlist/add_item` : `${contextPath}/api/wishlist/delete_item`;
        
        $.ajax({
            url: url,
            type: 'POST',
            data: { productId: productId },
            success: function() {
                const productIdStr = productId.toString();
                if (isAdding) {
                    if (!wishlistState.includes(productIdStr)) {
                        wishlistState.push(productIdStr);
                    }
                } else {
                    const index = wishlistState.indexOf(productIdStr);
                    if (index > -1) {
                        wishlistState.splice(index, 1);
                    }
                }
                // Actualiza el aspecto del botón y notifica a otros componentes
                renderWishlistStatus(button, productId);
                window.dispatchEvent(new CustomEvent('wishlistUpdated'));
            },
            error: function() {
                alert('No se pudo actualizar tu lista de deseos.');
            }
        });
    }

    // --- 3. RENDERIZADO DE CONTROLES ---

    // renderCartControls no necesita cambios
    function renderCartControls(container, product) {
        container.innerHTML = '';
        const qty = cartState[product.id] || 0;
        if (qty === 0) {
            const btn = document.createElement('button');
            if (product.stock === 0) {
                btn.disabled = true;
                btn.className = 'ml-2 bg-gray-200 text-gray-400 px-3 py-1 text-sm rounded cursor-not-allowed';
                btn.textContent = 'Añadir';
            } else {
                btn.className = 'ml-2 bg-pistachio text-white px-2 py-2 text-sm rounded hover:bg-dark-pistachio transition flex items-center gap-1';
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
    
    // MODIFICADO: Ahora usa 'wishlistState' en lugar de 'wishlist'
    function renderWishlistStatus(button, productId) {
        const svg = button.querySelector('svg');
        if (wishlistState.includes(productId.toString())) {
            svg.setAttribute('fill', 'currentColor');
            svg.classList.remove('text-gray-400');
            svg.classList.add('text-red-500');
        } else {
            svg.setAttribute('fill', 'none');
            svg.classList.remove('text-red-500');
            svg.classList.add('text-gray-400');
        }
    }
    
    // --- 4. INICIALIZACIÓN DE TARJETAS DE PRODUCTO ---
    const productCards = document.querySelectorAll('.product-card');

    productCards.forEach(card => {
        const productData = JSON.parse(card.dataset.product);
        const cartContainer = card.querySelector('.cart-controls-container');
        const wishlistBtn = card.querySelector('.wishlist-btn');

        if (cartContainer) {
            renderCartControls(cartContainer, productData);
        }

        if (wishlistBtn) {
            // Renderiza el estado inicial del corazón al cargar la página
            renderWishlistStatus(wishlistBtn, productData.id);
            
            // MODIFICADO: Listener con lógica condicional
            wishlistBtn.addEventListener('click', () => {
                const productIdStr = productData.id.toString();
                const isInWishlist = wishlistState.includes(productIdStr);

                if (isAuthenticated) {
                    // --- Usuario Autenticado: Llama al servidor ---
                    // Si está en la lista, queremos eliminarlo (isAdding = false)
                    // Si no está, queremos añadirlo (isAdding = true)
                    updateServerWishlist(productData.id, !isInWishlist, wishlistBtn);

                } else {
                    // --- Usuario Invitado: Usa localStorage ---
                    const index = wishlistState.indexOf(productIdStr);
                    if (index > -1) {
                        wishlistState.splice(index, 1); // Eliminar
                    } else {
                        wishlistState.push(productIdStr); // Añadir
                    }
                    saveWishlistState();
                    renderWishlistStatus(wishlistBtn, productData.id);
                }
            });
        }
    });
});