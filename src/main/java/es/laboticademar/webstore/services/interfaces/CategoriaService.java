package es.laboticademar.webstore.services.interfaces;

import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.entities.Categoria;
import es.laboticademar.webstore.entities.Familia;

public interface CategoriaService {
    List<Categoria> findAll();
    Optional<Categoria> findByNombre(String name);
    List<Categoria> findByFamilia(Familia familia);
}
