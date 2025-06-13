// src/main/java/es/laboticademar/webstore/repositories/CategoriaDAO.java
package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Categoria;

public interface CategoriaDAO extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNombre(String name);
}
