package es.laboticademar.webstore.controllers.restControllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.entities.Laboratorio;
import es.laboticademar.webstore.services.interfaces.LaboratorioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/laboratorio")
@RequiredArgsConstructor
public class LaboratorioApiController {

    private final LaboratorioService labService;

    @GetMapping("/get_labs")
    public List<Laboratorio> all(){
        return labService.findAll();
    }

}