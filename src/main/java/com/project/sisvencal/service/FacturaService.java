package com.project.sisvencal.service;

import com.project.sisvencal.models.Factura;
import java.util.List;

public interface FacturaService {
    
    public List<Factura> listarFacturas();
    
    public void guardar(Factura factura);
    
    public void eliminar(Factura factura);
    
    public Factura encontrarFactura(Factura factura);
    
}
