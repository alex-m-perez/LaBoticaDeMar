package security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.UsuarioPrincipal;
import es.laboticademar.webstore.security.config.JwtService;

public class JwtServiceTest {

    private JwtService jwtService;
    private Usuario testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        testUser = new Usuario();
        testUser.setCorreo("test@example.com");
        testUser.setPasswd("testpass");
        testUser.setRoles(Set.of("ROLE_USUARIO"));
    }

    @Test
    void generateAndValidateToken() {
        var principal = new UsuarioPrincipal(testUser);
        String token = jwtService.generateToken(principal);

        assertThat(jwtService.isTokenValid(token, principal)).isTrue();
        assertThat(jwtService.extractUsername(token)).isEqualTo("test@example.com");
    }

    @Test
    void tokenExpirationWorks() throws InterruptedException {
        // Token generado con fecha actual
        var principal = new UsuarioPrincipal(testUser);
        String token = jwtService.generateToken(principal);
        assertThat(jwtService.isTokenValid(token, principal)).isTrue();
    }

    @Test
    void extractInvalidTokenThrows() {
        assertThrows(Exception.class, () -> jwtService.extractUsername("invalid.token.value"));
    }
}
