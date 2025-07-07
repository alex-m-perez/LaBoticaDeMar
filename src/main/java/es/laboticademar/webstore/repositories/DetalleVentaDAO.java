package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.laboticademar.webstore.entities.CartItem;
import es.laboticademar.webstore.entities.DetalleVenta;

@Repository
public interface DetalleVentaDAO extends JpaRepository<DetalleVenta, Long> {
    
    Optional<CartItem> findByVentaId(Long ventaId);
    void deleteByVentaId(Long ventaIds);
}