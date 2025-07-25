package es.laboticademar.webstore.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private GuestCartProductoDTO producto;
    private Integer cantidad;
}