package es.laboticademar.webstore.enumerations;

public enum VentaEstadoEnum {

    ACEPTADO(0, "Aceptado", "El producto llegó dañado durante el envío", "689a3f"),
    PREPARANDO(1, "Preparando", "El producto presenta un defecto de fabricación", "689a3f"),
    ENVIADO(2, "Enviado", "Recibí un producto diferente al solicitado", "689a3f"),
    ENTREGADO(3, "Entregado", "Cantidad incorrecta recibida", "689a3f"),
    CANCELADO(4, "Cancelado", "Cantidad incorrecta recibida", "689a3f"),
    DEVOLUCION(5, "Devolución completa", "Cantidad incorrecta recibida", "689a3f");

    private final Integer id;
    private final String etiqueta;
    private final String descripcion;
    private final String color;

    VentaEstadoEnum(Integer id, String etiqueta, String descripcion, String color) {
        this.id = id;
        this.etiqueta = etiqueta;
        this.descripcion = descripcion;
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return etiqueta;
    }

    // Método útil para obtener el enum por ID
    public static VentaEstadoEnum fromId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID del estado no puede ser nulo");
        }
        // Correctly iterates over its OWN values
        for (VentaEstadoEnum estado : VentaEstadoEnum.values()) { 
            // Assumes VentaEstadoEnum has a public "getId()" method
            if (estado.getId().equals(id)) { 
                return estado;
            }
        }
        throw new IllegalArgumentException("Estado de venta no válido: " + id);
    }
}

