package es.laboticademar.webstore.controllers.restControllers;

import java.security.Principal;
import java.util.Collections;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.devolucion.DevolucionDetalleDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionPageDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionRequestDTO;
import es.laboticademar.webstore.services.interfaces.DevolucionService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/devoluciones")
@RequiredArgsConstructor
public class DevolucionRestController {

    private final DevolucionService devolucionService;

    @PostMapping("/new/{ventaId}")
    public ResponseEntity<?> solicitarDevolucion(
            @PathVariable Long ventaId,
            @RequestBody DevolucionRequestDTO dto,
            Principal principal) {

        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.singletonMap("message", "Usuario no autenticado."));
        }

        try {
            devolucionService.crearDevolucion(ventaId, dto, principal);
            return ResponseEntity.ok(Collections.singletonMap("message", "Devolución registrada con éxito."));
        } catch (Exception e) {
    return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<DevolucionPageDTO> getMisDevolucionesPaginadas(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        DevolucionPageDTO response = devolucionService.findDevolucionesByCurrentUser(principal, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DevolucionDetalleDTO> getDevolucionDetalle(
            Principal principal,
            @PathVariable Long id) {

        DevolucionDetalleDTO detalleDTO = devolucionService.findDevolucionDetailsByIdAndUser(id, principal);
        return ResponseEntity.ok(detalleDTO);
    }

}
