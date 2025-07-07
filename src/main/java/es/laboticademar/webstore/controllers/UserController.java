package es.laboticademar.webstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class UserController {

    /**
     * Maneja la carga inicial de la página de perfil y las peticiones de fragmentos.
     */
    @GetMapping({"", "/datos_personales", "/mis_compras", "/mis_devoluciones"})
    public String getProfileSection(HttpServletRequest request) {
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        
        // Si no es AJAX, es una carga de página completa (URL directa, F5)
        // Siempre servimos la página principal que contiene el script de carga.
        if (!isAjax) {
            return "user/perfil"; // Devuelve perfil.jsp
        }

        // Si es una petición AJAX, devolvemos solo el fragmento de la vista correspondiente.
        String uri = request.getRequestURI();
        String section = uri.substring(uri.lastIndexOf('/') + 1);

        switch (section) {
            case "datos_personales":
                return "user/datos_personales"; // Devuelve /WEB-INF/jsp/profile_fragments/_datos.jsp
            case "mis_compras":
                return "user/mis_compras";
            case "mis_devoluciones":
                return "user/mis_devoluciones";
            default:
                // Por defecto, o si la URL es solo /profile, cargamos la sección de datos.
                return "user/datos_personales";
        }
    }
}