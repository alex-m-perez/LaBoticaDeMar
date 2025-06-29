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
            List<Long> familiaIds, // Cambiado a Lista
            List<Long> categoriaIds, // Cambiado a Lista
            List<Long> subCategoriaIds, // Cambiado a Lista
            FamiliaService familiaSvc,
            CategoriaService categoriaSvc,
            SubcategoriaService subcategoriaSvc
    ) {
        List<Breadcrumb> crumbs = new ArrayList<>();
        String baseUrl = "/product?page=" + page + "&size=" + size;

        // 1. Miga de pan base, siempre presente.
        crumbs.add(new Breadcrumb(
                "Todos los productos",
                baseUrl + buildQueryStringExcluding(req, "familia", "categoria", "subCategoria")
        ));

        // 2. Nivel FAMILIA: solo se añade si hay EXACTAMENTE UNA familia seleccionada.
        if (familiaIds == null || familiaIds.size() > 1) {
            // Si hay 0 o más de 1 familia, detenemos la construcción de migas aquí.
            return crumbs;
        }

        // Si llegamos aquí, es porque familiaIds.size() == 1
        Long uniqueFamiliaId = familiaIds.get(0);
        String familiaLabel = familiaSvc.findById(uniqueFamiliaId).map(Familia::getNombre).orElse("Familia");
        crumbs.add(new Breadcrumb(familiaLabel, baseUrl + buildQueryStringKeeping(req, "familia")));


        // 3. Nivel CATEGORÍA: solo se añade si hay EXACTAMENTE UNA categoría seleccionada.
        if (categoriaIds == null || categoriaIds.size() > 1) {
            // Si hay 0 o más de 1 categoría, nos detenemos después de la familia.
            return crumbs;
        }

        // Si llegamos aquí, es porque categoriaIds.size() == 1
        Long uniqueCategoriaId = categoriaIds.get(0);

        // Validación de consistencia (opcional pero recomendada)
        if (!categoriaSvc.existsByIdAndFamiliaId(uniqueCategoriaId, uniqueFamiliaId)) {
            return crumbs; // La categoría no pertenece a la familia, cortamos.
        }

        String categoriaLabel = categoriaSvc.findById(uniqueCategoriaId).map(Categoria::getNombre).orElse("Categoría");
        crumbs.add(new Breadcrumb(categoriaLabel, baseUrl + buildQueryStringKeeping(req, "familia", "categoria")));


        // 4. Nivel SUBCATEGORÍA: solo se añade si hay EXACTAMENTE UNA subcategoría.
        if (subCategoriaIds == null || subCategoriaIds.size() > 1) {
            // Si hay 0 o más de 1 subcategoría, nos detenemos después de la categoría.
            return crumbs;
        }

        // Si llegamos aquí, es porque subCategoriaIds.size() == 1
        Long uniqueSubCategoriaId = subCategoriaIds.get(0);

        // Validación de consistencia (opcional pero recomendada)
        if (!subcategoriaSvc.existsByIdAndCategoriaId(uniqueSubCategoriaId, uniqueCategoriaId)) {
            return crumbs; // La subcategoría no pertenece a la categoría, cortamos.
        }

        String subCategoriaLabel = subcategoriaSvc.findById(uniqueSubCategoriaId).map(Subcategoria::getNombre).orElse("Subcategoría");
        crumbs.add(new Breadcrumb(subCategoriaLabel, baseUrl + buildQueryStringKeeping(req, "familia", "categoria", "subCategoria")));

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