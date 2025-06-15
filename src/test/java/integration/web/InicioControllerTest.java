package integration.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import es.laboticademar.webstore.WebstoreApplication;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.enumerations.PreferenciaEnum;
import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.UsuarioService;

@SpringBootTest(
    classes = WebstoreApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class InicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private DestacadoService destacadoService;

    @Test
    @DisplayName("GET / → main/welcome y modelo con destacados")
    void getWelcomePage() throws Exception {
        // Creamos algunos Productos de ejemplo
        Producto p1 = new Producto();
        p1.setId(BigDecimal.valueOf(1));
        p1.setNombre("Producto A");
        Producto p2 = new Producto();
        p2.setId(BigDecimal.valueOf(2));
        p2.setNombre("Producto B");
        List<Producto> fakeDest = List.of(p1, p2);

        when(destacadoService.getAllDestacados()).thenReturn(fakeDest);

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("main/welcome"))
            .andExpect(model().attribute("destacados", fakeDest));
    }

    @Test
    @DisplayName("GET /profile → perfil")
    void getProfile() throws Exception {
        mockMvc.perform(get("/profile"))
            .andExpect(status().isOk())
            .andExpect(view().name("perfil"));
    }

    @Test
    @DisplayName("GET /cart → purchases/shopping_cart")
    void getCart() throws Exception {
        mockMvc.perform(get("/cart"))
            .andExpect(status().isOk())
            .andExpect(view().name("purchases/shopping_cart"));
    }

    @Test
    @DisplayName("GET /wishlist → purchases/wishlist")
    void getWishlist() throws Exception {
        mockMvc.perform(get("/wishlist"))
            .andExpect(status().isOk())
            .andExpect(view().name("purchases/wishlist"));
    }

    @Test
    @DisplayName("GET /login → main/login")
    void getLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("main/login"));
    }

    @Test
    @DisplayName("GET /register → main/registro y enum de preferencias")
    void getRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
            .andExpect(status().isOk())
            .andExpect(view().name("main/registro"))
            .andExpect(model().attribute("preferenciasEnumList", PreferenciaEnum.values()));
    }

    @Test
    @DisplayName("GET /product → product/product")
    void getProductPage() throws Exception {
        mockMvc.perform(get("/product"))
            .andExpect(status().isOk())
            .andExpect(view().name("product/product"));
    }
}
