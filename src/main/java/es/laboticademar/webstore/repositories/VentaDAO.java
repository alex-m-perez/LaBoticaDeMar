package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.laboticademar.webstore.entities.ShoppingCart;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface VentaDAO extends JpaRepository<Venta, Long> {
    Optional<ShoppingCart> findByClienteId(Long usuarioId);
    Page<Venta> findByCliente(Usuario cliente, Pageable pageable);
    Optional<Venta> findByIdAndCliente(Long id, Usuario cliente);
}