package es.laboticademar.webstore.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import es.laboticademar.webstore.entities.Producto;

public interface ProductDAO extends JpaRepository<Producto, BigDecimal> {
    List<Producto> findTop10ByNombreContainingIgnoreCase(String nombre);
    List<Producto> findTop10ByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
}
