package es.laboticademar.webstore.services.impl;

import org.springframework.stereotype.Service;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class StripeService {

    public String createPaymentIntent(Long amount, String currency) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
            .setAmount(amount)               // en céntimos: p. ej. 500 = 5,00€
            .setCurrency(currency)           // "eur", "usd", etc.
            .build();

        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getClientSecret();
    }
}
