package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "color_producto")
public class ColorProducto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_color_producto")
    private Long idColorProducto;
    
    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name = "id_color")
    private Color color;
    
}
