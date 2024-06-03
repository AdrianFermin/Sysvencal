package com.project.sisvencal.datos;

import com.project.sisvencal.models.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeloDao extends JpaRepository<Modelo, Long>{
    
}
