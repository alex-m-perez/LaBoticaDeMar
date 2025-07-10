package es.laboticademar.webstore.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.laboticademar.webstore.entities.Evaluation;

@Repository
public interface EvaluationDAO extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByProductId(BigDecimal productId);
    Optional<Evaluation> findByProductIdAndUserId(BigDecimal productId, Long userId);
}