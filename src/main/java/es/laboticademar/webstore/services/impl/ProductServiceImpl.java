// src/main/java/es/laboticademar/webstore/services/impl/ProductServiceImpl.java
package es.laboticademar.webstore.services.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Producto getProductoById(BigDecimal id) {
        return productDAO.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<Producto> getAllProductos() {
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
            dto.setCategoriaEtiqueta(
                ent.getCategoria() != null
                    ? ent.getCategoria().getNombre()
                    : ""
            );
            return dto;
        });
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
}
