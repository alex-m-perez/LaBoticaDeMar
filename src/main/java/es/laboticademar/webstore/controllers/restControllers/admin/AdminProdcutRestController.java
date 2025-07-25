package es.laboticademar.webstore.controllers.restControllers.admin;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.laboticademar.webstore.dto.producto.ProductoDTO;
import es.laboticademar.webstore.services.interfaces.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/api/products")
@RequiredArgsConstructor
public class AdminProdcutRestController {

    private final ProductService productService;
    
    @GetMapping("/get_pagable_list")
    public Page<ProductoDTO> getProductosPaginados(
        HttpServletRequest request,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "25") int size,
        @RequestParam(required = false) String id,
        @RequestParam(required = false) String nombreProducto,
        @RequestParam(required = false) List<Long> familia,
        @RequestParam(required = false) List<Long> categoria,
        @RequestParam(required = false) List<Long> subCategoria,
        @RequestParam(required = false) List<Long> tipo,
        @RequestParam(required = false) List<Long> laboratorio,
        @RequestParam(required = false) Boolean activo,
        @RequestParam(required = false) Boolean stock,
        @RequestParam(required = false) Boolean conDescuento, // <-- AÑADIR ESTA LÍNEA
        @RequestParam(required = false) Float precioMin,
        @RequestParam(required = false) Float precioMax
    ) {

        Page<ProductoDTO> productPage = productService.getAllProducts(
            page, size, id, nombreProducto, activo, 
            familia, categoria, subCategoria, tipo,
            laboratorio, stock, conDescuento,
            precioMin, precioMax
        );

        return productPage;
    }

    @PostMapping(path = "/new", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductoDTO> createOrUpdateProduct(
            // Spring mapea los campos del form a las propiedades del DTO
            ProductoDTO productoDTO, 
            // Capturamos el archivo de imagen
            @RequestParam(value = "imagenFile", required = false) MultipartFile imagenFile) {
        try {
            ProductoDTO savedProduct = productService.saveProductWithImage(productoDTO, imagenFile);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
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

    @GetMapping(path = "/search_names", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> searchNames(
        @RequestParam("q") String q,
        @RequestParam(value = "active", defaultValue = "false") boolean active
    ) {
        return productService.findNamesContaining(q);
    }
}