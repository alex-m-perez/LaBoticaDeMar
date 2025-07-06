package es.laboticademar.webstore.dto;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
public class UsuarioPersonalDataDTO{

    private String nombre;
    private String apellido1;
    private String apellido2;
    private String correo;
    private Integer telefono;
    private LocalDate fechaNac;
    private Integer genero;

    private String calle;
    private Integer numero;
    private Integer piso;
    private String puerta;
    private String localidad;
    private Integer codigoPostal;
    private String provincia;
    private String pais;
    
    private Integer puntos;
    private Set<Integer> preferencias = new HashSet<>();
}
