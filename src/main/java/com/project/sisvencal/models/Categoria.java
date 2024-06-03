package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categoria")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;
    
    @ManyToOne
    @JoinColumn(name = "id_genero")
    private Genero genero;
    
    @Column(name = "categoria")
    private String nombre;
}
