package es.laboticademar.webstore.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

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

    @Override
    public Map<Character, List<Laboratorio>> getLaboratoriosAgrupadosPorLetra() {
        Map<Character, List<Laboratorio>> laboratoriosAgrupados = new TreeMap<>();

        List<Laboratorio> todos = laboratorioDAO.findAllByOrderByNombreAsc();

        for (Laboratorio lab : todos) {
            if (lab.getNombre() != null && !lab.getNombre().isEmpty()) {
                char primeraLetra = Character.toUpperCase(lab.getNombre().charAt(0));

                laboratoriosAgrupados.computeIfAbsent(primeraLetra, k -> new ArrayList<>()).add(lab);
            }
        }

        return laboratoriosAgrupados;
    }
}
