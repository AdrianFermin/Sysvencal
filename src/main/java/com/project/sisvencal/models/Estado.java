package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "estado")
public class Estado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long idEstado;
    
    @Column(name = "estado")
    private String estado;
}
