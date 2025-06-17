package integration.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import es.laboticademar.webstore.WebstoreApplication;
import es.laboticademar.webstore.services.impl.AuthenticationService;

@SpringBootTest(classes = WebstoreApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@EntityScan(basePackages = "es.laboticademar.webstore.entities")
class SecurityConfigIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authService;

    @BeforeEach
    void cleanBefore() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void cleanAfter() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldAllowAccessToPublicRoutes() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
        mockMvc.perform(get("/login")).andExpect(status().isOk());
        mockMvc.perform(get("/product")).andExpect(status().isOk());
    }

    @Test
    void shouldBlockAccessToProtectedRoutesWithoutAuth() throws Exception {
        mockMvc.perform(get("/profile")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/admin/home")).andExpect(status().is3xxRedirection());
        mockMvc.perform(get("/cart")).andExpect(status().is3xxRedirection());
    }
}
