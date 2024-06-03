package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "imagen")
public class Imagen {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_imagen")
    private Long idImagen;
    
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
    
    @Column(name = "imagen")
    private String imagen;
}
