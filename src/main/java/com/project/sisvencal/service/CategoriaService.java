package com.project.sisvencal.service;

import com.project.sisvencal.models.Categoria;
import java.util.List;

public interface CategoriaService {
    
    public List<Categoria> listarCategorias();
    
    public void guardarCategoria(Categoria categoria);
    
    public Categoria encontrarCategoria(Categoria categoria);
}
