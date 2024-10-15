package es.laboticademar.webstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.services.DestacadoService;
import es.laboticademar.webstore.services.UsuarioService;


@Controller
@RequestMapping("")
public class InicioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private DestacadoService destacadoService;

    @GetMapping("")
    public String accessWelcomePage(Model model) {

        model.addAttribute("destacados", destacadoService.getAllDestacados());

        return "main/welcome";
    }

    @GetMapping("/profile")
    public String accessProfile() {
        return "perfil";
    }

    @GetMapping("/cart")
    public String goShoppingCart() {
        return "shopping_cart";
    }

    @GetMapping("/wishlist")
    public String goWishlist() {
        return "wishlist";
    }

    @GetMapping("/product")
    public String goProduct() {
        return "product/product";
    }

    @GetMapping("/prueba")
    public String mostrarFormLogin(Model model) {
        List<Usuario> usuarios = usuarioService.getAllUsers();
        String prueba = "prueba";
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getNombre());
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("prueba", prueba);
        return "login";  // Nombre del JSP de inicio, sin la extensión .jsp
    }
}