package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Destacado;

public interface DestacadoDAO extends JpaRepository<Destacado, Integer> {
    
}
