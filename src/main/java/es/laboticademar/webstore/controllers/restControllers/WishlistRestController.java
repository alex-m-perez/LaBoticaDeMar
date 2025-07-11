package es.laboticademar.webstore.controllers.restControllers;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.wishlist.WishlistDTO;
import es.laboticademar.webstore.services.interfaces.WishlistService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistRestController {

    private final WishlistService wishlistService;

    @PostMapping("/add_item")
    public ResponseEntity<Void> addItemoWishlist(Principal principal, @RequestParam("productId") BigDecimal productId) {
        if (principal == null) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
        try {
            wishlistService.addProductoToWishlist(principal, productId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            // Por si el producto no existe
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/delete_item")
    public ResponseEntity<Void> deleteItemFromWishlist(Principal principal, @RequestParam("productId") BigDecimal productId) {
        if (principal == null) {
            return ResponseEntity.status(401).build(); // 401 Unauthorized
        }
        try {
            wishlistService.removeProductoFromWishlist(principal, productId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/state")
    public ResponseEntity<List<String>> getWishlistState(Principal principal) {
        List<String> wishlistState = wishlistService.getWishlistProductIdsForUser(principal);
        return ResponseEntity.ok(wishlistState);
    }

    @PostMapping("/merge")
    public ResponseEntity<Void> mergeWishlist(Principal principal, @RequestBody List<String> guestWishlistProductIds) {
        wishlistService.mergeGuestWishlist(principal, guestWishlistProductIds);
        return ResponseEntity.ok().build();
   }

   @PostMapping("/guest-details")
    public ResponseEntity<WishlistDTO> getGuestWishlistDetails(@RequestBody List<String> guestWishlistProductIds) {
        WishlistDTO wishlistDetails = wishlistService.getGuestWishlistDetails(guestWishlistProductIds);
        return ResponseEntity.ok(wishlistDetails);
   }
}