package es.laboticademar.webstore.config;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.laboticademar.webstore.dto.producto.ProductoDTO;
import es.laboticademar.webstore.dto.usuario.EmpleadoDTO;
import es.laboticademar.webstore.dto.usuario.UsuarioDTO;
import es.laboticademar.webstore.dto.usuario.UsuarioPersonalDataDTO;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.entities.Usuario;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Converter Fecha → LocalDate
        Converter<Date, LocalDate> dateToLocalDate = ctx -> {
            Date d = ctx.getSource();
            return (d == null)
                    ? null
                    : Instant.ofEpochMilli(d.getTime())
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
        };

        // Converter LocalDate → Date
        Converter<LocalDate, Date> localDateToDate = ctx -> {
            LocalDate ld = ctx.getSource();
            return (ld == null)
                    ? null
                    : Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
        };

        // Producto → ProductoDTO
        TypeMap<Producto, ProductoDTO> prodMap = mapper.createTypeMap(Producto.class, ProductoDTO.class);
        prodMap.addMapping(src -> src.getCategoria() != null ? src.getCategoria().getNombre() : null,
                ProductoDTO::setCategoriaNombre);
        prodMap.addMapping(src -> src.getFamilia() != null ? src.getFamilia().getNombre() : null,
                ProductoDTO::setFamiliaNombre);
        prodMap.addMapping(src -> src.getSubCategoria() != null ? src.getSubCategoria().getNombre() : null,
                ProductoDTO::setSubCategoriaNombre);
        prodMap.addMapping(src -> src.getLaboratorio() != null ? src.getLaboratorio().getNombre() : null,
                ProductoDTO::setLaboratorioNombre);
        prodMap.addMapping(src -> src.getTipo() != null ? src.getTipo().getNombre() : null,
                ProductoDTO::setTipoNombre);

        // Usuario → UsuarioDTO
        TypeMap<Usuario, UsuarioDTO> userMap = mapper.createTypeMap(Usuario.class, UsuarioDTO.class);
        userMap.addMapping(Usuario::getNombre,    UsuarioDTO::setNombre);
        userMap.addMapping(Usuario::getApellido1, UsuarioDTO::setApellido1);
        userMap.addMapping(Usuario::getApellido2, UsuarioDTO::setApellido2);
        userMap.addMapping(Usuario::getCorreo,     UsuarioDTO::setCorreo);
        userMap.addMapping(Usuario::getPasswd,     UsuarioDTO::setPasswd);
        userMap.addMappings(m -> m.using(dateToLocalDate)
                .map(Usuario::getFechaNac, UsuarioDTO::setFechaNac));
        userMap.addMapping(Usuario::getGenero,   UsuarioDTO::setGenero);
        userMap.addMapping(Usuario::getTelefono, UsuarioDTO::setTelefono);
        userMap.addMapping(Usuario::getAceptaPromociones, UsuarioDTO::setAceptaPromociones);
        userMap.addMapping(Usuario::getAceptaTerminos,     UsuarioDTO::setAceptaTerminos);
        userMap.addMapping(Usuario::getAceptaPrivacidad,   UsuarioDTO::setAceptaPrivacidad);
        userMap.addMapping(Usuario::getPreferencias,       UsuarioDTO::setPreferencias);

        // Usuario → UsuarioPersonalDataDTO
        TypeMap<Usuario, UsuarioPersonalDataDTO> personalMap = mapper.createTypeMap(Usuario.class, UsuarioPersonalDataDTO.class);
        personalMap.addMapping(Usuario::getNombre,     UsuarioPersonalDataDTO::setNombre);
        personalMap.addMapping(Usuario::getApellido1, UsuarioPersonalDataDTO::setApellido1);
        personalMap.addMapping(Usuario::getApellido2, UsuarioPersonalDataDTO::setApellido2);
        personalMap.addMapping(Usuario::getCorreo,     UsuarioPersonalDataDTO::setCorreo);
        personalMap.addMapping(Usuario::getTelefono,   UsuarioPersonalDataDTO::setTelefono);
        personalMap.addMappings(m -> m.using(dateToLocalDate)
                .map(Usuario::getFechaNac, UsuarioPersonalDataDTO::setFechaNac));
        personalMap.addMapping(Usuario::getGenero,     UsuarioPersonalDataDTO::setGenero);
        personalMap.addMapping(Usuario::getPuntos,     UsuarioPersonalDataDTO::setPuntos);
        personalMap.addMapping(Usuario::getPreferencias, UsuarioPersonalDataDTO::setPreferencias);

        // Desglose de direccionPostal
        personalMap.setPostConverter(ctx -> {
            Usuario src = ctx.getSource();
            UsuarioPersonalDataDTO dst = ctx.getDestination();
            String dir = src.getDireccionPostal();
            if (dir != null && !dir.trim().isEmpty()) {
                List<String> parts = Arrays.stream(dir.split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList());
                if (parts.size() > 0) {
                    String[] mainTokens = parts.get(0).split(" ");
                    if (mainTokens.length >= 2) {
                        dst.setNumero(parseInt(mainTokens[mainTokens.length-1]));
                        dst.setCalle(String.join(" ", Arrays.copyOf(mainTokens, mainTokens.length-1)));
                    } else {
                        dst.setCalle(parts.get(0));
                    }
                }
                if (parts.size() > 1) {
                    String[] subTokens = parts.get(1).split(" ");
                    if (subTokens.length >= 2) {
                        dst.setPiso(parseInt(subTokens[0]));
                        dst.setPuerta(subTokens[1]);
                    }
                }
                if (parts.size() > 2) dst.setCodigoPostal(parseInt(parts.get(2)));
                if (parts.size() > 3) dst.setLocalidad(parts.get(3));
                if (parts.size() > 4) {
                    Matcher m = Pattern.compile("^(.*)\\s*\\((.*)\\)$").matcher(parts.get(4));
                    if (m.find()) {
                        dst.setProvincia(m.group(1).trim());
                        dst.setPais(m.group(2).trim());
                    } else {
                        dst.setProvincia(parts.get(4));
                    }
                }
            }
            return dst;
        });
        

        // Usuario → EmpleadoDTO
        TypeMap<Usuario, EmpleadoDTO> empleadoMap = mapper.createTypeMap(Usuario.class, EmpleadoDTO.class);
        empleadoMap.addMappings(m -> m.using(dateToLocalDate).map(Usuario::getFechaNac, EmpleadoDTO::setFechaNac));
        empleadoMap.addMappings(m -> m.skip(EmpleadoDTO::setPassword)); // Nunca enviar la contraseña hasheada al frontend
        empleadoMap.setPostConverter(context -> {
            Usuario source = context.getSource();
            EmpleadoDTO destination = context.getDestination();
            String apellido2 = source.getApellido2() != null ? " " + source.getApellido2() : "";
            destination.setNombreCompleto((source.getNombre() + " " + source.getApellido1() + apellido2).trim());
            return destination;
        });

        // EmpleadoDTO -> Usuario
        TypeMap<EmpleadoDTO, Usuario> toUsuarioEntityMap = mapper.createTypeMap(EmpleadoDTO.class, Usuario.class);
        toUsuarioEntityMap.addMappings(m -> m.using(localDateToDate).map(EmpleadoDTO::getFechaNac, Usuario::setFechaNac));
        toUsuarioEntityMap.addMappings(m -> m.when(ctx -> ctx.getSource() != null && ((EmpleadoDTO)ctx.getSource()).getPassword() != null && !((EmpleadoDTO)ctx.getSource()).getPassword().isEmpty())
                                            .map(EmpleadoDTO::getPassword, Usuario::setPasswd));



        // DTO → Usuario (inverso)
        TypeMap<UsuarioPersonalDataDTO, Usuario> toEntity =
                mapper.createTypeMap(UsuarioPersonalDataDTO.class, Usuario.class);
        toEntity.addMapping(UsuarioPersonalDataDTO::getNombre,     Usuario::setNombre);
        toEntity.addMapping(UsuarioPersonalDataDTO::getApellido1, Usuario::setApellido1);
        toEntity.addMapping(UsuarioPersonalDataDTO::getApellido2, Usuario::setApellido2);
        toEntity.addMapping(UsuarioPersonalDataDTO::getTelefono,   Usuario::setTelefono);
        toEntity.addMappings(m -> m.using(localDateToDate)
                .map(UsuarioPersonalDataDTO::getFechaNac, Usuario::setFechaNac));
        toEntity.addMapping(UsuarioPersonalDataDTO::getGenero,      Usuario::setGenero);
        toEntity.addMapping(UsuarioPersonalDataDTO::getPreferencias, Usuario::setPreferencias);
        toEntity.setPostConverter(ctx -> {
            UsuarioPersonalDataDTO dto = ctx.getSource();
            Usuario usr = ctx.getDestination();
            StringBuilder sb = new StringBuilder();
            if (dto.getCalle() != null) sb.append(dto.getCalle());
            if (dto.getNumero() != null) sb.append(" ").append(dto.getNumero());
            sb.append(", ");
            if (dto.getPiso() != null) sb.append(dto.getPiso());
            if (dto.getPuerta() != null) sb.append(" ").append(dto.getPuerta());
            sb.append(", ");
            if (dto.getCodigoPostal() != null) sb.append(dto.getCodigoPostal());
            sb.append(", ");
            if (dto.getLocalidad() != null) sb.append(dto.getLocalidad());
            sb.append(", ");
            if (dto.getProvincia() != null) sb.append(dto.getProvincia());
            if (dto.getPais() != null) sb.append(" (").append(dto.getPais()).append(")");
            usr.setDireccionPostal(sb.toString());
            return usr;
        });

        return mapper;
    }

    private Integer parseInt(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}