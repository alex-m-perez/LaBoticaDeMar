package es.laboticademar.webstore.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Role;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.repositories.UsuarioRepo;
import es.laboticademar.webstore.security.auth.AuthenticationRequest;
import es.laboticademar.webstore.security.auth.AuthenticationResponse;
import es.laboticademar.webstore.security.auth.RegisterRequest;
import es.laboticademar.webstore.security.config.JwtService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    @Autowired
    private final UsuarioRepo usuarioRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @SuppressWarnings("null")
    public AuthenticationResponse register(RegisterRequest request) {
        var usuario = Usuario.builder()
            .nombre(request.getNombre())
            .apellido1(request.getApellido1())
            .apellido2(request.getApellido2())
            .correo(request.getCorreo())
            .direccionPostal(request.getDireccionPostal())
            .telefono(request.getTelefono())
            .passwd(passwordEncoder.encode(request.getPassword()))
            .role(Role.USUARIO)
            .build();
        usuarioRepo.save(usuario);
        var jwtToken = jwtService.generateToken(usuario);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getCorreo(), 
                request.getPassword()
            )
        );
        var usuario = usuarioRepo.getByCorreo(request.getCorreo()).orElseThrow();
        var jwtToken = jwtService.generateToken(usuario);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
    
}
