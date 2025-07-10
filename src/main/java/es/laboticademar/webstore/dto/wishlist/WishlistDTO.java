package es.laboticademar.webstore.dto.wishlist;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistDTO {
    private List<GuestWishlistProductoDTO> items;    
}
