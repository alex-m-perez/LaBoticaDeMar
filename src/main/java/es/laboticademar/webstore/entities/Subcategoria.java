package es.laboticademar.webstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subcategoria")
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(
        name = "categoria",
        nullable = true,
        foreignKey = @ForeignKey(
            name = "FK_subcategoria_categoria",
            foreignKeyDefinition =
                "FOREIGN KEY (`categoria`) REFERENCES `categoria`(`id`) ON DELETE SET NULL"
        )
    )
    private Categoria categoria;

    @Column(name = "NOMBRE", nullable = true, length = 60)
    private String nombre;
}
