package es.laboticademar.webstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Factura {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @Column(name= "numero_factura")
    private Integer numeroFactura;
    @OneToOne(targetEntity= TipoFactura.class)
    private TipoFactura tipo;
    private Float importe;
    @ManyToOne(targetEntity= Usuario.class)
    private Usuario cliente;
    @ManyToOne(targetEntity= Usuario.class)
    private Usuario emisor;
    private Integer puntos;

}
