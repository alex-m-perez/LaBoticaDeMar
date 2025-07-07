package es.laboticademar.webstore.services.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.PaymentDTO;
import es.laboticademar.webstore.entities.CartItem;
import es.laboticademar.webstore.entities.DetalleVenta;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.ShoppingCart;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.Venta;
import es.laboticademar.webstore.exceptions.InsufficientStockException;
import es.laboticademar.webstore.exceptions.InvalidPaymentDataException;
import es.laboticademar.webstore.repositories.ProductDAO;
import es.laboticademar.webstore.repositories.VentaDAO;
import es.laboticademar.webstore.services.interfaces.ShoppingCartService;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import es.laboticademar.webstore.services.interfaces.VentaService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final UsuarioService usuarioService;
    private final ShoppingCartService shoppingCartService;
    private final VentaDAO ventaDAO;
    private final ProductDAO productoDAO;

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
                    validarDatosDePago(paymentData);
                    validarStock(userCart);

                    // --- PASO 2: CREAR LA VENTA Y APLICAR DESCUENTOS ---
                    Venta nuevaVenta = crearVentaDesdeCarrito(usuario, userCart);
                    
                    if (Boolean.TRUE.equals(useDiscountPoints)) {
                        aplicarDescuentoYActualizarPuntos(nuevaVenta, usuario);
                    }

                    // --- PASO 3: PERSISTIR CAMBIOS ---
                    // La anotación @Transactional se encarga de que todo lo siguiente
                    // se ejecute como una única operación: o todo funciona, o nada se guarda.
                    
                    // Actualiza el stock de los productos
                    actualizarStock(userCart);
                    
                    // Guarda la venta con el total final
                    ventaDAO.save(nuevaVenta);
                    
                    // Guarda el usuario si sus puntos han cambiado
                    usuarioService.save(usuario); // O usuarioDAO.save(usuario)
                    
                    // Vacía el carrito del usuario
                    shoppingCartService.clearCart(principal);

                    return true;
                })
                .orElse(false);
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
            Integer pointsUsed = Math.round(discountToApply * 100);
            usuario.setPuntos(userPoints - pointsUsed);
        }
    }

    private Venta crearVentaDesdeCarrito(Usuario usuario, ShoppingCart userCart) {
        Venta venta = new Venta();
        venta.setCliente(usuario);
        venta.setFechaVenta(LocalDateTime.now());

        List<DetalleVenta> detalles = userCart.getItems().stream()
                .map(cartItem -> crearDetalleVenta(cartItem, venta))
                .collect(Collectors.toList());
        venta.setDetalles(detalles);
        
        Float montoTotal = (float) detalles.stream()
            .mapToDouble(detalle -> detalle.getPrecioUnitario() * detalle.getCantidad())
            .sum();
            
        venta.setMontoTotal(montoTotal);
        return venta;
    }
    
    private DetalleVenta crearDetalleVenta(CartItem cartItem, Venta venta) {
        Producto producto = cartItem.getProducto();
        DetalleVenta detalle = new DetalleVenta();
        
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setCantidad(cartItem.getCantidad());

        Float precioBase = producto.getPrice();
        Float descuento = producto.getDiscount();
        if (descuento != null && descuento > 0) {
            detalle.setPrecioUnitario(precioBase * (1 - descuento / 100));
        } else {
            detalle.setPrecioUnitario(precioBase);
        }
        return detalle;
    }

    // --- MÉTODOS DE VALIDACIÓN DE PAGO ---
    
    private void validarDatosDePago(PaymentDTO paymentData) {
        if (paymentData == null) {
            throw new InvalidPaymentDataException("Los datos de pago no pueden ser nulos.");
        }
        validarNumeroTarjeta(paymentData.getCardNumber());
        validarFechaCaducidad(paymentData.getCardExpiringDate());
        validarCvc(paymentData.getCardSecretNumber());
    }

    private void validarNumeroTarjeta(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new InvalidPaymentDataException("El número de la tarjeta es obligatorio.");
        }
        String digitsOnly = cardNumber.replace(" ", "");
        if (!digitsOnly.matches("\\d{16}")) {
            throw new InvalidPaymentDataException("El número de tarjeta debe contener exactamente 16 dígitos.");
        }
    }

    private void validarFechaCaducidad(String fecha) {
        if (fecha == null || !fecha.matches("\\d{2}/\\d{4}")) {
            throw new InvalidPaymentDataException("El formato de la fecha de caducidad debe ser MM/AAAA.");
        }
        String[] partes = fecha.split("/");
        try {
            int mes = Integer.parseInt(partes[0]);
            int anio = Integer.parseInt(partes[1]);
            if (mes < 1 || mes > 12) {
                throw new InvalidPaymentDataException("El mes de la fecha de caducidad no es válido.");
            }
            YearMonth fechaCaducidad = YearMonth.of(anio, mes);
            YearMonth fechaActual = YearMonth.now();
            if (fechaCaducidad.isBefore(fechaActual)) {
                throw new InvalidPaymentDataException("La tarjeta ha caducado.");
            }
            if (anio > fechaActual.getYear() + 15) {
                throw new InvalidPaymentDataException("El año de caducidad es demasiado lejano.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidPaymentDataException("La fecha de caducidad contiene caracteres no válidos.");
        }
    }

    private void validarCvc(Integer cvc) {
        if (cvc == null) {
            throw new InvalidPaymentDataException("El CVC es obligatorio.");
        }
        if (cvc < 0 || cvc > 999) {
            throw new InvalidPaymentDataException("El CVC debe ser un número de 3 dígitos.");
        }
    }
}