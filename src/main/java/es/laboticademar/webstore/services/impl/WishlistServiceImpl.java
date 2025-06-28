package es.laboticademar.webstore.services.impl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.Wishlist;
import es.laboticademar.webstore.repositories.WishlistDAO;
import es.laboticademar.webstore.services.interfaces.ProductService;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import es.laboticademar.webstore.services.interfaces.WishlistService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistDAO wishlistDAO;
    private final UsuarioService usuarioService;   // Asumo que tienes este servicio
    private final ProductService productoService;

    @Override
    @Transactional
    public Wishlist getOrCreateWishlistFromPrincipal(Principal principal) {
        if (principal == null) {
            // No se puede obtener una wishlist sin un usuario logueado.
            throw new IllegalStateException("El usuario debe estar autenticado para acceder a su wishlist.");
        }

        Long usuarioId = usuarioService.getIdFromPrincipal(principal);

        // Busca la wishlist por el ID de usuario. Si no existe, crea una nueva.
        return wishlistDAO.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioService.findById(usuarioId)
                            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioId));
                    
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUsuario(usuario);
                    newWishlist.setProductos(new ArrayList<>());
                    
                    return wishlistDAO.save(newWishlist); // La guardamos para que tenga un ID
                });
    }

    @Override
    @Transactional
    public void addProductoToWishlist(Principal principal, BigDecimal productoId) {
        Wishlist wishlist = getOrCreateWishlistFromPrincipal(principal);
        
        Producto producto = productoService.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + productoId));

        // Verificamos si el producto ya está en la lista para no añadirlo dos veces
        if (!wishlist.getProductos().contains(producto)) {
            wishlist.getProductos().add(producto);
            wishlistDAO.save(wishlist); // JPA se encarga de la tabla intermedia
        }
    }

    @Override
    @Transactional
    public void removeProductoFromWishlist(Principal principal, BigDecimal productoId) {
        Wishlist wishlist = getOrCreateWishlistFromPrincipal(principal);
        
        // Usamos removeIf para eliminar el producto de la lista de forma segura
        boolean removed = wishlist.getProductos().removeIf(producto -> producto.getId().equals(productoId));
        
        if (removed) {
            wishlistDAO.save(wishlist); // Guardamos los cambios si se ha eliminado algo
        }
    }

    @Override
    @Transactional
    public List<String> getWishlistProductIdsForUser(Principal principal) {
        // Si el usuario no está logueado, devolvemos una lista vacía.
        if (principal == null) {
            return Collections.emptyList();
        }

        // La lógica es la misma que antes, pero sin el paso final de conversión a JSON.
        return getOrCreateWishlistFromPrincipal(principal)
                    .getProductos()
                    .stream()
                    .map(producto -> producto.getId().toString())
                    .collect(Collectors.toList());
    }
}