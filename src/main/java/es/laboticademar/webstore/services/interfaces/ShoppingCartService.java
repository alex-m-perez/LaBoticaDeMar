package es.laboticademar.webstore.services.interfaces;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;

import es.laboticademar.webstore.entities.ShoppingCart;

public interface ShoppingCartService {
    public ShoppingCart getOrCreateShoppingCartByUsuarioId(Long usuarioId);
    public ShoppingCart getOrCreateShoppingCartFromPrincipal(Principal principal);
    public boolean deleteItem(Principal principal, BigDecimal itemId);
    public boolean addItem(Principal principal, BigDecimal itemId, Boolean add);
    public Map<String, Integer> getCartStateForUser(Principal principal);
}