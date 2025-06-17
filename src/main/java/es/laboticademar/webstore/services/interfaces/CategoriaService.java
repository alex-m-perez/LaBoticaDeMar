package es.laboticademar.webstore.services.interfaces;

import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.entities.Categoria;

public interface CategoriaService {
    List<Categoria> findAll();
    Optional<Categoria> findByNombre(String name);
}
