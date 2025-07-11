package es.laboticademar.webstore.dto.venta;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class VentaKpisDTO {
    // KPIs para "Hoy"
    private long totalVentasHoy;
    private BigDecimal ingresosHoy;

    // KPIs para el rango de fechas seleccionado
    private long totalVentasRango;
    private BigDecimal ingresosRango;
}