package es.laboticademar.webstore.services.interfaces;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import es.laboticademar.webstore.dto.UsuarioPersonalDataDTO;
import es.laboticademar.webstore.entities.Usuario;

public interface UsuarioService {
    public List<Usuario> getAllUsers();
    public Optional<Usuario> findById(Long id);
    public List<Usuario> getAllByCorreo();
    public Optional<Usuario> getUserByCorreo(String correo);
    public Usuario saveUsuario(Usuario usuario);
    public Usuario assignRoleToUser(String correo, String role);
    public Usuario removeRoleFromUser(String correo, String role);
    public Long getIdFromPrincipal(Principal principal);
    public UsuarioPersonalDataDTO getUserPersonalData(Principal principal);
    public Boolean updatePersonalData(Principal principal, UsuarioPersonalDataDTO data);

}
