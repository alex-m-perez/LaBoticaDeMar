package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import es.laboticademar.webstore.entities.Usuario;

public interface UsuarioRepo extends JpaRepository<Usuario, Long> {
    // No es necesario definir un método findAll() aquí
}
