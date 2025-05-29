package es.laboticademar.webstore.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping({"/home", "/ventas", "/devoluciones", "/products", "/empleados"})
    public String section(HttpServletRequest request) {
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

        // extraemos la "sección" de la URI, por ejemplo "products"
        String uri = request.getRequestURI();                          // e.g. "/tuApp/admin/products"
        String section = uri.substring(uri.lastIndexOf('/') + 1);

        if (isAjax) {
            // peticiones AJAX devuelven únicamente el fragmento correspondiente
            switch (section) {
                case "ventas":
                    return "admin/ventas_dashboard";
                case "devoluciones":
                    return "admin/devoluciones_dashboard";
                case "products":
                    return "admin/products_dashboard";
                case "empleados":
                    return "admin/empleados_dashboard";
                case "home":
                default:
                    // si quisieras un fragmento de home distinto, podrías devolverlo aquí
                    return "admin/home";
            }
        }

        // petición normal (refresh, url directa) → devolvemos el layout completo
        return "admin/home";
    }

}
