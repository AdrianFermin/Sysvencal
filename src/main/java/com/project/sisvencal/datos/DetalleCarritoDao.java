package com.project.sisvencal.datos;

import com.project.sisvencal.models.DetalleCarrito;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleCarritoDao extends JpaRepository<DetalleCarrito, Long>{
    
}
