package es.laboticademar.webstore.controllers.restControllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.producto.EvaluationRequestDTO;
import es.laboticademar.webstore.services.interfaces.EvaluationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluations")
@RequiredArgsConstructor
public class EvaluationRestController {

    private final EvaluationService evaluationService;

    @PostMapping(path = "/new")
    public ResponseEntity<Void> addEvaluation(@RequestBody EvaluationRequestDTO request, Principal principal) {
        evaluationService.saveEvaluation(request, principal);
        return ResponseEntity.ok().build();
    }
}