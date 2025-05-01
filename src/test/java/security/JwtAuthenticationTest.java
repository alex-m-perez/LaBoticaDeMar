package security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.UsuarioPrincipal;
import es.laboticademar.webstore.security.config.JwtAuthentication;
import es.laboticademar.webstore.security.config.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationTest {

    @Mock private JwtService jwtService;
    @Mock private UserDetailsService userDetailsService;
    private JwtAuthenticationTestWrapper jwtAuthentication;

    // Wrapper para exponer el método protected en test
    static class JwtAuthenticationTestWrapper extends JwtAuthentication {
        public JwtAuthenticationTestWrapper(JwtService jwtService, UserDetailsService uds) {
            super(jwtService, uds);
        }

        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
                throws ServletException, IOException {
            super.doFilterInternal(request, response, chain);
        }
    }

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        jwtAuthentication = new JwtAuthenticationTestWrapper(jwtService, userDetailsService);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void filterShouldSetAuthentication() throws ServletException, IOException {
        Usuario usuario = new Usuario();
        usuario.setCorreo("user@example.com");
        usuario.setPasswd("pass");
        usuario.setRoles(Set.of("ROLE_USUARIO"));
        var principal = new UsuarioPrincipal(usuario);

        when(jwtService.extractUsername(anyString())).thenReturn("user@example.com");
        when(userDetailsService.loadUserByUsername("user@example.com")).thenReturn(principal);
        when(jwtService.isTokenValid(anyString(), eq(principal))).thenReturn(true);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(new Cookie("jwtToken", "valid.jwt.token"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        jwtAuthentication.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
    }

    @Test
    void filterShouldNotAuthenticateWithoutToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        jwtAuthentication.doFilterInternal(request, response, chain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
