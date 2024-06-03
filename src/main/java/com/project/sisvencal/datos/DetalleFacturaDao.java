package com.project.sisvencal.datos;

import com.project.sisvencal.models.DetalleFactura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleFacturaDao extends JpaRepository<DetalleFactura, Long>{
    
}
