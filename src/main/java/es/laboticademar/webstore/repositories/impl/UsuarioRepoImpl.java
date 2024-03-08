package es.laboticademar.webstore.repositories.impl;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.repositories.interfaces.UsuarioRepo;
import jakarta.persistence.EntityManager;

@Repository
public class UsuarioRepoImpl extends SimpleJpaRepository<Usuario, Long> implements UsuarioRepo {

    private final EntityManager entityManager;

    public UsuarioRepoImpl(EntityManager entityManager) {
        super(Usuario.class, entityManager);
        this.entityManager = entityManager;
    }

    // No es necesario sobrescribir el método findAll()

    // Implementación de métodos adicionales personalizados si es necesario
}
