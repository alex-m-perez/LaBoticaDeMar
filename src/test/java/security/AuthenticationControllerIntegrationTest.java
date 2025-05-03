package security;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.laboticademar.webstore.WebstoreApplication;
import es.laboticademar.webstore.security.auth.AuthenticationRequest;
import es.laboticademar.webstore.security.auth.AuthenticationResponse;
import es.laboticademar.webstore.security.auth.RegisterRequest;
import es.laboticademar.webstore.services.impl.AuthenticationService;

@SpringBootTest(classes = WebstoreApplication.class)
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void cleanBefore() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void cleanAfter() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testRegisterEndpointReturnsToken() throws Exception {
        RegisterRequest request = new RegisterRequest("pass", "John", "Doe", "Smith", "john@example.com", "Calle", 123);
        AuthenticationResponse response = AuthenticationResponse.builder().token("mock-jwt-token").build();

        when(authService.register(request)).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    void testAuthenticateEndpointReturnsToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("john@example.com", "pass");
        AuthenticationResponse response = AuthenticationResponse.builder().token("mock-jwt-token").build();

        when(authService.authenticate(request)).thenReturn(response);

        mockMvc.perform(post("/auth/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }
}
