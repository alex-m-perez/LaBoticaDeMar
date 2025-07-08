package es.laboticademar.webstore.dto.venta;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaAdminResumenDTO {
    private Long id;
    private LocalDateTime fechaVenta;
    private Float montoTotal;
    private Integer totalItems;
    private Long clienteId;
    private String clienteNombre;
}