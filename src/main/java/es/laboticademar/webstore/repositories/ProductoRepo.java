package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Producto;

public interface ProductoRepo extends JpaRepository<Producto, Long> {
    // No es necesario definir un método findAll() aquí
}
