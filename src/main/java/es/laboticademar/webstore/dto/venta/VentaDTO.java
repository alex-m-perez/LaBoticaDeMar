package es.laboticademar.webstore.dto.venta;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VentaDTO {
    private Long id;
    private LocalDateTime fechaVenta;
    private Float montoTotal;
    private Integer puntosUtilizados;
    private List<DetalleVentaDTO> productos; // List of products in the sale
}