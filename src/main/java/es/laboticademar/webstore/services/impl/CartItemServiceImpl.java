package es.laboticademar.webstore.services.impl;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.CartItem;
import es.laboticademar.webstore.repositories.CartItemDAO;
import es.laboticademar.webstore.services.interfaces.CartItemService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {


    private final CartItemDAO cartItemDAO;

    @Override
    public CartItem saveOrUpdateProducto(CartItem item) {
        return cartItemDAO.save(item);
    }

    @Override
    public void delete(CartItem item) {
        cartItemDAO.delete(item);
    }

    @Override
    public Optional<CartItem> findByShoppingCartIdAndProductoId(Long shoppingCartId, BigDecimal productoId) {
        return cartItemDAO.findByShoppingCartIdAndProductoId(shoppingCartId, productoId);
    }
    
    @Override
    public void deleteByShoppingCartIdAndProductoId(Long shoppingCartId, BigDecimal productoId) {
        cartItemDAO.deleteByShoppingCartIdAndProductoId(shoppingCartId, productoId);
    }

    
}