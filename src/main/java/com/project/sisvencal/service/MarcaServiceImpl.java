package com.project.sisvencal.service;

import com.project.sisvencal.datos.MarcaDao;
import com.project.sisvencal.models.Marca;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarcaServiceImpl implements MarcaService{
    
    @Autowired
    private MarcaDao marcaDao;

    @Override
    public List<Marca> listarMarcas() {
        return (List<Marca>) marcaDao.findAll();
    }

    @Override
    public void guardarMarca(Marca marca) {
        marcaDao.save(marca);
    }

    @Override
    public Marca encontrarMarca(Marca marca) {
        return marcaDao.findById(marca.getIdMarca()).orElse(null);
    }
}
