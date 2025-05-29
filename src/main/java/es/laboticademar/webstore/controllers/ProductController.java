package es.laboticademar.webstore.controllers;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.services.interfaces.DestacadoService;
import es.laboticademar.webstore.services.interfaces.ProductService;


@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private DestacadoService destacadoService;

    @GetMapping("/{id}")
    public String goProduct(@PathVariable("id") BigDecimal id, Model model) {

        model.addAttribute("destacados", destacadoService.getAllDestacados());
        model.addAttribute("producto", productService.getProductoById(id));

        return "product/product";
    }

}