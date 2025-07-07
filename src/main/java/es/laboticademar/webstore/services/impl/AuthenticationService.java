package es.laboticademar.webstore.services.impl;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.UsuarioPrincipal;
import es.laboticademar.webstore.repositories.UsuarioDAO;
import es.laboticademar.webstore.security.auth.AuthenticationRequest;
import es.laboticademar.webstore.security.auth.AuthenticationResponse;
import es.laboticademar.webstore.security.auth.RegisterRequest;
import es.laboticademar.webstore.security.config.JwtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @SuppressWarnings("null")
    public AuthenticationResponse register(RegisterRequest request) {
        String direccion = String.format(
            "%s %s%s%s, %s, %s, %s (%s)",
            request.getCalle(), request.getNumero(),
            request.getPiso() != null ? ", " + request.getPiso() : "º",
            request.getPuerta() != null ? " " + request.getPuerta() : "",
            request.getCodigoPostal(), request.getLocalidad(),
            request.getProvincia(), request.getPais()
        );

        var usuario = Usuario.builder()
            .nombre(request.getNombre())
            .apellido1(request.getApellido1())
            .apellido2(request.getApellido2())
            .correo(request.getCorreo())
            .passwd(passwordEncoder.encode(request.getPassword()))
            .telefono(request.getTelefono())
            .fechaNac(java.sql.Date.valueOf(request.getFechaNac()))
            .genero(request.getGenero())
            .direccionPostal(direccion)
            .aceptaPromociones(Boolean.TRUE.equals(request.getAceptaPromociones()))
            .aceptaTerminos(Boolean.TRUE.equals(request.getAceptaTerminos()))
            .aceptaPrivacidad(Boolean.TRUE.equals(request.getAceptaPrivacidad()))
            .preferencias(request.getPreferencias())
            .roles(Set.of("ROLE_USUARIO"))
            .build();

        usuarioDAO.save(usuario);

        var usuarioPrincipal = new UsuarioPrincipal(usuario);
        var jwtToken = jwtService.generateToken(usuarioPrincipal);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getCorreo(), 
                request.getPassword()
            )
        );

        var usuario = usuarioDAO.getByCorreo(request.getCorreo()).orElseThrow();
        var usuarioPrincipal = new UsuarioPrincipal(usuario);
        var jwtToken = jwtService.generateToken(usuarioPrincipal);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
    
}
