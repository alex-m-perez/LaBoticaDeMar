package es.laboticademar.webstore.controllers.restControllers;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.ProductoDTO;
import es.laboticademar.webstore.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/product/api")
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
}
