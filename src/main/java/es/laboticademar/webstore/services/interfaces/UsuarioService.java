package es.laboticademar.webstore.services.interfaces;

import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.entities.Usuario;

public interface UsuarioService {
    List<Usuario> getAllUsers();
    List<Usuario> getAllByCorreo();
    Optional<Usuario> getUserByCorreo(String correo);

    // Métodos adicionales para gestionar usuarios y roles:
    Usuario saveUsuario(Usuario usuario);
    Usuario assignRoleToUser(String correo, String role);
    Usuario removeRoleFromUser(String correo, String role);
}
