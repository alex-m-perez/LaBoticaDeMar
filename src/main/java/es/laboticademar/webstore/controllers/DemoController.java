package es.laboticademar.webstore.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/demo")
public class DemoController{

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return ResponseEntity.ok("Esto va que chuta, a tope jefe de equipo");
    }
}
