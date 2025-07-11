package es.laboticademar.webstore.controllers.restControllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.venta.VentaDTO;
import es.laboticademar.webstore.dto.venta.VentaPageDTO;
import es.laboticademar.webstore.services.interfaces.VentaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentasRestController {

    private final VentaService ventaService;

    @GetMapping("/mis_compras")
    public ResponseEntity<VentaPageDTO> getMisComprasPaginadas(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        VentaPageDTO response = ventaService.findVentasByCurrentUser(principal, page, 10);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/mis_compras/{id}")
    public ResponseEntity<VentaDTO> getVentaDetalle(
            Principal principal,
            @PathVariable Long id) {

        VentaDTO detalleDTO = ventaService.findVentaDetailsByIdAndUser(id, principal);
        return ResponseEntity.ok(detalleDTO);
    }

}
