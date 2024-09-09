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
    public ResponseEntity<AuthenticationResponse> register (@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));

        /* Boolean isAuthenticated = authenticationService.authenticate(request).getToken() != null;
        if (isAuthenticated) {
            // Devolver respuesta con éxito
            AuthenticationResponse response = new AuthenticationResponse(true); // O el objeto que uses
            return ResponseEntity.ok(response);
        } else {
            // Devolver error 401 si la autenticación falla
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthenticationResponse(false));
        } */
    }

}
