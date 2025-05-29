package es.laboticademar.webstore.services.interfaces;

import java.util.Optional;
import es.laboticademar.webstore.entities.TipoProducto;

public interface TipoService {
    Optional<TipoProducto> findByName(String name);
}
