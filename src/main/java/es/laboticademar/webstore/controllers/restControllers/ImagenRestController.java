package es.laboticademar.webstore.controllers.restControllers;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/images") // Ruta base para la API
@RequiredArgsConstructor
public class ImagenRestController {

    private final ProductService productService;

    // El endpoint ahora busca por el ID del producto
    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getProductImage(@PathVariable BigDecimal id) {

        // 1. Buscamos el producto en la BBDD
        Optional<Producto> optionalProducto = productService.findById(id);

        // 2. Comprobamos si el producto existe
        if (optionalProducto.isEmpty()) {
            // Si el producto no existe, devolvemos 404 Not Found
            return ResponseEntity.notFound().build();
        }

        Producto producto = optionalProducto.get();

        // 3. Comprobamos si el producto tiene datos de imagen
        if (producto.getImagenData() != null && producto.getImagenData().length > 0) {
            // Si tiene imagen, la devolvemos
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Puedes cambiar esto a PNG si lo necesitas
                    .body(producto.getImagenData());
        } else {
            // Si el producto existe pero no tiene imagen, también es un "Not Found" para la imagen
            return ResponseEntity.notFound().build();
        }
    }
}