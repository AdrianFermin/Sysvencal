package com.project.sisvencal.datos;

import com.project.sisvencal.models.Imagen;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImagenDao extends JpaRepository<Imagen, Long>{
    
    
}
