package es.laboticademar.webstore.dto.cart;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {
    private List<CartItemDTO> items;
}