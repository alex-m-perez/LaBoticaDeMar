package es.laboticademar.webstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Laboratorio;

public interface LaboratorioDAO extends JpaRepository<Laboratorio, Long> {
    Optional<Laboratorio> findByNombre(String name);
    List<Laboratorio> findAllByOrderByNombreAsc();
}
