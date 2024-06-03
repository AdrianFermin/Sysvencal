package com.project.sisvencal.service;

import com.project.sisvencal.models.Imagen;
import java.util.List;

public interface ImagenService {
    
    public List<Imagen> listarImagenes();
    
    public void guardarImagen(Imagen imagen);
    
    public void eliminarImagen(Imagen imagen);

}
