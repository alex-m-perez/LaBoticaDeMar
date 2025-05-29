package es.laboticademar.webstore.services.interfaces;

import java.util.Optional;
import es.laboticademar.webstore.entities.Subcategoria;

public interface SubcategoriaService {
    Optional<Subcategoria> findByName(String name);
}
