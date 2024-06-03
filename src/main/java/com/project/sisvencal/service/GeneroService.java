package com.project.sisvencal.service;

import com.project.sisvencal.models.Genero;
import java.util.List;

public interface GeneroService {
    
    public List<Genero> listarGeneros();
    
    public void guardarGenero(Genero genero);
    
    public Genero encontrarGenero(Genero genero);
}
