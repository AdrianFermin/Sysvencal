package com.project.sisvencal.service;

import com.project.sisvencal.models.Carrito;
import java.util.List;

public interface CarritoService {
    
    public List<Carrito> listarCarritos();
    
    public void guardar(Carrito carrito);
    
    public void eliminar(Carrito carrito);
    
    public Carrito encontrarCarrito(Carrito carrito);
}
