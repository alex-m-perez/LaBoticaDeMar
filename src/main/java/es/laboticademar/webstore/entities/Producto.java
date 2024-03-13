package es.laboticademar.webstore.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Producto {

    @Id
	private Integer cn;
    private String nombre;
    private Integer stock;
    @ManyToOne(targetEntity= Familia.class)
    private Familia familia;

}
