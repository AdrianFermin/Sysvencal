package com.project.sisvencal.service;

import com.project.sisvencal.datos.ImagenDao;
import com.project.sisvencal.models.Imagen;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImagenServiceImpl implements ImagenService{
    
    @Autowired
    private ImagenDao imagenDao;

    @Override
    public List<Imagen> listarImagenes() {
        return (List<Imagen>) imagenDao.findAll();
    }

    @Override
    public void guardarImagen(Imagen imagen) {
        imagenDao.save(imagen);
    }

    @Override
    public void eliminarImagen(Imagen imagen) {
        imagenDao.delete(imagen);
    }
    
}
