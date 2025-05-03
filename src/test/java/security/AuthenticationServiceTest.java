package security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.laboticademar.webstore.WebstoreApplication;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.UsuarioPrincipal;
import es.laboticademar.webstore.repositories.UsuarioDAO;
import es.laboticademar.webstore.security.auth.AuthenticationRequest;
import es.laboticademar.webstore.security.auth.AuthenticationResponse;
import es.laboticademar.webstore.security.auth.RegisterRequest;
import es.laboticademar.webstore.security.config.JwtService;
import es.laboticademar.webstore.services.impl.AuthenticationService;

public class AuthenticationServiceTest {

@Mock
private UsuarioDAO usuarioDAO;

@Mock
private PasswordEncoder passwordEncoder;

@Mock
private JwtService jwtService;

@Mock
private AuthenticationManager authenticationManager;

@InjectMocks
private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }
    
    @Test
    void testRegisterReturnsToken() {
        RegisterRequest request = new RegisterRequest("pass", "John", "Doe", "Smith", "john@example.com", "123 St", 123456789);

        Usuario usuario = Usuario.builder()
            .nombre("John")
            .apellido1("Doe")
            .apellido2("Smith")
            .correo("john@example.com")
            .direccionPostal("123 St")
            .telefono(123456789)
            .passwd("encoded")
            .roles(Set.of("ROLE_USUARIO"))
            .build();

        when(passwordEncoder.encode("pass")).thenReturn("encoded");
        when(usuarioDAO.save(any(Usuario.class))).thenReturn(usuario);
        when(jwtService.generateToken(any(UsuarioPrincipal.class))).thenReturn("mock-jwt-token");


        AuthenticationResponse response = authenticationService.register(request);

        assertThat(response.getToken()).isEqualTo("mock-jwt-token");
    }

    @Test
    void testAuthenticateReturnsToken() {
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "pass");
        Usuario usuario = new Usuario();
        usuario.setCorreo("john@example.com");
        usuario.setPasswd("encoded");
        usuario.setRoles(Set.of("ROLE_USUARIO"));

        when(usuarioDAO.getByCorreo("john@example.com")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any())).thenReturn("jwt-token");

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertThat(response.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void testAuthenticateThrowsWhenUserNotFound() {
        AuthenticationRequest request = new AuthenticationRequest("missing@example.com", "pass");
        when(usuarioDAO.getByCorreo("missing@example.com")).thenReturn(Optional.empty());
        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));

        assertThrows(Exception.class, () -> authenticationService.authenticate(request));
    }
}
