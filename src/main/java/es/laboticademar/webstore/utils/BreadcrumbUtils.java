package es.laboticademar.webstore.utils;

import es.laboticademar.webstore.entities.Categoria;
import es.laboticademar.webstore.entities.Familia;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.Subcategoria;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import es.laboticademar.webstore.services.interfaces.SubcategoriaService;
import es.laboticademar.webstore.utils.objects.Breadcrumb;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BreadcrumbUtils {

    /**
     * Genera migas de pan para la página de listado de productos según los filtros activos.
     */
    public static List<Breadcrumb> generarBreadcrumbs(
            HttpServletRequest req,
            int page, int size,
            Long familia, Long categoria, Long subCategoria,
            FamiliaService familiaSvc,
            CategoriaService categoriaSvc,
            SubcategoriaService subcategoriaSvc
    ) {
        if (familia != null && categoria != null && !categoriaSvc.existsByIdAndFamiliaId(categoria, familia)) {
            categoria = null;
            subCategoria = null;
        }
        if (categoria != null && subCategoria != null && !subcategoriaSvc.existsByIdAndCategoriaId(subCategoria, categoria)) {
            subCategoria = null;
        }

        List<Breadcrumb> crumbs = new ArrayList<>();
        String baseUrl = "/product?page=" + page + "&size=" + size;

        crumbs.add(new Breadcrumb(
                "Todos los productos",
                baseUrl + buildQueryStringExcluding(req, "familia", "categoria", "subCategoria")
        ));

        if (familia != null) {
            String label = familiaSvc.findById(familia).map(Familia::getNombre).orElse("Familia");
            crumbs.add(new Breadcrumb(label, baseUrl + buildQueryStringKeeping(req, "familia")));
        }

        if (categoria != null) {
            String label = categoriaSvc.findById(categoria).map(Categoria::getNombre).orElse("Categoría");
            crumbs.add(new Breadcrumb(label, baseUrl + buildQueryStringKeeping(req, "familia", "categoria")));
        }

        if (subCategoria != null) {
            String label = subcategoriaSvc.findById(subCategoria).map(Subcategoria::getNombre).orElse("Subcategoría");
            crumbs.add(new Breadcrumb(label, baseUrl + buildQueryStringKeeping(req, "familia", "categoria", "subCategoria")));
        }

        return crumbs;
    }

    /**
     * Genera migas de pan para la página de detalle de un producto con la estructura:
     * Todos los productos > Familia > Categoría > Nombre del Producto
     */
    public static List<Breadcrumb> generarParaProducto(
            HttpServletRequest req,
            Producto producto,
            FamiliaService familiaSvc,
            CategoriaService categoriaSvc,
            SubcategoriaService subcategoriaSvc) {

        List<Breadcrumb> crumbs = new ArrayList<>();
        String contextPath = req.getContextPath();

        // Nivel 1: "Todos los productos"
        crumbs.add(new Breadcrumb("Todos los productos", contextPath + "/product"));
        
        // Asumimos que producto tiene acceso directo a su familia y categoría.
        Familia familia = producto.getFamilia();
        Categoria categoria = producto.getCategoria();

        // Nivel 2: Familia (si existe)
        if (familia != null) {
            String familiaUrl = UriComponentsBuilder.fromPath(contextPath + "/product")
                    .queryParam("familia", familia.getId())
                    .toUriString();
            crumbs.add(new Breadcrumb(familia.getNombre(), familiaUrl));
        }

        // Nivel 3: Categoría (si existe y pertenece a la familia)
        if (familia != null && categoria != null) {
            String categoriaUrl = UriComponentsBuilder.fromPath(contextPath + "/product")
                    .queryParam("familia", familia.getId())
                    .queryParam("categoria", categoria.getId())
                    .toUriString();
            crumbs.add(new Breadcrumb(categoria.getNombre(), categoriaUrl));
        }

        // Nivel 4: Nombre del producto (final, sin enlace)
        crumbs.add(new Breadcrumb(producto.getNombre(), null));

        return crumbs;
    }

    private static String buildQueryStringExcluding(HttpServletRequest req, String... excludeKeys) {
        Set<String> exclude = Set.of(excludeKeys);
        StringBuilder sb = new StringBuilder();
        req.getParameterMap().forEach((k, vals) -> {
            if (!exclude.contains(k) && !"page".equals(k) && !"size".equals(k) && vals != null) {
                for (String v : vals) {
                    if (v != null && !v.isEmpty()) {
                        sb.append("&").append(encode(k)).append("=").append(encode(v));
                    }
                }
            }
        });
        return sb.toString();
    }

    private static String buildQueryStringKeeping(HttpServletRequest req, String... includeKeys) {
        Set<String> include = Set.of(includeKeys);
        StringBuilder sb = new StringBuilder();
        req.getParameterMap().forEach((k, vals) -> {
            if (include.contains(k) && vals != null) {
                for (String v : vals) {
                    if (v != null && !v.isEmpty()) {
                        sb.append("&").append(encode(k)).append("=").append(encode(v));
                    }
                }
            }
        });
        return sb.toString();
    }
    
    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}