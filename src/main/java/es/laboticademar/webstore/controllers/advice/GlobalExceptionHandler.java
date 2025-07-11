package es.laboticademar.webstore.controllers.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import es.laboticademar.webstore.dto.ErrorResponse;
import es.laboticademar.webstore.exceptions.InsufficientStockException;
import es.laboticademar.webstore.exceptions.InvalidPaymentDataException;
import jakarta.servlet.http.HttpServletRequest; // Importante
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String AJAX_HEADER = "X-Requested-With";
    private static final String AJAX_VALUE = "XMLHttpRequest";

    @ExceptionHandler(InvalidPaymentDataException.class)
    public Object handleInvalidPaymentData(InvalidPaymentDataException ex, HttpServletRequest request) {
        log.error("Error de validación de pago: {}", ex.getMessage());

        // Si es una llamada AJAX, devuelve JSON con estado 400
        if (AJAX_VALUE.equals(request.getHeader(AJAX_HEADER))) {
            ErrorResponse error = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        // Si es una navegación normal, redirige a la página de error
        // (El modelo y la vista se resolverán a través de otro mecanismo o se puede devolver un ModelAndView)
        // Por simplicidad, podríamos redirigir a una página de error genérica.
        // O si tienes un controlador para /error, Spring lo manejará.
        // Para este caso, devolveremos el JSON que es lo que espera el flujo de pago.
        // Si necesitaras una página de error para otros flujos, se añadiría aquí.
         ErrorResponse error = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
         return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public Object handleInsufficientStock(InsufficientStockException ex, HttpServletRequest request) {
        log.error("Conflicto de stock: {}", ex.getMessage());

        // Si es AJAX
        if (AJAX_VALUE.equals(request.getHeader(AJAX_HEADER))) {
            ErrorResponse error = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
            return new ResponseEntity<>(error, HttpStatus.CONFLICT); // 409
        }
        
        // Si es normal (asumimos que este error solo puede pasar por AJAX en tu flujo actual)
        ErrorResponse error = new ErrorResponse(ex.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public Object handleGenericException(Exception ex, HttpServletRequest request, Model model) {
        log.error("Se ha producido una excepción no controlada:", ex);

        // Si es AJAX
        if (AJAX_VALUE.equals(request.getHeader(AJAX_HEADER))) {
            ErrorResponse error = new ErrorResponse("Ha ocurrido un error inesperado.", System.currentTimeMillis());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }

        // Si es una navegación normal, renderiza la página JSP de error
        model.addAttribute("errorDetails", "Ha ocurrido un error inesperado en el servidor.");
        return "error/500"; // Devuelve el nombre de la vista (tu página de error personalizada)
    }
}