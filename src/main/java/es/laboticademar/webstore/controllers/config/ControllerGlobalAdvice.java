package es.laboticademar.webstore.controllers.config;

import es.laboticademar.webstore.entities.UsuarioPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Añade al modelo el nombre completo del usuario autenticado en cada vista JSP.
 */
@ControllerAdvice
public class ControllerGlobalAdvice {

    @ModelAttribute("currentUserName")
    public String addLoggedUserName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null
            && auth.isAuthenticated()
            && auth.getPrincipal() instanceof UsuarioPrincipal principal) {

            var usuario = principal.getUsuario();
            String nombre = usuario.getNombre();
            String apellido1 = usuario.getApellido1();

            if (apellido1 != null && !apellido1.isBlank()) {
                return nombre + " " + apellido1;
            }
            return nombre;
        }
        return null;
    }

}
