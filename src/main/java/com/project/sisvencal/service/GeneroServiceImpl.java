package com.project.sisvencal.service;

import com.project.sisvencal.datos.GeneroDao;
import com.project.sisvencal.models.Genero;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneroServiceImpl implements GeneroService{
    
    @Autowired
    private GeneroDao generoDao;

    @Override
    public List<Genero> listarGeneros() {
        return (List<Genero>) generoDao.findAll();
    }

    @Override
    public void guardarGenero(Genero genero) {
        generoDao.save(genero);
    }

    @Override
    public Genero encontrarGenero(Genero genero) {
        return generoDao.findById(genero.getIdGenero()).orElse(null);
    }
    
}
