package es.laboticademar.webstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.laboticademar.webstore.entities.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> getByCorreo(String correo);
    
    @Query("SELECT u FROM Usuario u WHERE u.correo IS NOT NULL")
    List<Usuario> getAllByCorreo();
}
