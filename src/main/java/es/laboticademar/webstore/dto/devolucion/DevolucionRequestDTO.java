package es.laboticademar.webstore.dto.devolucion;

import lombok.Data;

@Data
public class DevolucionRequestDTO {
    private Integer motivoId;
    private String comentarios;
}