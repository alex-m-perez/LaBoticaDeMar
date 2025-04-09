package es.laboticademar.webstore.services;

import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.entities.Destacado;
import es.laboticademar.webstore.entities.Producto;

public interface DestacadoService {
    public Destacado saveOrUpdateDestacado(Destacado Destacado);
    public Optional<Destacado> getDestacadoById(Integer id);
    public List<Producto> getAllDestacados();
    public void deleteDestacadoById(Integer id);
}
