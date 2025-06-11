package es.laboticademar.webstore.services.interfaces;

import java.util.Optional;
import es.laboticademar.webstore.entities.Categoria;

public interface CategoriaService {
    Optional<Categoria> findByNombre(String name);
}
