package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import es.laboticademar.webstore.entities.Devolucion;
import es.laboticademar.webstore.entities.Usuario;

public interface DevolucionDAO extends JpaRepository<Devolucion, Long>, JpaSpecificationExecutor<Devolucion> {
    boolean existsByVentaId(Long ventaId);
    Page<Devolucion> findByCliente(Usuario cliente, Pageable pageable);
    Optional<Devolucion> findByIdAndCliente(Long id, Usuario cliente);

}
