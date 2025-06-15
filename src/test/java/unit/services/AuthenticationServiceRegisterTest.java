package unit.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.UsuarioPrincipal;
import es.laboticademar.webstore.repositories.UsuarioDAO;
import es.laboticademar.webstore.security.auth.AuthenticationResponse;
import es.laboticademar.webstore.security.auth.RegisterRequest;
import es.laboticademar.webstore.security.config.JwtService;
import es.laboticademar.webstore.services.impl.AuthenticationService;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceRegisterTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioDAO usuarioRDAO;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest req;

    @BeforeEach
    void setUp() {
        // Datos de ejemplo en español:
        req = RegisterRequest.builder()
            .nombre("Ana")
            .apellido1("López")
            .apellido2("Martín")
            .correo("ana.lopez@ejemplo.com")
            .password("secreto")
            .calle("C/ Mayor")
            .numero("10")
            .piso("2")
            .puerta("B")
            .codigoPostal("28080")
            .localidad("Madrid")
            .provincia("Madrid")
            .pais("España")
            .telefono(600112233)
            .fechaNac(LocalDate.of(1990, 5, 20)) 
            .genero(1)
            .aceptaPromociones(true)
            .aceptaTerminos(false)
            .aceptaPrivacidad(true)
            .preferencias(Set.of(1,2,3))
            .build();

        when(passwordEncoder.encode("secreto")).thenReturn("secretoCodificado");
        when(jwtService.generateToken(org.mockito.ArgumentMatchers.any(UsuarioPrincipal.class)))
            .thenReturn("token-final");
    }

    @Test
    void testRegistroGuardaUsuarioConCamposMapeadosCorrectamenteYDevuelveToken() {
        // Llamamos al método
        AuthenticationResponse resp = authenticationService.register(req);

        // Capturamos el usuario que se ha guardado
        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRDAO).save(captor.capture());
        Usuario usuarioGuardado = captor.getValue();

        // 1) Token
        assertThat(resp.getToken()).isEqualTo("token-final");

        // 2) Campos básicos
        assertThat(usuarioGuardado.getNombre()).isEqualTo("Ana");
        assertThat(usuarioGuardado.getApellido1()).isEqualTo("López");
        assertThat(usuarioGuardado.getApellido2()).isEqualTo("Martín");
        assertThat(usuarioGuardado.getCorreo()).isEqualTo("ana.lopez@ejemplo.com");
        assertThat(usuarioGuardado.getPasswd()).isEqualTo("secretoCodificado");
        assertThat(usuarioGuardado.getTelefono()).isEqualTo(600112233);
        assertThat(usuarioGuardado.getGenero()).isEqualTo(1);

        // 3) Fecha de nacimiento: comparamos epoch millis
        long expectedEpoch = java.sql.Date.valueOf(req.getFechaNac()).getTime();
        assertThat(usuarioGuardado.getFechaNac().getTime()).isEqualTo(expectedEpoch);

        // 4) Dirección formateada
        String esperadoDir = "C/ Mayor 10, 2 B, 28080, Madrid, Madrid (España)";
        assertThat(usuarioGuardado.getDireccionPostal()).isEqualTo(esperadoDir);

        // 5) Booleans
        assertThat(usuarioGuardado.getAceptaPromociones()).isTrue();
        assertThat(usuarioGuardado.getAceptaTerminos()).isFalse();
        assertThat(usuarioGuardado.getAceptaPrivacidad()).isTrue();

        // 6) Preferencias y roles
        assertThat(usuarioGuardado.getPreferencias()).containsExactlyInAnyOrder(1, 2, 3);
        assertThat(usuarioGuardado.getRoles()).containsExactly("ROLE_USUARIO");
    }

}
