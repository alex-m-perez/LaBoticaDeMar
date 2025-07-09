package es.laboticademar.webstore.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellido1", nullable = false)
    private String apellido1;

    @Column(name = "apellido2")
    private String apellido2;

    @Column(name = "correo", nullable = false, unique = true)
    private String correo;

    @Column(name = "passwd", nullable = true)
    private String passwd;

    @Column(name = "direccion_postal")
    private String direccionPostal;

    @Column(name = "telefono")
    private Integer telefono;

    @Column(name = "fecha_nacimiento", nullable = false)
    private Date fechaNac;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "genero", nullable = false)
    private Integer genero;

    @ElementCollection
    @CollectionTable(name = "usuario_preferencias", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "preferencia")
    private Set<Integer> preferencias = new HashSet<>();

    @Column(name = "acepta_promociones", nullable = false)
    private Boolean aceptaPromociones;

    @Column(name = "acepta_terminos", nullable = false)
    private Boolean aceptaTerminos;

    @Column(name = "acepta_privacidad", nullable = false)
    private Boolean aceptaPrivacidad;

    @Column(name = "puntos", nullable = true)
    private Integer puntos = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private Set<String> roles = new HashSet<>();
}
