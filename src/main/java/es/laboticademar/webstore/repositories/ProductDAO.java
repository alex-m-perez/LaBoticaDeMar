package es.laboticademar.webstore.repositories;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import es.laboticademar.webstore.entities.Producto;

public interface ProductDAO extends JpaRepository<Producto, BigDecimal>, JpaSpecificationExecutor<Producto>{
    List<Producto> findTop10ByNombreContainingIgnoreCase(String nombre);
    List<Producto> findTop10ByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    Long countByActivoTrue();
    Long countByActivoFalse();

    @Query("SELECT COALESCE(SUM(p.stock), 0) FROM Producto p")
    Integer sumTotalStock();
}
