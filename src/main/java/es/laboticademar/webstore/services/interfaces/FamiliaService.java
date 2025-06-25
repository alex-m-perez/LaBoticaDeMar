package es.laboticademar.webstore.services.interfaces;

import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.entities.Familia;

public interface FamiliaService {
    List<Familia> findAll();
    Optional<Familia> findByNombre(String name);
    Optional<Familia> findById(Long id);
}
