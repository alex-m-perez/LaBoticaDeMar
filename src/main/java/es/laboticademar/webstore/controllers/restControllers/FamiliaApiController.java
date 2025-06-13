package es.laboticademar.webstore.controllers.restControllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.entities.Familia;
import es.laboticademar.webstore.services.interfaces.FamiliaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/familia")
@RequiredArgsConstructor
public class FamiliaApiController {

    private final FamiliaService familiaService;

    @GetMapping("/get_familias")
    public List<Familia> all(){
        return familiaService.findAll();
    }


}