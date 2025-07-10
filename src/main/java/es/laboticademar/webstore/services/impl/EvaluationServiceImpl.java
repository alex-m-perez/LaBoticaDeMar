package es.laboticademar.webstore.services.impl;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.dto.producto.EvaluationRequestDTO;
import es.laboticademar.webstore.entities.Evaluation;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.repositories.EvaluationDAO;
import es.laboticademar.webstore.services.interfaces.EvaluationService;
import es.laboticademar.webstore.services.interfaces.ProductService;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

        // Opcional: Impedir que un usuario evalúe el mismo producto dos veces
        evaluationDAO.findByProductIdAndUserId(request.getProductId(), usuarioService.getIdFromPrincipal(principal))
            .ifPresent(e -> {
                throw new IllegalStateException("Este usuario ya ha evaluado el producto.");
            });

        // 1. Buscar el producto y el usuario
        Producto product = productService.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con id: " + request.getProductId()));

        Usuario user = usuarioService.getUserByCorreo(principal.getName())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con correo: " + principal.getName()));

        // 2. Guardar la nueva evaluación
        Evaluation newEvaluation = new Evaluation(request.getScore(), product, user);
        evaluationDAO.save(newEvaluation);

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