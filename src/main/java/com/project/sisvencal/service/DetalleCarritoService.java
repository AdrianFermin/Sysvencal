package com.project.sisvencal.service;

import com.project.sisvencal.models.DetalleCarrito;
import java.util.List;

public interface DetalleCarritoService {
    
    public List<DetalleCarrito> listarDetalleCarritos();
    
    public void guardar(DetalleCarrito detalleCarrito);
    
    public void guardarTodos(List<DetalleCarrito> carrito);
    
    public void eliminar(DetalleCarrito detalleCarrito);
    
    public void eliminarTodos(List<DetalleCarrito> carrito);
    
    public DetalleCarrito encontrarDetalleCarrito(DetalleCarrito detalleCarrito);
    
}
