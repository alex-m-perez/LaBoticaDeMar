package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Venta;

public interface VentaRepo extends JpaRepository<Venta, Long> {
    // No es necesario definir un método findAll() aquí
}
