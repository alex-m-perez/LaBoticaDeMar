package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Factura;

public interface FacturaDAO extends JpaRepository<Factura, Long> {
    // No es necesario definir un método findAll() aquí
}
