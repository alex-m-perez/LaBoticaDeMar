package es.laboticademar.webstore.services.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.laboticademar.webstore.entities.Producto;

@Service
public class FileStorageService {
    private final Path rootLocation;

    public FileStorageService(@Value("${images.storage-dir}") String storageDir) {
        this.rootLocation = Paths.get(storageDir);
    }

    /**
     * Almacena la imagen en subcarpeta /<familia>/<laboratorio>/.../ y devuelve
     * la ruta relativa (subcarpeta + nombre de fichero).
     */
    public String store(MultipartFile file, Producto producto) throws IOException {
        // 1) Construir lista de segmentos de ruta según atributos no nulos
        List<String> segments = new ArrayList<>();
        if (producto.getFamilia() != null)  segments.add(sanitize(producto.getFamilia().getNombre()));
        if (producto.getLaboratorio() != null)  segments.add(sanitize(producto.getLaboratorio().getNombre()));
        if (producto.getCategoria() != null)  segments.add(sanitize(producto.getCategoria().getNombre()));
        if (producto.getSubCategoria() != null)  segments.add(sanitize(producto.getSubCategoria().getNombre()));

        // 2) Crear la ruta completa en disco
        Path destinationDir = rootLocation;
        for (String seg : segments) {
            destinationDir = destinationDir.resolve(seg);
        }
        Files.createDirectories(destinationDir);

        // 3) Generar nombre de fichero y guardarlo
        String filename = UUID.randomUUID() + "_" + sanitize(file.getOriginalFilename());
        Path destination = destinationDir.resolve(filename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        // 4) Construir la ruta lógica (subdirectorios + filename)
        String rutaRelativa = Stream.concat(segments.stream(), Stream.of(filename))
                                    .collect(Collectors.joining("/"));
        return rutaRelativa;
    }

    /** 
     * Elimina caracteres no seguros de nombres de carpeta/fichero 
     */
    private String sanitize(String input) {
        return input
            .trim()
            .toLowerCase()
            .replaceAll("[^a-z0-9\\-_.]", "-");
    }
}

