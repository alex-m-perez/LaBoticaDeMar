package es.laboticademar.webstore.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import es.laboticademar.webstore.dto.ProductoDTO;
import es.laboticademar.webstore.entities.Producto;

public interface ProductService {
    Producto saveOrUpdateProducto(Producto producto);
    Optional<Producto> findById(BigDecimal id);
    List<Producto> findAll();
    void deleteProductoById(BigDecimal id);
    Page<ProductoDTO> getAllProducts(int page, int size);
    Page<ProductoDTO> getAllProducts(
        int page,
        int size,
        String id,
        String nombreProducto,
        Boolean activo,
        List<Long> familiaIds,
        List<Long> categoriaIds,
        List<Long> subCategoriaIds,
        List<Long> tipoIds,
        List<Long> laboratorioIds,
        Boolean stock,
        BigDecimal precioMin,
        BigDecimal precioMax
    );
    List<Producto> bulkUpload(MultipartFile file) throws Exception;
    List<String> findNamesContaining(String q);
    List<String> findNamesContainingActive(String q);
    Long countAllProducts();
    Long countByActivo(Boolean activo);
    Integer sumTotalStock();
}
