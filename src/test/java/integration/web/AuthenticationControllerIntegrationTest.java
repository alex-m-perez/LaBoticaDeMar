package integration.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.laboticademar.webstore.WebstoreApplication;
import es.laboticademar.webstore.security.auth.AuthenticationRequest;
import es.laboticademar.webstore.security.auth.AuthenticationResponse;
import es.laboticademar.webstore.security.auth.RegisterRequest;
import es.laboticademar.webstore.services.impl.AuthenticationService;

@SpringBootTest(classes = WebstoreApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthenticationControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean  private AuthenticationService authService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void registroDebeDevolverToken() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
            .password("clave")
            .nombre("Juan")
            .apellido1("Pérez")
            .apellido2("Gómez")
            .correo("juan@example.com")
            .calle("Calle Falsa, 123")
            .fechaNac(LocalDate.of(1990, 5, 20))
            .telefono(612345678)
            .build();

        AuthenticationResponse fakeResp = AuthenticationResponse.builder()
            .token("jwt-mock")
            .build();
        when(authService.register(any(RegisterRequest.class))).thenReturn(fakeResp);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-mock"));
    }

    @Test
    void autenticacionDebeDevolverToken() throws Exception {
        AuthenticationRequest req = new AuthenticationRequest("juan@example.com", "clave");
        AuthenticationResponse fakeResp = AuthenticationResponse.builder()
            .token("jwt-mock")
            .build();
        when(authService.authenticate(any(AuthenticationRequest.class))).thenReturn(fakeResp);

        mockMvc.perform(post("/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("jwt-mock"));
    }
}
