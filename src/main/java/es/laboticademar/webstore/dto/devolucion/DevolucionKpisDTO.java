package es.laboticademar.webstore.dto.devolucion;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DevolucionKpisDTO {
    private long totalDevolucionesHoy;
    private BigDecimal montoDevueltoHoy;
    private long totalDevolucionesRango;
    private BigDecimal montoDevueltoRango;
}