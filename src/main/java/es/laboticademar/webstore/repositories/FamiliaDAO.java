package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Familia;

public interface FamiliaDAO extends JpaRepository<Familia, Long> {
    Optional<Familia> findByNombre(String name);
}
