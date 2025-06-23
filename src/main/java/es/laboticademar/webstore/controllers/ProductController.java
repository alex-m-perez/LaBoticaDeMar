package es.laboticademar.webstore.controllers;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.laboticademar.webstore.services.interfaces.CategoriaService;
import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import es.laboticademar.webstore.services.interfaces.LaboratorioService;
import es.laboticademar.webstore.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final DestacadoService destacadoService;
    private final FamiliaService familiaService;
    private final CategoriaService categoriaService;
    private final LaboratorioService laboratorioService;

    @GetMapping("/{id}")
    public String goProductInfo(@PathVariable("id") BigDecimal id, Model model) {

        model.addAttribute("destacados", destacadoService.getAllDestacados());
        model.addAttribute("producto", productService.getProductoById(id));

        return "product/product_info";
    }

    @GetMapping
    public String verProductos(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "25") int size,
        @RequestParam(required = false) String id,
        @RequestParam(required = false) String nombreProducto,
        @RequestParam(required = false) Boolean activo,
        @RequestParam(required = false) Long categoria,
        @RequestParam(required = false) Long subCategoria,
        @RequestParam(required = false) Long tipo,
        @RequestParam(required = false) Long familia,
        @RequestParam(required = false) Long laboratorio,
        @RequestParam(required = false) Long presentacion,
        @RequestParam(required = false) Boolean stock,
        @RequestParam(required = false) BigDecimal precioMin,
        @RequestParam(required = false) BigDecimal precioMax,
        Model model
    ) {

        // Y también todos los filtros recibidos, para que el JSP pueda
        // marcar los checkboxes/inputs correspondientes:
        model.addAttribute("filtroId", id);
        model.addAttribute("filtroNombre", nombreProducto);
        model.addAttribute("filtroActivo", activo);
        model.addAttribute("filtroCategoria", categoria);
        model.addAttribute("filtroSubCategoria", subCategoria);
        model.addAttribute("filtroTipo", tipo);
        model.addAttribute("filtroFamilia", familia);
        model.addAttribute("filtroLaboratorio", laboratorio);
        model.addAttribute("filtroPresentacion", presentacion);
        model.addAttribute("filtroStock", stock);
        model.addAttribute("filtroPrecioMin", precioMin);
        model.addAttribute("filtroPrecioMax", precioMax);

        // para el sidebar:
        model.addAttribute("todasLasFamilias", familiaService.findAll());
        model.addAttribute("categoriasPorFamilia", categoriaService.findAll()); 
        model.addAttribute("todasLasSubcategorias", null);
        model.addAttribute("todosLosLaboratorios", laboratorioService.findAll());

        return "product/productos";  // productos.jsp
    }
}