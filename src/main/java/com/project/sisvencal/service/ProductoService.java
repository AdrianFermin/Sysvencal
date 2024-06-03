package com.project.sisvencal.service;

import com.project.sisvencal.models.Producto;
import java.util.List;

import java.util.List;

public interface ProductoService {

    public List<Producto> listarProductos();

    public void guardar(Producto producto);

    public void guardarTodos(List<Producto> productos);

    public void eliminar(Producto producto);

    public Producto encontrarProducto(Producto producto);

    public Producto encontrarPorCodigo(String codigo);

    public List<Producto> listarPorNombre(String nombre);
}
