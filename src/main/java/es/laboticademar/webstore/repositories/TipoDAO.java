package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.TipoProducto;

public interface TipoDAO extends JpaRepository<TipoProducto, Long> {
    Optional<TipoProducto> findByNombre(String name);
}
