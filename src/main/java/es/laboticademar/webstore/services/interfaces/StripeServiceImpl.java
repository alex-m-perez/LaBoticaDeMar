package es.laboticademar.webstore.services.interfaces;


/**
 * Interfaz para definir las operaciones de Stripe.
 */
public interface StripeServiceImpl {

    /**
     * Crea un PaymentIntent en Stripe y devuelve el clientSecret.
     *
     * @param amount   Importe en céntimos (por ejemplo, 500 = 5,00€)
     * @param currency Código de moneda ("eur", "usd", etc.)
     * @return clientSecret para confirmar el pago desde el frontend
     * @throws Exception si ocurre un error al llamar a la API de Stripe
     */
    String createPaymentIntent(Long amount, String currency) throws Exception;
}
