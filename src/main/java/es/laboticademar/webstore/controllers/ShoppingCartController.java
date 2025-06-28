package es.laboticademar.webstore.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.ShoppingCartService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final DestacadoService destacadoService;

    @GetMapping({"/", ""})
    public String goShoppingCart(Principal principal, Model model) {
        
        model.addAttribute("shoppingCart", shoppingCartService.getOrCreateShoppingCartFromPrincipal(principal));
        model.addAttribute("destacados", destacadoService.getAllDestacados());

        return "purchases/shopping_cart";
    }
}
