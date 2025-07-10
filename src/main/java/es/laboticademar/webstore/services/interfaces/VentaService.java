package es.laboticademar.webstore.services.interfaces;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

import es.laboticademar.webstore.dto.PaymentDTO;
import es.laboticademar.webstore.dto.venta.VentaAdminResumenDTO;
import es.laboticademar.webstore.dto.venta.VentaDTO;
import es.laboticademar.webstore.dto.venta.VentaEstadoDTO;
import es.laboticademar.webstore.dto.venta.VentaKpisDTO;
import es.laboticademar.webstore.dto.venta.VentaPageDTO;
import es.laboticademar.webstore.entities.Venta;

public interface VentaService {
    public Optional<Venta> findById(Long id);
    public Boolean realizarVenta(Principal principal, PaymentDTO paymentData, Boolean useDiscountPoints);
    public VentaPageDTO findVentasByCurrentUser(Principal principal, int page, int size);
    public VentaDTO findVentaDetailsByIdAndUser(Long ventaId, Principal principal);
    public VentaDTO findVentaDetailsById(Long ventaId);
    public Page<VentaAdminResumenDTO> findAllVentasFiltered(
            int page, int size, Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin,
            Float precioMin, Float precioMax, Integer estadoId, Integer numProductos);
    VentaKpisDTO getVentaKpis(Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin, Float precioMin, Float precioMax, Integer numProductos);
    public void updateVentaStatus(Long ventaId, Integer nuevoEstadoId);
    public List<VentaEstadoDTO> getAllVentaEstados();
}