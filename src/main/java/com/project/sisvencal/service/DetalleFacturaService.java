package com.project.sisvencal.service;

import com.project.sisvencal.models.DetalleFactura;
import java.util.List;

public interface DetalleFacturaService {
    
    public List<DetalleFactura> listarDetalleFacturas();
    
    public void guardar(DetalleFactura detalleFactura);
    
    public void guardarTodos(List<DetalleFactura> factura);
    
    public void eliminar(DetalleFactura detalleFactura);
    
    public DetalleFactura encontrarDetalleFactura(DetalleFactura detalleFactura);
}
