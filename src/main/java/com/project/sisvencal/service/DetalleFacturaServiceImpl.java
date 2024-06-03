package com.project.sisvencal.service;

import com.project.sisvencal.datos.DetalleFacturaDao;
import com.project.sisvencal.models.DetalleFactura;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleFacturaServiceImpl implements DetalleFacturaService{

    @Autowired
    private DetalleFacturaDao detalleFacturaDao;
    
    @Override
    public List<DetalleFactura> listarDetalleFacturas() {
        return (List<DetalleFactura>) detalleFacturaDao.findAll();
    }

    @Override
    public void guardar(DetalleFactura detalleFactura) {
        detalleFacturaDao.save(detalleFactura);
    }

    @Override
    public void eliminar(DetalleFactura detalleFactura) {
        detalleFacturaDao.delete(detalleFactura);
    }

    @Override
    public DetalleFactura encontrarDetalleFactura(DetalleFactura detalleFactura) {
        return detalleFacturaDao.findById(detalleFactura.getIdDetalleFactura()).orElse(null);
    }

    @Override
    public void guardarTodos(List<DetalleFactura> factura) {
        detalleFacturaDao.saveAll(factura);
    }
    
}
