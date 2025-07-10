package es.laboticademar.webstore.services.interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import es.laboticademar.webstore.dto.producto.ProductoDTO;
import es.laboticademar.webstore.entities.Producto;

public interface ProductService {
    public Producto saveOrUpdateProducto(Producto producto);
    public ProductoDTO saveProductWithImage(ProductoDTO dto, MultipartFile imagenFile) throws Exception;
    public Optional<Producto> findById(BigDecimal id);
    public List<Producto> findAll();
    public void deleteProductoById(BigDecimal id);
    public Page<ProductoDTO> getAllProducts(
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
        Boolean conDescuento,
        Float precioMin,
        Float precioMax
    );
    public List<Producto> bulkUpload(MultipartFile file) throws Exception;
    public List<String> findNamesContaining(String q);
    public List<String> findNamesContainingActive(String q);
    public Long countAllProducts();
    public Long countByActivo(Boolean activo);
    public Integer sumTotalStock();
}
