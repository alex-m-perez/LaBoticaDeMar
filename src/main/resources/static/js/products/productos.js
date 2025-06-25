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

// ——————————————————————————————
// Carrito “ligero” en localStorage
// ——————————————————————————————
let cart = JSON.parse(localStorage.getItem('cart') || '{}');
function saveCart() {
    localStorage.setItem('cart', JSON.stringify(cart));
}

// ——————————————————————————————
// Wishlist “ligero” en localStorage
// ——————————————————————————————
let wishlist = JSON.parse(localStorage.getItem('wishlist') || '[]');
function saveWishlist() {
    localStorage.setItem('wishlist', JSON.stringify(wishlist));
}

// ——————————————————————————————
// Renderiza los controles de carrito para un producto p dentro de container
// ——————————————————————————————
function renderCartControls(container, p) {
    container.innerHTML = '';
    const qty = cart[p.id] || 0;

    if (qty === 0) {
        // Botón “Añadir”
        const btn = document.createElement('button');
        // si no hay stock, deshabilitado
        if (p.stock === 0) {
            btn.disabled = true;
            btn.className = 'ml-2 bg-gray-200 text-gray-400 px-3 py-1 text-sm rounded cursor-not-allowed';
        } else {
            btn.className = 'ml-2 bg-pistachio text-white px-3 py-1 text-sm rounded hover:bg-pistachio-dark transition';
            btn.addEventListener('click', () => {
                cart[p.id] = 1;
                saveCart();
                renderCartControls(container, p);
            });
        }
        btn.textContent = 'Añadir';
        container.appendChild(btn);

    } else {
        // Controles “[− qty +]”
        const wrap = document.createElement('div');
        wrap.className = 'flex items-center border border-gray-200 rounded overflow-hidden';

        const btnMinus = document.createElement('button');
        btnMinus.className = 'px-2 text-gray-600 hover:bg-gray-100';
        btnMinus.textContent = '−';
        btnMinus.addEventListener('click', () => {
            const newQty = qty - 1;
            if (newQty > 0) {
                cart[p.id] = newQty;
            } else {
                delete cart[p.id];
            }
            saveCart();
            renderCartControls(container, p);
        });

        const input = document.createElement('input');
        input.type = 'text';
        input.value = qty;
        input.className = 'w-8 text-center text-sm';
        input.readOnly = true;

        const btnPlus = document.createElement('button');
        btnPlus.className = 'px-2 text-gray-600 hover:bg-gray-100';
        btnPlus.textContent = '+';
        btnPlus.addEventListener('click', () => {
            cart[p.id] = qty + 1;
            saveCart();
            renderCartControls(container, p);
        });

        wrap.appendChild(btnMinus);
        wrap.appendChild(input);
        wrap.appendChild(btnPlus);
        container.appendChild(wrap);
    }
}

// ——————————————————————————————
// Layout y paginación
// ——————————————————————————————
function calcLayout() {
    var w    = window.innerWidth,
        cols = w >= 1024 ? 4 : w >= 768 ? 3 : 2,
        rows = cols + 3;  // 4→7, 3→6, 2→5
    return { cols: cols, size: cols * rows };
}

function updateControls() {
    pageNumEl.textContent = currentPage + 1;
    totalEl.textContent   = totalPages;
    prevBtn.disabled      = (currentPage === 0);
    nextBtn.disabled      = (currentPage + 1 === totalPages);
}

// ——————————————————————————————
// Renderizado de la grid
// ——————————————————————————————
function renderGrid(items) {
    var layout = calcLayout();
    gridEl.className = [
        'grid',
        'grid-cols-' + layout.cols,
        'gap-0',
        'bg-white',
        'p-4'
    ].join(' ');
    gridEl.innerHTML = '';

    items.forEach(function(p, idx) {
        // calcula fila/columna para bordes
        var col = idx % layout.cols,
            row = Math.floor(idx / layout.cols);

        // wrapper de la celda
        var card = document.createElement('div');
        card.className = [
            'relative','flex','flex-col',
            'overflow-hidden','p-4',
            col > 0 && 'border-l',
            row > 0 && 'border-t',
            'border-gray-200'
        ].filter(Boolean).join(' ');

        // Favorito (wishlist)
        var fav = document.createElement('button');
        fav.className = 'absolute top-2 right-2';
        function updateFavIcon() {
            var filled = wishlist.includes(p.id);
            fav.innerHTML = filled
                ? '<svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-red-500" viewBox="0 0 20 20" fill="currentColor"><path d="M3.172 5.172a4 4 0 015.656 0L10 6.343l1.172-1.171a4 4 0 115.656 5.656L10 17.657l-6.828-6.828a4 4 0 010-5.657z"/></svg>'
                : '<svg xmlns="http://www.w3.org/2000/svg" class="w-7 h-7 text-gray-400 hover:text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"/></svg>';
        }
        updateFavIcon();
        fav.addEventListener('click', () => {
            if (wishlist.includes(p.id)) {
                wishlist = wishlist.filter(id => id !== p.id);
            } else {
                wishlist.push(p.id);
            }
            saveWishlist();
            updateFavIcon();
        });
        card.appendChild(fav);

        // Badge de descuento
        if (p.discount > 0) {
            var badge = document.createElement('span');
            badge.className = 'absolute top-2 left-2 bg-red-500 text-white text-xs font-semibold px-2 py-1 rounded';
            badge.textContent = '-' + Math.round(p.discount) + '%';
            card.appendChild(badge);
        }

        // Imagen o placeholder
        if (p.imagenPath) {
            var wrapImg = document.createElement('div');
            wrapImg.className = 'p-2 cursor-pointer';            // <-- aquí
            var img = document.createElement('img');
            img.src       = p.imagenPath;
            img.alt       = p.nombre;
            img.className = 'h-40 w-full object-cover rounded';
            // listener para redirigir al detalle
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

        // contenido principal
        var container = document.createElement('div');
        container.className = 'flex flex-col justify-between flex-grow';

        // 1) HEADER (nombre)
        var header = document.createElement('h3');
        header.className = 'text-sm font-medium mb-2 cursor-pointer hover:text-pistachio';
        header.textContent = p.nombre;
        header.onclick = () => window.location.href = window.contextPath + '/product/' + p.id;
        container.appendChild(header);

        // BottomDiv para alinear rating y footer
        var bottomDiv = document.createElement('div');
        bottomDiv.className = 'flex flex-col justify-between h-18';

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
            orig.textContent = '€ ' + p.price.toFixed(2);
            prices.appendChild(orig);

            var discVal = (p.price * (1 - p.discount/100)).toFixed(2);
            var disc = document.createElement('span');
            disc.className = 'text-lg font-bold text-gray-800';
            disc.textContent = '€ ' + discVal;
            prices.appendChild(disc);
        } else {
            var normal = document.createElement('span');
            normal.className = 'text-lg font-bold text-gray-800';
            normal.textContent = '€ ' + p.price.toFixed(2);
            prices.appendChild(normal);
        }
        footer.appendChild(prices);

        var controls = document.createElement('div');
        renderCartControls(controls, p);
        footer.appendChild(controls);

        bottomDiv.appendChild(footer);
        container.appendChild(bottomDiv);
        card.appendChild(container);
        gridEl.appendChild(card);
    });
}

// ——————————————————————————————
// Petición al API y control de eventos
// ——————————————————————————————
function loadPage(page) {
    var layout = calcLayout();
    var fm     = new FormData(filterForm);
    var params = new URLSearchParams({ page: page, size: layout.size });

    fm.forEach(function(v, k) {
        if (v !== '') params.append(k, v);
    });

    fetch(window.contextPath + '/product/api/get_pagable_list?' + params)
        .then(function(res) {
            if (!res.ok) throw new Error(res.statusText);
            return res.json();
        })
        .then(function(data) {
            currentPage = data.number;
            totalPages  = data.totalPages;
            renderGrid(data.content);
            updateControls();
            document.getElementById('totalCount').textContent = 'Productos encontrados: ' + data.totalElements;
        })
        .catch(console.error);
}

// Listeners
prevBtn.addEventListener('click', function() {
    if (currentPage > 0) loadPage(currentPage - 1);
});
nextBtn.addEventListener('click', function() {
    if (currentPage + 1 < totalPages) loadPage(currentPage + 1);
});
filterForm.addEventListener('submit', function(e) {
    loadPage(0);
});

// Redimensionar
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
loadPage(0);


document.addEventListener('DOMContentLoaded', () => {
    // Seleccionamos todos los elementos necesarios
    const sliderMin = document.getElementById('slider-min');
    const sliderMax = document.getElementById('slider-max');
    const inputMin = document.querySelector('input[name="precioMin"]');
    const inputMax = document.querySelector('input[name="precioMax"]');
    const progress = document.querySelector('.slider-progress');
    
    // Espacio mínimo entre los dos manejadores para que no se superpongan
    const priceGap = 3; // 50€ de diferencia mínima

    // Función para actualizar la barra de progreso visual
    function updateProgress() {
        const minVal = parseInt(sliderMin.value);
        const maxVal = parseInt(sliderMax.value);
        const minPercent = (minVal / sliderMin.max) * 100;
        const maxPercent = (maxVal / sliderMax.max) * 100;

        progress.style.left = minPercent + '%';
        progress.style.width = (maxPercent - minPercent) + '%';
    }

    // --- EVENT LISTENERS PARA LOS SLIDERS ---

    // Evento para el slider de MÍNIMO
    sliderMin.addEventListener('input', () => {
        let minVal = parseInt(sliderMin.value);
        let maxVal = parseInt(sliderMax.value);

        // Lógica para que no se crucen
        if (maxVal - minVal < priceGap) {
            minVal = maxVal - priceGap;
            sliderMin.value = minVal;
        }
        
        inputMin.value = minVal;
        updateProgress();
    });

    // Evento para el slider de MÁXIMO
    sliderMax.addEventListener('input', () => {
        let minVal = parseInt(sliderMin.value);
        let maxVal = parseInt(sliderMax.value);

        // Lógica para que no se crucen
        if (maxVal - minVal < priceGap) {
            maxVal = minVal + priceGap;
            sliderMax.value = maxVal;
        }

        inputMax.value = maxVal;
        updateProgress();
    });

    // --- EVENT LISTENERS PARA LOS INPUTS DE TEXTO ---

    // Sincronizar slider cuando se escribe en el input MÍNIMO
    inputMin.addEventListener('input', () => {
        let minVal = parseInt(inputMin.value);
        if (!isNaN(minVal) && minVal >= 0 && minVal <= 1000) {
            // Asegurarse de que no sobrepase al máximo
            if (minVal > parseInt(sliderMax.value) - priceGap) {
                minVal = parseInt(sliderMax.value) - priceGap;
                inputMin.value = minVal;
            }
            sliderMin.value = minVal;
            updateProgress();
        }
    });

    // Sincronizar slider cuando se escribe en el input MÁXIMO
    inputMax.addEventListener('input', () => {
        let maxVal = parseInt(inputMax.value);
        if (!isNaN(maxVal) && maxVal >= 0 && maxVal <= 1000) {
            // Asegurarse de que no sea menor que el mínimo
            if (maxVal < parseInt(sliderMin.value) + priceGap) {
                maxVal = parseInt(sliderMin.value) + priceGap;
                inputMax.value = maxVal;
            }
            sliderMax.value = maxVal;
            updateProgress();
        }
    });

    // Carga inicial de la barra de progreso
    updateProgress();
});