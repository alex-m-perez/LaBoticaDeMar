// src/main/java/es/laboticademar/webstore/services/impl/FamiliaServiceImpl.java
package es.laboticademar.webstore.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Familia;
import es.laboticademar.webstore.repositories.FamiliaDAO;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FamiliaServiceImpl implements FamiliaService {

    private final FamiliaDAO familiaDAO;

    @Override
    public List<Familia> findAll() {
        return familiaDAO.findAll();
    }

    @Override
    public Optional<Familia> findByNombre(String name) {
        return familiaDAO.findByNombre(name);
    }

    @Override
    public Optional<Familia> findById(Long id) {
        return familiaDAO.findById(id);
    }
}
