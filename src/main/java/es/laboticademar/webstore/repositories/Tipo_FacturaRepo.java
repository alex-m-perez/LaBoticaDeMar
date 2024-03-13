package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.TipoFactura;

public interface Tipo_FacturaRepo extends JpaRepository<TipoFactura, Long> {
    // No es necesario definir un método findAll() aquí
}
