package es.laboticademar.webstore.services.interfaces;

import java.security.Principal;
import java.time.LocalDate;

import org.springframework.data.domain.Page;

import es.laboticademar.webstore.dto.devolucion.DevolucionAdminDetalleDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionAdminResumenDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionDetalleDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionKpisDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionPageDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionRequestDTO;

public interface DevolucionService {
    void crearDevolucion(Long ventaId, DevolucionRequestDTO dto, Principal principal);
    DevolucionPageDTO findDevolucionesByCurrentUser(Principal principal, int page, int size);
    DevolucionDetalleDTO findDevolucionDetailsByIdAndUser(Long devolucionId, Principal principal);
    Page<DevolucionAdminResumenDTO> findAllDevolucionesFiltered(int page, int size, Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin, Float montoMin, Float montoMax);
    DevolucionKpisDTO getDevolucionKpis(Long clienteId, Long idUsuario, LocalDate fechaInicio, LocalDate fechaFin, Float montoMin, Float montoMax);
    DevolucionAdminDetalleDTO findDevolucionDetailsById(Long id);

}
