package es.laboticademar.webstore.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.repositories.ProductDAO;
import es.laboticademar.webstore.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductDAO productDAO;

    @Override
    public Producto saveOrUpdateProducto(Producto producto) {
        return productDAO.save(producto);
    }

    @Override
    public Producto getProductoById(Integer id) {
        return productDAO.findById(id).get();
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
