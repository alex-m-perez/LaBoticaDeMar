package es.laboticademar.webstore.repositories;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import es.laboticademar.webstore.entities.Producto;

public interface ProductDAO extends JpaRepository<Producto, BigDecimal> {
}
