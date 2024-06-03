package com.project.sisvencal.datos;

import com.project.sisvencal.models.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneroDao extends JpaRepository<Genero, Long>{
    
}
