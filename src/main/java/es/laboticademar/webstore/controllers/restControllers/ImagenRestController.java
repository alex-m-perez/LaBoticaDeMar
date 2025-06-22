package es.laboticademar.webstore.controllers.restControllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import es.laboticademar.webstore.entities.Imagen;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.repositories.ImagenDAO;
import es.laboticademar.webstore.repositories.ProductDAO;
import es.laboticademar.webstore.services.impl.FileStorageService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImagenRestController {

    private final FileStorageService storage;
    private final ImagenDAO imagenDAO;
    private final ProductDAO productDAO;

    @PostMapping
    public ResponseEntity<Map<String, Object>> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("productoId") BigDecimal productoId) {

        Producto producto = productDAO.findById(productoId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no existe"));

        try {
            String ruta = storage.store(file, producto);

            Imagen img = Imagen.builder()
                .ruta(ruta)
                .fecha(new Date())
                .producto(producto)
                .build();
            img = imagenDAO.save(img);

            return ResponseEntity.ok(Map.of(
                "id", img.getId(),
                "url", "/images/" + ruta   // será, por ejemplo, /images/familia/lab/cat/uuid_nombre.jpg
            ));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No pudo guardarse la imagen", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Void> getUrl(@PathVariable Long id) {
        Imagen img = imagenDAO.findById(id)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        // redirige al handler estático de /images/**
        return ResponseEntity.status(HttpStatus.FOUND)
                             .location(URI.create("/images/" + img.getRuta()))
                             .build();
    }
}
