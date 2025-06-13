package es.laboticademar.webstore.controllers.restControllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.CategoriaDTO;
import es.laboticademar.webstore.dto.FamiliaDTO;
import es.laboticademar.webstore.services.interfaces.CategoriaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categoria")
@RequiredArgsConstructor
public class CategoriaApiController {

    private final CategoriaService categoriaService;

    @GetMapping(path = "/get_categorias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoriaDTO> all() {
        return categoriaService.findAll()
            .stream()
            .map(cat -> new CategoriaDTO(
                cat.getId(),
                cat.getNombre(),
                new FamiliaDTO(
                  cat.getFamilia().getId(),
                  cat.getFamilia().getNombre()
                )
            ))
            .collect(Collectors.toList());
    }

}