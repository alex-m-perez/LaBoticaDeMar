package es.laboticademar.webstore.entities;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto")
public class Producto {

    @Id
    // DECIMAL(7,1) → 6 dígitos antes del punto, 1 dígito después
    @Column(name = "ID", nullable = false, precision = 7, scale = 1)
    private BigDecimal id;

    @Column(name = "NOMBRE", nullable = false, length = 60)
    private String nombre;

    @Column(name = "DESCRIPCION", nullable = true, length = 1250)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FAMILIA", nullable = false)
    private Familia familia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORIA", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SUBCATEGORIA", nullable = true)
    private Subcategoria subCategoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LABORATORIO", nullable = true)
    private Laboratorio laboratorio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TIPO", nullable = true)
    private TipoProducto tipo;

    @Column(name = "STOCK", nullable = true)
    private Integer stock;

    @Column(name = "ACTIVO", nullable = false)
    private Boolean activo;

    @Column(name = "PRECIO", nullable = true)
    private Float price;

    @Column(name = "DESCUENTO", nullable = true)
    private Float discount;

    @Column(name = "IMAGEN_PATH", nullable = true, length = 255)
    private String imagenPath;

    @Column(name = "RATING", nullable = true)
    private Integer rating;

    @Column(name = "RATING_COUNT", nullable = true)
    private Integer ratingCount;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "productos")
    private List<Wishlist> wishlists;
}
