package es.laboticademar.webstore.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;


@Controller
public class InicioController {
	
	@Autowired
    private UsuarioService usuarioService;
	
	/*
	 * @GetMapping("/prueba") public String greeting(Model model) { List<Usuario>
	 * usuarios = inicioService.getAllUsers(); String prueba = "prueba"; for(Usuario
	 * usuario: usuarios) { System.out.println(usuario.getNombre()); }
	 * model.addAttribute("usuarios", usuarios); model.addAttribute("prueba",
	 * prueba); return "inicio"; }
	 */
	
	@RequestMapping(value = {"/prueba"})
	public String prueba(Map<String, Object> map, HttpServletRequest request) {
		try {
			List<Usuario> usuarios = usuarioService.getAllUsers();
			String prueba = "prueba";
			for(Usuario usuario: usuarios) {
				System.out.println(usuario.getNombre());
			}
			map.put("usuarios", usuarios);
			map.put("prueba", prueba);
			return "inicio";
		}catch(Exception e){
			e.printStackTrace();
			return "inicio";
		}
		
	}
}