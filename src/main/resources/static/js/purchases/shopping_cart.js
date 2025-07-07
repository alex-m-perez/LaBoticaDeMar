/**
 * =================================================================
 * SCRIPT REFACTORIZADO PARA EL CARRITO Y PAGO
 * =================================================================
 * Estructura:
 * 1. Utilidades: Helpers como `formatCurrency`.
 * 2. Lógica de Cálculo: Funciones puras que devuelven datos.
 * 3. Renderizado de UI: Funciones que manipulan el DOM.
 * 4. Orquestador de UI: Una función `updateUI` que une cálculo y renderizado.
 * 5. Manejadores de Eventos y API: Funciones que responden a acciones del usuario.
 * 6. Inicialización: Un único `DOMContentLoaded` que arranca todo.
 * =================================================================
 */

const GUEST_CART_KEY = 'cart';
const GUEST_WISHLIST_KEY = 'wishlist';

const isAuthenticated = document.body.dataset.authenticated === 'true';
const contextPath = document.body.dataset.contextPath || '';

// El estado del carrito y wishlist se carga desde el servidor o localStorage
let cartState = isAuthenticated ? (userCartState || {}) : JSON.parse(localStorage.getItem(GUEST_CART_KEY) || '{}');

// 1. UTILIDADES
// =================================

/**
 * Formatea un número como moneda EUR.
 * @param {number} value - El valor numérico a formatear.
 * @returns {string} El valor formateado como moneda.
 */
function formatCurrency(value) {
    return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(value);
}


// 2. LÓGICA DE CÁLCULO
// =================================

/**
 * Lee el DOM y calcula los totales base del carrito.
 * @returns {{totalItemsPrice: number, totalSavings: number}}
 */
function getCartState() {
    let totalItemsPrice = 0;
    let totalSavings = 0;
    document.querySelectorAll('.cart-item-row').forEach(row => {
        const price = parseFloat(row.dataset.price);
        const discount = parseFloat(row.dataset.discount || 0);
        const quantity = parseInt(row.querySelector('.qty-input').value);

        if (discount > 0) {
            totalSavings += (price * (discount / 100)) * quantity;
        }
        const effectivePrice = price * (1 - discount / 100);
        totalItemsPrice += effectivePrice * quantity;
    });
    return { totalItemsPrice, totalSavings };
}


// 3. RENDERIZADO DE LA INTERFAZ DE USUARIO (UI)
// =================================

/**
 * Actualiza los elementos visuales de cada fila del carrito (total y botones).
 */
function renderCartItems() {
    document.querySelectorAll('.cart-item-row').forEach(row => {
        const price = parseFloat(row.dataset.price);
        const discount = parseFloat(row.dataset.discount || 0);
        const stock = parseInt(row.dataset.stock);
        const quantity = parseInt(row.querySelector('.qty-input').value);
        
        row.querySelector('.plus-btn').disabled = quantity >= stock;
        const effectivePrice = price * (1 - discount / 100);
        row.querySelector('.line-total').textContent = formatCurrency(effectivePrice * quantity);
    });
}

/**
 * Actualiza el banner superior de ahorros y puntos.
 * @param {object} state - El objeto de estado con { totalItemsPrice, totalSavings }.
 */
function renderTopBanner({ totalItemsPrice, totalSavings }) {
    const banner = document.getElementById('savings-banner');
    if (!banner) return;

    // Si no hay precio, no muestres el banner
    if (totalItemsPrice <= 0) {
        banner.classList.add('hidden');
        return;
    }

    // Si hay precio, asegúrate de que el banner sea visible
    banner.classList.remove('hidden');

    const pointsToGenerate = Math.floor(totalItemsPrice * 5);
    let bannerHTML = '¡Con este pedido ';
    bannerHTML += totalSavings > 0 ? `ahorrarás <span class="font-bold">${formatCurrency(totalSavings)}</span> y generarás ` : 'generarás ';
    bannerHTML += `<span class="font-bold">${pointsToGenerate} puntos</span>!`;
    banner.innerHTML = bannerHTML;
}

/**
 * Actualiza el cuadro de resumen del pedido.
 * @param {object} state - El objeto de estado con todos los datos del resumen.
 */
function renderOrderSummary(state) {
    const { baseSubtotal, ivaAmount, shippingCost, pointsDiscount, finalTotal, totalItemsPrice } = state;

    document.getElementById('cart-subtotal').textContent = formatCurrency(baseSubtotal);
    document.getElementById('cart-iva').textContent = formatCurrency(ivaAmount);
    document.getElementById('cart-total').textContent = formatCurrency(finalTotal);

    const shippingSpan = document.getElementById('cart-shipping');
    if (shippingCost === 0 && totalItemsPrice > 0) {
        shippingSpan.innerHTML = '<span class="font-bold text-green-600">Gratis</span>';
    } else {
        shippingSpan.textContent = formatCurrency(shippingCost);
    }
    
    const shippingRow = document.getElementById('shipping-row');
    let pointsRow = document.getElementById('points-discount-row');
    if (pointsDiscount > 0) {
        if (!pointsRow) {
            pointsRow = document.createElement('div');
            pointsRow.id = 'points-discount-row';
            pointsRow.className = 'flex justify-between text-red-600 font-medium';
            shippingRow.insertAdjacentElement('afterend', pointsRow);
        }
        pointsRow.innerHTML = `<span>Uso de puntos</span><span>-${formatCurrency(pointsDiscount)}</span>`;
    } else {
        pointsRow?.remove();
    }
}


// 4. ORQUESTADOR DE UI
// =================================

/**
 * Función central que recalcula todo y actualiza toda la UI.
 */
function updateUI() {
    const { totalItemsPrice, totalSavings } = getCartState();

    const ivaAmount = totalItemsPrice * (1 - 1 / 1.21);
    const baseSubtotal = totalItemsPrice - ivaAmount;
    const shippingCost = (totalItemsPrice > 60 || totalItemsPrice === 0) ? 0.0 : (totalItemsPrice > 40 ? 1.99 : 3.99);

    let pointsDiscount = 0;
    const usePointsCheckbox = document.getElementById('use-points');
    if (usePointsCheckbox?.checked) {
        const userPoints = parseInt(usePointsCheckbox.dataset.userPoints || '0');
        pointsDiscount = Math.min(userPoints / 100, totalItemsPrice + shippingCost);
    }
    
    const finalTotal = totalItemsPrice + shippingCost - pointsDiscount;

    const summaryState = { totalItemsPrice, totalSavings, baseSubtotal, ivaAmount, shippingCost, pointsDiscount, finalTotal };
    
    renderCartItems();
    renderTopBanner(summaryState);
    renderOrderSummary(summaryState);
}


// 5. MANEJADORES DE EVENTOS Y API
// =================================

function handleQuantityChange(btn, delta) {
    const row = $(btn).closest('.cart-item-row');
    const productId = row.data('product-id').toString();

    if (isAuthenticated) {
        const currentValue = parseInt(row.find('.qty-input').val());
        if (delta < 0 && currentValue <= 1) {
            handleDeleteItem(productId, btn);
        } else {
            sendIncrementalUpdate(productId, delta > 0);
        }
    } else {
        const guestCart = JSON.parse(localStorage.getItem(GUEST_CART_KEY) || '{}');
        const keyInCart = Object.keys(guestCart).find(key => key === productId);

        if (!keyInCart) return;

        const newQty = guestCart[keyInCart] + delta;

        if (newQty <= 0) {
            handleDeleteItem(productId, btn);
        } else {
            guestCart[keyInCart] = newQty;
            localStorage.setItem(GUEST_CART_KEY, JSON.stringify(guestCart));
            row.find('.qty-input').val(newQty); // Actualiza el input visualmente
            updateUI();
            window.dispatchEvent(new CustomEvent('cartUpdated'));
        }
    }
}

function sendIncrementalUpdate(productId, isAdding) {
    const row = document.querySelector(`.cart-item-row[data-product-id="${productId}"]`);
    if (row) row.querySelectorAll('button').forEach(b => b.disabled = true);

    $.ajax({
        url: `${contextPath}/api/cart/add_item`,
        type: 'POST',
        data: { itemId: productId, add: isAdding },
        success: () => {
            const input = row.querySelector('.qty-input');
            input.value = isAdding ? parseInt(input.value) + 1 : parseInt(input.value) - 1;
            window.dispatchEvent(new CustomEvent('cartUpdated'));
            updateUI();
        },
        error: () => alert('Error al actualizar la cantidad.'),
        complete: () => {
             if (row) row.querySelectorAll('button').forEach(b => b.disabled = false);
             updateUI();
        }
    });
}


function handleDeleteItem(productId, button) {
    if (!confirm('¿Seguro que quieres eliminar este producto?')) return;

    const row = button.closest('.cart-item-row');

    if (isAuthenticated) {
        eliminarDelCarrito(productId, button); // Llama a la función AJAX para usuarios logueados
    } else {
        const guestCart = JSON.parse(localStorage.getItem(GUEST_CART_KEY) || '{}');

        // --- LÍNEA CORREGIDA: Compara como texto ---
        const keyToDelete = Object.keys(guestCart).find(key => key === productId);

        if (keyToDelete) {
            delete guestCart[keyToDelete];
        }

        localStorage.setItem(GUEST_CART_KEY, JSON.stringify(guestCart));

        $(button).closest('.cart-item-row').fadeOut(300, function() {
            $(this).remove();
            if (Object.keys(guestCart).length === 0) renderGuestCart({ items: [] });
            updateUI();
            window.dispatchEvent(new CustomEvent('cartUpdated'));
        });
    }
}

function eliminarDelCarrito(productoId, buttonElement) {
    const contextPath = window.contextPath || '';

    $.ajax({
        url: `${contextPath}/api/cart/delete_item`,
        type: 'POST',
        data: { itemId: productoId },
        success: () => {
            $(buttonElement).closest('.cart-item-row').fadeOut(300, function() {
                $(this).remove();
                window.dispatchEvent(new CustomEvent('cartUpdated'));
                if (document.querySelectorAll('.cart-item-row').length === 0) {
                    window.location.reload();
                } else {
                    updateUI();
                }
            });
        },
        error: () => alert('Error al eliminar el producto.')
    });
}




// 6. INICIALIZACIÓN DE LA APLICACIÓN
// =================================

/**
 * Configura los event listeners para el formulario de pago.
 */
function setupPaymentFormListeners() {
    const paymentForm = document.getElementById('payment-form');
    if (!paymentForm) return;

    const cardNumberInput = document.getElementById('cardNumber');
    const expiryInput = document.getElementById('expiryDate');
    const cvcInput = document.getElementById('cvc');
    const expiryErrorElement = document.getElementById('expiry-error');

    cardNumberInput?.addEventListener('input', (e) => {
        let rawValue = e.target.value.replace(/\D/g, '');
        if (rawValue.length > 16) rawValue = rawValue.slice(0, 16);
        e.target.value = rawValue.match(/.{1,4}/g)?.join(' ') || '';
    });

    expiryInput?.addEventListener('input', (e) => {
        if (e.target.value.length === 2 && e.inputType === "insertText") {
            e.target.value = e.target.value.replace(/\D/g, '') + '/';
            e.target.selectionStart = e.target.selectionEnd = e.target.value.length;
            return;
        }
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 2) value = value.slice(0, 2) + '/' + value.slice(2, 6);
        e.target.value = value;
    });
    
    expiryInput?.addEventListener('blur', (e) => {
        const { value } = e.target;
        const [month, year] = value.split('/').map(s => parseInt(s, 10));
        const now = new Date();
        const currentMonth = now.getMonth() + 1;
        const currentYear = now.getFullYear();
        let errorMessage = '';
        if (value.length !== 7) errorMessage = 'Formato debe ser MM/AAAA.';
        else if (isNaN(month) || month < 1 || month > 12) errorMessage = 'El mes no es válido.';
        else if (isNaN(year) || year < currentYear || year > currentYear + 15) errorMessage = 'El año no es válido.';
        else if (year === currentYear && month < currentMonth) errorMessage = 'La fecha no puede ser pasada.';
        
        expiryErrorElement.textContent = errorMessage;
        expiryInput.classList.toggle('border-red-500', !!errorMessage);
    });

    cvcInput?.addEventListener('input', (e) => {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length > 3) value = value.slice(0, 3);
        e.target.value = value;
    });
    
    cvcInput?.addEventListener('blur', (e) => {
        const isValid = e.target.value.length === 0 || e.target.value.length === 3;
        cvcInput.classList.toggle('border-red-500', !isValid);
    });
}

/**
 * Configura los listeners globales y de la página.
 */
function setupEventListeners() {
    const usePointsCheckbox = document.getElementById('use-points');
    usePointsCheckbox?.addEventListener('change', updateUI);

    const cartContainer = document.querySelector('.lg\\:w-2\\/3');
    cartContainer?.addEventListener('click', (e) => {
        // Detiene la propagación para evitar cualquier comportamiento inesperado
        e.stopPropagation();

        if (e.target.closest('.plus-btn')) {
            handleQuantityChange(e.target, 1);
        } else if (e.target.closest('.minus-btn')) {
            handleQuantityChange(e.target, -1);
        } else if (e.target.closest('.delete-btn')) {
            // Llama a la nueva función centralizada de eliminación
            const row = e.target.closest('.cart-item-row');
            const productId = row.dataset.productId;
            handleDeleteItem(productId, e.target);
        }
    });
    
    setupPaymentFormListeners();
}

/**
 * Procesa el pago recopilando los datos del formulario y enviándolos al backend.
 * Incluye el flag 'usePoints' en una cabecera HTTP.
 */
function procesarPago() {
    // 1. Recopilar los datos del formulario de pago
    
    const paymentData = {
        cardNumber: document.getElementById('cardNumber').value,
        cardExpiringDate: document.getElementById('expiryDate').value,
        cardSecretNumber: parseInt(document.getElementById('cvc').value, 10)
    };

    // 2. Obtener el valor del checkbox de puntos
    const usePoints = document.getElementById('use-points').checked; // Devuelve true o false

    // 3. Obtener el contextPath (si lo necesitas para la URL)
    const contextPath = window.contextPath || '';

    // Deshabilitar el botón de pago para evitar clics duplicados
    const paymentButton = document.getElementById('checkout'); // Ajusta el selector si es necesario
    paymentButton.classList.add('pointer-events-none', 'bg-gray-400');
    
    // 4. Realizar la llamada AJAX
    $.ajax({
        url: `${contextPath}/api/cart/buy_cart`, // La URL de tu endpoint para realizar la venta
        type: 'POST',
        contentType: 'application/json', // Esencial al enviar un cuerpo JSON
        data: JSON.stringify(paymentData), // Convierte el objeto a un string JSON para el body
        headers: {
            'X-Use-Points': usePoints // Aquí se añade la cabecera personalizada
        },
        success: function(response) {
            // La venta fue exitosa
            alert('¡Gracias por tu compra!');
            // Redirigir a una página de confirmación
            window.location.href = `${contextPath}/orden-confirmada`; 
        },
        error: function(jqXHR) {
            // Hubo un error en el backend (ej: stock, datos de pago inválidos)
            alert('Error al procesar el pago: ' + (jqXHR.responseJSON?.message || jqXHR.responseText || 'Error desconocido'));
            // Volver a habilitar el botón
            paymentButton.classList.remove('pointer-events-none', 'bg-gray-400');
        }
    });
}

/**
 * Renderiza las filas de la tabla del carrito para un usuario invitado
 * a partir de los datos recibidos de la API.
 * @param {object} cartData - El objeto ShoppingCartDTO devuelto por la API.
 */
/**
 * Renderiza las filas de la tabla del carrito para un usuario invitado
 * a partir de los datos recibidos de la API.
 * VERSIÓN CORREGIDA: Ahora muestra correctamente los precios con descuento.
 */
function renderGuestCart(cartData) {
    const tbody = $('#cart-items-body');
    tbody.empty();

    if (!cartData.items || cartData.items.length === 0) {
        tbody.html(`<tr><td colspan="4" class="text-center p-12"><p class="text-gray-600 text-2xl font-bold mb-4">Tu carrito está vacío.</p><a href="${window.contextPath || ''}/" class="inline-block bg-pistachio text-white px-6 py-2 rounded hover:bg-dark-pistachio">Continuar Comprando</a></td></tr>`);
        return;
    }

    cartData.items.forEach(item => {
        const product = item.producto;
        const effectivePrice = product.discount > 0 ? product.price * (1 - product.discount / 100) : product.price;

        // --- NUEVA LÓGICA PARA LA CELDA DE PRECIO ---
        let priceCellHtml;
        if (product.discount > 0) {
            // Si hay descuento, crea la estructura con el precio tachado y el nuevo en rojo
            priceCellHtml = `
                <div class="flex flex-col items-center">
                    <span class="text-xs text-gray-400 line-through">${formatCurrency(product.price)}</span>
                    <span class="text-red-600 font-semibold">${formatCurrency(effectivePrice)}</span>
                </div>
            `;
        } else {
            // Si no hay descuento, muestra solo el precio normal
            priceCellHtml = formatCurrency(product.price);
        }
        // --- FIN DE LA NUEVA LÓGICA ---

        const rowHTML = `
            <tr class="cart-item-row" data-price="${product.price}" data-product-id="${product.id}" data-discount="${product.discount || 0}" data-stock="${product.stock}">
                <td class="py-4 px-4">
                    <div class="flex items-center">
                        <img src="${window.contextPath || ''}${product.imagenPath}" alt="${product.nombre}" class="h-16 w-16 object-cover rounded mr-4" />
                        <div class="flex-grow">
                            <p class="font-semibold">${product.nombre}</p>
                            <p class="text-xs text-gray-500">${product.laboratorioNombre}</p>
                        </div>
                        <button type="button" class="delete-btn p-2 ml-4 hover:bg-red-100 rounded">
                            <img src="${window.contextPath || ''}/images/bin.svg" alt="Eliminar" class="h-5 w-5 pointer-events-none"/>
                        </button>
                    </div>
                </td>
                <td class="py-4 px-4 text-center">
                    <div class="quantity-control inline-flex border border-gray-200 rounded overflow-hidden">
                        <button type="button" class="minus-btn px-3 py-1 bg-gray-100 hover:bg-gray-200">−</button>
                        <input type="number" value="${item.cantidad}" class="qty-input w-12 text-center no-spinner" readonly/>
                        <button type="button" class="plus-btn px-3 py-1 bg-gray-100 hover:bg-gray-200">+</button>
                    </div>
                </td>
                <td class="py-4 px-4 text-center">${priceCellHtml}</td> {/* <-- Se usa la variable con el HTML correcto */}
                <td class="py-4 px-4 font-semibold text-center line-total">${formatCurrency(effectivePrice * item.cantidad)}</td>
            </tr>`;
        tbody.append(rowHTML);
    });
}

/**
 * Función principal que arranca la aplicación.
 */
/**
 * Función principal que arranca la aplicación.
 */
function initializeApp() {
    const isAuthenticated = document.body.dataset.authenticated === 'true';
    setupEventListeners();

    if (isAuthenticated) {
        if (document.querySelectorAll('.cart-item-row').length > 0) updateUI();
    } else {
           
        const guestCart = JSON.parse(localStorage.getItem('cart') || '{}');
        if (Object.keys(guestCart).length > 0) {
            $.ajax({
                url: `${window.contextPath || ''}/api/cart/guest-details`,
                type: 'POST', 
                contentType: 'application/json', 
                data: JSON.stringify(guestCart),
                success: (cartData) => {
                    renderGuestCart(cartData);
                    updateUI();
                },
                error: () => console.error("No se pudieron cargar los detalles del carrito de invitado.")
            });
        }
    }
}

// Arrancar la aplicación cuando el DOM esté listo.
document.addEventListener('DOMContentLoaded', initializeApp);