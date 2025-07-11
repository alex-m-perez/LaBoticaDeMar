package es.laboticademar.webstore.services.interfaces;

import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.entities.TipoProducto;

public interface TipoService {
    public Optional<TipoProducto> findByNombre(String name);
    public List<TipoProducto> findAll();
}
