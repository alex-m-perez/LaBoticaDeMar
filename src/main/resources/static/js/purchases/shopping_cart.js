function formatCurrency(value) {
    return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(value);
}

function eliminarDelCarrito(productoId, buttonElement) {
    // Confirmación opcional para el usuario
    if (!confirm('¿Estás seguro de que quieres eliminar este producto del carrito?')) {
        return;
    }

    // Usamos el contextPath para construir la URL correctamente
    const contextPath = document.body.dataset.contextPath || '';

    $.ajax({
        url: '/api/cart/delete_item',
        type: 'POST', // Usamos POST para operaciones que modifican estado
        data: {
            itemId: productoId 
        },
        success: function(response) {
            // Si el backend confirma el borrado...
            const rowToRemove = $(buttonElement).closest('.cart-item-row');
            
            // Efecto de desvanecimiento y eliminación de la fila
            rowToRemove.fadeOut(300, function() {
                $(this).remove();
                
                // Si ya no quedan items, recarga la página para mostrar el mensaje de "carrito vacío"
                if ($('.cart-item-row').length === 0) {
                    window.location.reload();
                } else {
                    // Si quedan items, recalcula los totales
                    recalculateTotals();
                }
            });
        },
        error: function() {
            // Manejo de errores
            alert('Error: No se pudo eliminar el producto. Por favor, inténtalo de nuevo.');
        }
    });
}


// Función principal para recalcular todos los totales
function recalculateTotals() {
    let totalItemsPrice = 0;
    const itemRows = document.querySelectorAll('.cart-item-row');

    itemRows.forEach(row => {
        const price = parseFloat(row.dataset.price);
        const quantityInput = row.querySelector('.qty-input');
        const quantity = parseInt(quantityInput.value);
        const lineTotal = price * quantity;
        totalItemsPrice += lineTotal;

        const lineTotalCell = row.querySelector('.line-total');
        if (lineTotalCell) {
            lineTotalCell.textContent = formatCurrency(lineTotal);
        }
    });

    const baseSubtotal = totalItemsPrice / 1.21;
    const ivaAmount = totalItemsPrice - baseSubtotal;

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
    if (shippingCost === 0.0 && totalItemsPrice > 0) {
        shippingSpan.innerHTML = '<span class="font-bold text-green-600">Gratis</span>';
    } else {
        shippingSpan.textContent = formatCurrency(shippingCost);
    }
}

// Función para manejar los botones de +/-
function updateQuantity(btn, delta) {
    const container = btn.closest('.quantity-control');
    const input = container.querySelector('.qty-input');
    let currentValue = parseInt(input.value) || 1;
    let newValue = Math.max(1, currentValue + delta);
    input.value = newValue;
    input.dispatchEvent(new Event('change'));
}

// Se ejecuta cuando el DOM está completamente cargado
document.addEventListener('DOMContentLoaded', function() {
    // Guardamos el contextPath en el body para que sea accesible globalmente en JS
    const contextPath = document.querySelector('script[src*="/js/purchases/shopping_cart.js"]').src.split('/js/')[0];
    document.body.dataset.contextPath = contextPath;
    
    // Si hay items en el carrito, calcula los totales iniciales
    if (document.querySelectorAll('.cart-item-row').length > 0) {
        recalculateTotals();
    }
    
    document.querySelectorAll('.qty-input').forEach(input => {
        // Hacemos que el campo sea de solo lectura para forzar el uso de botones
        input.setAttribute('readonly', true); 
        input.addEventListener('change', recalculateTotals);
    });
});