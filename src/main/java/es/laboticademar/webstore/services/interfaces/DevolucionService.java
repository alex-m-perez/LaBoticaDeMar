package es.laboticademar.webstore.services.interfaces;

import java.security.Principal;

import es.laboticademar.webstore.dto.devolucion.DevolucionDetalleDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionPageDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionRequestDTO;

public interface DevolucionService {
    void crearDevolucion(Long ventaId, DevolucionRequestDTO dto, Principal principal);
    DevolucionPageDTO findDevolucionesByCurrentUser(Principal principal, int page, int size);
    DevolucionDetalleDTO findDevolucionDetailsByIdAndUser(Long devolucionId, Principal principal);

}
