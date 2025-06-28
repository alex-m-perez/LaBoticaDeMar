package es.laboticademar.webstore.services.interfaces;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import es.laboticademar.webstore.entities.Wishlist;

public interface WishlistService {
    Wishlist getOrCreateWishlistFromPrincipal(Principal principal);
    void addProductoToWishlist(Principal principal, BigDecimal productoId);
    void removeProductoFromWishlist(Principal principal, BigDecimal productoId);
    List<String> getWishlistProductIdsForUser(Principal principal);
}