package es.laboticademar.webstore.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Categoria;
import es.laboticademar.webstore.repositories.CategoriaDAO;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaDAO categoriaDAO;

    @Override
    public Optional<Categoria> findByName(String name) {
        return categoriaDAO.findByName(name);
    }
}
