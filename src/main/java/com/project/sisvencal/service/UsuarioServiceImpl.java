package com.project.sisvencal.service;

import com.project.sisvencal.datos.UsuarioDao;
import com.project.sisvencal.models.Usuario;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService{
    
    @Autowired
    private UsuarioDao usuarioDao;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return (List<Usuario>) usuarioDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Usuario usuario) {
        usuarioDao.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Usuario usuario) {
        usuarioDao.delete(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarUsuario(Usuario usuario) {
        return usuarioDao.findById(usuario.getIdUsuario()).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarPorEmail(String email) {
        var usuario = usuarioDao.findByEmail(email);
        return usuario;
    }
    
}
