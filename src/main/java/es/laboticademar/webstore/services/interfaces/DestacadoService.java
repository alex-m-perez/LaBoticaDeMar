package es.laboticademar.webstore.services.interfaces;

import java.util.List;

import es.laboticademar.webstore.entities.Destacado;
import es.laboticademar.webstore.entities.Producto;

public interface DestacadoService {
    public Destacado saveOrUpdateDestacado(Destacado Destacado);
    public Destacado getDestacadoById(Integer id);
    public List<Producto> getAllDestacados();
    public void deleteDestacadoById(Integer id);
}
