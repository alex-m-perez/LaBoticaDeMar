package es.laboticademar.webstore.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {
    private String cardNumber;
    private String cardExpiringDate;
    private Integer cardSecretNumber;
}
