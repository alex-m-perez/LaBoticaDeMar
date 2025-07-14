package es.laboticademar.webstore.controllers.restControllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.BreadcrumbDTO;
import es.laboticademar.webstore.dto.producto.ProductPageDTO;
import es.laboticademar.webstore.dto.producto.ProductSearchDTO;
import es.laboticademar.webstore.dto.producto.ProductoDTO;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import es.laboticademar.webstore.services.interfaces.ProductService;
import es.laboticademar.webstore.services.interfaces.SubcategoriaService;
import es.laboticademar.webstore.utils.BreadcrumbUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
    public class ProductRestController {

    private final ProductService productService;
    private final FamiliaService familiaService;
    private final CategoriaService categoriaService;
    private final SubcategoriaService subcategoriaService;

    @GetMapping("/get_pagable_list")
    public ResponseEntity<ProductPageDTO> getProductosPaginados(
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
            page, size, id, nombreProducto, true, 
            familia, categoria, subCategoria, tipo,
            laboratorio, stock, conDescuento,
            precioMin, precioMax
        );

        List<BreadcrumbDTO> crumbs = BreadcrumbUtils.generarBreadcrumbs(
            request, page, size,
            familia, categoria, subCategoria,
            familiaService, categoriaService, subcategoriaService
        );

        ProductPageDTO response = new ProductPageDTO(productPage, crumbs);

        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/search_names", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ProductSearchDTO> searchNames(
        @RequestParam("q") String q,
        @RequestParam(value = "active", defaultValue = "false") boolean active
    ) {
        return productService.findNamesContainingActive(q);
    }
}
