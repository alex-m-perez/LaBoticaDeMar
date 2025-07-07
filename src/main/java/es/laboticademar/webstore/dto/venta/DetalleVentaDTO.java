package es.laboticademar.webstore.dto.venta;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVentaDTO {
    // From ProductoDTO
    private BigDecimal id;
    private String nombre;
    private String imagenPath;
    private String laboratorioNombre;

    // From DetalleVenta
    private Integer cantidad;
    private Float precioUnitario; // The price at the time of purchase
}