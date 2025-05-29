package es.laboticademar.webstore.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Subcategoria;
import es.laboticademar.webstore.repositories.SubcategoriaDAO;
import es.laboticademar.webstore.services.interfaces.SubcategoriaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubcategoriaServiceImpl implements SubcategoriaService {

    private final SubcategoriaDAO subcategoriaDAO;

    @Override
    public Optional<Subcategoria> findByName(String name) {
        return subcategoriaDAO.findByName(name);
    }
}
