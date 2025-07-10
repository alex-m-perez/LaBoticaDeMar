package es.laboticademar.webstore.services.interfaces;

import java.security.Principal;

import es.laboticademar.webstore.dto.producto.EvaluationRequestDTO;

public interface EvaluationService {
    void saveEvaluation(EvaluationRequestDTO request, Principal principal);
}