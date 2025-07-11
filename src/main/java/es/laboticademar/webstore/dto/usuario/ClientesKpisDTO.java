package es.laboticademar.webstore.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientesKpisDTO {
    private TopCompradorDTO topCompradorDTO;
    private TopGastadorDTO topGastadorDTO;
    private TopDevolucionesDTO topDevolucionesDTO;
}