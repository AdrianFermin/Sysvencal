package com.project.sisvencal.datos;

import com.project.sisvencal.models.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoDao extends JpaRepository<Producto, Long> {

    
    public List<Producto> findAllByNombreContaining(String nombre);

    
    public Producto findByCodigoContaining(String codigo);
}
