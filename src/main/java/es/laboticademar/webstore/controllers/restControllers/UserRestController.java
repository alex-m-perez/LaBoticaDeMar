package es.laboticademar.webstore.controllers.restControllers;

import java.security.Principal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.UsuarioPersonalDataDTO;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UsuarioService usuarioService;

    @GetMapping(path = "/data")
    public UsuarioPersonalDataDTO getPeronsalData(Principal principal) {
        return usuarioService.getUserPersonalData(principal);
    }

    @PostMapping(path = "/update_data")
    public ResponseEntity<Void> updatePersonalData(Principal principal, @RequestBody UsuarioPersonalDataDTO formData) {
        if (usuarioService.updatePersonalData(principal, formData))  return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().body(null);
    }

}