package es.laboticademar.webstore.controllers.restControllers.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.usuario.TopCompradorDTO;
import es.laboticademar.webstore.dto.usuario.TopDevolucionesDTO;
import es.laboticademar.webstore.dto.usuario.TopGastadorDTO;
import es.laboticademar.webstore.dto.usuario.UsuarioBusquedaDTO;
import es.laboticademar.webstore.dto.usuario.UsuarioDetalleDTO;
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

   // Endpoint para la lista principal de clientes
    @GetMapping("/get_pagable_list")
    public Page<UsuarioDetalleDTO> getAllClientes(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return usuarioService.findAllClientes(pageable);
    }

    // Endpoint para los detalles de un solo cliente (modal)
    @GetMapping("/{id}")
    public UsuarioDetalleDTO getUsuarioDetalle(@PathVariable Long id) {
        return usuarioService.findUsuarioDetailsById(id);
    }

    // --- Endpoints para KPIs Paginados ---

    @GetMapping("/kpis/top-compradores")
    public Page<TopCompradorDTO> getTopCompradores(
            @PageableDefault(size = 5) Pageable pageable) {
        return usuarioService.findTopCompradores(pageable);
    }

    @GetMapping("/kpis/top-gastadores")
    public Page<TopGastadorDTO> getTopGastadores(
            @PageableDefault(size = 5) Pageable pageable) {
        return usuarioService.findTopGastadores(pageable);
    }

    @GetMapping("/kpis/top-devoluciones")
    public Page<TopDevolucionesDTO> getTopDevoluciones(
            @PageableDefault(size = 5) Pageable pageable) {
        return usuarioService.findTopDevoluciones(pageable);
    }
}