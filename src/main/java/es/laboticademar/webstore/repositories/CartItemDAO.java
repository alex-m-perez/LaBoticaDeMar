package es.laboticademar.webstore.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.laboticademar.webstore.entities.CartItem;

@Repository
public interface CartItemDAO extends JpaRepository<CartItem, Long> {
    
    Optional<CartItem> findByShoppingCartIdAndProductoId(Long shoppingCartId, BigDecimal productoId);
    void deleteByShoppingCartIdAndProductoId(Long shoppingCartId, BigDecimal productoId);
}