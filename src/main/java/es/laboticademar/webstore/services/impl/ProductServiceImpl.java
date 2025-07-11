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
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.laboticademar.webstore.dto.producto.ProductoDTO;
import es.laboticademar.webstore.entities.Categoria;
import es.laboticademar.webstore.entities.Familia;
import es.laboticademar.webstore.entities.Laboratorio;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.Subcategoria;
import es.laboticademar.webstore.entities.TipoProducto;
import es.laboticademar.webstore.mappers.ExcelToProductMapper;
import es.laboticademar.webstore.repositories.CategoriaDAO;
import es.laboticademar.webstore.repositories.FamiliaDAO;
import es.laboticademar.webstore.repositories.LaboratorioDAO;
import es.laboticademar.webstore.repositories.ProductDAO;
import es.laboticademar.webstore.repositories.SubcategoriaDAO;
import es.laboticademar.webstore.repositories.TipoDAO;
import es.laboticademar.webstore.services.interfaces.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;
    private final FamiliaDAO familiaDAO;
    private final CategoriaDAO categoriaDAO;
    private final SubcategoriaDAO subcategoriaDAO;
    private final LaboratorioDAO laboratorioDAO;
    private final TipoDAO tipoProductoDAO;
    private final ExcelToProductMapper mapper;

    @Override
    public Producto saveOrUpdateProducto(Producto producto) {
        return productDAO.save(producto);
    }

    @Override
    @Transactional
    public ProductoDTO saveProductWithImage(ProductoDTO dto, MultipartFile imagenFile) throws Exception {
        if (imagenFile != null && !imagenFile.isEmpty()) {
            dto.setImagenData(imagenFile.getBytes());
        }
        Producto entity = mapDtoToEntity(dto);
        Producto savedEntity = productDAO.save(entity);
        return mapEntityToDto(savedEntity);
    }

    private Producto mapDtoToEntity(ProductoDTO dto) {
        // Busca si el producto ya existe para actualizarlo, o crea uno nuevo.
        Producto entity = productDAO.findById(new BigDecimal(dto.getId()))
                                    .orElse(new Producto());

        // Mapeo de campos simples
        entity.setId(new BigDecimal(dto.getId()));
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        entity.setComposition(dto.getComposition());
        entity.setUse(dto.getUse());
        entity.setStock(dto.getStock());
        entity.setActivo(dto.getActivo());
        entity.setPrice(dto.getPrice());
        entity.setDiscount(dto.getDiscount());
        entity.setRating(dto.getRating());
        entity.setRatingCount(dto.getRatingCount());

        // Mapeamos los datos de la imagen si se proporcionaron
        if (dto.getImagenData() != null) {
            entity.setImagenData(dto.getImagenData());
        }

        // Mapeo de relaciones buscando las entidades completas en la BBDD
        if (dto.getFamiliaId() != null) {
            Familia f = familiaDAO.findById(dto.getFamiliaId()).orElseThrow(() -> new EntityNotFoundException("Familia no encontrada con ID: " + dto.getFamiliaId()));
            entity.setFamilia(f);
        }
        if (dto.getCategoriaId() != null) {
            Categoria c = categoriaDAO.findById(dto.getCategoriaId()).orElseThrow(() -> new EntityNotFoundException("Categoría no encontrada con ID: " + dto.getCategoriaId()));
            entity.setCategoria(c);
        }
        if (dto.getSubCategoriaId() != null) {
            Subcategoria sc = subcategoriaDAO.findById(dto.getSubCategoriaId()).orElseThrow(() -> new EntityNotFoundException("Subcategoría no encontrada con ID: " + dto.getSubCategoriaId()));
            entity.setSubCategoria(sc);
        }
        if (dto.getLaboratorioId() != null) {
            Laboratorio l = laboratorioDAO.findById(dto.getLaboratorioId()).orElseThrow(() -> new EntityNotFoundException("Laboratorio no encontrado con ID: " + dto.getLaboratorioId()));
            entity.setLaboratorio(l);
        }
        if (dto.getTipoId() != null) {
            TipoProducto t = tipoProductoDAO.findById(dto.getTipoId()).orElseThrow(() -> new EntityNotFoundException("Tipo no encontrado con ID: " + dto.getTipoId()));
            entity.setTipo(t);
        }

        return entity;
    }

    /**
     * Helper privado para mapear de Entidad a DTO.
     */
    private ProductoDTO mapEntityToDto(Producto entity) {
        if (entity == null) return null;

        ProductoDTO dto = new ProductoDTO();

        // Mapeo de campos simples
        dto.setId(entity.getId().toString());
        dto.setNombre(entity.getNombre());
        dto.setDescripcion(entity.getDescripcion());
        dto.setComposition(entity.getComposition());
        dto.setUse(entity.getUse());
        dto.setStock(entity.getStock());
        dto.setActivo(entity.getActivo());
        dto.setPrice(entity.getPrice());
        dto.setDiscount(entity.getDiscount());
        dto.setRating(entity.getRating());
        dto.setRatingCount(entity.getRatingCount());

        // Mapeamos los datos de la imagen
        dto.setImagenData(entity.getImagenData());

        // Mapeo de IDs de las relaciones
        if (entity.getFamilia() != null) {
            dto.setFamiliaId(entity.getFamilia().getId());
        }
        if (entity.getCategoria() != null) {
            dto.setCategoriaId(entity.getCategoria().getId());
        }
        if (entity.getSubCategoria() != null) {
            dto.setSubCategoriaId(entity.getSubCategoria().getId());
        }
        if (entity.getLaboratorio() != null) {
            dto.setLaboratorioId(entity.getLaboratorio().getId());
        }
        if (entity.getTipo() != null) {
            dto.setTipoId(entity.getTipo().getId());
        }

        return dto;
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
    public Page<ProductoDTO> getAllProducts(
            int page, int size, String id, String nombreProducto, Boolean activo,
            List<Long> familiaIds, List<Long> categoriaIds, List<Long> subCategoriaIds,
            List<Long> tipoIds, List<Long> laboratorioIds, Boolean stock, 
            Boolean conDescuento, Float precioMin, Float precioMax) {

        Specification<Producto> spec = createProductSpecification(id, nombreProducto, activo, familiaIds, categoriaIds, subCategoriaIds, tipoIds, laboratorioIds, stock, conDescuento, precioMin, precioMax);
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre"));

        return productDAO.findAll(spec, pageable).map(this::mapEntityToDto);
    }


    // He movido la lógica de Specification a un método privado para más claridad
    private Specification<Producto> createProductSpecification(
            String id, String nombreProducto, Boolean activo,
            List<Long> familiaIds, List<Long> categoriaIds, List<Long> subCategoriaIds,
            List<Long> tipoIds, List<Long> laboratorioIds, Boolean stock, 
            Boolean conDescuento, Float precioMin, Float precioMax) {

        // Empezamos con una especificación vacía que no filtra nada.
        Specification<Producto> spec = Specification.where(null);

        // Añadimos filtros condicionalmente.
        if (activo != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("activo"), activo));
        }

        if (id != null && !id.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(root.get("id").as(String.class), "%" + id + "%"));
        }

        if (nombreProducto != null && !nombreProducto.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("nombre")), "%" + nombreProducto.toLowerCase() + "%"));
        }

        if (familiaIds != null && !familiaIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("familia").get("id").in(familiaIds));
        }

        if (categoriaIds != null && !categoriaIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("categoria").get("id").in(categoriaIds));
        }

        if (subCategoriaIds != null && !subCategoriaIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("subCategoria").get("id").in(subCategoriaIds));
        }

        if (tipoIds != null && !tipoIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("tipo").get("id").in(tipoIds));
        }

        if (laboratorioIds != null && !laboratorioIds.isEmpty()) {
            spec = spec.and((root, query, cb) -> root.get("laboratorio").get("id").in(laboratorioIds));
        }

        if (stock != null) {
            if (stock) { // Si stock es true, buscar productos con stock > 0
                spec = spec.and((root, query, cb) -> cb.greaterThan(root.get("stock"), 0));
            } else { // Si stock es false, buscar productos con stock = 0 o null
                spec = spec.and((root, query, cb) -> cb.or(cb.isNull(root.get("stock")), cb.equal(root.get("stock"), 0)));
            }
        }

        if (conDescuento != null && conDescuento) {
            spec = spec.and((root, query, cb) -> 
                cb.and(
                    cb.isNotNull(root.get("discount")),
                    cb.greaterThan(root.get("discount"), 0f) // <-- Usamos 0f para comparar con Float
                )
            );
        }

        if (precioMin != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), precioMin));
        }

        if (precioMax != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), precioMax));
        }

        return spec;
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
