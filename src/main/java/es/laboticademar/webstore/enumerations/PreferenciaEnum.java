package es.laboticademar.webstore.enumerations;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum PreferenciaEnum {
    NUTRICION_DIETETICA(1, "Nutrición y Dietética"),
    BEBES_MAMAS(2, "Bebés y Mamás"),
    ORTOPEDIA_MOVILIDAD(3, "Ortopedia y Movilidad"),
    DERMOCOSMETICA(4, "Dermocosmética"),
    HIGIENE_CUIDADO_PERSONAL(5, "Higiene y Cuidado Personal"),
    SALUD_SEXUAL(6, "Salud Sexual"),
    FITOTERAPIA_PRODUCTOS_NATURALES(7, "Fitoterapia y Productos Naturales"),
    VETERINARIA(8, "Veterinaria"),
    BOTIQUIN_PRIMEROS_AUXILIOS(9, "Botiquín y Primeros Auxilios"),
    CAPILAR_CUIDADO_CABELLO(10, "Capilar y Cuidado del Cabello");

    private final int id;
    private final String label;

    PreferenciaEnum(int id, String label) {
        this.id = id;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    /** Devuelve la lista de todas las preferencias */
    public static List<PreferenciaEnum> all() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    /** Obtiene el enum a partir de su id */
    public static PreferenciaEnum fromId(int id) {
        return Arrays.stream(values())
                     .filter(p -> p.id == id)
                     .findFirst()
                     .orElseThrow(() ->
                         new IllegalArgumentException("ID de preferencia inválido: " + id)
                     );
    }
}
