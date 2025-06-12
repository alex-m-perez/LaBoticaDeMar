package es.laboticademar.webstore.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import es.laboticademar.webstore.dto.ProductoDTO;
import es.laboticademar.webstore.entities.Producto;

public interface ProductService {
    Producto saveOrUpdateProducto(Producto producto);
    Producto getProductoById(BigDecimal id);
    List<Producto> findAll();
    void deleteProductoById(BigDecimal id);
    Page<ProductoDTO> getAllProducts(int page, int size);
    List<Producto> bulkUpload(MultipartFile file) throws Exception;
    List<String> findNamesContaining(String q);
    List<String> findNamesContainingActive(String q);
}
