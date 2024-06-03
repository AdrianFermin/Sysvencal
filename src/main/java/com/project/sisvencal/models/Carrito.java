package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "carrito")
public class Carrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_carrito")
    private Long idCarrito;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
}
