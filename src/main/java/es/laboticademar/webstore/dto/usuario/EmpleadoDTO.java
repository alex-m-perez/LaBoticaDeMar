package es.laboticademar.webstore.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmpleadoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String correo;
    private String password; // Para establecer/cambiar la contraseña. No se enviará la actual.
    private String direccionPostal;
    private Integer telefono;
    private LocalDate fechaNac;
    private Boolean activo;
    private Integer genero;
    private Set<String> roles;

    // Campo derivado, no es parte del formulario directo pero útil en la lista
    private String nombreCompleto;
}