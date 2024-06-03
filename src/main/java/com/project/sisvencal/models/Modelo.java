package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "modelo")
public class Modelo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long idModelo;
    
    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;
    
    @Column(name = "modelo")
    private String nombre;
}
