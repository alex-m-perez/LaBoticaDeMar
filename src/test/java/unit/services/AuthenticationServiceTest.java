package unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.UsuarioPrincipal;
import es.laboticademar.webstore.repositories.UsuarioDAO;
import es.laboticademar.webstore.security.auth.AuthenticationRequest;
import es.laboticademar.webstore.security.auth.AuthenticationResponse;
import es.laboticademar.webstore.security.auth.RegisterRequest;
import es.laboticademar.webstore.security.config.JwtService;
import es.laboticademar.webstore.services.impl.AuthenticationService;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock private UsuarioDAO usuarioDAO;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private AuthenticationManager authenticationManager;
    @InjectMocks private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void givenValidRegisterRequest_whenRegister_thenReturnToken() {
        // Given
        RegisterRequest req = RegisterRequest.builder()
            .password("miContraseña")
            .nombre("Luis")
            .apellido1("Martínez")
            .apellido2("García")
            .correo("luis.martinez@ejemplo.com")
            .calle("Calle Mayor, 45")
            .fechaNac(LocalDate.of(1990, 5, 20))
            .telefono(612345678)
            .build();

        Usuario saved = Usuario.builder()
            .nombre("Luis")
            .apellido1("Martínez")
            .apellido2("García")
            .correo("luis.martinez@ejemplo.com")
            .direccionPostal("Calle Mayor, 45")
            .telefono(612345678)
            .passwd("codificada")
            .roles(Set.of("ROLE_USUARIO"))
            .build();

        when(passwordEncoder.encode("miContraseña")).thenReturn("codificada");
        when(usuarioDAO.save(any(Usuario.class))).thenReturn(saved);
        when(jwtService.generateToken(any(UsuarioPrincipal.class))).thenReturn("token-mock");

        // When
        AuthenticationResponse resp = authenticationService.register(req);

        // Then
        assertThat(resp.getToken()).isEqualTo("token-mock");
    }

    @Test
    void givenValidAuthRequest_whenAuthenticate_thenReturnToken() {
        // Given
        AuthenticationRequest req = new AuthenticationRequest("juan@example.com", "clave");
        Usuario user = new Usuario();
        user.setCorreo("juan@example.com");
        user.setPasswd("codificada");
        user.setRoles(Set.of("ROLE_USUARIO"));

        when(usuarioDAO.getByCorreo("juan@example.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UsuarioPrincipal.class))).thenReturn("jwt-token");

        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);

        // When
        AuthenticationResponse resp = authenticationService.authenticate(req);

        // Then
        assertThat(resp.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void givenMissingUser_whenAuthenticate_thenThrow() {
        // Given
        AuthenticationRequest req = new AuthenticationRequest("desconocido@example.com", "clave");
        when(usuarioDAO.getByCorreo("desconocido@example.com")).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any())).thenReturn(
            org.mockito.Mockito.mock(Authentication.class)
        );

        // When / Then
        assertThrows(Exception.class, () -> authenticationService.authenticate(req));
    }
}
