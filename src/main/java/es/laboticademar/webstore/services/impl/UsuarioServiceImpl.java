package es.laboticademar.webstore.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.laboticademar.webstore.entities.Usuario;
import es.laboticademar.webstore.repositories.UsuarioDAO;
import es.laboticademar.webstore.services.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {

	@Autowired
    private UsuarioDAO usuarioRDAO;

    @Override
    public List<Usuario> getAllUsers() {
        List<Usuario> usuarios = usuarioRDAO.findAll();
        return usuarios;
    }

    @Override
    public List<Usuario> getAllByCorreo() {
        List<Usuario> usuarios = usuarioRDAO.getAllByCorreo();
        return usuarios;
    }
}

