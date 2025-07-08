package es.laboticademar.webstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.laboticademar.webstore.entities.Usuario;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> getByCorreo(String correo);
    
    @Query("SELECT u FROM Usuario u WHERE u.correo IS NOT NULL")
    List<Usuario> getAllByCorreo();

    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(CONCAT(u.nombre, ' ', u.apellido1, ' ', u.apellido2)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(CONCAT(u.apellido1, ' ', u.apellido2)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(CONCAT(u.nombre, ' ', u.apellido1)) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Usuario> findByNombreFlexible(@Param("query") String query, Pageable pageable);
}
