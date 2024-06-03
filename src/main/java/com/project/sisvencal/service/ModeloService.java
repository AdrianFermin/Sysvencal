package com.project.sisvencal.service;

import com.project.sisvencal.models.Modelo;
import java.util.List;

public interface ModeloService {
    
    public List<Modelo> listarModelos();
    
    public void guardarModelo(Modelo modelo);
    
    public Modelo encontrarModelo(Modelo modelo);
}
