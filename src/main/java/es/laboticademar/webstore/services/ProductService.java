package es.laboticademar.webstore.services;

import java.util.List;
import es.laboticademar.webstore.entities.Producto;

public interface ProductService {
    public Producto saveOrUpdateProducto(Producto producto);
    public Producto getProductoById(Integer id);
    public List<Producto> getAllProductos();
    public void deleteProductoById(Integer id);
}
