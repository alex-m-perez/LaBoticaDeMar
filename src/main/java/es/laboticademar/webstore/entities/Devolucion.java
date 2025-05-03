package es.laboticademar.webstore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Devolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(targetEntity= Producto.class)
    private Producto producto;
    @ManyToOne(targetEntity= Factura.class)
    private Factura factura;
    private Float importe;
    @ManyToOne(targetEntity= Usuario.class)
    private Usuario emisor;

}
