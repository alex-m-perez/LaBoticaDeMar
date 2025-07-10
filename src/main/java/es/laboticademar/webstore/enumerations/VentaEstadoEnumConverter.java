package es.laboticademar.webstore.enumerations;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true) // autoApply hace que se use en todos los campos de tipo VentaEstadoEnum
public class VentaEstadoEnumConverter implements AttributeConverter<VentaEstadoEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(VentaEstadoEnum estadoEnum) {
        if (estadoEnum == null) {
            return null;
        }
        return estadoEnum.getId();
    }

    @Override
    public VentaEstadoEnum convertToEntityAttribute(Integer id) {
        if (id == null) {
            return null;
        }
        return VentaEstadoEnum.fromId(id);
    }
}