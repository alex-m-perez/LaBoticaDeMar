package es.laboticademar.webstore.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioBusquedaDTO {
    private Long id;
    private String nombreCompleto;
}