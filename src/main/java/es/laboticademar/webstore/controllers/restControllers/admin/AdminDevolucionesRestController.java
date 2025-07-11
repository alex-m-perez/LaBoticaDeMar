package es.laboticademar.webstore.controllers.restControllers.admin;

import es.laboticademar.webstore.dto.devolucion.DevolucionAdminDetalleDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionAdminResumenDTO;
import es.laboticademar.webstore.dto.devolucion.DevolucionKpisDTO;
import es.laboticademar.webstore.services.interfaces.DevolucionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequestMapping("/admin/api/devoluciones")
@RequiredArgsConstructor
public class AdminDevolucionesRestController {

    private final DevolucionService devolucionService;

    @GetMapping("/get_pagable_list")
    public Page<DevolucionAdminResumenDTO> getAllDevoluciones(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Float montoMin,
            @RequestParam(required = false) Float montoMax) {
        return devolucionService.findAllDevolucionesFiltered(page, size, clienteId, idUsuario, fechaInicio, fechaFin, montoMin, montoMax);
    }

    @GetMapping("/kpis")
    public DevolucionKpisDTO getDevolucionKpis(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Float montoMin,
            @RequestParam(required = false) Float montoMax) {
        return devolucionService.getDevolucionKpis(clienteId, idUsuario, fechaInicio, fechaFin, montoMin, montoMax);
    }
    
    @GetMapping("/{id}")
    public DevolucionAdminDetalleDTO getDevolucionDetalleAdmin(@PathVariable Long id) {
        return devolucionService.findDevolucionDetailsById(id);
    }
}