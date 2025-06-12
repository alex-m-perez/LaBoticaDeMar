package es.laboticademar.webstore.controllers.restControllers;

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

import es.laboticademar.webstore.dto.ProductoDTO;
import es.laboticademar.webstore.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/api/products")
@RequiredArgsConstructor
public class ProductoApiController {

    private final ProductService productService;

    @GetMapping("/get_pagable_list")
    public Page<ProductoDTO> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return productService.getAllProducts(page, size);
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
        if (active) {
            return productService.findNamesContainingActive(q);
        } else {
            return productService.findNamesContaining(q);
        }
    }

}