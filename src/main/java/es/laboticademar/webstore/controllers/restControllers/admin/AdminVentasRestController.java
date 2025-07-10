package es.laboticademar.webstore.controllers.restControllers.admin;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.venta.VentaAdminResumenDTO;
import es.laboticademar.webstore.dto.venta.VentaDTO;
import es.laboticademar.webstore.dto.venta.VentaEstadoDTO;
import es.laboticademar.webstore.dto.venta.VentaKpisDTO;
import es.laboticademar.webstore.services.interfaces.VentaService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/api/ventas")
@RequiredArgsConstructor
public class AdminVentasRestController {

    private final VentaService ventaService;
    
    // En AdminVentasRestController.java
    @GetMapping("/get_pagable_list")
    public Page<VentaAdminResumenDTO> getAllVentas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Float precioMin,
            @RequestParam(required = false) Float precioMax,
            @RequestParam(required = false) Integer estadoId,
            @RequestParam(required = false) Integer numProductos) {

        return ventaService.findAllVentasFiltered(
                page, size, clienteId, idUsuario, fechaInicio, fechaFin, precioMin, precioMax, estadoId, numProductos);
    }

    @GetMapping("/kpis")
    public VentaKpisDTO getVentasKpis(
            @RequestParam(required = false) Long clienteId,
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(required = false) Float precioMin,
            @RequestParam(required = false) Float precioMax,
            @RequestParam(required = false) Integer numProductos) {

        return ventaService.getVentaKpis(clienteId, idUsuario, fechaInicio, fechaFin, precioMin, precioMax, numProductos);
    }

    @GetMapping("/estados")
    public List<VentaEstadoDTO> getAllVentaEstados() {
        return ventaService.getAllVentaEstados();
    }

    // 2. Endpoint genérico con variable: se declara DESPUÉS
    @GetMapping("/{id}")
    public VentaDTO getVentaDetalleAdmin(@PathVariable Long id) {
        return ventaService.findVentaDetailsById(id);
    }

    @PutMapping("/{id}/estado")
    public void updateVentaStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        Integer nuevoEstadoId = body.get("nuevoEstadoId");
        ventaService.updateVentaStatus(id, nuevoEstadoId);
    }

    
    

}