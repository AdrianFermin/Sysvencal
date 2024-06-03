package com.project.sisvencal.service;

import com.project.sisvencal.models.Marca;
import java.util.List;

public interface MarcaService {
    
    public List<Marca> listarMarcas();
    
    public void guardarMarca(Marca marca);
    
    public Marca encontrarMarca(Marca marca);
}
