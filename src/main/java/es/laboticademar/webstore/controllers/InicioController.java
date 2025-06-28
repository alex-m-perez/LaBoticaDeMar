package es.laboticademar.webstore.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import es.laboticademar.webstore.enumerations.PreferenciaEnum;
import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.LaboratorioService;
import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("")
@RequiredArgsConstructor
public class InicioController {

    private final DestacadoService destacadoService;
    private final LaboratorioService laboratorioService;
    private final ObjectMapper objectMapper;
    
    @GetMapping("/")
    public String goWelcomePage(Model model, Principal principal) {
        String test = "";
        try {
            test = objectMapper.writeValueAsString(laboratorioService.getLaboratoriosAgrupadosPorLetra());
        } catch (JsonProcessingException e) {
            // Log the exception for debugging purposes
            // You can use a logger like SLF4J
            // log.error("Error serializing laboratorios to JSON", e);
            // Optionally, you can add an error message to the model
            model.addAttribute("jsonError", "Could not load laboratory data.");
        }
        model.addAttribute("destacados", destacadoService.getAllDestacados());
        return "main/welcome";
    }

    @GetMapping("/login")
    public String goLoginPage() {
        return "main/login";
    }

    @GetMapping("/register")
    public String goRegisterPage(Model model) {
        model.addAttribute("preferenciasEnumList", PreferenciaEnum.values());
        return "main/registro";
    }
}