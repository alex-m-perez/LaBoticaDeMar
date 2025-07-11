package unit.security;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.UsuarioPrincipal;
import es.laboticademar.webstore.security.config.JwtService;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtServiceTest {

    private JwtService jwtService;
    private Usuario testUser;

    @BeforeEach
    void setUp() {
        // 1. Instanciar el servicio
        jwtService = new JwtService();
        
        // 2. Definir una clave secreta (en Base64) solo para los tests
        //    Esta clave debe ser suficientemente larga y segura para el algoritmo HS256
        String testSecretKey = "NDAzMGI3ZGE3YjM4OTQ2YjMyZGNlYjU4MjFhZGU3Njc5ZGRlMzM3YjYxMjU4Y2U5OTU5YjYyYjVjZGI5YjkyYw==";

        // 3. Inyectar la clave secreta en el campo privado 'SECRET_KEY' del servicio
        //    Esto es crucial porque @Value no funciona en un test unitario simple.
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", testSecretKey);

        // 4. Preparar el usuario de prueba
        testUser = new Usuario();
        testUser.setCorreo("test@example.com");
        testUser.setPasswd("testpass");
        testUser.setRoles(Set.of("ROLE_USUARIO"));
    }

    @Test
    void generateAndValidateToken() {
        UsuarioPrincipal principal = new UsuarioPrincipal(testUser);
        
        // El servicio ahora tiene la clave y puede generar el token
        String token = jwtService.generateToken(principal);

        // Las aserciones funcionarán correctamente
        assertThat(token).isNotNull();
        assertThat(jwtService.isTokenValid(token, principal)).isTrue();
        assertThat(jwtService.extractUsername(token)).isEqualTo("test@example.com");
    }

    @Test
    void extractInvalidTokenThrows() {
        // Esta prueba ahora valida el manejo de un token malformado,
        // en lugar de fallar por una clave nula.
        // Es buena práctica capturar la excepción más específica posible.
        String invalidToken = "esto.no.es.un.token.valido";
        assertThrows(MalformedJwtException.class, () -> jwtService.extractUsername(invalidToken));
    }
}