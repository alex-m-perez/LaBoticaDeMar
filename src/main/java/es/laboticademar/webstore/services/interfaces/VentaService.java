package es.laboticademar.webstore.services.interfaces;

import java.security.Principal;
import java.util.Optional;

import es.laboticademar.webstore.dto.PaymentDTO;
import es.laboticademar.webstore.dto.venta.VentaDTO;
import es.laboticademar.webstore.dto.venta.VentaPageDTO;
import es.laboticademar.webstore.entities.Venta;

public interface VentaService {
    public Optional<Venta> findById(Long id);
    public Boolean realizarVenta(Principal principal, PaymentDTO paymentData, Boolean useDiscountPoints);
    public VentaPageDTO findVentasByCurrentUser(Principal principal, int page, int size);
    public VentaDTO findVentaDetailsByIdAndUser(Long ventaId, Principal principal);

}