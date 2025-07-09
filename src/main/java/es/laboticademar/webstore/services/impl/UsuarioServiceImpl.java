package es.laboticademar.webstore.services.impl;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.usuario.EmpleadoDTO;
import es.laboticademar.webstore.dto.usuario.UsuarioBusquedaDTO;
import es.laboticademar.webstore.dto.usuario.UsuarioPersonalDataDTO;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.repositories.UsuarioDAO;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

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
    public Usuario save(Usuario usuario) {
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


    @Override
    public Integer getUserPoints(Principal principal) {
        return getUserByCorreo(principal.getName())
            .map(Usuario::getPuntos)
            .orElse(0);
    }


    @Override
    public List<UsuarioBusquedaDTO> findNombresCompletosContaining(String query) {
        PageRequest pageable = PageRequest.of(0, 10);
        List<Usuario> usuarios = usuarioDAO.findByNombreFlexible(query, pageable);

        return usuarios.stream()
                .map(u -> {
                    String nombre = u.getNombre() != null ? u.getNombre() : "";
                    String apellido1 = u.getApellido1() != null ? u.getApellido1() : "";
                    String apellido2 = u.getApellido2() != null ? u.getApellido2() : "";
                    String nombreCompleto = (nombre + " " + apellido1 + " " + apellido2).trim().replaceAll("\\s+", " ");
                    // Devuelve el nuevo DTO con ID y nombre
                    return new UsuarioBusquedaDTO(u.getId(), nombreCompleto);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> findEmpleados(Pageable pageable) {
        // Llama al método del repositorio para obtener solo los empleados
        return usuarioDAO.findByRolesContaining("ROLE_EMPLEADO", pageable);
    }

    @Override
    public void setActivo(Long id, boolean activo) {
        Usuario usuario = usuarioDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con id: " + id));
        usuario.setActivo(activo);
        usuarioDAO.save(usuario);
    }

    @Override
    public Usuario updateEmpleado(Long id, EmpleadoDTO empleadoDTO) {
        Usuario usuarioExistente = usuarioDAO.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con id: " + id));

        modelMapper.map(empleadoDTO, usuarioExistente);
        if (empleadoDTO.getPassword() != null && !empleadoDTO.getPassword().isEmpty()) {
            usuarioExistente.setPasswd(passwordEncoder.encode(empleadoDTO.getPassword()));
        }

        return usuarioDAO.save(usuarioExistente);
    }
}
