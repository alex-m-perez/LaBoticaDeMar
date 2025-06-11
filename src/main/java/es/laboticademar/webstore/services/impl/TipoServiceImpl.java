package es.laboticademar.webstore.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.TipoProducto;
import es.laboticademar.webstore.repositories.TipoDAO;
import es.laboticademar.webstore.services.interfaces.TipoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoServiceImpl implements TipoService {

    private final TipoDAO tipoDAO;

    @Override
    public Optional<TipoProducto> findByNombre(String name) {
        return tipoDAO.findByNombre(name);
    }
}
