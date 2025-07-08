package es.laboticademar.webstore.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Categoria;
import es.laboticademar.webstore.entities.Subcategoria;
import es.laboticademar.webstore.repositories.SubcategoriaDAO;
import es.laboticademar.webstore.services.interfaces.SubcategoriaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubcategoriaServiceImpl implements SubcategoriaService {

    private final SubcategoriaDAO subcategoriaDAO;

    @Override
    public Optional<Subcategoria> findByNombre(String name) {
        return subcategoriaDAO.findByNombre(name);
    }

    @Override
    public List<Subcategoria> findByCategoria(Categoria categoria) {
        return subcategoriaDAO.findByCategoria(categoria);
    }

    @Override
    public Optional<Subcategoria> findById(Long id) {
        return subcategoriaDAO.findById(id);
    }

    @Override
    public List<Subcategoria> findAll() {
        return subcategoriaDAO.findAll();
    }

    @Override
    public boolean existsByIdAndCategoriaId(Long subCategoriaId, Long categoriaId) {
        return subcategoriaDAO.existsByIdAndCategoriaId(subCategoriaId, categoriaId);
    }
}
