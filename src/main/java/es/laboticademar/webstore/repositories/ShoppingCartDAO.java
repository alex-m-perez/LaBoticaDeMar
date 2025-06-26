package es.laboticademar.webstore.repositories;

import es.laboticademar.webstore.entities.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartDAO extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByUsuarioId(Long usuarioId);
}