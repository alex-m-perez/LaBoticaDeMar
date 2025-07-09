package es.laboticademar.webstore.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import es.laboticademar.webstore.dto.usuario.TopCompradorDTO;
import es.laboticademar.webstore.dto.usuario.TopDevolucionesDTO;
import es.laboticademar.webstore.dto.usuario.TopGastadorDTO;
import es.laboticademar.webstore.entities.Usuario;

public interface UsuarioDAO extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> getByCorreo(String correo);
    
    @Query("SELECT u FROM Usuario u WHERE u.correo IS NOT NULL")
    List<Usuario> getAllByCorreo();

    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(CONCAT(u.nombre, ' ', u.apellido1, ' ', u.apellido2)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(CONCAT(u.apellido1, ' ', u.apellido2)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(CONCAT(u.nombre, ' ', u.apellido1)) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Usuario> findByNombreFlexible(@Param("query") String query, Pageable pageable);

    Page<Usuario> findByRolesContaining(String rol, Pageable pageable);

    @Query("SELECT u FROM Usuario u WHERE 'ROLE_USUARIO' MEMBER OF u.roles")
    Page<Usuario> findClientes(Pageable pageable);

    @Query("SELECT new es.laboticademar.webstore.dto.usuario.TopGastadorDTO(" +
           "u.id, CONCAT(u.nombre, ' ', u.apellido1), COUNT(v.id), SUM(v.montoTotal)) " +
           "FROM Venta v JOIN v.cliente u " +
           "GROUP BY u.id, u.nombre, u.apellido1 " +
           "ORDER BY SUM(v.montoTotal) DESC")
    Page<TopGastadorDTO> findTopGastadores(Pageable pageable);

    @Query("SELECT new es.laboticademar.webstore.dto.usuario.TopCompradorDTO(" +
           "u.id, CONCAT(u.nombre, ' ', u.apellido1), COUNT(v.id), AVG(v.montoTotal)) " +
           "FROM Venta v JOIN v.cliente u " +
           "GROUP BY u.id, u.nombre, u.apellido1 " +
           "ORDER BY COUNT(v.id) DESC")
    Page<TopCompradorDTO> findTopCompradores(Pageable pageable);

    @Query("SELECT new es.laboticademar.webstore.dto.usuario.TopDevolucionesDTO(" +
           "u.id, CONCAT(u.nombre, ' ', u.apellido1), COUNT(v.id), SUM(v.montoTotal)) " +
           "FROM Venta v JOIN v.cliente u " +
           "WHERE v.estado = es.laboticademar.webstore.enumerations.VentaEstadoEnum.DEVOLUCION " +
           "GROUP BY u.id, u.nombre, u.apellido1 " +
           "ORDER BY COUNT(v.id) DESC")
    Page<TopDevolucionesDTO> findTopDevoluciones(Pageable pageable);
}
