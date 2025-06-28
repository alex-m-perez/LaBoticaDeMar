package es.laboticademar.webstore.controllers.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception e, Model model) {
        logger.error("Se ha producido una excepción no controlada: ", e);

        if (e.getMessage() != null) {
            model.addAttribute("errorDetails", e.getMessage());
        }

        return "error/500";
    }
}