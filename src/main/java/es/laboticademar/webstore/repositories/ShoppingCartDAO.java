package es.laboticademar.webstore.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.entities.ShoppingCart;

@Repository
public interface ShoppingCartDAO extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUsuarioId(Long usuarioId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.shoppingCart.id = :cartId AND ci.producto.id = :productoId")
    void deleteItem(Long cartId, BigDecimal productoId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO cart_item (shopping_cart_id, producto_id, cantidad) VALUES (:cartId, :productoId, 1) " +
                   "ON DUPLICATE KEY UPDATE cantidad = cantidad + 1", nativeQuery = true)
    void addItem(Long cartId, BigDecimal productoId);


    @Transactional
    @Modifying
    @Query(
        "UPDATE CartItem ci SET ci.cantidad = ci.cantidad - 1 " +
        "WHERE ci.shoppingCart.id = :cartId " +
        "AND ci.producto.id = :productoId " +
        "AND ci.cantidad > 0")
    void substractItem(Long cartId, BigDecimal productoId);
}