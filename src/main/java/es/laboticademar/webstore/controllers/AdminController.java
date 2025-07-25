package es.laboticademar.webstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.enumerations.PreferenciaEnum;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import es.laboticademar.webstore.services.interfaces.LaboratorioService;
import es.laboticademar.webstore.services.interfaces.ProductService;
import es.laboticademar.webstore.services.interfaces.SubcategoriaService;
import es.laboticademar.webstore.services.interfaces.TipoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor 
public class AdminController {

    private final CategoriaService categoriaService;
    private final FamiliaService familiaService;
    private final LaboratorioService laboratorioService;
    private final ProductService productService;
    private final SubcategoriaService subcategoriaService;
    private final TipoService tipoService;

    @ModelAttribute
    public void populateProductsSelects(Model model, HttpServletRequest req) {
        String section = req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/')+1);
        if ("products".equals(section)) {
            model.addAttribute("familias",        familiaService.findAll());
            model.addAttribute("categorias",      categoriaService.findAll());
            model.addAttribute("subcategorias",   subcategoriaService.findAll());
            model.addAttribute("tipos",           tipoService.findAll());
            model.addAttribute("laboratorios",    laboratorioService.findAll());
            model.addAttribute("totalProductos",  productService.countAllProducts());
            model.addAttribute("totalActivos",    productService.countByActivo(true));
            model.addAttribute("totalInactivos",  productService.countByActivo(false));
            model.addAttribute("stockTotal",      productService.sumTotalStock());
        }
    }
    
    @GetMapping({"/home", "/ventas", "/devoluciones", "/products", "/ofertas", "/empleados", "/usuarios"})
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
                case "ofertas":
                    return "admin/ofertas_dashboard";
                case "empleados":
                    return "admin/empleados_dashboard";
                case "usuarios":
                    return "admin/usuarios_dashboard";
                case "home":
                default:
                    // si quisieras un fragmento de home distinto, podrías devolverlo aquí
                    return "admin/home";
            }
        }

        // petición normal (refresh, url directa) → devolvemos el layout completo
        return "admin/home";
    }

    @GetMapping("/register")
    public String goRegisterPage(Model model) {
        model.addAttribute("preferenciasEnumList", PreferenciaEnum.values());
        return "admin/registro_empleado";
    }

}
