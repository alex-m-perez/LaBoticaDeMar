package es.laboticademar.webstore.controllers;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import es.laboticademar.webstore.services.interfaces.LaboratorioService;
import es.laboticademar.webstore.services.interfaces.ProductService;
import es.laboticademar.webstore.services.interfaces.SubcategoriaService;
import es.laboticademar.webstore.utils.BreadcrumbUtils;
import es.laboticademar.webstore.utils.objects.Breadcrumb;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

	private final HttpServletRequest request;
	private final ProductService productService;
	private final DestacadoService destacadoService;
	private final FamiliaService familiaService;
	private final CategoriaService categoriaService;
	private final SubcategoriaService subcategoriaService;
	private final LaboratorioService laboratorioService;

    @GetMapping("/{id}")
    public String goProductInfo(@PathVariable("id") BigDecimal id,Model model) {
        // 1) Carga el producto y destacados
        Producto producto = productService.findById(id);
        model.addAttribute("producto", producto);
        model.addAttribute("destacados", destacadoService.getAllDestacados());

        // 2) Genera migas con el nuevo método
        List<Breadcrumb> crumbs = BreadcrumbUtils.generarParaProducto(
            request,
            producto,
            familiaService,
            categoriaService,
            subcategoriaService
        );
        model.addAttribute("breadcrumbs", crumbs);

        return "product/producto_info";
    }

    @GetMapping
    public String verProductos(
        HttpServletRequest request,
        @RequestParam(defaultValue="0")      int page,
        @RequestParam(defaultValue="25")     int size,
        @RequestParam(required=false)        String id,
        @RequestParam(required=false)        String nombreProducto,
        @RequestParam(required=false)        Boolean activo,
        @RequestParam(required=false)        Long categoria,
        @RequestParam(required=false)        Long subCategoria,
        @RequestParam(required=false)        Long tipo,
        @RequestParam(required=false)        Long familia,
        @RequestParam(required=false)        Long laboratorio,
        @RequestParam(required=false)        Long presentacion,
        @RequestParam(required=false)        Boolean stock,
        @RequestParam(required=false)        BigDecimal precioMin,
        @RequestParam(required=false)        BigDecimal precioMax,
        Model model
    ) {
        // — filtros para el JSP
        model.addAttribute("filtroId",           id);
        model.addAttribute("filtroNombre",       nombreProducto);
        model.addAttribute("filtroActivo",       activo);
        model.addAttribute("filtroCategoria",    categoria);
        model.addAttribute("filtroSubCategoria", subCategoria);
        model.addAttribute("filtroTipo",         tipo);
        model.addAttribute("filtroFamilia",      familia);
        model.addAttribute("filtroLaboratorio",  laboratorio);
        model.addAttribute("filtroPresentacion", presentacion);
        model.addAttribute("filtroStock",        stock);
        model.addAttribute("filtroPrecioMin",    precioMin);
        model.addAttribute("filtroPrecioMax",    precioMax);

        // — datos sidebar
        model.addAttribute("todasLasFamilias",      familiaService.findAll());
        model.addAttribute("categoriasPorFamilia",   categoriaService.findAll());
        model.addAttribute("todasLasSubcategorias",  subcategoriaService.findAll());
        model.addAttribute("todosLosLaboratorios",   laboratorioService.findAll());

        // — generamos migas de pan
        List<Breadcrumb> crumbs = BreadcrumbUtils.generarBreadcrumbs(
            request, page, size,
            familia, categoria, subCategoria,
            familiaService, categoriaService, subcategoriaService
        );
        model.addAttribute("breadcrumbs", crumbs);

        return "product/productos";
    }

}
