package es.laboticademar.webstore.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.WishlistService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final DestacadoService destacadoService;
    private final WishlistService wishlistService;

    @GetMapping({"/", ""})
    public String goWishlist(Principal principal, Model model) {
        model.addAttribute("likedProducts", wishlistService.getOrCreateWishlistFromPrincipal(principal).getProductos());
        model.addAttribute("destacados", destacadoService.getAllDestacados());

        return "purchases/wishlist";
    }
}
