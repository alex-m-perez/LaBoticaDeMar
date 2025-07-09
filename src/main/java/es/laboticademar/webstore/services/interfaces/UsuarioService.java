package es.laboticademar.webstore.services.interfaces;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import es.laboticademar.webstore.dto.usuario.EmpleadoDTO;
import es.laboticademar.webstore.dto.usuario.UsuarioBusquedaDTO;
import es.laboticademar.webstore.dto.usuario.UsuarioPersonalDataDTO;
import es.laboticademar.webstore.entities.Usuario;

public interface UsuarioService {
    public List<Usuario> getAllUsers();
    public Optional<Usuario> findById(Long id);
    public List<Usuario> getAllByCorreo();
    public Optional<Usuario> getUserByCorreo(String correo);
    public Usuario save(Usuario usuario);
    public Usuario assignRoleToUser(String correo, String role);
    public Usuario removeRoleFromUser(String correo, String role);
    public Long getIdFromPrincipal(Principal principal);
    public UsuarioPersonalDataDTO getUserPersonalData(Principal principal);
    public Boolean updatePersonalData(Principal principal, UsuarioPersonalDataDTO data);
    public Integer getUserPoints(Principal principal);
    public List<UsuarioBusquedaDTO> findNombresCompletosContaining(String query);
    public Page<Usuario> findEmpleados(Pageable pageable);
    public void setActivo(Long id, boolean activo);
    public Usuario updateEmpleado(Long id, EmpleadoDTO empleadoDTO);
}
