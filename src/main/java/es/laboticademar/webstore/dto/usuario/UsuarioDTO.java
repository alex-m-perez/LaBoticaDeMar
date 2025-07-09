package es.laboticademar.webstore.dto.usuario;

import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

@Data
public class UsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String correo;
    private String passwd;

    private LocalDate fechaNac;
    private Integer genero;

    private String calle;
    private Integer numero;
    private String localidad;
    private Integer codigoPostal;
    private String provincia;
    private String pais;
    private Integer telefono;

    // Nuevos campos de consentimientos
    private Boolean aceptaPromociones;
    private Boolean aceptaTerminos;
    private Boolean aceptaPrivacidad;

    // IDs de preferencias seleccionadas (paso 3)
    private Set<Integer> preferencias = new HashSet<>();
}
