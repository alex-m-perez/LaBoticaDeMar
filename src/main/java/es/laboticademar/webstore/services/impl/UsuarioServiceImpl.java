package es.laboticademar.webstore.services.impl;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.UsuarioPersonalDataDTO;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.repositories.UsuarioDAO;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final ModelMapper modelMapper;

    @Override
    public List<Usuario> getAllUsers() {
        return usuarioDAO.findAll();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return usuarioDAO.findById(id);
    }

    @Override
    public List<Usuario> getAllByCorreo() {
        return usuarioDAO.getAllByCorreo();
    }

    @Override
    public Optional<Usuario> getUserByCorreo(String correo) {
        return usuarioDAO.getByCorreo(correo);
    }

    @Override
    public Usuario saveUsuario(Usuario usuario) {
        return usuarioDAO.save(usuario);
    }

    @Override
    public Usuario assignRoleToUser(String correo, String role) {
        Usuario usuario = usuarioDAO.getByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
        // Agregamos el rol al set (por ejemplo: "ROLE_USER" o "ROLE_ADMIN")
        usuario.getRoles().add(role);
        return usuarioDAO.save(usuario);
    }

    @Override
    public Usuario removeRoleFromUser(String correo, String role) {
        Usuario usuario = usuarioDAO.getByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
        usuario.getRoles().remove(role);
        return usuarioDAO.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Long getIdFromPrincipal(Principal principal) {
        if (principal == null)  throw new IllegalArgumentException("El objeto Principal no puede ser nulo.");

        String email = principal.getName();
        return usuarioDAO.getByCorreo(email)
                .map(Usuario::getId)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró un usuario para el correo asociado al Principal: " + email));
    }

    @Override
    public UsuarioPersonalDataDTO getUserPersonalData(Principal principal) {
        Optional<Usuario> user = getUserByCorreo(principal.getName());
        return modelMapper.map(user, UsuarioPersonalDataDTO.class);
    }


    @Override
    public Boolean updatePersonalData(Principal principal, UsuarioPersonalDataDTO data) {
        return getUserByCorreo(principal.getName())
            .map(existing -> {
                modelMapper.map(data, existing);
                usuarioDAO.save(existing);
                return true;
            })
            .orElse(false);
    }


    
}
