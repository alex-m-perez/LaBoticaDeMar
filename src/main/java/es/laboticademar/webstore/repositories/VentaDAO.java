package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.laboticademar.webstore.entities.ShoppingCart;
import es.laboticademar.webstore.entities.Venta;

@Repository
public interface VentaDAO extends JpaRepository<Venta, Long> {
    Optional<ShoppingCart> findByClienteId(Long usuarioId);
}