package es.laboticademar.webstore.services.interfaces;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.entities.Usuario;

public interface UsuarioService {
    List<Usuario> getAllUsers();
    Optional<Usuario> findById(Long id);
    List<Usuario> getAllByCorreo();
    Optional<Usuario> getUserByCorreo(String correo);
    Usuario saveUsuario(Usuario usuario);
    Usuario assignRoleToUser(String correo, String role);
    Usuario removeRoleFromUser(String correo, String role);
    Long getIdFromPrincipal(Principal principal);
}
