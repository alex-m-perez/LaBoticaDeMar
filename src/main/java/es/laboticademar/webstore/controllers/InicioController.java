package es.laboticademar.webstore.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.enumerations.PreferenciaEnum;
import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.ShoppingCartService;
import es.laboticademar.webstore.services.interfaces.WishlistService;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class InicioController {

    private final DestacadoService destacadoService;
    private final ShoppingCartService shoppingCartService;
    private final WishlistService wishlistService;
    
    @GetMapping("/")
    public String goWelcomePage(Model model, Principal principal) {
        model.addAttribute("destacados", destacadoService.getAllDestacados());
        return "main/welcome";
    }

    @GetMapping("/login")
    public String goLoginPage() {
        return "main/login";
    }

    @GetMapping("/register")
    public String goRegisterPage(Model model) {
        model.addAttribute("preferenciasEnumList", PreferenciaEnum.values());
        return "main/registro";
    }
}