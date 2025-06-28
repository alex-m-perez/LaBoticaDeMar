package es.laboticademar.webstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("")
    public String accessProfile() {
        return "perfil";
    }
}
