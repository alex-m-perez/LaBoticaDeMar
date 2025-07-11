// ==========================================================
// SECCIÓN 1: ESTADO GLOBAL UNIFICADO Y CONFIGURACIÓN
// ==========================================================
const GUEST_CART_KEY = 'cart';
const GUEST_WISHLIST_KEY = 'wishlist';

const isAuthenticated = document.body.dataset.authenticated === 'true';
const contextPath = window.contextPath || '';

let cartState = isAuthenticated ? (userCartState || {}) : JSON.parse(localStorage.getItem(GUEST_CART_KEY) || '{}');
let wishlistState = isAuthenticated ? (userWishlistState || []) : JSON.parse(localStorage.getItem(GUEST_WISHLIST_KEY) || '[]');


// ==========================================================
// SECCIÓN 2: VARIABLES GLOBALES Y ELEMENTOS DEL DOM (Original)
// ==========================================================
var currentPage = 0,
    totalPages  = 1,
    currentCols = 0;

var gridEl     = document.getElementById('productsGrid'),
    prevBtn    = document.getElementById('prevBtn'),
    nextBtn    = document.getElementById('nextBtn'),
    pageNumEl  = document.getElementById('pageNum'),
    totalEl    = document.getElementById('totalPages'),
    filterForm = document.getElementById('filterForm');

    
// ==========================================================
// SECCIÓN 3: HELPERS, API Y MANEJADORES DE ESTADO
// ==========================================================
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

// --- Funciones de comunicación con la API (para usuarios autenticados) ---
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

// --- Manejadores de acciones del usuario ---
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


// ==========================================================
// SECCIÓN 4: RENDERIZADO DE CONTROLES
// ==========================================================
function renderCartControls(container, product) {
    container.innerHTML = '';
    const qty = cartState[product.id] || 0;

    if (qty === 0) {
        const btn = document.createElement('button');
        btn.className = 'p-2 rounded-md bg-pistachio hover:bg-dark-pistachio transition';
        if (product.stock === 0 || product.price === 0) {
            btn.disabled = true;
            btn.className = 'p-2 rounded-md bg-gray-200 cursor-not-allowed';
            btn.innerHTML = `<img src="${contextPath}/images/shopping-cart-gray.svg" class="h-5 w-5" alt="Añadir al carrito">`;
        } else {
            btn.innerHTML = `<img src="${contextPath}/images/shopping-cart-white.svg" class="h-5 w-5" alt="Añadir al carrito">`;
            btn.onclick = (e) => { e.stopPropagation(); handleAddToCart(product, container); };
        }
        container.appendChild(btn);
    } else {
        const wrap = document.createElement('div');
        wrap.className = 'flex items-center border border-gray-300 rounded-md';
        wrap.innerHTML = `
            <button class="btn-minus px-2 py-1 text-lg text-gray-600 hover:bg-gray-100 rounded-l-md">−</button>
            <input type="text" value="${qty}" class="w-8 text-center text-sm font-semibold border-0 focus:ring-0" readonly>
            <button class="btn-plus px-2 py-1 text-lg text-gray-600 hover:bg-gray-100 rounded-r-md">+</button>
        `;
        wrap.querySelector('.btn-minus').onclick = (e) => { e.stopPropagation(); handleQuantityChange(product, -1, container); };
        const plusBtn = wrap.querySelector('.btn-plus');
        plusBtn.onclick = (e) => { e.stopPropagation(); handleQuantityChange(product, 1, container); };
        if (qty >= product.stock) {
            plusBtn.disabled = true;
            plusBtn.classList.add("text-gray-300", "cursor-not-allowed");
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
// SECCIÓN 5: CÓDIGO DE RENDERIZADO DE GRID Y PAGINACIÓN
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
    
    gridEl.className = 'grid grid-cols-' + (items && items.length > 0 ? layout.cols : 1) + ' gap-0 bg-white p-4';
    gridEl.innerHTML = '';

    if (items && items.length > 0) {
        if (paginationControls) paginationControls.style.display = 'flex';
        items.forEach(function(p, idx) {
            // Toda la creación de la tarjeta (col, row, card, badge, img, etc.) se mantiene igual
            var col = idx % layout.cols;
            var row = Math.floor(idx / layout.cols);
            var card = document.createElement('div');
            card.className = [
                'relative','flex','flex-col','overflow-hidden','p-2',
                col > 0 && 'border-l', row > 0 && 'border-t', 'border-gray-200'
            ].filter(Boolean).join(' ');

            // <<< INICIO DE LA INTEGRACIÓN DE CONTROLES >>>
            
            // 1. Botón de Wishlist
            var favBtn = document.createElement('button');
            favBtn.className = 'wishlist-btn absolute top-2 right-2 z-10';
            favBtn.innerHTML = '<svg class="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/></svg>';
            renderWishlistStatus(favBtn, p.id);
            favBtn.onclick = (e) => {
                e.stopPropagation();
                handleWishlistToggle(p, favBtn);
            };
            card.appendChild(favBtn);

            if (p.discount > 0) {
                var badge = document.createElement('span');
                badge.className = 'absolute top-2 left-2 bg-red-500 text-white text-xs font-semibold px-2 py-1 rounded z-10';
                badge.textContent = '-' + Math.round(p.discount) + '%';
                card.appendChild(badge);
            }

            if (p.imagenData) {
                var wrapImg = document.createElement('div');
                wrapImg.className = 'cursor-pointer';
                var img = document.createElement('img');

                // --- LÍNEA CORREGIDA ---
                // Construimos la URL manualmente usando el ID del producto
                img.src = `${window.contextPath || ''}/api/images/${p.id}`;
                // --- FIN DE LA CORRECCIÓN ---

                img.alt     = p.nombre;
                img.className = 'h-40 w-full object-contain rounded';

                wrapImg.addEventListener('click', function() {
                    window.location.href = `${window.contextPath || ''}/product/${p.id}`;
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

            // El código del rating y disponibilidad se mantiene
            var bottomDiv = document.createElement('div');
            bottomDiv.className = 'mt-auto';
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
            middleBlock.appendChild(ratingWrap);
            var avail = document.createElement('span');
            avail.className = 'text-xs font-semibold mb-4 ' + (p.stock>0 ? 'text-green-600' : 'text-red-600');
            avail.textContent = p.stock>0 ? 'Disponible' : 'Sin stock';
            middleBlock.appendChild(avail);
            bottomDiv.appendChild(middleBlock);
            
            // El footer de la tarjeta (precios y botón de carrito)
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
            
            // 2. Controles del Carrito
            var controlsContainer = document.createElement('div');
            controlsContainer.className = 'cart-controls-container'; // Le damos una clase por si acaso
            renderCartControls(controlsContainer, p);
            footer.appendChild(controlsContainer);
            
            bottomDiv.appendChild(footer);
            container.appendChild(bottomDiv);
            card.appendChild(container);

            // <<< FIN DE LA INTEGRACIÓN DE CONTROLES >>>

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
// SECCIÓN 6: LISTENERS
// ==========================================================
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

function setupEventListeners() {
    prevBtn.addEventListener('click', function() {
        if (currentPage > 0) loadPage(currentPage - 1);
    });

    nextBtn.addEventListener('click', function() {
        if (currentPage + 1 < totalPages) loadPage(currentPage + 1);
    });

    filterForm.addEventListener('submit', function(e) {
        e.preventDefault();
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
}


// ==========================================================
// SECCIÓN 7: INICIALIZACIÓN DE LA APLICACIÓN
// ==========================================================
function syncUI() {
    // **CAMBIO CLAVE**: Recargamos el estado desde la fuente de verdad cada vez que se ejecuta.
    cartState = isAuthenticated ? userCartState : JSON.parse(localStorage.getItem('cart') || '{}');
    wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
    
    // El resto es la lógica que ya tenías
    window.dispatchEvent(new CustomEvent('cartUpdated', { detail: { newState: cartState } }));
    currentCols = calcLayout().cols;
    setupEventListeners();
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
    window.dispatchEvent(new CustomEvent('cartUpdated', { detail: { newState: cartState } }));
    currentCols = calcLayout().cols;
    setupEventListeners();
}

// Llama a la función principal cuando el DOM esté listo.
document.addEventListener('DOMContentLoaded', initializeApp);
*/