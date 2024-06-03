package com.project.sisvencal.service;

import com.project.sisvencal.datos.ProductoDao;
import com.project.sisvencal.models.Producto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoDao productoDao;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarProductos() {
        return (List<Producto>) productoDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Producto producto) {
        productoDao.save(producto);
    }

    @Override
    @Transactional
    public void eliminar(Producto producto) {
        productoDao.delete(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto encontrarProducto(Producto producto) {
        return productoDao.findById(producto.getIdProducto()).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorNombre(String nombre) {
        return productoDao.findAllByNombreContaining(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto encontrarPorCodigo(String codigo) {
        return productoDao.findByCodigoContaining(codigo);
    }

    @Override
    @Transactional
    public void guardarTodos(List<Producto> productos) {
        productoDao.saveAll(productos);
    }

}
