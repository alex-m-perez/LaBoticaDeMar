package es.laboticademar.webstore.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.services.DestacadoService;
import es.laboticademar.webstore.services.ProductService;
import es.laboticademar.webstore.services.UsuarioService;


@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private DestacadoService destacadoService;

    @GetMapping("/{id}")
    public String goProduct(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("destacados", destacadoService.getAllDestacados());
        model.addAttribute("producto", productService.getProductoById(id));

        return "product/product";
    }
    
}