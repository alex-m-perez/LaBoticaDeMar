package es.laboticademar.webstore.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.laboticademar.webstore.dto.BreadcrumbDTO;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import es.laboticademar.webstore.services.interfaces.LaboratorioService;
import es.laboticademar.webstore.services.interfaces.ProductService;
import es.laboticademar.webstore.services.interfaces.SubcategoriaService;
import es.laboticademar.webstore.services.interfaces.TipoService;
import es.laboticademar.webstore.utils.BreadcrumbUtils;
import jakarta.servlet.http.HttpServletRequest;
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
    private final TipoService tipoService;

    @GetMapping("/{id}")
    public String goProductInfo(@PathVariable("id") BigDecimal id, Model model) {
        // 1) Carga el producto y destacados
        Optional<Producto> productoOptional = productService.findById(id);
        if (!productoOptional.isPresent()) return "errro/500";
        model.addAttribute("producto", productoOptional.get());
        model.addAttribute("destacados", destacadoService.getAllDestacados());

        // 2) Genera migas con el nuevo método
        List<BreadcrumbDTO> crumbs = BreadcrumbUtils.generarParaProducto(
            request,
            productoOptional.get(),
            familiaService,
            categoriaService,
            subcategoriaService
        );
        model.addAttribute("breadcrumbs", crumbs);

        return "product/producto_detail";
    }

    @GetMapping
    public String verProductos(
        HttpServletRequest request,
        @RequestParam(defaultValue="0")      int page,
        @RequestParam(defaultValue="25")     int size,
        @RequestParam(required=false)        String id,
        @RequestParam(required=false)        String nombreProducto,
        @RequestParam(required=false)        List<Long> familia,
        @RequestParam(required=false)        List<Long> categoria,
        @RequestParam(required=false)        List<Long> subCategoria,
        @RequestParam(required=false)        List<Long> tipo,
        @RequestParam(required=false)        List<Long> laboratorio,
        @RequestParam(required=false)        Boolean stock,
        @RequestParam(required=false)        BigDecimal precioMin,
        @RequestParam(required=false)        BigDecimal precioMax,
        Model model
    ) {
        // — filtros producto
        model.addAttribute("filtroId",           id);
        model.addAttribute("filtroNombre",       nombreProducto);
        model.addAttribute("filtroCategoria",    categoria);
        model.addAttribute("filtroSubCategoria", subCategoria);
        model.addAttribute("filtroTipo",         tipo);
        model.addAttribute("filtroFamilia",      familia);
        model.addAttribute("filtroLaboratorio",  laboratorio);
        model.addAttribute("filtroStock",        stock);
        model.addAttribute("filtroPrecioMin",    precioMin);
        model.addAttribute("filtroPrecioMax",    precioMax);

        // — datos sidebar
        model.addAttribute("todosLosLaboratorios",   laboratorioService.findAll());
        model.addAttribute("tipos",   tipoService.findAll());

        // — generamos migas de pan
        List<BreadcrumbDTO> crumbs = BreadcrumbUtils.generarBreadcrumbs(
            request, page, size,
            familia, categoria, subCategoria,
            familiaService, categoriaService, subcategoriaService
        );
        model.addAttribute("breadcrumbs", crumbs);

        return "product/productos";
    }

}
