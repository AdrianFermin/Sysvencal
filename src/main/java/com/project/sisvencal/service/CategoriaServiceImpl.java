package com.project.sisvencal.service;

import com.project.sisvencal.datos.CategoriaDao;
import com.project.sisvencal.models.Categoria;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoriaServiceImpl implements CategoriaService{
    
    @Autowired
    private CategoriaDao categoriaDao;

    @Override
    public List<Categoria> listarCategorias() {
        return (List<Categoria>) categoriaDao.findAll();
    }

    @Override
    public void guardarCategoria(Categoria categoria) {
        categoriaDao.save(categoria);
    }

    @Override
    public Categoria encontrarCategoria(Categoria categoria) {
        return categoriaDao.findById(categoria.getIdCategoria()).orElse(null);
    }
    
}
