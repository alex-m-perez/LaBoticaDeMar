package es.laboticademar.webstore.services;

import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.entities.Producto;

public interface ProductService {
    public Producto saveOrUpdateProducto(Producto producto);
    public Optional<Producto> getProductoById(Integer id);
    public List<Producto> getAllProductos();
    public void deleteProductoById(Integer id);
}
