package es.laboticademar.webstore.controllers.restControllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.laboticademar.webstore.dto.ProductoDTO;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.services.impl.FileStorageService;
import es.laboticademar.webstore.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/api/products")
@RequiredArgsConstructor
public class ProductoRestController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;
    
    @GetMapping("/get_pagable_list")
    public Page<ProductoDTO> list(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "25") int size,

        @RequestParam(required = false) String id,
        @RequestParam(required = false) String nombreProducto,
        @RequestParam(required = false) Boolean activo,
        @RequestParam(required = false) Long categoria,
        @RequestParam(required = false) Long subCategoria,
        @RequestParam(required = false) Long tipo,
        @RequestParam(required = false) Long familia,
        @RequestParam(required = false) Long laboratorio,
        @RequestParam(required = false) Long presentacion,
        @RequestParam(required = false) Boolean stock,
        @RequestParam(required = false) BigDecimal precioMin,
        @RequestParam(required = false) BigDecimal precioMax
    ) {
        return productService.getAllProducts(
            page, size,
            id,
            nombreProducto,
            activo,
            categoria,
            subCategoria,
            tipo,
            familia,
            laboratorio,
            presentacion,
            stock,
            precioMin,
            precioMax
        );
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadProducts(
            @RequestParam("file") MultipartFile file) throws Exception {
        try {
            productService.bulkUpload(file);
            return ResponseEntity.ok().build();
        } catch (Exception e ) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
    }
    }

    /**  
     * Crea o actualiza un producto, guarda la imagen en disco y guarda la ruta en el producto.  
     */
    @PostMapping(
      path = "/new",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProductoDTO> createOrUpdateProduct(
        @ModelAttribute ProductoDTO dto,
        @RequestParam(value = "imagen", required = false) MultipartFile imagen
    ) throws IOException {
        // 1) Si viene con ID, lo cargamos; si no, creamos uno nuevo
        Producto producto = (dto.getId() != null)
            ? productService.getProductoById(dto.getId())
            : new Producto();

        // 2) Copiamos propiedades comunes del DTO al entity (excluyendo imagenPath)
        BeanUtils.copyProperties(dto, producto, "imagenPath");

        // 3) Guardamos la imagen (si se envió) y asignamos la ruta lógica
        if (imagen != null && !imagen.isEmpty()) {
            String ruta = fileStorageService.store(imagen, producto);
            producto.setImagenPath(ruta);
        }

        // 4) Persistimos producto (nuevo o modificado)
        Producto saved = productService.saveOrUpdateProducto(producto);

        // 5) Devolvemos el DTO actualizado
        return ResponseEntity.ok(ProductoDTO.fromEntity(saved));
    }

    @GetMapping(path = "/search_names", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> searchNames(
        @RequestParam("q") String q,
        @RequestParam(value = "active", defaultValue = "false") boolean active
    ) {
        if (active) {
            return productService.findNamesContainingActive(q);
        } else {
            return productService.findNamesContaining(q);
        }
    }

}