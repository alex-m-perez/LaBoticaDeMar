package es.laboticademar.webstore.dto.wishlist;

import es.laboticademar.webstore.entities.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuestWishlistProductoDTO {
    private String id;
    private String nombre;
    private byte[] imagenData;
    private Float price;
    private Float discount;
    private Integer stock;
    private String laboratorioNombre;

    // --- MÉTODO DE FÁBRICA ESTÁTICO (LA VERSIÓN MÁS ROBUSTA) ---
    public static GuestWishlistProductoDTO fromEntity(Producto producto) {
        GuestWishlistProductoDTO dto = new GuestWishlistProductoDTO();
        dto.setId(producto.getId().toString());
        dto.setNombre(producto.getNombre());
        dto.setImagenData(producto.getImagenData());
        dto.setPrice(producto.getPrice());
        dto.setDiscount(producto.getDiscount());
        dto.setStock(producto.getStock());
        dto.setLaboratorioNombre(producto.getLaboratorio() != null ? producto.getLaboratorio().getNombre() : "N/A");
        return dto;
    }
}