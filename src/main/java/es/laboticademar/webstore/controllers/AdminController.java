package es.laboticademar.webstore.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.services.DestacadoService;
import es.laboticademar.webstore.services.UsuarioService;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/home")
    public String goSalesHome() {
        return "admin/home";
    }

    @GetMapping("/ventas")
    public String goGeneralSalesDashboard() {
        return "admin/general_dashboard";
    }
}