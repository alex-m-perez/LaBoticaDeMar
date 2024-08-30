package es.laboticademar.webstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.services.UsuarioService;


@Controller
@RequestMapping("/home")
public class InicioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";  // Nombre del JSP de login, sin la extensión .jsp
    }

    @GetMapping("/prueba")
    public String greeting(Model model) {
        List<Usuario> usuarios = usuarioService.getAllUsers();
        String prueba = "prueba";
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getNombre());
        }
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("prueba", prueba);
        return "login";  // Nombre del JSP de inicio, sin la extensión .jsp
    }
}