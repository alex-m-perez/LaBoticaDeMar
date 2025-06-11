package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Subcategoria;

public interface SubcategoriaDAO extends JpaRepository<Subcategoria, Long> {
    Optional<Subcategoria> findByNombre(String name);
}
