package es.laboticademar.webstore.dto.usuario;

import java.util.Date;
import java.util.Set;
import lombok.Data;

@Data
public class UsuarioDetalleDTO {
    private Long id;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String correo;
    private String direccionPostal;
    private Integer telefono;
    private Date fechaNac;
    private Boolean activo;
    private Integer genero;
    private Set<String> preferencias;
    private Boolean aceptaPromociones;
    private Boolean aceptaTerminos;
    private Boolean aceptaPrivacidad;
    private Integer puntos;
    private Set<String> roles;

    public String getNombreCompleto() {
        return String.join(" ", 
            (nombre != null ? nombre : ""),
            (apellido1 != null ? apellido1 : ""),
            (apellido2 != null ? apellido2 : "")
        ).trim().replaceAll("\\s+", " ");
    }
}