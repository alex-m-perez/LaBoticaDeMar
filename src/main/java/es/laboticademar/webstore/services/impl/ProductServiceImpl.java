package es.laboticademar.webstore.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.repositories.ProductDAO;
import es.laboticademar.webstore.services.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    @Override
    public Producto saveOrUpdateProducto(Producto producto) {
        return productDAO.save(producto);
    }

    @Override
    public Producto getProductoById(Integer id) {
        return productDAO.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<Producto> getAllProductos() {
        return productDAO.findAll();
    }
    

    @Override
    public void deleteProductoById(Integer id) {
        productDAO.deleteById(id);
    }
}
