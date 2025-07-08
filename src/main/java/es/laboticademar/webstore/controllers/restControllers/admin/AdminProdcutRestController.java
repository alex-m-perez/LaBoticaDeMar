package es.laboticademar.webstore.controllers.restControllers.admin;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.laboticademar.webstore.dto.ProductoDTO;
import es.laboticademar.webstore.services.interfaces.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/api/products")
@RequiredArgsConstructor
public class AdminProdcutRestController {

    private final ProductService productService;
    
    @GetMapping("/get_pagable_list")
    public Page<ProductoDTO> list(
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
        @RequestParam(required = false) BigDecimal precioMin,
        @RequestParam(required = false) BigDecimal precioMax
    ) {

        Page<ProductoDTO> productPage = productService.getAllProducts(
            page, size, id, nombreProducto, activo, 
            familia, categoria, subCategoria, tipo,
            laboratorio, stock, precioMin, precioMax
        );

        return productPage;
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
}