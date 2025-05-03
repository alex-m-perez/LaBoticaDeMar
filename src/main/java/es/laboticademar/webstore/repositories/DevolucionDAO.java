package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Devolucion;

public interface DevolucionDAO extends JpaRepository<Devolucion, Long> {
    // No es necesario definir un método findAll() aquí
}
