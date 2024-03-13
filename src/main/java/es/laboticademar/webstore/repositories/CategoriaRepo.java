package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Categoria;

public interface CategoriaRepo extends JpaRepository<Categoria, Long> {
    // No es necesario definir un método findAll() aquí
}
