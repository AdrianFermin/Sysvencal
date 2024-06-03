package com.project.sisvencal.service;

import com.project.sisvencal.datos.CarritoDao;
import com.project.sisvencal.models.Carrito;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoServiceImpl implements CarritoService{

    @Autowired
    private CarritoDao carritoDao;
    
    @Override
    public List<Carrito> listarCarritos() {
        return (List<Carrito>) carritoDao.findAll();
    }

    @Override
    public void guardar(Carrito carrito) {
        carritoDao.save(carrito);
    }

    @Override
    public void eliminar(Carrito carrito) {
        carritoDao.delete(carrito);
    }
    
    @Override
    public Carrito encontrarCarrito(Carrito carrito) {
        return carritoDao.findById(carrito.getIdCarrito()).orElse(null);
    }
    
}
