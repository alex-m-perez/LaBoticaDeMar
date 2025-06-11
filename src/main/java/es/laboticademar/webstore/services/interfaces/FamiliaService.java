package es.laboticademar.webstore.services.interfaces;

import java.util.Optional;
import es.laboticademar.webstore.entities.Familia;

public interface FamiliaService {
    Optional<Familia> findByNombre(String name);
}
