package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> getByCorreo(String correo);
}
