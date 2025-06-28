package es.laboticademar.webstore.services.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import es.laboticademar.webstore.entities.Laboratorio;

public interface LaboratorioService {
    List<Laboratorio> findAll();
    Optional<Laboratorio> findByNombre(String name);
    Map<Character, List<Laboratorio>> getLaboratoriosAgrupadosPorLetra();
}
