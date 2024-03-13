package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Familia;

public interface FamiliaRepo extends JpaRepository<Familia, Long> {
    // No es necesario definir un método findAll() aquí
}
