package es.laboticademar.webstore.dto.devolucion;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DevolucionDetalleDTO {
    private Long id;
    private LocalDateTime fechaSolicitud;
    private Long ventaId;
    private String motivo;
    private String comentarios;
}