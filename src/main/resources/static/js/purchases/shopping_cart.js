//shopping_cart.js
document.addEventListener('DOMContentLoaded', function() {
    const contextPathEl = document.querySelector('script[src*="/js/purchases/shopping_cart.js"]');
    if (contextPathEl) {
        const contextPath = contextPathEl.src.split('/js/')[0];
        document.body.dataset.contextPath = contextPath;
    }
    
    if (document.querySelectorAll('.cart-item-row').length > 0) {
        recalculateTotals();
    }
});

// Función para formatear valores a moneda EUR
function formatCurrency(value) {
    return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(value);
}

// --- NUEVA FUNCIÓN ---
// Envía la actualización incremental (+1 o -1) al backend.
function sendIncrementalUpdate(productId, isAdding) {
    const contextPath = document.body.dataset.contextPath || '';
    
    // Deshabilitar temporalmente los botones para evitar clics duplicados
    const row = document.querySelector(`.cart-item-row[data-product-id="${productId}"]`);
    if (row) {
        row.querySelectorAll('button').forEach(b => b.disabled = true);
    }

    $.ajax({
        url: `${contextPath}/api/cart/add_item`, // URL correcta de tu endpoint
        type: 'POST',
        data: {
            itemId: productId,
            add: isAdding // Parámetro booleano 'add'
        },
        success: function(response) {
            // El backend ha sido actualizado. Ahora actualizamos el frontend.
            const input = row.querySelector('.qty-input');
            const currentQty = parseInt(input.value);
            // Actualizamos el valor del input localmente
            input.value = isAdding ? currentQty + 1 : currentQty - 1;
            
            recalculateTotals(); // Recalcula todos los precios, IVA, totales y estado de los botones
            window.dispatchEvent(new CustomEvent('cartUpdated'));
        },
        error: function() {
            alert('Error: No se pudo actualizar la cantidad. La página se recargará.');
            window.location.reload();
        },
        complete: function() {
             // Volver a habilitar los botones después de la llamada, excepto los que deban estar deshabilitados por stock
            if (row) {
                 row.querySelectorAll('button').forEach(b => b.disabled = false);
                 // Asegurarse de que el estado final de los botones sea correcto
                 recalculateTotals();
            }
        }
    });
}

// Función para eliminar un producto del carrito (sin cambios, ya es correcta)
function eliminarDelCarrito(productoId, buttonElement) {
    if (!confirm('¿Estás seguro de que quieres eliminar este producto del carrito?')) {
        return;
    }
    const contextPath = document.body.dataset.contextPath || '';

    $.ajax({
        url: `${contextPath}/api/cart/delete_item`,
        type: 'POST',
        data: { itemId: productoId },
        success: function(response) {
            const rowToRemove = $(buttonElement).closest('.cart-item-row');
            rowToRemove.fadeOut(300, function() {
                $(this).remove();
                window.dispatchEvent(new CustomEvent('cartUpdated'));
                if ($('.cart-item-row').length === 0) {
                    window.location.reload();
                } else {
                    recalculateTotals();
                }
            });
            
        },
        error: function() {
            alert('Error: No se pudo eliminar el producto.');
        }
    });
}

// --- FUNCIÓN MODIFICADA ---
// Maneja los botones de +/- y decide si actualizar o eliminar.
function updateQuantity(btn, delta) {
    const row = btn.closest('.cart-item-row');
    const input = row.querySelector('.qty-input');
    const productId = row.dataset.productId;
    const stock = parseInt(row.dataset.stock);
    let currentValue = parseInt(input.value);

    if (delta > 0) { // Si se presiona '+'
        if (currentValue >= stock) {
            return; // No hacer nada si se ha alcanzado el stock máximo
        }
        sendIncrementalUpdate(productId, true); // true para 'add'
    } else { // Si se presiona '-'
        if (currentValue <= 1) {
            // Si la cantidad es 1, llamamos a la función de eliminar que tiene confirmación
            eliminarDelCarrito(productId, btn);
            return;
        }
        sendIncrementalUpdate(productId, false); // false para 'add'
    }
}

// Función principal para recalcular totales (sin cambios, ya es correcta)
function recalculateTotals() {
    let totalItemsPrice = 0;
    const itemRows = document.querySelectorAll('.cart-item-row');

    itemRows.forEach(row => {
        const price = parseFloat(row.dataset.price);
        const discount = parseFloat(row.dataset.discount || 0);
        const stock = parseInt(row.dataset.stock);
        const quantityInput = row.querySelector('.qty-input');
        const quantity = parseInt(quantityInput.value);

        const plusBtn = row.querySelector('.plus-btn');
        if (plusBtn) {
            plusBtn.disabled = quantity >= stock;
        }

        const effectivePrice = discount > 0 ? price * (1 - discount / 100) : price;
        const lineTotal = effectivePrice * quantity;
        totalItemsPrice += lineTotal;

        const lineTotalCell = row.querySelector('.line-total');
        if (lineTotalCell) {
            lineTotalCell.textContent = formatCurrency(lineTotal);
        }
    });

    const ivaAmount = totalItemsPrice * (1 - 1 / 1.21);
    const baseSubtotal = totalItemsPrice - ivaAmount;

    let shippingCost;
    if (totalItemsPrice > 60) {
        shippingCost = 0.0;
    } else if (totalItemsPrice > 40) {
        shippingCost = 1.99;
    } else {
        shippingCost = 3.99;
    }
    
    if (totalItemsPrice === 0) {
        shippingCost = 0.0;
    }

    const finalTotal = totalItemsPrice + shippingCost;

    document.getElementById('cart-subtotal').textContent = formatCurrency(baseSubtotal);
    document.getElementById('cart-iva').textContent = formatCurrency(ivaAmount);
    document.getElementById('cart-total').textContent = formatCurrency(finalTotal);

    const shippingSpan = document.getElementById('cart-shipping');
    if (shippingCost === 0.0 && totalItemsPrice > 60) {
        shippingSpan.innerHTML = '<span class="font-bold text-green-600">Gratis</span>';
    } else {
        shippingSpan.textContent = formatCurrency(shippingCost);
    }
}