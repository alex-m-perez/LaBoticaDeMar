package es.laboticademar.webstore.controllers.restControllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.services.impl.StripeService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final StripeService stripeService;

    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestBody Map<String, Object> request) throws Exception {
        Long amount = ((Number) request.get("amount")).longValue();
        String currency = (String) request.get("currency");
        String clientSecret = stripeService.createPaymentIntent(amount, currency);
        return ResponseEntity.ok(Map.of("clientSecret", clientSecret));
    }
}
