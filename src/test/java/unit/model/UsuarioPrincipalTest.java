package unit.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.UsuarioPrincipal;

class UsuarioPrincipalTest {

    @Test
    void testGetAuthoritiesAndCredentials() {
        Usuario user = new Usuario();
        user.setCorreo("user@example.com");
        user.setPasswd("pass");
        user.setRoles(Set.of("ROLE_USUARIO", "ROLE_ADMIN"));

        UsuarioPrincipal principal = new UsuarioPrincipal(user);

        assertThat(principal.getAuthorities()).hasSize(2);
        assertThat(principal.getUsername()).isEqualTo("user@example.com");
        assertThat(principal.getPassword()).isEqualTo("pass");
        assertThat(principal.isEnabled()).isTrue();
    }
}
