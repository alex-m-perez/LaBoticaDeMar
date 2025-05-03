package es.laboticademar.webstore.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "NOMBRE", nullable = false, length = 60)
    private String nombre;

    @Column(name = "CATEGORIA", nullable = true)
    private Integer categoria;

    @Column(name = "STOCK", nullable = true)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "FAMILIA", nullable = false)
    private Familia familia;

    @Column(name = "DESCRIPCION", nullable = true, length = 350)
    private String descripcion;

    @Column(name = "LABORATORIO", nullable = true, length = 255)
    private String laboratorio;

    @Column(name = "PRECIO", nullable = true)
    private Float price;

    @Column(name = "DESCUENTO", nullable = true)
    private Float discount;

    @Column(name = "IMAGEN_PATH", nullable = true, length = 255)
    private String imagenPath;

    @Column(name = "RATING", nullable = true, length = 1)
    private Integer rating;

    @Column(name = "RATING_COUNT", nullable = true, length = 1)
    private Integer ratingCount;

    @ManyToMany(mappedBy = "productos")
    private List<Wishlist> wishlists;
}
