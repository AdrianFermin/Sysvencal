package com.project.sisvencal.datos;

import com.project.sisvencal.models.Marca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarcaDao extends JpaRepository<Marca, Long>{
    
}
