package es.laboticademar.webstore.services.interfaces;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import es.laboticademar.webstore.dto.wishlist.WishlistDTO;
import es.laboticademar.webstore.entities.Wishlist;

public interface WishlistService {
    public Wishlist getOrCreateWishlistFromPrincipal(Principal principal);
    public void addProductoToWishlist(Principal principal, BigDecimal productoId);
    public void removeProductoFromWishlist(Principal principal, BigDecimal productoId);
    public List<String> getWishlistProductIdsForUser(Principal principal);
    public void mergeGuestWishlist(Principal principal, List<String> guestProductIds);
    public WishlistDTO getGuestWishlistDetails(List<String> guestProductIds);
}