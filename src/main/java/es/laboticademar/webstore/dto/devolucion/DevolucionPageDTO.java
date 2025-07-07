package es.laboticademar.webstore.dto.devolucion;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionPageDTO {
    // La página ahora contiene el DTO de detalle completo
    private Page<DevolucionDetalleDTO> devolucionesPage;
}