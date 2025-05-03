package security;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.UsuarioPrincipal;

public class UsuarioPrincipalTest {

    @Test
    void testGetAuthorities() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("user@example.com");
        usuario.setPasswd("pass");
        usuario.setRoles(Set.of("ROLE_USUARIO", "ROLE_ADMIN"));

        UsuarioPrincipal principal = new UsuarioPrincipal(usuario);

        assertThat(principal.getAuthorities()).hasSize(2);
        assertThat(principal.getUsername()).isEqualTo("user@example.com");
        assertThat(principal.getPassword()).isEqualTo("pass");
        assertThat(principal.isEnabled()).isTrue();
    }
}
