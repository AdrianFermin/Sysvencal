package com.project.sisvencal.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "factura")
public class Factura {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long idFactura;
    
    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;
    
    @Column(name = "fecha")
    private String fecha;
    
    @Column(name = "costo")
    private Long costo;
    
    @Column(name = "itbis")
    private Long itbis;
    
    @Column(name = "metodo")
    private String metodo;
    
}
