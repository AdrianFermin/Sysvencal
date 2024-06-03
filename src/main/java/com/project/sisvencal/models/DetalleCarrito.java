package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_carrito")
public class DetalleCarrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_carrito")
    private Long idDetalleCarrito;
    
    @Column(name = "cantidad")
    private int cantidad;
    
    @ManyToOne
    @JoinColumn(name = "carrito")
    private Carrito carrito;
    
    @ManyToOne
    @JoinColumn(name = "producto")
    private Producto producto;
    
}
