package es.laboticademar.webstore.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class ProductoDTO {
    private BigDecimal id;
    private String nombre;
    private String categoriaEtiqueta;
    private Integer stock;
    private Float price;
    private Boolean activo;
}