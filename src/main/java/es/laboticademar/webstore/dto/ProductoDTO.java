package es.laboticademar.webstore.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String categoriaEtiqueta;
    private Integer stock;
    private Float price;
    private Boolean activo;
}