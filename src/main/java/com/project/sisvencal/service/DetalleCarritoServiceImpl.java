package com.project.sisvencal.service;

import com.project.sisvencal.datos.DetalleCarritoDao;
import com.project.sisvencal.models.DetalleCarrito;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleCarritoServiceImpl implements DetalleCarritoService{

    @Autowired
    private DetalleCarritoDao detalleCarritoDao;
    
    
    @Override
    public List<DetalleCarrito> listarDetalleCarritos() {
        return (List<DetalleCarrito>) detalleCarritoDao.findAll();
    }

    @Override
    public void guardar(DetalleCarrito detalleCarrito) {
        detalleCarritoDao.save(detalleCarrito);
    }

    @Override
    public void eliminar(DetalleCarrito detalleCarrito) {
        detalleCarritoDao.delete(detalleCarrito);
    }

    @Override
    public DetalleCarrito encontrarDetalleCarrito(DetalleCarrito detalleCarrito) {
        return detalleCarritoDao.findById(detalleCarrito.getIdDetalleCarrito()).orElse(null);
    }

    @Override
    public void guardarTodos(List<DetalleCarrito> carrito) {
        detalleCarritoDao.saveAll(carrito);
    }

    @Override
    public void eliminarTodos(List<DetalleCarrito> carrito) {
        detalleCarritoDao.deleteAll(carrito);
    }

    
}
