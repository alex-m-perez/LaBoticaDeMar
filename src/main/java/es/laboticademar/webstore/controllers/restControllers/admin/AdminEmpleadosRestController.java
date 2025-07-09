package es.laboticademar.webstore.controllers.restControllers.admin;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.laboticademar.webstore.dto.usuario.EmpleadoDTO;
import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.services.interfaces.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/api/empleados")
@RequiredArgsConstructor
public class AdminEmpleadosRestController {

    private final UsuarioService usuarioService;
    private final ModelMapper modelMapper;

    @GetMapping("/get_pagable_list")
    public Page<EmpleadoDTO> list(Pageable pageable) {
        Page<Usuario> usuarioPage = usuarioService.findEmpleados(pageable);

        return usuarioPage.map(usuario -> modelMapper.map(usuario, EmpleadoDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> getEmpleadoById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con id: " + id));
        return ResponseEntity.ok(modelMapper.map(usuario, EmpleadoDTO.class));
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> toggleActivationStatus(@PathVariable Long id, @RequestParam boolean activar) {
        usuarioService.setActivo(id, activar);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> updateEmpleado(@PathVariable Long id, @RequestBody EmpleadoDTO empleadoDTO) {
        Usuario actualizado = usuarioService.updateEmpleado(id, empleadoDTO);
        return ResponseEntity.ok(modelMapper.map(actualizado, EmpleadoDTO.class));
    }
}