package es.laboticademar.webstore.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Categoria;
import es.laboticademar.webstore.entities.Familia;
import es.laboticademar.webstore.repositories.CategoriaDAO;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaDAO categoriaDAO;

    @Override
    public List<Categoria> findAll() {
        return categoriaDAO.findAll();
    }
    
    @Override
    public Optional<Categoria> findByNombre(String name) {
        return categoriaDAO.findByNombre(name);
    }

    @Override
    public List<Categoria> findByFamilia(Familia familia) {
        return categoriaDAO.findByFamilia(familia);
    }

    @Override
    public Optional<Categoria> findById(Long id) {
        return categoriaDAO.findById(id);
    }

    @Override
    public boolean existsByIdAndFamiliaId(Long categoriaId, Long familiaId) {
        return categoriaDAO.existsByIdAndFamiliaId(categoriaId, familiaId);
    }
}
