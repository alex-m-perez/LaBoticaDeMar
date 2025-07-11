package es.laboticademar.webstore.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopCompradorDTO {
    private Long usuarioId;
    private String nombreCompleto;
    private Long cantidadCompras;
    private Double promedioCompra; // Usamos Double para promedios
}
