package es.laboticademar.webstore.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.laboticademar.webstore.dto.ProductoDTO;
import es.laboticademar.webstore.entities.Producto;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        TypeMap<Producto, ProductoDTO> typeMap =
            mapper.createTypeMap(Producto.class, ProductoDTO.class);

        typeMap.addMapping(
            src -> {
                return src.getCategoria() != null
                    ? src.getCategoria().getEtiqueta()
                    : "";
            },
            ProductoDTO::setCategoriaEtiqueta
        );

        return mapper;
    }
}
