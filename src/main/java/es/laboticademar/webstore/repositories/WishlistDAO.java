package es.laboticademar.webstore.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.laboticademar.webstore.entities.Wishlist;

@Repository
public interface WishlistDAO extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByUsuarioId(Long usuarioId);
}