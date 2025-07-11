package es.laboticademar.webstore.services.interfaces;

import java.math.BigDecimal;
import java.util.Optional;

import es.laboticademar.webstore.entities.CartItem;

public interface CartItemService {
    CartItem saveOrUpdateProducto(CartItem item);
    void delete(CartItem item);
    Optional<CartItem> findByShoppingCartIdAndProductoId(Long shoppingCartId, BigDecimal productoId);
    void deleteByShoppingCartIdAndProductoId(Long shoppingCartId, BigDecimal productoId);
}