package es.laboticademar.webstore.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<<< HEAD:src/main/java/es/laboticademar/webstore/repositories/FacturaDAO.java
import es.laboticademar.webstore.entities.Factura;

public interface FacturaDAO extends JpaRepository<Factura, Long> {
========
import es.laboticademar.webstore.entities.Familia;

public interface FamiliaRDAO extends JpaRepository<Familia, Long> {
>>>>>>>> main:src/main/java/es/laboticademar/webstore/repositories/FamiliaRDAO.java
    // No es necesario definir un método findAll() aquí
}
