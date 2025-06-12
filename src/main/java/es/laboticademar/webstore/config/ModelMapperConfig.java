package es.laboticademar.webstore.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.dto.ProductoDTO;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Creamos un mapeo explícito para los nombres de las relaciones
        TypeMap<Producto, ProductoDTO> typeMap =
            mapper.createTypeMap(Producto.class, ProductoDTO.class);

        typeMap.addMapping(
            src -> src.getCategoria() != null ? src.getCategoria().getNombre() : null,
            ProductoDTO::setCategoriaNombre
        );
        typeMap.addMapping(
            src -> src.getFamilia() != null ? src.getFamilia().getNombre() : null,
            ProductoDTO::setFamiliaNombre
        );
        typeMap.addMapping(
            src -> src.getSubCategoria() != null ? src.getSubCategoria().getNombre() : null,
            ProductoDTO::setSubCategoriaNombre
        );
        typeMap.addMapping(
            src -> src.getLaboratorio() != null ? src.getLaboratorio().getNombre() : null,
            ProductoDTO::setLaboratorioNombre
        );
        typeMap.addMapping(
            src -> src.getTipo() != null ? src.getTipo().getNombre() : null,
            ProductoDTO::setTipoNombre
        );

        return mapper;
    }
}
