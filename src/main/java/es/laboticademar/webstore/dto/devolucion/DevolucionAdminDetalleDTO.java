package es.laboticademar.webstore.dto.devolucion;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DevolucionAdminDetalleDTO {
    private Long id;
    private LocalDateTime fechaSolicitud;
    private BigDecimal montoDevuelto;
    private Long ventaId;
    private String motivo;
    private String comentarios;
    private String clienteNombre;
}