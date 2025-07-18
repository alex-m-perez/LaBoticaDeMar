package es.laboticademar.webstore.services.impl;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.cart.CartItemDTO;
import es.laboticademar.webstore.dto.cart.GuestCartProductoDTO;
import es.laboticademar.webstore.dto.cart.ShoppingCartDTO;
import es.laboticademar.webstore.entities.CartItem;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.ShoppingCart;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.repositories.ShoppingCartDAO;
import es.laboticademar.webstore.services.interfaces.CartItemService;
import es.laboticademar.webstore.services.interfaces.ProductService;
import es.laboticademar.webstore.services.interfaces.ShoppingCartService;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * Implementación de la interfaz ShoppingCartService.
 */
@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {


    private final ShoppingCartDAO shoppingCartDAO;
    private final UsuarioService usuarioService;
    private final CartItemService cartItemService;
    private final ProductService productService;

    @Override
    @Transactional
    public ShoppingCart getOrCreateShoppingCartByUsuarioId(Long usuarioId) {
        return shoppingCartDAO.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioService.findById(usuarioId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "No se puede crear un carrito para un usuario inexistente. Usuario no encontrado con ID: " + usuarioId));

                    ShoppingCart nuevoCarrito = ShoppingCart.builder()
                            .usuario(usuario)
                            .items(new ArrayList<>())
                            .ultimaModificacion(LocalDateTime.now())
                            .build();

                    return shoppingCartDAO.save(nuevoCarrito);
                });
    }


    @Override
    public ShoppingCart getOrCreateShoppingCartFromPrincipal(Principal principal) {
        if (principal == null)  return new ShoppingCart();

        ShoppingCart cart = new ShoppingCart();
        try {
            Long usuarioId = usuarioService.getIdFromPrincipal(principal);
            cart = getOrCreateShoppingCartByUsuarioId(usuarioId);
        } catch (Exception e) {
            throw new RuntimeException("No pudimos encontrar tu carrito de la compra. Por favor, intenta iniciar sesión de nuevo.", e);
        }

        return cart;
    }

    @Override
    public boolean deleteItem(Principal principal, BigDecimal itemId) {
        if (principal == null || itemId == null) {
            return false;
        }
        try {
            ShoppingCart userCart = getOrCreateShoppingCartFromPrincipal(principal);
            Long cartId = userCart.getId();

            shoppingCartDAO.deleteItem(cartId, itemId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean addItem(Principal principal, BigDecimal productId, Boolean add) {
        if (principal == null || productId == null) {
            return false;
        }

        Producto producto = productService.findById(productId)
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
        if (!producto.getActivo()) return false;

        try {
            ShoppingCart userCart = getOrCreateShoppingCartFromPrincipal(principal);
            Optional<CartItem> optionalCartItem = cartItemService.findByShoppingCartIdAndProductoId(userCart.getId(), productId);

            if (optionalCartItem.isPresent()) {
                CartItem existingItem = optionalCartItem.get();
                int currentQuantity = existingItem.getCantidad();

                if (add) {
                    existingItem.setCantidad(currentQuantity + 1);
                    cartItemService.saveOrUpdateProducto(existingItem);
                } else {
                    if (currentQuantity > 1) {
                        existingItem.setCantidad(currentQuantity - 1);
                        cartItemService.saveOrUpdateProducto(existingItem);
                    } else {
                        cartItemService.delete(existingItem);
                    }
                }
            } else {
                if (add) {
                    CartItem newItem = new CartItem();
                    newItem.setShoppingCart(userCart);
                    newItem.setProducto(producto);
                    newItem.setCantidad(1);
                    cartItemService.saveOrUpdateProducto(newItem);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Map<String, Integer> getCartStateForUser(Principal principal) {
        if (principal == null) {
            // Si no hay usuario, devuelve un mapa vacío.
            return Collections.emptyMap();
        }

        ShoppingCart cart = this.getOrCreateShoppingCartFromPrincipal(principal);

        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            return Collections.emptyMap();
        }

        // Creamos el mapa que enviaremos al frontend
        Map<String, Integer> cartStateMap = new HashMap<>();

        // Iteramos sobre los items del carrito para llenar el mapa
        for (CartItem item : cart.getItems()) {
            // Usamos el ID del producto como clave (convertido a String) y la cantidad como valor
            String productId = item.getProducto().getId().toString();
            Integer quantity = item.getCantidad();
            cartStateMap.put(productId, quantity);
        }

        return cartStateMap;
    }

    @Override
    @Transactional
    public void clearCart(Principal principal) {
        ShoppingCart cart = getOrCreateShoppingCartFromPrincipal(principal);
        cart.getItems().clear();
        shoppingCartDAO.save(cart);
    }


    @Override
    @Transactional
    public void mergeGuestCart(Principal principal, Map<String, Integer> guestCart) {
        // Obtiene el carrito persistente del usuario
        ShoppingCart userCart = getOrCreateShoppingCartFromPrincipal(principal);

        // Itera sobre cada producto del carrito de invitado
        guestCart.forEach((productIdStr, guestQuantity) -> {
            BigDecimal productId = new BigDecimal(productIdStr);

            // Busca el producto en la BBDD para obtener el stock actualizado
            Producto producto = productService.findById(productId).orElse(null);
            if (producto == null || producto.getStock() <= 0) {
                return; // Si el producto no existe o no tiene stock, lo ignoramos
            }

            // Busca si el producto ya existía en el carrito del usuario
            CartItem existingItem = userCart.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productId))
                .findFirst()
                .orElse(null);

            if (existingItem != null) {
                // Si el producto ya existe, se actualiza su cantidad.
                int newQuantity = existingItem.getCantidad() + guestQuantity;
                existingItem.setCantidad(Math.min(newQuantity, producto.getStock())); // Se limita la cantidad al stock disponible.

            } else {
                // Si el producto es nuevo, se crea y añade al carrito.
                CartItem newItem = new CartItem();
                newItem.setProducto(producto);
                newItem.setCantidad(Math.min(guestQuantity, producto.getStock())); // Se limita la cantidad inicial al stock.
                newItem.setShoppingCart(userCart);
                userCart.getItems().add(newItem);
            }
        });

        // Guarda el carrito del usuario con los productos fusionados
        shoppingCartDAO.save(userCart);
    }


    @Override
    public ShoppingCartDTO getGuestCartDetails(Map<String, Integer> guestCart) {
        ShoppingCartDTO cartDTO = new ShoppingCartDTO(new ArrayList<>());

        guestCart.forEach((productIdStr, quantity) -> {
            // Usamos BigDecimal para buscar el producto
            productService.findById(new BigDecimal(productIdStr)).ifPresent(producto -> {

                // Mapeamos la entidad a un DTO para enviarlo al frontend
                GuestCartProductoDTO productoDTO = new GuestCartProductoDTO().fromEntityToGuestCartProducto(producto);

                CartItemDTO itemDTO = new CartItemDTO();
                itemDTO.setProducto(productoDTO);
                // La cantidad se limita al stock disponible
                itemDTO.setCantidad(Math.min(quantity, producto.getStock()));

                cartDTO.getItems().add(itemDTO);
            });
        });
      return cartDTO;
    }
}