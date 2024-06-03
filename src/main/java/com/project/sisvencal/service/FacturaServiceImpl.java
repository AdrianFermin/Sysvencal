package com.project.sisvencal.service;

import com.project.sisvencal.datos.FacturaDao;
import com.project.sisvencal.models.Factura;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacturaServiceImpl implements FacturaService{
    
    @Autowired
    private FacturaDao facturaDao;

    @Override
    public List<Factura> listarFacturas() {
        return (List<Factura>) facturaDao.findAll();
    }

    @Override
    public void guardar(Factura factura) {
        facturaDao.save(factura);
    }

    @Override
    public void eliminar(Factura factura) {
        facturaDao.delete(factura);
    }

    @Override
    public Factura encontrarFactura(Factura factura) {
        return facturaDao.findById(factura.getIdFactura()).orElse(null);
    }
    
}
