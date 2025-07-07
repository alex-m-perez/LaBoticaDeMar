package es.laboticademar.webstore.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.PaymentDTO;
import es.laboticademar.webstore.dto.venta.DetalleVentaDTO;
import es.laboticademar.webstore.dto.venta.VentaDTO;
import es.laboticademar.webstore.dto.venta.VentaPageDTO;
import es.laboticademar.webstore.dto.venta.VentaResumenDTO;
import es.laboticademar.webstore.entities.CartItem;
import es.laboticademar.webstore.entities.DetalleVenta;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.ShoppingCart;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.Venta;
import es.laboticademar.webstore.exceptions.InsufficientStockException;
import es.laboticademar.webstore.repositories.ProductDAO;
import es.laboticademar.webstore.repositories.VentaDAO;
import es.laboticademar.webstore.services.interfaces.ShoppingCartService;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import es.laboticademar.webstore.services.interfaces.VentaService;
import es.laboticademar.webstore.utils.CreditCardUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final UsuarioService usuarioService;
    private final ShoppingCartService shoppingCartService;
    private final VentaDAO ventaDAO;
    private final ProductDAO productoDAO;

    @Override
    public Optional<Venta> findById(Long id) {
        return ventaDAO.findById(id);
    }


    @Override
    @Transactional
    public Boolean realizarVenta(Principal principal, PaymentDTO paymentData, Boolean useDiscountPoints) {
        return usuarioService.findById(usuarioService.getIdFromPrincipal(principal))
            .map(usuario -> {
                ShoppingCart userCart = shoppingCartService.getOrCreateShoppingCartFromPrincipal(principal);
                if (userCart.getItems().isEmpty()) {
                    return false;
                }

                // --- PASO 1: VALIDACIONES PREVIAS ---
                CreditCardUtils.validarDatosDePago(paymentData);
                validarStock(userCart);

                // --- PASO 2: CREAR LA VENTA Y APLICAR DESCUENTOS ---
                Venta nuevaVenta = crearVentaDesdeCarrito(usuario, userCart);
                if (Boolean.TRUE.equals(useDiscountPoints)) {
                    aplicarDescuentoYActualizarPuntos(nuevaVenta, usuario);
                }

                // --- PASO 3: PERSISTIR CAMBIOS ---
                actualizarStock(userCart);
                ventaDAO.save(nuevaVenta);

                // 3.3 CALCULAR Y ACUMULAR PUNTOS GENERADOS
                BigDecimal totalFinal = BigDecimal.valueOf(nuevaVenta.getMontoTotal())
                    .setScale(2, RoundingMode.HALF_UP);
                int puntosGenerados = totalFinal
                    .multiply(BigDecimal.valueOf(5))
                    .setScale(0, RoundingMode.DOWN)
                    .intValue();
                usuario.setPuntos(usuario.getPuntos() + puntosGenerados);

                // 3.4 Guardar usuario y limpiar carrito
                usuarioService.save(usuario);
                shoppingCartService.clearCart(principal);
                return true;
            })
            .orElse(false);
    }

    private Venta crearVentaDesdeCarrito(Usuario usuario, ShoppingCart userCart) {
        Venta venta = new Venta();
        venta.setCliente(usuario);
        venta.setFechaVenta(LocalDateTime.now());

        List<DetalleVenta> detalles = userCart.getItems().stream()
            .map(ci -> crearDetalleVenta(ci, venta))
            .collect(Collectors.toList());
        venta.setDetalles(detalles);

        // Suma linea a linea, redondeando cada total de detalle a 2 decimales
        BigDecimal suma = detalles.stream()
            .map(det -> BigDecimal.valueOf(det.getPrecioUnitario())
                .multiply(BigDecimal.valueOf(det.getCantidad()))
                .setScale(2, RoundingMode.HALF_UP))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);

        venta.setMontoTotal(suma.floatValue());
        return venta;
    }

    private DetalleVenta crearDetalleVenta(CartItem cartItem, Venta venta) {
        Producto producto = cartItem.getProducto();
        DetalleVenta detalle = new DetalleVenta();
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setCantidad(cartItem.getCantidad());

        // Calcula precio unitario con descuento y redondea a 2 decimales
        BigDecimal base = BigDecimal.valueOf(producto.getPrice());
        BigDecimal unitPrice = (producto.getDiscount() > 0
            ? base.multiply(BigDecimal.valueOf(1 - producto.getDiscount() / 100.0))
            : base)
            .setScale(2, RoundingMode.HALF_UP);

        detalle.setPrecioUnitario(unitPrice.floatValue());
        return detalle;
    }
    
    // --- MÉTODOS PRIVADOS DE AYUDA ---

    private void validarStock(ShoppingCart cart) {
        for (CartItem item : cart.getItems()) {
            if (item.getProducto().getStock() < item.getCantidad()) {
                throw new InsufficientStockException("No hay stock suficiente para: " + item.getProducto().getNombre());
            }
        }
    }

    private void actualizarStock(ShoppingCart cart) {
        for (CartItem item : cart.getItems()) {
            Producto producto = item.getProducto();
            producto.setStock(producto.getStock() - item.getCantidad());
            productoDAO.save(producto);
        }
    }

    private void aplicarDescuentoYActualizarPuntos(Venta venta, Usuario usuario) {
        Integer userPoints = usuario.getPuntos();
        if (userPoints == null || userPoints <= 0) {
            return;
        }

        Float potentialDiscount = userPoints / 100.0f;
        Float currentTotal = venta.getMontoTotal();
        Float discountToApply = Math.min(currentTotal, potentialDiscount);

        if (discountToApply > 0) {
            venta.setMontoTotal(currentTotal - discountToApply);
            venta.setPuntosUtilizados(usuario.getPuntos());
            usuario.setPuntos(0);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public VentaPageDTO findVentasByCurrentUser(Principal principal, int page, int size) {
        Usuario currentUser = getCurrentUser(principal);

        // Crear objeto de paginación, ordenando por fecha de venta descendente (más recientes primero)
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaVenta").descending());

        Page<Venta> ventasPage = ventaDAO.findByCliente(currentUser, pageable);

        // Mapear la página de entidades a una página de DTOs
        Page<VentaResumenDTO> dtoPage = ventasPage.map(this::convertToVentaResumenDTO);

        return new VentaPageDTO(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO findVentaDetailsByIdAndUser(Long ventaId, Principal principal) {
        Usuario currentUser = getCurrentUser(principal);

        // Buscar la venta por ID y por propietario para asegurar que el usuario tiene permiso
        Venta venta = ventaDAO.findByIdAndCliente(ventaId, currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado o no tiene permiso para verlo. ID: " + ventaId));

        return convertToVentaDetalleDTO(venta);
    }

    /**
     * Método de utilidad para obtener la entidad Usuario a partir de UserDetails.
     */
    private Usuario getCurrentUser(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("El usuario no está autenticado.");
        }
        String username = principal.getName();
        return usuarioService.getUserByCorreo(username) // o findByUsername, dependiendo de tu implementación
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    /**
     * Convierte una entidad Venta a su DTO de resumen.
     */
    private VentaResumenDTO convertToVentaResumenDTO(Venta venta) {
        return VentaResumenDTO.builder()
                .id(venta.getId())
                .fechaVenta(venta.getFechaVenta())
                .montoTotal(venta.getMontoTotal())
                // Calcula el número total de artículos sumando las cantidades de cada detalle
                .totalItems(venta.getDetalles().stream().mapToInt(DetalleVenta::getCantidad).sum())
                .build();
    }

    /**
     * Convierte una entidad Venta a su DTO de detalle completo.
     */
    private VentaDTO convertToVentaDetalleDTO(Venta venta) {
        return VentaDTO.builder()
                .id(venta.getId())
                .fechaVenta(venta.getFechaVenta())
                .montoTotal(venta.getMontoTotal())
                .puntosUtilizados(venta.getPuntosUtilizados())
                .productos(venta.getDetalles().stream()
                        .map(this::convertToProductoVentaDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Convierte una entidad DetalleVenta a su DTO de producto dentro de una venta.
     */
    private DetalleVentaDTO convertToProductoVentaDTO(DetalleVenta detalle) {
        return DetalleVentaDTO.builder()
                // Datos del producto en el momento de la compra
                .id(detalle.getProducto().getId())
                .nombre(detalle.getProducto().getNombre())
                .imagenPath(detalle.getProducto().getImagenPath())
                .laboratorioNombre(detalle.getProducto().getLaboratorio() != null ? detalle.getProducto().getLaboratorio().getNombre() : "N/D")
                // Datos específicos de la venta
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .build();
    }



}