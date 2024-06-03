package com.project.sisvencal.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "detalle_factura")
public class DetalleFactura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_factura")
    private Long idDetalleFactura;

    @Column(name = "cantidad")
    private int cantidad;
    
    @Column(name = "precio")
    private int precio;
    
    @ManyToOne
    @JoinColumn(name = "size")
    private Size size;

    @ManyToOne
    @JoinColumn(name = "id_factura")
    private Factura factura;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

}
