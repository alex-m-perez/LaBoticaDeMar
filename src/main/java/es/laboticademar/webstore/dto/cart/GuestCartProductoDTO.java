package es.laboticademar.webstore.dto.cart;

import es.laboticademar.webstore.entities.Producto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestCartProductoDTO {

    private String      id;
    private String      nombre;
    private String      descripcion;

    private Long        familiaId;
    private String      familiaNombre;

    private Long        categoriaId;
    private String      categoriaNombre;

    private Long        subCategoriaId;
    private String      subCategoriaNombre;

    private Long        laboratorioId;
    private String      laboratorioNombre;

    private Long        tipoId;
    private String      tipoNombre;

    private Integer     stock;
    private Boolean     activo;
    private Float       price;
    private Float       discount;
    private String      imagenPath;
    private Integer     rating;
    private Integer     ratingCount;

    
    public static GuestCartProductoDTO fromEntityToGuestCartProducto(Producto p) {
        return GuestCartProductoDTO.builder()
            .id(p.getId() != null ? p.getId().toString() : null) // Convertido a String
            .nombre(p.getNombre())
            .descripcion(p.getDescripcion())
            // Familia
            .familiaId(p.getFamilia() != null ? p.getFamilia().getId().longValue() : null)
            .familiaNombre(p.getFamilia() != null ? p.getFamilia().getNombre() : null)
            // Categoría
            .categoriaId(p.getCategoria() != null ? p.getCategoria().getId().longValue() : null)
            .categoriaNombre(p.getCategoria() != null ? p.getCategoria().getNombre() : null)
            // Subcategoría
            .subCategoriaId(p.getSubCategoria() != null ? p.getSubCategoria().getId().longValue() : null)
            .subCategoriaNombre(p.getSubCategoria() != null ? p.getSubCategoria().getNombre() : null)
            // Laboratorio
            .laboratorioId(p.getLaboratorio() != null ? p.getLaboratorio().getId().longValue() : null)
            .laboratorioNombre(p.getLaboratorio() != null ? p.getLaboratorio().getNombre() : null)
            // Tipo
            .tipoId(p.getTipo() != null ? p.getTipo().getId().longValue() : null)
            .tipoNombre(p.getTipo() != null ? p.getTipo().getNombre() : null)
            // Campos simples
            .stock(p.getStock())
            .activo(p.getActivo())
            .price(p.getPrice())
            .discount(p.getDiscount())
            .imagenPath(p.getImagenPath())
            .rating(p.getRating())
            .ratingCount(p.getRatingCount())
            .build();
    }
}
