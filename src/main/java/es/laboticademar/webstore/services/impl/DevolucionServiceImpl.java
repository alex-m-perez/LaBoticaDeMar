package es.laboticademar.webstore.services.impl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.devolucion.DevolucionDetalleDTO;
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
}