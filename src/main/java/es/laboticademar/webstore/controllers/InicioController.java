package es.laboticademar.webstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.enumerations.PreferenciaEnum;
import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.UsuarioService;


@Controller
@RequestMapping("")
public class InicioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DestacadoService destacadoService;
    

    @GetMapping("/")
    public String accessWelcomePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("destacados", destacadoService.getAllDestacados());
        return "main/welcome";
    }

    @GetMapping("/profile")
    public String accessProfile() {
        return "perfil";
    }

    @GetMapping("/cart")
    public String goShoppingCart() {
        return "purchases/shopping_cart";
    }

    @GetMapping("/wishlist")
    public String goWishlist() {
        return "purchases/wishlist";
    }

    @GetMapping("/login")
    public String goLoginPage() {
        return "main/login";
    }

    @GetMapping("/register")
    public String goRegisterPage(Model model) {
        // Mete directamente el array de enums en el modelo
        model.addAttribute("preferenciasEnumList", PreferenciaEnum.values());
        return "main/registro";
    }

    @GetMapping("/product")
    public String goProduct() {
        return "product/product";
    }
}