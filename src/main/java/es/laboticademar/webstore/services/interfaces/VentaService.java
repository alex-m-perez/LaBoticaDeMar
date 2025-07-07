package es.laboticademar.webstore.services.interfaces;

import java.security.Principal;

import es.laboticademar.webstore.dto.PaymentDTO;

public interface VentaService {
    public Boolean realizarVenta(Principal principal, PaymentDTO paymentData, Boolean useDiscountPoints);
}