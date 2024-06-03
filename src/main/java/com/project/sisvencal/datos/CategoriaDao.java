package com.project.sisvencal.datos;

import com.project.sisvencal.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaDao extends JpaRepository<Categoria, Long>{
    
}
