package es.laboticademar.webstore.services.impl;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.laboticademar.webstore.dto.producto.EvaluationRequestDTO;
import es.laboticademar.webstore.entities.Evaluation;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.repositories.EvaluationDAO;
import es.laboticademar.webstore.services.interfaces.EvaluationService;
import es.laboticademar.webstore.services.interfaces.ProductService;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationDAO evaluationDAO;
    private final ProductService productService;
    private final UsuarioService usuarioService;

    @Override
    @Transactional
    public void saveEvaluation(EvaluationRequestDTO request, Principal principal) {
        if (principal == null) return;
        // Validar que la puntuación esté en el rango permitido
        if (request.getScore() < 1 || request.getScore() > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5.");
        }

        Producto product = productService.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + request.getProductId()));

        Usuario user = usuarioService.getUserByCorreo(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con correo: " + principal.getName()));


        // Opcional: Impedir que un usuario evalúe el mismo producto dos veces
        Optional<Evaluation> existingEvaluationOpt = evaluationDAO.findByProductIdAndUserId(product.getId(), user.getId());

        // 2. Si está presente: actualiza; si no: crea uno nuevo
        Evaluation evaluation = existingEvaluationOpt
            .map(eval -> {
                // YA EXISTE: actualiza la puntuación
                eval.setScore(request.getScore());
                return eval;
            })
            .orElseGet(() -> {
                // NO EXISTE: crea una nueva evaluación
                return new Evaluation(request.getScore(), product, user);
            });

        // 3. Guarda (tanto actualización como nueva entidad funcionan igual)
        evaluationDAO.save(evaluation);

        // 3. Obtener todas las evaluaciones para ese producto
        List<Evaluation> allEvaluations = evaluationDAO.findByProductId(product.getId());

        // 4. Calcular la media
        double averageScore = allEvaluations.stream()
                .mapToInt(Evaluation::getScore)
                .average()
                .orElse(0.0);

        // 5. Actualizar el producto
        product.setRating((int) Math.round(averageScore));
        product.setRatingCount(allEvaluations.size());

        productService.saveOrUpdateProducto(product);
    }
}