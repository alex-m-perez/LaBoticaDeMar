package es.laboticademar.webstore.controllers.restControllers.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.security.auth.AuthenticationRequest;
import es.laboticademar.webstore.security.auth.AuthenticationResponse;
import es.laboticademar.webstore.security.auth.RegisterRequest;
import es.laboticademar.webstore.services.impl.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/register_empleado")
    public ResponseEntity<AuthenticationResponse> registerEmpleado(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.registerEmployee(request));
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request,
                                                                 HttpServletResponse response) {
        try {
            // Autentica y genera el JWT.
            AuthenticationResponse authResponse = authenticationService.authenticate(request);
            
            // Crear la cookie con el token.
            Cookie jwtCookie = new Cookie("jwtToken", authResponse.getToken());
            jwtCookie.setHttpOnly(true); // Evita acceso desde JavaScript.
            jwtCookie.setSecure(false);   // Solo se envía vía HTTPS.
            jwtCookie.setPath("/");      // Disponible en toda la aplicación.
            jwtCookie.setMaxAge(3600);   // 1 hora (ajustable según necesidades).
            
            // Agregar la cookie a la respuesta.
            response.addCookie(jwtCookie);
            
            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);   // si usas HTTPS
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        
        return ResponseEntity.ok().build();
    }
}