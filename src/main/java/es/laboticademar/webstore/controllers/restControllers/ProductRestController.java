package es.laboticademar.webstore.controllers.restControllers;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.ProductoDTO;
import es.laboticademar.webstore.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping("/get_pagable_list")
    public Page<ProductoDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String nombreProducto,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) List<Long> familia,
            @RequestParam(required = false) List<Long> categoria,
            @RequestParam(required = false) List<Long> subCategoria,
            @RequestParam(required = false) List<Long> tipo,
            @RequestParam(required = false) List<Long> laboratorio,
            @RequestParam(required = false) Boolean stock,
            @RequestParam(required = false) BigDecimal precioMin,
            @RequestParam(required = false) BigDecimal precioMax
    ) {
        // El parámetro 'activo' aquí es clave para que no se desordenen los demás
        return productService.getAllProducts(
            page, size, id, nombreProducto, true, 
            familia, categoria, subCategoria, tipo,
            laboratorio, stock, precioMin, precioMax
        );
    }
}
