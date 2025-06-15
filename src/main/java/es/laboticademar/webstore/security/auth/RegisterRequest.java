package es.laboticademar.webstore.security.auth;

import java.time.LocalDate;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String correo;
    private String password;

    private String calle;
    private String numero;
    private String piso;
    private String puerta;
    private String localidad;
    private String codigoPostal;
    private String provincia;
    private String pais;

    private Integer telefono;
    private LocalDate fechaNac;
    private Integer genero;

    private Boolean aceptaPromociones;
    private Boolean aceptaTerminos;
    private Boolean aceptaPrivacidad;

    private Set<Integer> preferencias;
}
