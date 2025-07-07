package es.laboticademar.webstore.utils;

import java.time.YearMonth;

import es.laboticademar.webstore.dto.PaymentDTO;
import es.laboticademar.webstore.exceptions.InvalidPaymentDataException;

public class CreditCardUtils {
    public static void validarDatosDePago(PaymentDTO paymentData) {
        if (paymentData == null) {
            throw new InvalidPaymentDataException("Los datos de pago no pueden ser nulos.");
        }
        validarNumeroTarjeta(paymentData.getCardNumber());
        validarFechaCaducidad(paymentData.getCardExpiringDate());
        validarCvc(paymentData.getCardSecretNumber());
    }

    private static void validarNumeroTarjeta(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) {
            throw new InvalidPaymentDataException("El número de la tarjeta es obligatorio.");
        }
        String digitsOnly = cardNumber.replace(" ", "");
        if (!digitsOnly.matches("\\d{16}")) {
            throw new InvalidPaymentDataException("El número de tarjeta debe contener exactamente 16 dígitos.");
        }
    }

    private static void validarFechaCaducidad(String fecha) {
        if (fecha == null || !fecha.matches("\\d{2}/\\d{4}")) {
            throw new InvalidPaymentDataException("El formato de la fecha de caducidad debe ser MM/AAAA.");
        }
        String[] partes = fecha.split("/");
        try {
            int mes = Integer.parseInt(partes[0]);
            int anio = Integer.parseInt(partes[1]);
            if (mes < 1 || mes > 12) {
                throw new InvalidPaymentDataException("El mes de la fecha de caducidad no es válido.");
            }
            YearMonth fechaCaducidad = YearMonth.of(anio, mes);
            YearMonth fechaActual = YearMonth.now();
            if (fechaCaducidad.isBefore(fechaActual)) {
                throw new InvalidPaymentDataException("La tarjeta ha caducado.");
            }
            if (anio > fechaActual.getYear() + 15) {
                throw new InvalidPaymentDataException("El año de caducidad es demasiado lejano.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidPaymentDataException("La fecha de caducidad contiene caracteres no válidos.");
        }
    }

    private static void validarCvc(Integer cvc) {
    if (cvc == null) {
            throw new InvalidPaymentDataException("El CVC es obligatorio.");
        }
        if (cvc < 0 || cvc > 999) {
            throw new InvalidPaymentDataException("El CVC debe ser un número de 3 dígitos.");
        }
    }
}
