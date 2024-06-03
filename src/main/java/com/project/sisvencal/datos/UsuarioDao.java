package com.project.sisvencal.datos;

import com.project.sisvencal.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDao extends JpaRepository<Usuario, Long>{
    
    public Usuario findByEmail(String email);
}
