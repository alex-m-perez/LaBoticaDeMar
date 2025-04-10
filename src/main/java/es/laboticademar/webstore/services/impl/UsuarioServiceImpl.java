package es.laboticademar.webstore.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.repositories.UsuarioDAO;
import es.laboticademar.webstore.services.UsuarioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO UsuarioDAO;

    @Override
    public List<Usuario> getAllUsers() {
        return UsuarioDAO.findAll();
    }

    @Override
    public List<Usuario> getAllByCorreo() {
        return UsuarioDAO.getAllByCorreo();
    }

    @Override
    public Optional<Usuario> getUserByCorreo(String correo) {
        return UsuarioDAO.getByCorreo(correo);
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        return UsuarioDAO.save(usuario);
    }

    @Override
    public Usuario assignRoleToUser(String correo, String role) {
        Usuario usuario = UsuarioDAO.getByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
        // Agregamos el rol al set (por ejemplo: "ROLE_USER" o "ROLE_ADMIN")
        usuario.getRoles().add(role);
        return UsuarioDAO.save(usuario);
    }

    @Override
    public Usuario removeRoleFromUser(String correo, String role) {
        Usuario usuario = UsuarioDAO.getByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
        usuario.getRoles().remove(role);
        return UsuarioDAO.save(usuario);
    }
}
