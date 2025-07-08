package es.laboticademar.webstore.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Destacado;
import es.laboticademar.webstore.entities.Producto;
import es.laboticademar.webstore.repositories.DestacadoDAO;
import es.laboticademar.webstore.services.interfaces.DestacadoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DestacadoServiceImpl implements DestacadoService{
    
    private final DestacadoDAO destacadoDAO;

    @Override
    public Destacado saveOrUpdateDestacado(Destacado destacado) {
        return destacadoDAO.save(destacado);
    }

    @Override
    public Destacado getDestacadoById(Integer id) {
        return destacadoDAO.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado con ID: " + id));
    }

    @Override
    public List<Producto> getAllDestacados() {
        List<Destacado> destacados = destacadoDAO.findAll();
        List<Producto> result = new ArrayList<>();
        for (Destacado destacado : destacados) {
            result.add(destacado.getProducto());
        }
        return result;
    }
    

    @Override
    public void deleteDestacadoById(Integer id) {
        destacadoDAO.deleteById(id);
    }
}
