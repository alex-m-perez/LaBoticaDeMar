package es.laboticademar.webstore.controllers.restControllers.admin;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.usuario.UsuarioBusquedaDTO;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/api/usuarios")
@RequiredArgsConstructor
public class AdminUsuarioRestController {

    private final UsuarioService usuarioService;
    
    @GetMapping("/search_names")
    // El tipo de retorno ahora es una lista de UsuarioBusquedaDTO
    public List<UsuarioBusquedaDTO> searchUserNames(@RequestParam("q") String query) {
        return usuarioService.findNombresCompletosContaining(query);
   }
}