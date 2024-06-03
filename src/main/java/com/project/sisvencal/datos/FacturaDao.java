package com.project.sisvencal.datos;

import com.project.sisvencal.models.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaDao extends JpaRepository<Factura, Long>{
    
}
