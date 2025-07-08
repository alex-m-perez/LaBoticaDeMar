package es.laboticademar.webstore.services.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.devolucion.DevolucionAdminDetalleDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionAdminResumenDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionDetalleDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionKpisDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionPageDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionRequestDTO;
import es.laboticademar.webstore.entities.DetalleDevolucion;
import es.laboticademar.webstore.entities.Devolucion;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.entities.Venta;
import es.laboticademar.webstore.enumerations.DevolucionEnum;
import es.laboticademar.webstore.repositories.DevolucionDAO;
import es.laboticademar.webstore.services.interfaces.DevolucionService;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import es.laboticademar.webstore.services.interfaces.VentaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DevolucionServiceImpl implements DevolucionService {

    private final VentaService ventaService;
    private final DevolucionDAO devolucionDAO;
    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public void crearDevolucion(Long ventaId, DevolucionRequestDTO dto, Principal principal) {
        // 1. Obtener el usuario autenticado
        Usuario cliente = usuarioService.getUserByCorreo(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado."));

        // 2. Obtener la venta original
        Venta venta = ventaService.findById(ventaId)
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada con ID: " + ventaId));

        // 3. Verificar que la venta pertenece al cliente
        if (!venta.getCliente().getId().equals(cliente.getId())) {
            throw new AccessDeniedException("No tienes permiso para solicitar una devolución para esta venta.");
        }
        
        // 4. (Opcional) Verificar si ya existe una devolución para esta venta
        if (devolucionDAO.existsByVentaId(ventaId)) {
            throw new IllegalStateException("Ya existe una solicitud de devolución para esta venta.");
        }

        // 5. Crear la entidad Devolucion
        Devolucion devolucion = new Devolucion();
        devolucion.setVenta(venta);
        devolucion.setCliente(cliente);
        devolucion.setFechaSolicitud(LocalDateTime.now());
        devolucion.setMotivoCategoria(DevolucionEnum.fromId(dto.getMotivoId()));
        devolucion.setComentarios(dto.getComentarios());

        // 6. Crear los detalles de la devolución y reponer el stock
        List<DetalleDevolucion> detallesDevolucion = venta.getDetalles().stream()
                .map(detalleVenta -> {
                    
                    // --- INICIO: LÓGICA PARA REPONER STOCK ---
                    Producto productoDevuelto = detalleVenta.getProducto();
                    int cantidadDevuelta = detalleVenta.getCantidad();
                    productoDevuelto.setStock(productoDevuelto.getStock() + cantidadDevuelta);
                    // No es necesario llamar a productoDAO.save(productoDevuelto) por la transaccionalidad
                    // --- FIN: LÓGICA PARA REPONER STOCK ---

                    DetalleDevolucion detalleDevolucion = new DetalleDevolucion();
                    detalleDevolucion.setDevolucion(devolucion); // Enlazar con la devolución padre
                    detalleDevolucion.setProducto(productoDevuelto);
                    detalleDevolucion.setCantidad(cantidadDevuelta);
                    detalleDevolucion.setPrecioUnitario(detalleVenta.getPrecioUnitario());
                    return detalleDevolucion;
                }).collect(Collectors.toList());

        devolucion.setDetalles(detallesDevolucion);

    // 7. Guardar la devolución (guarda la devolución y actualiza el stock de los productos en la misma transacción)
        devolucionDAO.save(devolucion);
    }

    @Override
    @Transactional(readOnly = true)
    public DevolucionPageDTO findDevolucionesByCurrentUser(Principal principal, int page, int size) {
        Usuario currentUser = getCurrentUser(principal);
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaSolicitud").descending());

        Page<Devolucion> devolucionesPage = devolucionDAO.findByCliente(currentUser, pageable);
        Page<DevolucionDetalleDTO> dtoPage = devolucionesPage.map(this::convertToDevolucionResumenDTO);

        return new DevolucionPageDTO(dtoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public DevolucionDetalleDTO findDevolucionDetailsByIdAndUser(Long devolucionId, Principal principal) {
        Usuario currentUser = getCurrentUser(principal);
        Devolucion devolucion = devolucionDAO.findByIdAndCliente(devolucionId, currentUser)
                .orElseThrow(() -> new EntityNotFoundException("Devolución no encontrada o no tiene permiso para verla."));

        return convertToDevolucionDetalleDTO(devolucion);
    }

    // --- MÉTODOS PRIVADOS DE CONVERSIÓN ---

    private Usuario getCurrentUser(Principal principal) {
        if (principal == null) {
            throw new IllegalArgumentException("El usuario no está autenticado.");
        }
        return usuarioService.getUserByCorreo(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + principal.getName()));
    }

    private DevolucionDetalleDTO convertToDevolucionResumenDTO(Devolucion devolucion) {
        return DevolucionDetalleDTO.builder()
                .id(devolucion.getId())
                .fechaSolicitud(devolucion.getFechaSolicitud())
                .ventaId(devolucion.getVenta().getId())
                .motivo(devolucion.getMotivoCategoria().getEtiqueta())
                .build();
    }

    private DevolucionDetalleDTO convertToDevolucionDetalleDTO(Devolucion devolucion) {
        return DevolucionDetalleDTO.builder()
                .id(devolucion.getId())
                .fechaSolicitud(devolucion.getFechaSolicitud())
                .ventaId(devolucion.getVenta().getId())
                .motivo(devolucion.getMotivoCategoria().getEtiqueta())
                .comentarios(devolucion.getComentarios())
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public Page<DevolucionAdminResumenDTO> findAllDevolucionesFiltered(int page, int size, Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin, Float montoMin, Float montoMax) {
        // La especificación solo filtra por lo que se puede en la BD (usuario, fecha)
        Specification<Devolucion> spec = buildDevolucionSpecification(clienteId, idUsuario, fechaInicio, fechaFin);

        // Obtenemos TODOS los resultados que coinciden con los filtros de BD
        List<Devolucion> allMatchingDevoluciones = devolucionDAO.findAll(spec);

        // Ahora filtramos en memoria por el monto calculado
        List<DevolucionAdminResumenDTO> filteredList = allMatchingDevoluciones.stream()
                .map(this::mapToDevolucionAdminResumenDTO) // Mapeamos a DTO para tener el monto calculado
                .filter(dto -> {
                    boolean minOk = (montoMin == null) || dto.getMontoDevuelto().floatValue() >= montoMin;
                    boolean maxOk = (montoMax == null) || dto.getMontoDevuelto().floatValue() <= montoMax;
                    return minOk && maxOk;
                })
                .collect(Collectors.toList());

        // Creamos manualmente el objeto Page para la paginación
        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaSolicitud").descending());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredList.size());

        List<DevolucionAdminResumenDTO> pageContent = (start > filteredList.size()) ? List.of() : filteredList.subList(start, end);

        return new PageImpl<>(pageContent, pageable, filteredList.size());
    }

    @Override
    @Transactional(readOnly = true)
    public DevolucionKpisDTO getDevolucionKpis(Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin, Float montoMin, Float montoMax) {
        LocalDate hoy = LocalDate.now();
        // Especificación para "Hoy"
        Specification<Devolucion> specHoy = buildDevolucionSpecification(clienteId, idUsuario, hoy, hoy);

        // Especificación para el rango de fechas
        LocalDate inicioRango = (fechaInicio != null) ? fechaInicio : hoy.withDayOfMonth(1);
        LocalDate finRango = (fechaFin != null) ? fechaFin : hoy.with(TemporalAdjusters.lastDayOfMonth());
        Specification<Devolucion> specRango = buildDevolucionSpecification(clienteId, idUsuario, inicioRango, finRango);

        // Construcción del DTO
        DevolucionKpisDTO kpis = new DevolucionKpisDTO();
        kpis.setTotalDevolucionesHoy(countDevoluciones(specHoy, montoMin, montoMax));
        kpis.setMontoDevueltoHoy(sumMontoDevuelto(specHoy, montoMin, montoMax));
        kpis.setTotalDevolucionesRango(countDevoluciones(specRango, montoMin, montoMax));
        kpis.setMontoDevueltoRango(sumMontoDevuelto(specRango, montoMin, montoMax));

        return kpis;
    }

    @Override
    @Transactional(readOnly = true)
    public DevolucionAdminDetalleDTO findDevolucionDetailsById(Long id) {
        return devolucionDAO.findById(id)
                .map(this::mapToDevolucionAdminDetalleDTO)
                .orElseThrow(() -> new EntityNotFoundException("Devolución no encontrada con ID: " + id));
    }

    // --- Métodos privados de cálculo y mapeo ---

    private long countDevoluciones(Specification<Devolucion> spec, Float montoMin, Float montoMax) {
        return devolucionDAO.findAll(spec).stream()
                .filter(d -> {
                    BigDecimal monto = calculateMontoTotalDevolucion(d);
                    boolean minOk = (montoMin == null) || monto.floatValue() >= montoMin;
                    boolean maxOk = (montoMax == null) || monto.floatValue() <= montoMax;
                    return minOk && maxOk;
                })
                .count();
    }

    private BigDecimal sumMontoDevuelto(Specification<Devolucion> spec, Float montoMin, Float montoMax) {
        return devolucionDAO.findAll(spec).stream()
                .filter(d -> {
                    BigDecimal monto = calculateMontoTotalDevolucion(d);
                    boolean minOk = (montoMin == null) || monto.floatValue() >= montoMin;
                    boolean maxOk = (montoMax == null) || monto.floatValue() <= montoMax;
                    return minOk && maxOk;
                })
                .map(this::calculateMontoTotalDevolucion)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Specification<Devolucion> buildDevolucionSpecification(Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin) {
        if (clienteId != null && idUsuario != null && !clienteId.equals(idUsuario)) {
            return (root, query, cb) -> cb.disjunction();
        }
        Long finalIdUsuario = clienteId != null ? clienteId : idUsuario;

        Specification<Devolucion> spec = Specification.where(null);

        if (finalIdUsuario != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Devolucion, Usuario> clienteJoin = root.join("cliente");
                return cb.equal(clienteJoin.get("id"), finalIdUsuario);
            });
        }
        if (fechaInicio != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fechaSolicitud"), fechaInicio.atStartOfDay()));
        }
        if (fechaFin != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("fechaSolicitud"), fechaFin.atTime(23, 59, 59)));
        }
        // Los filtros de monto se eliminan de aquí porque se aplican en memoria
        return spec;
    }

    // --- MAPPERS ---

    private DevolucionAdminResumenDTO mapToDevolucionAdminResumenDTO(Devolucion d) {
        Usuario cliente = d.getCliente();
        String nombreCompleto = (cliente.getNombre() + " " + cliente.getApellido1() + " " + (cliente.getApellido2() != null ? cliente.getApellido2() : "")).trim();

        return DevolucionAdminResumenDTO.builder()
                .id(d.getId())
                .fechaSolicitud(d.getFechaSolicitud())
                .montoDevuelto(calculateMontoTotalDevolucion(d)) // Monto calculado
                .ventaId(d.getVenta().getId())
                .clienteId(cliente.getId())
                .clienteNombre(nombreCompleto)
                .build();
    }

    private DevolucionAdminDetalleDTO mapToDevolucionAdminDetalleDTO(Devolucion d) {
        Usuario cliente = d.getCliente();
        String nombreCompleto = (cliente.getNombre() + " " + cliente.getApellido1() + " " + (cliente.getApellido2() != null ? cliente.getApellido2() : "")).trim();

        return DevolucionAdminDetalleDTO.builder()
                .id(d.getId())
                .fechaSolicitud(d.getFechaSolicitud())
                .montoDevuelto(calculateMontoTotalDevolucion(d)) // Monto calculado
                .ventaId(d.getVenta().getId())
                .motivo(d.getMotivoCategoria().toString()) // Convertir Enum a String
                .comentarios(d.getComentarios())
                .clienteNombre(nombreCompleto)
                .build();
    }

    // --- HELPER ---

    private BigDecimal calculateMontoTotalDevolucion(Devolucion devolucion) {
        if (devolucion.getDetalles() == null || devolucion.getDetalles().isEmpty()) {
            return BigDecimal.ZERO;
        }
        return devolucion.getDetalles().stream()
                .map(detalle -> BigDecimal.valueOf(detalle.getPrecioUnitario())
                                          .multiply(BigDecimal.valueOf(detalle.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}