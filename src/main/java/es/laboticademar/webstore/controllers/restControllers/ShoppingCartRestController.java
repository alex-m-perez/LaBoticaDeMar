package es.laboticademar.webstore.controllers.restControllers;

import java.math.BigDecimal;
import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.services.interfaces.ShoppingCartService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartRestController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping(path = "/add_item")
    public ResponseEntity<Void> addItem(Principal principal, @RequestParam("itemId") BigDecimal itemId, @RequestParam("add") Boolean add) {

        boolean isSuccess = shoppingCartService.addItemT(principal, itemId, add);

        if (isSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/delete_item")
    public ResponseEntity<Void> deleteItem(Principal principal, @RequestParam("itemId") BigDecimal itemId) {

        // Ahora llamas al service con el tipo correcto
        boolean isSuccess = shoppingCartService.deleteItem(principal, itemId);

        if (isSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
    }
    }

}