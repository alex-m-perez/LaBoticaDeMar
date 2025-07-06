//product_detail.js
document.addEventListener('DOMContentLoaded', () => {

    // =================================================================
    // PARTE 1: CONFIGURACIÓN CENTRAL Y ESTADO UNIFICADO (SIN CAMBIOS)
    // =================================================================
    const isAuthenticated = document.body.dataset.authenticated === 'true';
    const contextPath = window.contextPath || '';
    
    let cartState = isAuthenticated ? userCartState : JSON.parse(localStorage.getItem('cart') || '{}');
    let wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');

    const iconCartPath = `${contextPath}/images/shopping-cart-white.svg`;
    const iconCheckPath = `${contextPath}/images/check.svg`;

    function saveCartState() {
        if (!isAuthenticated) {
            localStorage.setItem('cart', JSON.stringify(cartState));
        }
        window.dispatchEvent(new CustomEvent('cartUpdated'));
    }

    function saveWishlist() {
        localStorage.setItem('wishlist', JSON.stringify(wishlist));
        window.dispatchEvent(new CustomEvent('wishlistUpdated'));
    }

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

    // ================================================================================
    // PARTE 2: LÓGICA DEL PRODUCTO PRINCIPAL CON ESTILOS ACTUALIZADOS
    // ================================================================================
    function initializeMainProductControls() {
        const container = document.getElementById('product-detail-container');
        if (!container) return;

        const productId = container.dataset.productId;
        const stock = parseInt(container.dataset.productStock, 10);
        
        const qtyContainer = document.getElementById('main-product-qty-container');
        const cartContainer = document.getElementById('main-product-cart-container');
        const wishlistContainer = document.getElementById('main-product-wishlist-container');
        
        const renderMainControls = () => {
            const qtyInCart = cartState[productId] || 0;
            
            qtyContainer.innerHTML = '';
            cartContainer.innerHTML = '';
            wishlistContainer.innerHTML = '';

            renderMainWishlistButton(wishlistContainer, productId);

            if (qtyInCart === 0) {
                qtyContainer.classList.add('hidden');
                
                const btnAdd = document.createElement('button');
                // **ESTILO:** Clases para altura y padding uniformes
                btnAdd.className = 'h-12 px-6 py-3 flex gap-2 items-center font-semibold rounded-lg transition';
                
                if (stock > 0) {
                    btnAdd.classList.add('bg-pistachio', 'text-white', 'hover:bg-dark-pistachio');
                    btnAdd.innerHTML = `<span>AÑADIR</span><img src="${iconCartPath}" class="h-5" alt="Añadir"/>`;
                    btnAdd.onclick = () => handleQuantityChange(true, true);
                } else {
                    btnAdd.disabled = true;
                    btnAdd.textContent = 'SIN STOCK';
                    btnAdd.classList.add('bg-gray-400', 'text-white', 'cursor-not-allowed');
                }
                cartContainer.appendChild(btnAdd);

            } else {
                qtyContainer.classList.remove('hidden');

                const wrap = document.createElement('div');
                // **ESTILO:** Clases para altura y padding uniformes
                wrap.className = 'h-12 flex items-center border-2 border-gray-200 rounded-lg';
                
                const btnMinus = document.createElement('button');
                btnMinus.className = 'px-3 h-full text-lg text-gray-600 hover:bg-gray-100 rounded-l-md transition';
                btnMinus.textContent = '−';
                btnMinus.onclick = () => handleQuantityChange(false);
                
                const input = document.createElement('input');
                input.type = 'text';
                input.value = qtyInCart;
                input.className = 'w-12 h-full text-center text-md font-bold border-0 focus:ring-0 bg-transparent';
                input.readOnly = true;
                
                const btnPlus = document.createElement('button');
                btnPlus.className = 'px-3 h-full text-lg text-gray-600 hover:bg-gray-100 rounded-r-md transition';
                btnPlus.textContent = '+';
                btnPlus.disabled = qtyInCart >= stock;
                if(btnPlus.disabled) btnPlus.classList.add('cursor-not-allowed', 'text-gray-300');
                btnPlus.onclick = () => handleQuantityChange(true);
                
                wrap.appendChild(btnMinus);
                wrap.appendChild(input);
                wrap.appendChild(btnPlus);
                qtyContainer.appendChild(wrap);

                const btnAdded = document.createElement('button');
                // **ESTILO:** Clases para altura y padding uniformes. cursor-default para quitar hover.
                btnAdded.className = 'h-12 px-6 py-3 flex gap-2 items-center bg-pistachio text-white font-semibold rounded-lg cursor-default';
                btnAdded.innerHTML = `<span>AÑADIDO</span><img src="${iconCheckPath}" class="h-5" alt="Añadido"/>`;
                cartContainer.appendChild(btnAdded);
            }
        };

        const handleQuantityChange = (isIncrement, isFirstTime = false) => {
            const qtyInCart = cartState[productId] || 0;
            
            if(isIncrement) {
                if(qtyInCart >= stock && !isFirstTime) return;
                const qtyToAdd = isFirstTime ? 1 : qtyInCart + 1;

                if (isAuthenticated) {
                    updateServerCart(productId, true).done(() => {
                        cartState[productId] = qtyToAdd;
                        saveCartState();
                        renderMainControls();
                    });
                } else {
                    cartState[productId] = qtyToAdd;
                    saveCartState();
                    renderMainControls();
                }
            } else { // Decrement
                if (qtyInCart <= 1) {
                    if (isAuthenticated) {
                        deleteFromServerCart(productId).done(() => {
                            delete cartState[productId];
                            saveCartState();
                            renderMainControls();
                        });
                    } else {
                        delete cartState[productId];
                        saveCartState();
                        renderMainControls();
                    }
                } else {
                    if (isAuthenticated) {
                        updateServerCart(productId, false).done(() => {
                           cartState[productId] = qtyInCart - 1;
                           saveCartState();
                           renderMainControls();
                        });
                    } else {
                        cartState[productId] = qtyInCart - 1;
                        saveCartState();
                        renderMainControls();
                    }
                }
            }
        };

        const renderMainWishlistButton = (container, productId) => {
             const btnWish = document.createElement('button');
             // **ESTILO:** Clases para altura y padding. 'group' para habilitar el hover en el SVG hijo.
             btnWish.className = 'group h-12 w-12 flex items-center justify-center border-2 border-gray-200 rounded-lg hover:bg-red-50 transition';
             
             const isFavorited = wishlist.includes(productId.toString());
             const fillColor = isFavorited ? 'currentColor' : 'none';
             // **ESTILO:** Clases para el hover del icono. 'group-hover:text-red-500' hace que el icono reaccione al hover del botón.
             const textColor = isFavorited ? 'text-red-500' : 'text-gray-400 group-hover:text-red-500';
             
             btnWish.innerHTML = `<svg class="w-7 h-7 ${textColor} transition-colors" fill="${fillColor}" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/></svg>`;
             
             btnWish.onclick = () => {
                const idStr = productId.toString();
                const index = wishlist.indexOf(idStr);
                if(index > -1) { wishlist.splice(index, 1); } else { wishlist.push(idStr); }
                saveWishlist();
                renderMainControls();
             };
             container.appendChild(btnWish);
        };
        
        renderMainControls();
    }


    // ======================================================================
    // PARTE 3: LÓGICA PARA LOS CARRUSELES (SIN NINGÚN CAMBIO)
    // ======================================================================
    function renderCarouselCartControls(container, product) {
        container.innerHTML = '';
        const qty = cartState[product.id] || 0;

        if (qty === 0) {
            const btn = document.createElement('button');
            if (product.stock === 0) {
                btn.disabled = true;
                btn.className = 'ml-2 bg-gray-200 text-gray-400 px-2 py-2 text-sm rounded cursor-not-allowed flex items-center';
            } else {
                btn.className = 'ml-2 bg-pistachio text-white px-2 py-2 text-sm rounded hover:bg-dark-pistachio transition flex items-center gap-1';
                btn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    if (isAuthenticated) {
                        updateServerCart(product.id, true).done(() => {
                            cartState[product.id] = (cartState[product.id] || 0) + 1;
                            renderCarouselCartControls(container, product);
                            window.dispatchEvent(new CustomEvent('cartUpdated'));
                        });
                    } else {
                        cartState[product.id] = 1;
                        saveCartState();
                        renderCarouselCartControls(container, product);
                    }
                });
            }
            btn.innerHTML = `<img src="${contextPath}/images/shopping-cart-white.svg" class="h-5 w-5" alt="Añadir al carrito"/>`;
            container.appendChild(btn);
        } else {
            const wrap = document.createElement('div');
            wrap.className = 'flex items-center border border-gray-200 rounded';

            const btnMinus = document.createElement('button');
            btnMinus.className = 'px-2 py-1 text-sm text-gray-600 hover:bg-gray-100';
            btnMinus.textContent = '−';
            btnMinus.addEventListener('click', (e) => {
                e.stopPropagation();
                if (isAuthenticated) {
                    if (qty > 1) {
                        updateServerCart(product.id, false).done(() => {
                            cartState[product.id]--;
                            renderCarouselCartControls(container, product);
                            window.dispatchEvent(new CustomEvent('cartUpdated'));
                        });
                    } else {
                        deleteFromServerCart(product.id).done(() => {
                            delete cartState[product.id];
                            renderCarouselCartControls(container, product);
                            window.dispatchEvent(new CustomEvent('cartUpdated'));
                        });
                    }
                } else {
                    if (qty > 1) { cartState[product.id]--; } else { delete cartState[product.id]; }
                    saveCartState();
                    renderCarouselCartControls(container, product);
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
            btnPlus.addEventListener('click', (e) => {
                e.stopPropagation();
                if (qty >= product.stock) return;
                if (isAuthenticated) {
                    updateServerCart(product.id, true).done(() => {
                        cartState[product.id]++;
                        renderCarouselCartControls(container, product);
                        window.dispatchEvent(new CustomEvent('cartUpdated'));
                    });
                } else {
                    cartState[product.id]++;
                    saveCartState();
                    renderCarouselCartControls(container, product);
                }
            });
            
            wrap.appendChild(btnMinus);
            wrap.appendChild(input);
            wrap.appendChild(btnPlus);
            container.appendChild(wrap);
        }
    }

    function renderCarouselWishlistStatus(button, productId) {
        const idStr = productId.toString();
        const svg = button.querySelector('svg');
        if (wishlist.includes(idStr)) {
            svg.setAttribute('fill', 'currentColor');
            svg.classList.add('text-red-500');
            svg.classList.remove('text-gray-400');
        } else {
            svg.setAttribute('fill', 'none');
            svg.classList.remove('text-red-500');
            svg.classList.add('text-gray-400');
        }
    }

    // ==========================================================
    // PARTE 4: INICIALIZACIÓN DE TODOS LOS COMPONENTES
    // ==========================================================
    
    initializeMainProductControls();

    document.querySelectorAll('.product-card').forEach(card => {
        try {
            const productData = JSON.parse(card.dataset.product);
            const cartContainer = card.querySelector('.cart-controls-container');
            const wishlistBtn = card.querySelector('.wishlist-btn');

            if (cartContainer) {
                renderCarouselCartControls(cartContainer, productData);
            }

            if (wishlistBtn) {
                renderCarouselWishlistStatus(wishlistBtn, productData.id);
                wishlistBtn.addEventListener('click', (e) => {
                    e.stopPropagation();
                    const productIdStr = productData.id.toString();
                    const index = wishlist.indexOf(productIdStr);

                    if (index > -1) {
                        wishlist.splice(index, 1);
                    } else {
                        wishlist.push(productIdStr);
                    }
                    saveWishlist();
                    renderCarouselWishlistStatus(wishlistBtn, productData.id);
                });
            }
        } catch (error) {
            console.error("Error al procesar una tarjeta de producto del carrusel:", error, card.dataset.product);
        }
    });
});