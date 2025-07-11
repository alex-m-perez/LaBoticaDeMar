package es.laboticademar.webstore.dto.venta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VentaEstadoDTO {
    private Integer id;
    private String label;
}