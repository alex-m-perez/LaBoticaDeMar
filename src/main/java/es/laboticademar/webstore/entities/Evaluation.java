package es.laboticademar.webstore.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluacion")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private Integer score; // Puntuación de 1 a 5

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Producto product;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Usuario user;

    public Evaluation(Integer score, Producto product, Usuario user) {
        this.score = score;
        this.product = product;
        this.user = user;
    }
}