//product-grid.js
// ==========================================================
// SECCIÓN 1: CONFIGURACIÓN Y ESTADO UNIFICADO (NUEVO)
// ==========================================================
const isAuthenticated = document.body.dataset.authenticated === 'true';
let cartState = isAuthenticated ? userCartState : JSON.parse(localStorage.getItem('cart') || '{}');
let wishlistState = isAuthenticated ? userWishlistState : JSON.parse(localStorage.getItem('wishlist') || '[]');


// ==========================================================
// SECCIÓN 2: VARIABLES GLOBALES Y ELEMENTOS DEL DOM (ORIGINAL)
// ==========================================================
var currentPage = 0,
    totalPages  = 1,
    currentCols = 0;

// Elementos del DOM
var gridEl     = document.getElementById('productsGrid'),
    prevBtn    = document.getElementById('prevBtn'),
    nextBtn    = document.getElementById('nextBtn'),
    pageNumEl  = document.getElementById('pageNum'),
    totalEl    = document.getElementById('totalPages'),
    filterForm = document.getElementById('filterForm');


// ==========================================================
// SECCIÓN 3: FUNCIONES DE AYUDA Y AJAX (NUEVO)
// ==========================================================

// Guarda el estado del carrito (solo para invitados) y notifica a la app.
function saveCartState() {
    if (!isAuthenticated) {
        localStorage.setItem('cart', JSON.stringify(cartState));
    }
    window.dispatchEvent(new CustomEvent('cartUpdated'));
}

// Funciones AJAX para usuarios autenticados
function updateServerCart(productId, isAdding, container, product) {
    fetch(window.contextPath + '/api/cart/add_item', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ itemId: productId, add: isAdding })
    })
    .then(response => {
        if (!response.ok) throw new Error('Error de red al actualizar el carrito');
        const currentQty = cartState[productId] || 0;
        cartState[productId] = isAdding ? currentQty + 1 : currentQty - 1;
        renderCartControls(container, product);
        window.dispatchEvent(new CustomEvent('cartUpdated'));
    })
    .catch(error => alert('No se pudo actualizar el carrito. ' + error.message));
}

function deleteFromServerCart(productId, container, product) {
    fetch(window.contextPath + '/api/cart/delete_item', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ itemId: productId })
    })
    .then(response => {
        if (!response.ok) throw new Error('Error de red al eliminar el producto');
        delete cartState[productId];
        renderCartControls(container, product);
        window.dispatchEvent(new CustomEvent('cartUpdated'));
    })
    .catch(error => alert('No se pudo eliminar el producto. ' + error.message));
}

function saveWishlistState() {
    if (!isAuthenticated) {
        localStorage.setItem('wishlist', JSON.stringify(wishlistState));
    }
    window.dispatchEvent(new CustomEvent('wishlistUpdated'));
}

function updateServerWishlist(productId, isAdding, button) {
    const url = isAdding 
        ? `${window.contextPath}/api/wishlist/add_item` 
        : `${window.contextPath}/api/wishlist/delete_item`;
    
    fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ productId: productId })
    })
    .then(response => {
        if (!response.ok) throw new Error('Error de red');
        
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
        
        renderWishlistStatus(button, productId);
        window.dispatchEvent(new CustomEvent('wishlistUpdated'));
    })
    .catch(() => alert('No se pudo actualizar tu lista de deseos.'));
}

// --- NUEVA FUNCIÓN PARA RENDERIZAR EL BOTÓN DE WISHLIST ---
// Cambia el icono del corazón (relleno o vacío) según si el producto está en la lista.
function renderWishlistStatus(button, productId) {
    const isInWishlist = wishlistState.includes(productId.toString());
    
    if (isInWishlist) {
        // Corazón relleno (producto en la wishlist)
        button.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-red-500" viewBox="0 0 20 20" fill="currentColor"><path d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.828a4 4 0 010-5.657z"/></svg>';
    } else {
        // Corazón vacío (producto no en la wishlist)
        button.innerHTML = '<svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-gray-400 hover:text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/></svg>';
    }
}


// ==========================================================
// SECCIÓN 4: RENDERIZADO DE CONTROLES DE CARRITO (REEMPLAZO TOTAL)
// ==========================================================
function renderCartControls(container, p) {
    container.innerHTML = '';
    const qty = cartState[p.id] || 0;

    if (qty === 0) {
        // --- Botón de "Añadir al carrito" como icono ---
        const btn = document.createElement('button');
        const icon = document.createElement('img');
        icon.className = 'h-5 w-5';
        
        // Comportamiento basado en stock
        if (p.stock === 0 || p.price === 0) {
            btn.disabled = true;
            btn.className = 'p-2 rounded-md bg-gray-200 cursor-not-allowed';
            icon.src = `${window.contextPath}/images/shopping-cart-gray.svg`; // Icono gris
        } else {
            btn.className = 'p-2 rounded-md bg-pistachio hover:bg-dark-pistachio transition';
            icon.src = `${window.contextPath}/images/shopping-cart-white.svg`; // Icono blanco
            btn.addEventListener('click', (e) => {
                e.stopPropagation();
                if (isAuthenticated) {
                    updateServerCart(p.id, true, container, p);
                } else {
                    cartState[p.id] = 1;
                    saveCartState();
                    renderCartControls(container, p);
                }
            });
        }
        btn.appendChild(icon);
        container.appendChild(btn);

    } else {
        // --- Controles de cantidad "[−] [qty] [+]" ---
        const wrap = document.createElement('div');
        wrap.className = 'flex items-center border border-gray-300 rounded-md';

        const btnMinus = document.createElement('button');
        btnMinus.className = 'px-2 py-1 text-lg text-gray-600 hover:bg-gray-100 rounded-l-md';
        btnMinus.textContent = '−';
        btnMinus.addEventListener('click', (e) => {
            e.stopPropagation();
            if (isAuthenticated) {
                if (qty > 1) {
                    updateServerCart(p.id, false, container, p);
                } else {
                    deleteFromServerCart(p.id, container, p);
                }
            } else {
                if (qty > 1) {
                    cartState[p.id]--;
                } else {
                    delete cartState[p.id];
                }
                saveCartState();
                renderCartControls(container, p);
            }
        });

        const input = document.createElement('input');
        input.type = 'text';
        input.value = qty;
        input.className = 'w-8 text-center text-sm font-semibold border-0 focus:ring-0';
        input.readOnly = true;

        const btnPlus = document.createElement('button');
        btnPlus.className = 'px-2 py-1 text-lg text-gray-600 hover:bg-gray-100 rounded-r-md';
        btnPlus.textContent = '+';
        if (qty >= p.stock) {
            btnPlus.disabled = true;
            btnPlus.classList.add("text-gray-300", "cursor-not-allowed");
        }
        btnPlus.addEventListener('click', (e) => {
            e.stopPropagation();
            if (isAuthenticated) {
                updateServerCart(p.id, true, container, p);
            } else {
                cartState[p.id]++;
                saveCartState();
                renderCartControls(container, p);
            }
        });

        wrap.appendChild(btnMinus);
        wrap.appendChild(input);
        wrap.appendChild(btnPlus);
        container.appendChild(wrap);
    }
}

function renderBreadcrumbs(crumbs) {
    const breadcrumbList = document.getElementById('breadcrumb-list');
    if (!breadcrumbList) return;

    breadcrumbList.innerHTML = ''; // Limpia las migas anteriores

    crumbs.forEach((crumb, index) => {
        const li = document.createElement('li');

        if (crumb.href) {
            const a = document.createElement('a');
            // Usamos window.contextPath definido en el JSP para la URL base
            a.href = window.contextPath + crumb.href;
            a.className = 'hover:underline';
            a.textContent = crumb.label;
            li.appendChild(a);
        } else {
            // Para el último elemento que no tiene enlace
            li.textContent = crumb.label;
        }

        breadcrumbList.appendChild(li);

        // Añade el separador, excepto para el último elemento
        if (index < crumbs.length - 1) {
            const separator = document.createElement('li');
            separator.className = 'mx-2';
            separator.textContent = '/';
            breadcrumbList.appendChild(separator);
        }
    });
}


// =======================================================================
// SECCIÓN 5: CÓDIGO ORIGINAL PARA LAYOUT, PAGINACIÓN Y RENDERIZADO
// (MANTENIDO INTACTO, EXCEPTO POR UNA LÍNEA DE ESTILO)
// =======================================================================
function calcLayout() {
    var w    = window.innerWidth,
        cols = w >= 1024 ? 4 : w >= 768 ? 3 : 2,
        rows = cols + 3;
    return { cols: cols, size: cols * rows };
}

function updateControls() {
    pageNumEl.textContent = currentPage + 1;
    totalEl.textContent   = totalPages;
    prevBtn.disabled      = (currentPage === 0);
    nextBtn.disabled      = (currentPage + 1 === totalPages);
}

function renderGrid(items) {
    var layout = calcLayout();
    const paginationControls = document.getElementById('pagination-controls');
    
    gridEl.className = 'grid grid-cols-' + layout.cols + ' gap-0 bg-white p-4';
    gridEl.innerHTML = '';

    if (items && items.length > 0) {
        if (paginationControls) paginationControls.style.display = 'flex';
        items.forEach(function(p, idx) {
            var col = idx % layout.cols;
            var row = Math.floor(idx / layout.cols);
            var card = document.createElement('div');
            card.className = [
                'relative','flex','flex-col','overflow-hidden','p-4',
                col > 0 && 'border-l',
                row > 0 && 'border-t',
                'border-gray-200'
            ].filter(Boolean).join(' ');

            // --- MODIFICADO: Creación del botón de Wishlist con la nueva lógica unificada ---
            var favBtn = document.createElement('button');
            favBtn.className = 'absolute top-2 right-2 z-10';

            // 1. Renderiza el estado inicial del corazón al crear la tarjeta
            renderWishlistStatus(favBtn, p.id);

            // 2. Añade el listener con la lógica condicional (autenticado vs. invitado)
            favBtn.addEventListener('click', (e) => {
                e.stopPropagation(); // Evita que se active el click de la tarjeta

                const productIdStr = p.id.toString();
                const isInWishlist = wishlistState.includes(productIdStr);

                if (isAuthenticated) {
                    // Usuario autenticado: llama al servidor
                    updateServerWishlist(p.id, !isInWishlist, favBtn);
                } else {
                    // Usuario invitado: usa localStorage
                    const index = wishlistState.indexOf(productIdStr);
                    if (index > -1) {
                        wishlistState.splice(index, 1); // Eliminar
                    } else {
                        wishlistState.push(productIdStr); // Añadir
                    }
                    saveWishlistState();
                    renderWishlistStatus(favBtn, p.id);
                }
            });
            card.appendChild(favBtn);

            if (p.discount > 0) {
                var badge = document.createElement('span');
                badge.className = 'absolute top-2 left-2 bg-red-500 text-white text-xs font-semibold px-2 py-1 rounded z-10';
                badge.textContent = '-' + Math.round(p.discount) + '%';
                card.appendChild(badge);
            }

            if (p.imagenPath) {
                var wrapImg = document.createElement('div');
                wrapImg.className = 'p-2 cursor-pointer';
                var img = document.createElement('img');
                img.src       = window.contextPath + p.imagenPath;
                img.alt       = p.nombre;
                img.className = 'h-40 w-full object-cover rounded';
                wrapImg.addEventListener('click', function() {
                    window.location.href = window.contextPath + '/product/' + p.id;
                });
                wrapImg.appendChild(img);
                card.appendChild(wrapImg);
            } else { 
                var placeholder = document.createElement('div');
                placeholder.className = 'h-40 w-full bg-gray-200 flex items-center justify-center rounded p-2';
                placeholder.innerHTML =
                    '<svg xmlns="http://www.w3.org/2000/svg" class="w-10 h-10 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">'
                + '<path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 7h4l2-3h6l2 3h4v13H3V7z"/>'
                + '<circle cx="12" cy="13" r="4" stroke="currentColor" stroke-width="2"/>'
                + '</svg>';
                card.appendChild(placeholder);
            }

            var container = document.createElement('div');
            container.className = 'flex flex-col justify-between flex-grow mt-2';

            var header = document.createElement('h3');
            header.className = 'text-sm font-medium mb-2 cursor-pointer hover:text-pistachio flex-grow';
            header.textContent = p.nombre;
            header.onclick = () => window.location.href = window.contextPath + '/product/' + p.id;
            container.appendChild(header);

            var bottomDiv = document.createElement('div');
            bottomDiv.className = 'mt-auto'; // Para empujar el footer hacia abajo

            var middleBlock = document.createElement('div');
            middleBlock.className = 'flex flex-col mb-2'; // Añadido margen inferior
            
            // 2) RATING + DISPONIBILIDAD
            var middleBlock = document.createElement('div');
            middleBlock.className = 'flex flex-col';

            var ratingWrap = document.createElement('div');
            ratingWrap.className = 'flex items-center space-x-1';
            for (var i = 1; i <= 5; i++) {
                var star = document.createElementNS('http://www.w3.org/2000/svg','svg');
                star.setAttribute('viewBox','0 0 20 20');
                star.setAttribute('fill','currentColor');
                star.classList.add('w-4','h-4', i <= p.rating ? 'text-yellow-400' : 'text-gray-300');
                star.innerHTML = '<path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.286 3.97a1 1 0 00.95.69h4.178c.969 0 1.371 1.24.588 1.81l-3.385 2.46a1 1 0 00-.364 1.118l1.286 3.97c.3.921-.755 1.688-1.54 1.118l-3.385-2.46a1 1 0 00-1.175 0l-3.385 2.46c-.785.57-1.84-.197-1.54-1.118l1.286-3.97a1 1 0 00-.364-1.118L2.047 9.397c-.783-.57-.38-1.81.588-1.81h4.178a1 1 0 00.95-.69l1.286-3.97z"/>';
                ratingWrap.appendChild(star);
            }
            var cnt = document.createElement('span');
            cnt.className = 'text-xs text-gray-500';
            cnt.textContent = '(' + (p.ratingCount||0) + ')';
            middleBlock.appendChild(ratingWrap);

            var avail = document.createElement('span');
            avail.className = 'text-xs font-semibold mb-4 ' + (p.stock>0 ? 'text-green-600' : 'text-red-600');
            avail.textContent = p.stock>0 ? 'Disponible' : 'Sin stock';
            middleBlock.appendChild(avail);

            bottomDiv.appendChild(middleBlock);

            // 3) FOOTER: precios + carrito
            var footer = document.createElement('div');
            footer.className = 'flex justify-between items-end';

            var prices = document.createElement('div');
            prices.className = 'flex flex-col';
            if (p.discount > 0) {
                var orig = document.createElement('span');
                orig.className = 'text-sm text-gray-400 line-through';
                orig.textContent = '€' + p.price.toFixed(2);
                prices.appendChild(orig);

                var discVal = (p.price * (1 - p.discount / 100));
                var disc = document.createElement('span');
                // ÚNICO CAMBIO DE ESTILO: Añadida la clase 'text-red-600'
                disc.className = 'text-lg font-bold text-red-600'; 
                disc.textContent = '€' + discVal.toFixed(2);
                prices.appendChild(disc);
            } else {
                var normal = document.createElement('span');
                normal.className = 'text-lg font-bold text-gray-800';
                normal.textContent = '€' + p.price.toFixed(2);
                prices.appendChild(normal);
            }
            footer.appendChild(prices);

            var controls = document.createElement('div');
            // AHORA LLAMA A LA NUEVA FUNCIÓN renderCartControls
            renderCartControls(controls, p);
            footer.appendChild(controls);

            bottomDiv.appendChild(middleBlock);
            bottomDiv.appendChild(footer);
            container.appendChild(bottomDiv);
            card.appendChild(container);
            gridEl.appendChild(card);
        });
    } else {
        if (paginationControls) paginationControls.style.display = 'none';

        // Se cambia el estilo del contenedor para centrar el mensaje
        gridEl.className = 'flex items-center justify-center bg-white p-4 rounded-lg shadow-sm min-h-[50vh]';

        const messageContainer = document.createElement('div');
        messageContainer.className = 'text-center';

        const icon = document.createElement('div');
        icon.innerHTML = `<svg xmlns="http://www.w3.org/2000/svg" class="mx-auto h-16 w-16 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="1">
                          <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                        </svg>`;
        messageContainer.appendChild(icon);

        const title = document.createElement('h2');
        title.className = 'mt-4 text-xl font-semibold text-gray-800';
        title.textContent = 'No se han encontrado productos';
        messageContainer.appendChild(title);

        const subtitle = document.createElement('p');
        subtitle.className = 'mt-2 text-base text-gray-500';
        subtitle.textContent = 'Prueba a cambiar o eliminar alguno de los filtros aplicados.';
        messageContainer.appendChild(subtitle);

        gridEl.appendChild(messageContainer);
    }
}

// ==========================================================
// REEMPLAZA TU FUNCIÓN loadPage EXISTENTE CON ESTA VERSIÓN
// ==========================================================
function loadPage(page) {
    var layout = calcLayout();
    var fm = new FormData(filterForm);
    var params = new URLSearchParams({ page: page, size: layout.size });

    fm.forEach(function(v, k) {
        if (v !== '') params.append(k, v);
    });

    // La URL del fetch no cambia
    fetch(window.contextPath + '/api/product/get_pagable_list?' + params)
        .then(res => {
            if (!res.ok) throw new Error(res.statusText);
            return res.json();
        })
        .then(data => {
            // --- CAMBIOS AQUÍ ---
            // Ahora los datos están anidados dentro de 'pageData' y 'breadcrumbs'
            const pageData = data.pageData;

            currentPage = pageData.number;
            totalPages  = pageData.totalPages;
            renderGrid(pageData.content);
            updateControls();
            document.getElementById('totalCount').textContent = pageData.totalElements + ' productos encontrados';

            // ¡Llamada a la nueva función para actualizar las migas!
            renderBreadcrumbs(data.breadcrumbs); 
        })
        .catch(console.error);
}

// ==========================================================
// SECCIÓN 6: LISTENERS Y CARGA INICIAL (ORIGINAL)
// ==========================================================
prevBtn.addEventListener('click', function() {
    if (currentPage > 0) loadPage(currentPage - 1);
});
nextBtn.addEventListener('click', function() {
    if (currentPage + 1 < totalPages) loadPage(currentPage + 1);
});
filterForm.addEventListener('submit', function(e) {
    e.preventDefault(); // Evita que la página se recargue

    const fm = new FormData(filterForm);
    const params = new URLSearchParams();
    fm.forEach((value, key) => {
        if (value !== '') {
            params.append(key, value);
        }
    });
    history.replaceState(null, '', window.location.pathname + '?' + params.toString());
    window.scrollTo({ top: 0, behavior: 'smooth' });

    loadPage(0);
});

var resizeTO;
window.addEventListener('resize', function() {
    clearTimeout(resizeTO);
    resizeTO = setTimeout(function() {
        var newCols = calcLayout().cols;
        if (newCols !== currentCols) {
            currentCols = newCols;
            loadPage(0);
        }
    }, 200);
});

// Carga inicial
currentCols = calcLayout().cols;

