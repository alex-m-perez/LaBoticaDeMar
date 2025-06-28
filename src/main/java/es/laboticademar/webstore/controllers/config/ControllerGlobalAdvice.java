package es.laboticademar.webstore.controllers.config;

import java.security.Principal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.laboticademar.webstore.entities.Categoria;
import es.laboticademar.webstore.entities.Familia;
import es.laboticademar.webstore.entities.Subcategoria;
import es.laboticademar.webstore.entities.UsuarioPrincipal;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import es.laboticademar.webstore.services.interfaces.ShoppingCartService;
import es.laboticademar.webstore.services.interfaces.SubcategoriaService;
import es.laboticademar.webstore.services.interfaces.WishlistService; // <-- 1. IMPORTAR
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerGlobalAdvice {

    private final FamiliaService familiaService;
    private final CategoriaService categoriaService;
    private final SubcategoriaService subcategoriaService;
    private final ShoppingCartService shoppingCartService;
    private final WishlistService wishlistService;
    private final ObjectMapper objectMapper;

    @ModelAttribute("currentUserName")
    public String addLoggedUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
                && auth.isAuthenticated()
                && auth.getPrincipal() instanceof UsuarioPrincipal principal) {

            var usuario = principal.getUsuario();
            String nombre = usuario.getNombre();
            String apellido1 = usuario.getApellido1();

            if (apellido1 != null && !apellido1.isBlank()) {
                return nombre + " " + apellido1;
            }
            return nombre;
    }
        return null;
    }

    @ModelAttribute("familiaCategorias")
    public Map<Familia, List<Map<Categoria, List<Subcategoria>>>> addCategorias() {
        List<Familia> familias = familiaService.findAll();
        Map<Familia, List<Map<Categoria, List<Subcategoria>>>> result = new LinkedHashMap<>();
        for (Familia fam : familias) {
            List<Categoria> cats = categoriaService.findByFamilia(fam);
            List<Map<Categoria, List<Subcategoria>>> listCatConSubs = new ArrayList<>();
            for (Categoria cat : cats) {
                List<Subcategoria> subs = subcategoriaService.findByCategoria(cat);
                Map<Categoria, List<Subcategoria>> m = new LinkedHashMap<>();
                m.put(cat, subs);
                listCatConSubs.add(m);
            }
            result.put(fam, listCatConSubs);
        }
        return result;
    }

    @ModelAttribute("userCartJson")
    public String addUserCartStateToJson(Principal principal) {
        if (principal == null)  return "{}";

        try {
            Map<String, Integer> cartState = shoppingCartService.getCartStateForUser(principal);
            return objectMapper.writeValueAsString(cartState);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
    
    @ModelAttribute("userWishlistJson")
    public String addUserWishlistStateToJson(Principal principal) {
        if (principal == null)  return "[]";

        try {
            List<String> wishlistIds = wishlistService.getWishlistProductIdsForUser(principal);
            return objectMapper.writeValueAsString(wishlistIds);
        } catch (JsonProcessingException e) {
            return "[]";
        }
    }
}