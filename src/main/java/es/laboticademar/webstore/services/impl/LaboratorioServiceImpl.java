package es.laboticademar.webstore.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Familia;
import es.laboticademar.webstore.entities.Laboratorio;
import es.laboticademar.webstore.repositories.LaboratorioDAO;
import es.laboticademar.webstore.services.interfaces.LaboratorioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LaboratorioServiceImpl implements LaboratorioService {

    private final LaboratorioDAO laboratorioDAO;

    @Override
    public List<Laboratorio> findAll() {
        return laboratorioDAO.findAll();
    }

    @Override
    public Optional<Laboratorio> findByNombre(String name) {
        return laboratorioDAO.findByNombre(name);
    }
}
