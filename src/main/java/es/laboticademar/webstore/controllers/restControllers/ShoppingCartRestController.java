package es.laboticademar.webstore.controllers.restControllers;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.PaymentDTO;
import es.laboticademar.webstore.dto.ShoppingCartDTO;
import es.laboticademar.webstore.services.interfaces.ShoppingCartService;
import es.laboticademar.webstore.services.interfaces.VentaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartRestController {

    private final ShoppingCartService shoppingCartService;
    private final VentaService ventaService;

    @PostMapping(path = "/add_item")
    public ResponseEntity<Void> addItem(Principal principal, @RequestParam("itemId") BigDecimal itemId, @RequestParam("add") Boolean add) {

        boolean isSuccess = shoppingCartService.addItem(principal, itemId, add);

        if (isSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/delete_item")
    public ResponseEntity<Void> deleteItem(Principal principal, @RequestParam("itemId") BigDecimal itemId) {

        boolean isSuccess = shoppingCartService.deleteItem(principal, itemId);

        if (isSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
      }
    }

    @PostMapping("/merge")
    public ResponseEntity<Void> mergeCarts(Principal principal, @RequestBody Map<String, Integer> guestCart) {
        shoppingCartService.mergeGuestCart(principal, guestCart);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/buy_cart")
    public ResponseEntity<Void> buyCartItems(
            Principal principal, 
            @RequestBody PaymentDTO paymentData,
            @RequestHeader(name = "X-Use-Points", defaultValue = "false") Boolean usePoints) 
    {
        if (ventaService.realizarVenta(principal, paymentData, usePoints)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/guest-details")
    public ResponseEntity<ShoppingCartDTO> getGuestCartDetails(@RequestBody Map<String, Integer> guestCart) {
        ShoppingCartDTO cartDetails = shoppingCartService.getGuestCartDetails(guestCart);
        return ResponseEntity.ok(cartDetails);
   }
}