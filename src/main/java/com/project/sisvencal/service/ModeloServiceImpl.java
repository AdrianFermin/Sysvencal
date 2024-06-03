package com.project.sisvencal.service;

import com.project.sisvencal.datos.ModeloDao;
import com.project.sisvencal.models.Modelo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModeloServiceImpl implements ModeloService{

    @Autowired
    private ModeloDao modeloDao;
    
    @Override
    public List<Modelo> listarModelos() {
        return (List<Modelo>) modeloDao.findAll();
    }

    @Override
    public void guardarModelo(Modelo modelo) {
        modeloDao.save(modelo);
    }

    @Override
    public Modelo encontrarModelo(Modelo modelo) {
        return modeloDao.findById(modelo.getIdModelo()).orElse(null);
    }
    
}
