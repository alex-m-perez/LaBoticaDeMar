package es.laboticademar.webstore.entities;

import java.time.LocalDateTime;
import java.util.List;

import es.laboticademar.webstore.enumerations.DevolucionEnum;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "devolucion")
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENTE_ID", nullable = false)
    private Usuario cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VENTA_ID", nullable = false)
    private Venta venta;

    @Column(name = "FECHA_SOLICITUD", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "MOTIVO", nullable = false)
    private DevolucionEnum motivoCategoria;

    @Column(name = "COMENTARIOS", length = 650)
    private String comentarios;

    @OneToMany(
        mappedBy = "devolucion",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private List<DetalleDevolucion> detalles;
}