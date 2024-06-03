package com.project.sisvencal.datos;

import com.project.sisvencal.models.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoDao extends JpaRepository<Carrito, Long>{
    
}
