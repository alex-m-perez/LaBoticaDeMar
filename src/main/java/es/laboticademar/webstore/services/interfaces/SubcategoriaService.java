package es.laboticademar.webstore.services.interfaces;

import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.entities.Categoria;
import es.laboticademar.webstore.entities.Subcategoria;

public interface SubcategoriaService {
    Optional<Subcategoria> findByNombre(String name);
    List<Subcategoria> findByCategoria(Categoria categoria);
    Optional<Subcategoria> findById(Long id);
    List<Subcategoria> findAll();
    boolean existsByIdAndCategoriaId(Long subCategoriaId, Long categoriaId);
}
