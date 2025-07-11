package es.laboticademar.webstore.services.impl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.wishlist.GuestWishlistProductoDTO;
import es.laboticademar.webstore.dto.wishlist.WishlistDTO;
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
@RequiredArgsConstructor // Perfecto para la inyección de dependencias
public class WishlistServiceImpl implements WishlistService {

    // La inyección con @RequiredArgsConstructor y 'final' es correcta
    private final WishlistDAO wishlistDAO;
    private final UsuarioService usuarioService;
    private final ProductService productoService;

    @Override
    @Transactional
    public Wishlist getOrCreateWishlistFromPrincipal(Principal principal) {
        if (principal == null) {
            throw new IllegalStateException("El usuario debe estar autenticado para acceder a su wishlist.");
        }
        Long usuarioId = usuarioService.getIdFromPrincipal(principal);
        return wishlistDAO.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    Usuario usuario = usuarioService.findById(usuarioId)
                            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + usuarioId));
                    Wishlist newWishlist = new Wishlist();
                    newWishlist.setUsuario(usuario);
                    newWishlist.setProductos(new ArrayList<>());
                    return wishlistDAO.save(newWishlist);
                });
    }

    @Override
    @Transactional
    public void addProductoToWishlist(Principal principal, BigDecimal productoId) {
        Wishlist wishlist = getOrCreateWishlistFromPrincipal(principal);
        Producto producto = productoService.findById(productoId)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + productoId));
        if (!wishlist.getProductos().contains(producto)) {
            wishlist.getProductos().add(producto);
            wishlistDAO.save(wishlist);
        }
    }

    @Override
    @Transactional
    public void removeProductoFromWishlist(Principal principal, BigDecimal productoId) {
        Wishlist wishlist = getOrCreateWishlistFromPrincipal(principal);
        boolean removed = wishlist.getProductos().removeIf(producto -> producto.getId().equals(productoId));
        if (removed) {
            wishlistDAO.save(wishlist);
        }
    }

    @Override
    @Transactional
    public List<String> getWishlistProductIdsForUser(Principal principal) {
        if (principal == null) {
            return Collections.emptyList();
        }
        return getOrCreateWishlistFromPrincipal(principal)
                .getProductos()
                .stream()
                .map(producto -> producto.getId().toString())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void mergeGuestWishlist(Principal principal, List<String> guestProductIds) {
        Wishlist userWishlist = getOrCreateWishlistFromPrincipal(principal);
        Set<String> existingProductIds = userWishlist.getProductos().stream()
                .map(item -> item.getId().toString())
                .collect(Collectors.toSet());

        guestProductIds.forEach(productIdStr -> {
            if (!existingProductIds.contains(productIdStr)) {
                BigDecimal productId = new BigDecimal(productIdStr);
                productoService.findById(productId).ifPresent(producto -> {
                    userWishlist.getProductos().add(producto);
                });
            }
        });
        wishlistDAO.save(userWishlist);
    }


    // --- CAMBIO PRINCIPAL AQUÍ ---
    @Override
    public WishlistDTO getGuestWishlistDetails(List<String> guestProductIds) {
        List<GuestWishlistProductoDTO> detailedItems = guestProductIds.stream()
            .map(this::findAndMapProduct) 
            .filter(item -> item != null) 
            .collect(Collectors.toList());

        return new WishlistDTO(detailedItems);
    }


    // MÉTODO AUXILIAR: Se mantiene como la única fuente de verdad para el mapeo.
    private GuestWishlistProductoDTO findAndMapProduct(String productIdStr) {
        try {
            BigDecimal productId = new BigDecimal(productIdStr);
            // Llamada al método estático. Es más claro y seguro.
            return productoService.findById(productId)
                    .map(GuestWishlistProductoDTO::fromEntity) 
                    .orElse(null);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}