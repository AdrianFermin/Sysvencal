package com.project.sisvencal.service;

import com.project.sisvencal.models.Usuario;
import java.util.List;

public interface UsuarioService {
    
    public List<Usuario> listarUsuarios();
    
    public void guardar(Usuario usuario);
    
    public void eliminar(Usuario usuario);
    
    public Usuario encontrarUsuario(Usuario usuario);
    
    public Usuario encontrarPorEmail(String email);
}
