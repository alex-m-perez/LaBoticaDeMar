// src/main/java/es/laboticademar/webstore/services/impl/ProductServiceImpl.java
package es.laboticademar.webstore.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.laboticademar.webstore.dto.ProductoDTO;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.mappers.ExcelToProductMapper;
import es.laboticademar.webstore.repositories.ProductDAO;
import es.laboticademar.webstore.services.interfaces.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;
    private final ExcelToProductMapper mapper;

    @Override
    public Producto saveOrUpdateProducto(Producto producto) {
        return productDAO.save(producto);
    }

    @Override
    public Optional<Producto> findById(BigDecimal id) {
        return productDAO.findById(id);
    }

    @Override
    public List<Producto> findAll() {
        return productDAO.findAll();
    }

    @Override
    public void deleteProductoById(BigDecimal id) {
        productDAO.deleteById(id);
    }

    @Override
    public Page<ProductoDTO> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        Page<Producto> entities = productDAO.findAll(pageable);

        return entities.map(ent -> {
            ProductoDTO dto = new ProductoDTO();
            // Copiamos propiedades sencillas
            org.springframework.beans.BeanUtils.copyProperties(ent, dto);
            // Ajuste para la etiqueta de categoría
            dto.setCategoriaNombre(
                ent.getCategoria() != null
                    ? ent.getCategoria().getNombre()
                    : ""
            );
            return dto;
        });
    }

    @Override
    public Page<ProductoDTO> getAllProducts(
        int page,
        int size,
        String id,
        String nombreProducto,
        Boolean activo,
        Long categoriaId,
        Long subCategoriaId,
        Long tipoId,
        Long familiaId,
        Long laboratorioId,
        Long presentacionId,
        Boolean stock,
        BigDecimal precioMin,
        BigDecimal precioMax
    ) {
        Specification<Producto> spec = Specification.where(null);

        if (id != null && !id.isBlank()) {
            try {
                BigDecimal idVal = new BigDecimal(id.trim());
                spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("id"), idVal)
                );
            } catch (NumberFormatException e) { }
        }
        if (nombreProducto != null && !nombreProducto.isBlank()) {
            spec = spec.and((r, q, cb) ->
                cb.like(cb.lower(r.get("nombre")),
                        "%"+nombreProducto.toLowerCase()+"%"));
        }
        if (activo != null) {
            spec = spec.and((r, q, cb) ->
                cb.equal(r.get("activo"), activo));
        }
        if (categoriaId != null) {
            spec = spec.and((r, q, cb) ->
                cb.equal(r.get("categoria").get("id"), categoriaId));
        }
        if (subCategoriaId != null) {
            spec = spec.and((r, q, cb) ->
                cb.equal(r.get("subCategoria").get("id"), subCategoriaId));
        }
        if (tipoId != null) {
            spec = spec.and((r, q, cb) ->
                cb.equal(r.get("tipo").get("id"), tipoId));
        }
        if (familiaId != null) {
            spec = spec.and((r, q, cb) ->
                cb.equal(r.get("familia").get("id"), familiaId));
        }
        if (laboratorioId != null) {
            spec = spec.and((r, q, cb) ->
                cb.equal(r.get("laboratorio").get("id"), laboratorioId));
        }
        if (presentacionId != null) {
            spec = spec.and((r, q, cb) ->
                cb.equal(r.get("presentacion").get("id"), presentacionId));
        }
        if (stock != null) {
            if (stock) {
                spec = spec.and((r, q, cb) ->
                    cb.greaterThan(r.get("stock"), 0));
            } else {
                spec = spec.and((r, q, cb) ->
                    cb.equal(r.get("stock"), 0));
            }
        }
        if (precioMin != null) {
            spec = spec.and((r, q, cb) ->
                cb.ge(r.get("price"), precioMin));
        }
        if (precioMax != null) {
            spec = spec.and((r, q, cb) ->
                cb.le(r.get("price"), precioMax));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre"));
        return productDAO.findAll(spec, pageable)
                   .map(ProductoDTO::fromEntity);
    }

    @Override
    public List<Producto> bulkUpload(MultipartFile file) throws Exception {
        List<Producto> duplicados = new ArrayList<>();

        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;  // salto cabecera

                BigDecimal id = mapper.extractId(row);
                if (productDAO.existsById(id)) {
                    // Sólo guardamos identificador y nombre para reporting
                    Producto p = new Producto();
                    p.setId(id);
                    p.setNombre(new DataFormatter().formatCellValue(row.getCell(1)).trim());
                    duplicados.add(p);
                    continue;
                }

                Producto nuevo = mapper.map(row);
                productDAO.save(nuevo);
            }
        }

        return duplicados;
    }

    @Override
    public List<String> findNamesContaining(String q) {
      return productDAO
        .findTop10ByNombreContainingIgnoreCase(q)
        .stream()
        .map(Producto::getNombre)
        .toList();
    }

    @Override
    public List<String> findNamesContainingActive(String q) {
        return productDAO.findTop10ByNombreContainingIgnoreCaseAndActivoTrue(q)
                          .stream()
                          .map(Producto::getNombre)
                          .toList();
    }

    @Override
    public Long countAllProducts() {
        return productDAO.count();
    }

    @Override
    public Long countByActivo(Boolean activo) {
        try {
            if (activo)  return productDAO.countByActivoTrue();
            if (!activo)  return productDAO.countByActivoFalse();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (long) 0;
    }

    @Override
    public Integer sumTotalStock() {
        return productDAO.sumTotalStock();
    }
}
