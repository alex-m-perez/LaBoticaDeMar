package es.laboticademar.webstore.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.PaymentDTO;
import es.laboticademar.webstore.dto.venta.DetalleVentaDTO;
import es.laboticademar.webstore.dto.venta.VentaAdminResumenDTO;
import es.laboticademar.webstore.dto.venta.VentaDTO;
import es.laboticademar.webstore.dto.venta.VentaEstadoDTO;
import es.laboticademar.webstore.dto.venta.VentaKpisDTO;
import es.laboticademar.webstore.dto.venta.VentaPageDTO;
import es.laboticademar.webstore.dto.venta.VentaResumenDTO;
import es.laboticademar.webstore.entities.CartItem;
import es.laboticademar.webstore.entities.DetalleVenta;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.ShoppingCart;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.Venta;
import es.laboticademar.webstore.enumerations.VentaEstadoEnum;
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
        venta.setEstado(VentaEstadoEnum.ACEPTADO);

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
                .estado(VentaEstadoEnum.fromId(venta.getEstado().getId()).getEtiqueta())
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
                // Añadimos el ID y la etiqueta del estado al DTO
                .estadoId(venta.getEstado() != null ? venta.getEstado().getId() : null)
                .estado(venta.getEstado() != null ? venta.getEstado().getEtiqueta() : "Desconocido")
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
                .imagenData(detalle.getProducto().getImagenData())
                .laboratorioNombre(detalle.getProducto().getLaboratorio() != null ? detalle.getProducto().getLaboratorio().getNombre() : "N/D")
                // Datos específicos de la venta
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<VentaAdminResumenDTO> findAllVentasFiltered(
            int page, int size, Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin,
            Float precioMin, Float precioMax, Integer estadoId, Integer numProductos) {

        if (clienteId != null && idUsuario != null && !clienteId.equals(idUsuario)) {
            return Page.empty();
        }

        Long finalIdUsuario = clienteId != null ? clienteId : idUsuario;

        Specification<Venta> spec = Specification.where(null);

        if (finalIdUsuario != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("cliente").get("id"), finalIdUsuario));
        }
        if (fechaInicio != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaVenta"), fechaInicio.atStartOfDay()));
        }
        if (fechaFin != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaVenta"), fechaFin.atTime(23, 59, 59)));
        }
        if (precioMin != null) {
            spec = spec.and((root, query, cb) -> cb.ge(root.get("montoTotal"), precioMin));
        }
        if (precioMax != null) {
            spec = spec.and((root, query, cb) -> cb.le(root.get("montoTotal"), precioMax));
        }
        if (estadoId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("estado"), estadoId));
        }
        if (numProductos != null) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.size(root.get("detalles")), numProductos));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaVenta").descending());
        return ventaDAO.findAll(spec, pageable).map(this::mapToVentaAdminResumenDTO);
        }


    @Override
    @Transactional(readOnly = true)
    public VentaDTO findVentaDetailsById(Long ventaId) {
        Venta venta = ventaDAO.findById(ventaId)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + ventaId));

        // Llama al nuevo método conversor que crearemos a continuación
        return convertToVentaDetalleDTO(venta);
    }

    /**
     * MAPPER AÑADIDO: Convierte una entidad Venta al DTO de resumen para el admin.
     * Este DTO incluye información del cliente.
     */
    private VentaAdminResumenDTO mapToVentaAdminResumenDTO(Venta venta) {
        // Usamos el patrón builder para una construcción más limpia y fluida
        return VentaAdminResumenDTO.builder()
                .id(venta.getId())
                .fechaVenta(venta.getFechaVenta())
                .montoTotal(venta.getMontoTotal())

                // Llama al método getNombreCompleto() del cliente para evitar lógica repetida
                .clienteId(venta.getCliente() != null ? venta.getCliente().getId() : null)
                .clienteNombre(venta.getCliente() != null ? venta.getCliente().getNombreCompleto() : "N/A")

                // Convierte el Enum del estado a su etiqueta de texto
                .estado(venta.getEstado() != null ? venta.getEstado().getEtiqueta() : "Desconocido")

                // Calcula el total de items de forma segura
                .totalItems(venta.getDetalles() != null ? venta.getDetalles().stream().mapToInt(DetalleVenta::getCantidad).sum() : 0)

                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VentaEstadoDTO> getAllVentaEstados() {
        return Arrays.stream(VentaEstadoEnum.values())
                .map(estado -> new VentaEstadoDTO(estado.getId(), estado.getEtiqueta()))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public VentaKpisDTO getVentaKpis(Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin, Float precioMin, Float precioMax, Integer numProductos) {

        // --- Rango para KPIs de "Hoy" ---
        LocalDate hoy = LocalDate.now();
        Specification<Venta> specHoy = buildVentaSpecification(clienteId, idUsuario, hoy, hoy, precioMin, precioMax, numProductos);

        // --- Rango para KPIs de "Rango de Fechas" ---
        LocalDate inicioRango = (fechaInicio != null) ? fechaInicio : LocalDate.now().withDayOfMonth(1);
        LocalDate finRango = (fechaFin != null) ? fechaFin : LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
        Specification<Venta> specRango = buildVentaSpecification(clienteId, idUsuario, inicioRango, finRango, precioMin, precioMax, numProductos);

        // --- Construcción del DTO ---
        VentaKpisDTO kpis = new VentaKpisDTO();
        kpis.setTotalVentasHoy(countVentas(specHoy));
        kpis.setIngresosHoy(sumIngresos(specHoy));
        kpis.setTotalVentasRango(countVentas(specRango));
        kpis.setIngresosRango(sumIngresos(specRango));

        return kpis;
    }

    // --- Métodos privados para los cálculos ---

    private long countVentas(Specification<Venta> spec) {
        return ventaDAO.count(spec);
    }

    private BigDecimal sumIngresos(Specification<Venta> spec) {
        List<Venta> ventas = ventaDAO.findAll(spec);
        return ventas.stream()
                .map(venta -> BigDecimal.valueOf(venta.getMontoTotal()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // --- Lógica de filtrado reutilizable ---

    private Specification<Venta> buildVentaSpecification(Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin, Float precioMin, Float precioMax, Integer numProductos) {
        // Lógica de conflicto de IDs
        if (clienteId != null && idUsuario != null && !clienteId.equals(idUsuario)) {
            return (root, query, cb) -> cb.disjunction(); // Devuelve una condición que nunca es verdadera
        }
        Long finalIdUsuario = clienteId != null ? clienteId : idUsuario;

        Specification<Venta> spec = Specification.where(null);
        // ... (Aquí va la lógica de construcción de la Specification que ya tenías)
        if (finalIdUsuario != null) { spec = spec.and((root, query, cb) -> cb.equal(root.get("cliente").get("id"), finalIdUsuario)); }
        if (fechaInicio != null) { spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaVenta"), fechaInicio.atStartOfDay())); }
        if (fechaFin != null) { spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaVenta"), fechaFin.atTime(23, 59, 59))); }
        // ... etc para los otros filtros
        
        return spec;
    }


    @Override
    @Transactional
    public void updateVentaStatus(Long ventaId, Integer nuevoEstadoId) {
        Venta venta = ventaDAO.findById(ventaId)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + ventaId));

        VentaEstadoEnum estado = VentaEstadoEnum.fromId(nuevoEstadoId);
        venta.setEstado(estado); // Asumiendo que Venta.estado es de tipo Integer

        ventaDAO.save(venta);
    }



}