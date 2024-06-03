package com.project.sisvencal.models;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "producto")
public class Producto {

    // Identificador único del producto
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long idProducto;

    // Código del producto
    @Column(name = "codigo")
    private String codigo;

    // Nombre del producto
    @Column(name = "nombre")
    private String nombre;

    // Cantidad de unidades en stock
    @Column(name = "stock")
    private int stock;

    // Precio del producto
    @Column(name = "precio")
    private int precio;

    // Relación muchos a uno con la entidad Marca
    @ManyToOne
    @JoinColumn(name = "id_marca")
    private Marca marca;

    // Relación muchos a uno con la entidad Modelo
    @ManyToOne
    @JoinColumn(name = "id_modelo")
    private Modelo modelo;

    // Relación muchos a uno con la entidad Categoria
    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    // Relación muchos a uno con la entidad Genero
    @ManyToOne
    @JoinColumn(name = "id_genero")
    private Genero genero;

    // Relación muchos a uno con la entidad Size (tamaño máximo)
    @ManyToOne
    @JoinColumn(name = "max_size")
    private Size maxSize;

    // Relación muchos a uno con la entidad Size (tamaño mínimo)
    @ManyToOne
    @JoinColumn(name = "min_size")
    private Size minSize;

    // Descripción del producto
    @Column(name = "descripcion")
    private String descripcion;
    
    // Estado del producto
    @ManyToOne
    @JoinColumn(name = "estado")
    private Estado estado;

    // Constructor sin parámetros requerido por JPA
    public Producto() {
    }

    // Constructor utilizado para inicializar algunos campos al crear un producto
    public Producto(int precio, Marca marca, Categoria categoria, Genero genero, Size maxSize, Size minSize) {
        this.precio = precio;
        this.marca = marca;
        this.categoria = categoria;
        this.genero = genero;
        this.maxSize = maxSize;
        this.minSize = minSize;
    }

    public List<Producto> filtrarPrecio(List<Producto> productos, int precio) {
        // Lista que almacenará los productos que deben ser eliminados
        List<Producto> preEliminar = new ArrayList<>();

        // Itera sobre la lista de productos
        for (Producto producto : productos) {
            // Compara el precio del producto con el precio máximo permitido
            if (producto.getPrecio() <= precio) {
                // Si el precio es menor o igual al límite, se agrega a la lista de productos a mantener
                preEliminar.add(producto);
            }
        }

        // Retorna la lista de productos filtrada por precio
        return preEliminar;
    }

    public List<Producto> filtrarMarca(List<Producto> productos, Long marca) {
        List<Producto> preEliminar = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.marca.getIdMarca() == marca) {
                preEliminar.add(producto);
            }
        }

        return preEliminar;
    }

    public List<Producto> filtrarGenero(List<Producto> productos, Long genero) {
        List<Producto> preEliminar = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.genero.getIdGenero() == genero) {
                preEliminar.add(producto);
            }
        }

        return preEliminar;
    }

    public List<Producto> filtrarCategoria(List<Producto> productos, Long categoria) {
        List<Producto> preEliminar = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.categoria.getIdCategoria() == categoria) {
                preEliminar.add(producto);
            }
        }

        return preEliminar;
    }

    public List<Producto> filtrarMinSize(List<Producto> productos, Long size) {
        List<Producto> preEliminar = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.minSize.getIdSize() >= size) {
                preEliminar.add(producto);
            }
        }

        return preEliminar;
    }

    public List<Producto> filtrarMaxSize(List<Producto> productos, Long size) {
        List<Producto> preEliminar = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.maxSize.getIdSize() <= size) {
                preEliminar.add(producto);
            }
        }

        return preEliminar;
    }
}
