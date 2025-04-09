package es.laboticademar.webstore.controllers;

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
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request,
                                                                 HttpServletResponse response) {
        try {
            // Autentica al usuario y genera el JWT.
            AuthenticationResponse authResponse = authenticationService.authenticate(request);

            // Crear la cookie con el JWT.
            Cookie jwtCookie = new Cookie("jwtToken", authResponse.getToken());
            jwtCookie.setHttpOnly(true); // Evita que el token sea accesible desde JavaScript.
            jwtCookie.setSecure(true);   // Recomendable: solo se enviará en conexiones HTTPS.
            jwtCookie.setPath("/");      // La cookie estará disponible para toda la aplicación.
            jwtCookie.setMaxAge(3600);   // Opcional: establecer tiempo de expiración en segundos (ej., 1 hora).

            // Agregar la cookie a la respuesta.
            response.addCookie(jwtCookie);

            return ResponseEntity.ok(authResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    
}
