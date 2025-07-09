package es.laboticademar.webstore.dto.producto;

import lombok.Data;

@Data
public class ProductsKpisDTO {

    private long totalProductos;
    private long productosActivos;
    private long productosInactivos;
    private long stockTotal;

}