package es.laboticademar.webstore.services.interfaces;

import java.util.Optional;
import es.laboticademar.webstore.entities.Laboratorio;

public interface LaboratorioService {
    Optional<Laboratorio> findByNombre(String name);
}
