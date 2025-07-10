package es.laboticademar.webstore.dto.producto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class EvaluationRequestDTO {
    private BigDecimal productId;
    private Integer score;
}