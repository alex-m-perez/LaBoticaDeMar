package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Imagen;

public interface ImagenDAO extends JpaRepository<Imagen, Long> {
    
}
