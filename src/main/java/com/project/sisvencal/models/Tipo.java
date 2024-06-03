package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tipo")
public class Tipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Long idTipo;
    
    @Column(name = "tipo")
    private String tipo;
    
    
}
