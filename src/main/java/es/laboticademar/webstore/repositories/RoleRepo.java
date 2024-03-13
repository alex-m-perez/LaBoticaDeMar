package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
    // No es necesario definir un método findAll() aquí
}
