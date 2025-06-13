package es.laboticademar.webstore.enumerations;

public enum MotivoDevolucion {

    PRODUCTO_DANADO_ENVIO(1, "Producto dañado en envío", "El producto llegó dañado durante el envío"),
    PRODUCTO_DEFECTUOSO(2, "Producto defectuoso", "El producto presenta un defecto de fabricación"),
    PRODUCTO_EQUIVOCADO(3, "Producto equivocado", "Recibí un producto diferente al solicitado"),
    CANTIDAD_INCORRECTA(4, "Cantidad incorrecta", "Cantidad incorrecta recibida"),
    PRODUCTO_CADUCADO(5, "Producto caducado", "Producto caducado o con fecha próxima no indicada"),
    EMBALAJE_ABIERTO(6, "Embalaje abierto", "El embalaje del producto estaba abierto o alterado al recibirlo"),
    FALTA_COMPONENTES(7, "Faltan componentes", "Faltan elementos del producto o accesorios"),
    ERROR_DESCRIPCION_WEB(8, "Error en descripción", "El producto no coincide con la descripción del sitio web"),
    PRODUCTO_REPETIDO(9, "Producto repetido", "Pedido duplicado por error del sistema o del usuario"),
    ENTREGA_INADECUADA(10, "Entrega inadecuada", "Producto afectado por mala conservación durante el transporte");

    private final int id;
    private final String etiqueta;
    private final String descripcion;

    MotivoDevolucion(int id, String etiqueta, String descripcion) {
        this.id = id;
        this.etiqueta = etiqueta;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return etiqueta;
    }

    // Método útil para obtener el enum por ID
    public static MotivoDevolucion fromId(int id) {
        for (MotivoDevolucion motivo : values()) {
            if (motivo.id == id) {
                return motivo;
            }
        }
        throw new IllegalArgumentException("ID inválido para MotivoDevolucion: " + id);
    }
}
