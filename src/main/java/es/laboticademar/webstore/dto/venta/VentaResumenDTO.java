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
public class VentaResumenDTO {
    private Long id;
    private LocalDateTime fechaVenta;
    private String estado;
    private Float montoTotal;
    private Integer totalItems; // Total number of items in the sale
}