package com.project.sisvencal.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.project.sisvencal.models.*;
import com.project.sisvencal.service.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class MainController {

    //Estas son las declaraciones de las variables que necesito para que todo en el controlador funcione
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private SizeService sizeService;

    @Autowired
    private MarcaService marcaService;

    @Autowired
    private ModeloService modeloService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ImagenService imagenService;

    @Autowired
    private DetalleCarritoService detalleCarritoService;

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private GeneroService generoService;

    @Autowired
    private FacturaService facturaService;

    @Autowired
    private DetalleFacturaService detalleFacturaService;

    @Autowired
    private PaypalService paypalService;

    public static final String SUCCESS_URL = "payment/success";
    public static final String CANCEL_URL = "payment/cancel";

    List<DetalleCarrito> carrito = new ArrayList<>();

    List<DetalleCarrito> carrito2 = new ArrayList<>();

    List<Producto> fakeCarrito = new ArrayList<>();

    Carrito carritoActive = null;

    Usuario usuarioLogued = null;

    String mensaje = null;

    int bienvenida = 0;

    int total = 0;

    double itbisTotal = 0;

    double itbis = 0;

    String errorMessage;

    String errorCode;

    Marca marcaSelected = new Marca();

    Producto productoCreating = null;

    Factura facturaCreating = null;

    List<DetalleFactura> factura = new ArrayList<>();

    String inputSizes;

    List<SizeDisponibles> sizeD = new ArrayList<>();

    //UTILS
    //Estos son metodos de utilidad que ayudan a no reutilizar codigo
    // Verifica si el usuario autenticado es un administrador
    public boolean isAdmin() {
        // Verifica si el usuario está autenticado
        if (isLogued()) {
            // Si está autenticado, verifica si el tipo de usuario es administrador (idTipo == 1)
            return usuarioLogued.getTipo().getIdTipo() == 1;
        } else {
            // Si no está autenticado, devuelve false
            return false;
        }
    }

    // Verifica si hay un usuario autenticado o logueado
    public boolean isLogued() {
        // Verifica si el usuarioLogued no es nulo
        return usuarioLogued != null;
    }

    // Compara las sumas de hash de dos cadenas de texto
    public boolean checkPass(String str1, String str2) {
        // Devuelve true si las sumas de hash de las dos cadenas son iguales
        return str1.hashCode() == str2.hashCode();
    }

    // Compara dos cadenas de texto para verificar si son iguales
    public boolean checkRepeat(String str1, String str2) {
        // Devuelve true si las dos cadenas son iguales usando el método equals
        return str1.equals(str2);
    }

    // Aplica filtros a la lista de productos según los criterios proporcionados
    public List<Producto> applyFilters(Producto producto) {
        // Obtiene la lista completa de productos
        List<Producto> productos = productoService.listarProductos();
        // Lista auxiliar para almacenar productos antes de ser eliminados
        List<Producto> preEliminar = new ArrayList<Producto>();

        // Filtra por precio si se proporciona un valor no nulo
        if (producto.getPrecio() != 0) {
            productos = producto.filtrarPrecio(productos, producto.getPrecio());
        }

        // Filtra por marca si se proporciona un objeto de marca no nulo con un ID no nulo
        if (producto.getMarca() != null) {
            if (producto.getMarca().getIdMarca() != 0) {
                productos = producto.filtrarMarca(productos, producto.getMarca().getIdMarca());
            }
        }

        // Filtra por género si se proporciona un objeto de género no nulo con un ID no nulo
        if (producto.getGenero() != null) {
            if (producto.getGenero().getIdGenero() != 0) {
                productos = producto.filtrarGenero(productos, producto.getGenero().getIdGenero());
            }
        }

        // Filtra por categoría si se proporciona un objeto de categoría no nulo con un ID no nulo
        if (producto.getCategoria() != null) {
            if (producto.getCategoria().getIdCategoria() != 0) {
                productos = producto.filtrarCategoria(productos, producto.getCategoria().getIdCategoria());
            }
        }

        // Filtra por tamaño mínimo si se proporciona un objeto de tamaño mínimo no nulo con un ID no nulo
        if (producto.getMinSize() != null) {
            if (producto.getMinSize().getIdSize() != 0) {
                productos = producto.filtrarMinSize(productos, producto.getMinSize().getIdSize());
            }
        }

        // Filtra por tamaño máximo si se proporciona un objeto de tamaño máximo no nulo con un ID no nulo
        if (producto.getMaxSize() != null) {
            if (producto.getMaxSize().getIdSize() != 0) {
                productos = producto.filtrarMaxSize(productos, producto.getMaxSize().getIdSize());
            }
        }

        // Devuelve la lista de productos después de aplicar todos los filtros
        return productos;
    }

    // Obtiene una lista de imágenes que representan las portadas de productos
    public List<Imagen> getPortadas() {
        // Obtiene la lista completa de imágenes
        var imagenes = imagenService.listarImagenes();
        // Lista para almacenar las imágenes de portada
        List<Imagen> imagenes2 = new ArrayList<>();
        // Almacena la primera imagen como imagen anterior
        Imagen imagenAnterior = imagenes.get(0);
        // Agrega la primera imagen a la lista de imágenes de portada
        imagenes2.add(imagenAnterior);

        // Itera sobre las imágenes restantes
        for (Imagen imagen : imagenes) {
            // Compara el ID del producto de la imagen actual con el de la imagen anterior
            if (imagen.getProducto().getIdProducto() != imagenAnterior.getProducto().getIdProducto()) {
                // Si son diferentes, agrega la imagen actual a la lista de imágenes de portada
                imagenes2.add(imagen);
                // Actualiza la imagen anterior con la imagen actual
                imagenAnterior = imagen;
            }
        }

        // Devuelve la lista de imágenes de portada
        return imagenes2;
    }

    // Obtiene la portada de un producto específico según su ID
    public Imagen getPortada(Long idProducto) {
        // Obtiene la lista de imágenes de portada utilizando el método getPortadas()
        var imagenes = getPortadas();
        // Inicializa la variable imagen como nula
        Imagen imagen = null;

        // Itera sobre la lista de imágenes de portada
        for (Imagen imagen2 : imagenes) {
            // Compara el ID del producto de la imagen actual con el ID del producto proporcionado
            if (imagen2.getProducto().getIdProducto() == idProducto) {
                // Si coincide, asigna la imagen actual a la variable imagen
                imagen = imagen2;
            }
        }

        // Devuelve la imagen de portada del producto específico, o null si no se encuentra
        return imagen;
    }

    // Asigna un carrito a un usuario y devuelve los detalles del carrito asociado
    public List<DetalleCarrito> asignarCarrito(Usuario usuario) {
        // Obtiene la lista completa de detalles de carrito
        List<DetalleCarrito> dcs = detalleCarritoService.listarDetalleCarritos();
        // Obtiene la lista completa de carritos
        List<Carrito> carritos = carritoService.listarCarritos();
        // Inicializa la variable c como nula
        Carrito c = null;
        // Lista para almacenar los detalles del carrito asociado
        List<DetalleCarrito> carrito2 = new ArrayList<>();

        // Itera sobre la lista de carritos
        for (Carrito cc : carritos) {
            // Compara el ID del usuario asociado al carrito con el ID del usuario proporcionado
            if (cc.getUsuario().getIdUsuario() == usuario.getIdUsuario()) {
                // Si coincide, asigna el carrito actual a la variable c y lo establece como carrito activo
                c = cc;
                carritoActive = c;
            }
        }

        // Itera sobre la lista de detalles de carrito
        for (DetalleCarrito dc : dcs) {
            // Compara el ID del carrito asociado al detalle de carrito con el ID del carrito activo
            if (dc.getCarrito().getIdCarrito() == c.getIdCarrito()) {
                // Si coincide, agrega el detalle de carrito a la lista de detalles del carrito asociado
                carrito2.add(dc);
                // Agrega el producto correspondiente al detalle de carrito a la lista fakeCarrito
                fakeCarrito.add(dc.getProducto());
            }
        }

        // Devuelve la lista de detalles del carrito asociado
        return carrito2;
    }

    // Calcula el total de un carrito sumando el precio total de cada producto multiplicado por la cantidad correspondiente
    public int calcularTotal(List<DetalleCarrito> dcs) {
        for (DetalleCarrito dc : carrito) {
            total += dc.getCantidad() * dc.getProducto().getPrecio();
        }
        return total;
    }

    // Realiza la limpieza de los carritos eliminando todos los detalles de carrito asociados a un producto específico
    public void limpiezaCarritos(Producto producto) {
        // Busca el producto proporcionado
        producto = productoService.encontrarProducto(producto);
        // Obtiene la lista completa de detalles de carrito
        var dcs = detalleCarritoService.listarDetalleCarritos();

        // Itera sobre los detalles de carrito
        for (DetalleCarrito dc : dcs) {
            // Si el detalle de carrito está asociado al producto proporcionado, lo elimina
            if (dc.getProducto() == producto) {
                detalleCarritoService.eliminar(dc);
            }
        }

        // Vuelve a asignar el carrito para actualizar la lista de detalles de carrito
        carrito = asignarCarrito(usuarioLogued);
    }

    //MAPEOS
    //Aqui es donde se asignan que funcion y a que pagina se enviara al usuario dependiendo de la URL
    //Inicio
    // Maneja las solicitudes GET para la ruta "/"
    @GetMapping("/")
    public String inicio(Model model, Producto producto, @RequestParam(value = "busqueda", required = false) String busqueda) {
        // Verifica si el usuario está autenticado
        if (isLogued()) {
            // Limpia algunas listas y variables de sesión
            carrito2.clear();
            factura.clear();
            sizeD.clear();
            facturaCreating = null;

            // Obtiene listas de marcas, tamaños, categorías, imágenes y portadas
            var marcas = marcaService.listarMarcas();
            var size = sizeService.listarSize();
            var categorias = categoriaService.listarCategorias();
            var imagenes = imagenService.listarImagenes();
            var portadas = getPortadas();

            // Inicializa variables para calcular el total y el ITBIS
            total = 0;
            total = calcularTotal(carrito);
            itbisTotal = 0;
            itbisTotal = Math.round(total * 0.18 + total);
            itbis = 0;
            itbis = Math.round(total * 0.18);

            // Obtiene la lista de productos aplicando filtros o buscando por nombre
            List<Producto> productos;
            if (busqueda == null) {
                productos = applyFilters(producto);
            } else {
                productos = productoService.listarPorNombre(busqueda);
            }

            // Agrega atributos al modelo para ser utilizados en la vista
            model.addAttribute("usuarioLogued", usuarioLogued);
            model.addAttribute("productos", productos);
            model.addAttribute("marcas", marcas);
            model.addAttribute("sizes", size);
            model.addAttribute("categorias", categorias);
            model.addAttribute("imagenes", imagenes);
            model.addAttribute("portadas", portadas);
            model.addAttribute("carrito", carrito);
            model.addAttribute("total", total);
            model.addAttribute("itbisTotal", itbisTotal);
            model.addAttribute("itbis", itbis);

            // Maneja la lógica de la variable de bienvenida
            if (bienvenida != 0) {
                model.addAttribute("bienvenida", bienvenida);
                bienvenida = 0;
            } else {
                model.addAttribute("bienvenida", 0);
            }

            return "index"; // Devuelve el nombre de la vista
        } else {
            // Redirige a la página de inicio de sesión si el usuario no está autenticado
            if ("correcto".equals(mensaje)) {

            } else {
                mensaje = null;
            }
            return "redirect:/login";
        }
    }

    //Registro
    // Maneja las solicitudes GET para la ruta "/registrar"
    @GetMapping("/registrar")
    public String registroPage(Usuario usuario, Model model) {
        // Agrega el mensaje al modelo para ser utilizado en la vista
        model.addAttribute("mensaje", mensaje);
        // Devuelve el nombre de la vista "registro"
        return "registro";
    }

    // Maneja las solicitudes POST para la ruta "/registrar"
    @PostMapping("/registrar")
    public String registro(Usuario usuario, Model model) {
        // Busca un usuario existente con el mismo correo electrónico
        Usuario usuario2 = usuarioService.encontrarPorEmail(usuario.getEmail());

        // Verifica si el usuario con el correo electrónico ya existe
        if (usuario2 == null) {
            // Verifica si se ha seleccionado un género
            if (usuario.getGenero().getIdGenero() == 0) {
                mensaje = "Indique su género";
                model.addAttribute("mensaje", mensaje);
                return "registro";
            }

            // Establece el estado y tipo predeterminados para el nuevo usuario
            Estado estado = new Estado();
            estado.setIdEstado(1L);
            usuario.setEstado(estado);

            Tipo tipo = new Tipo();
            tipo.setIdTipo(2L);
            usuario.setTipo(tipo);

            // Guarda el nuevo usuario en la base de datos
            usuarioService.guardar(usuario);
            // Busca nuevamente el usuario para obtener su ID asignado
            usuario = usuarioService.encontrarUsuario(usuario);

            // Crea un nuevo carrito asociado al nuevo usuario
            Carrito carrito2 = new Carrito();
            carrito2.setUsuario(usuario);
            carritoService.guardar(carrito2);

            // Establece el mensaje de éxito y redirige a la página principal
            mensaje = "correcto";
            return "redirect:/";
        } else {
            // Si el usuario ya existe, establece un mensaje de error y vuelve a la página de registro
            mensaje = "Correo ya registrado";
            model.addAttribute("mensaje", mensaje);
            return "registro";
        }
    }

    // Maneja las solicitudes GET para la ruta "/tunel"
    @GetMapping("/tunel")
    public String tunel(Model model) {
        // Establece el mensaje como nulo
        mensaje = null;
        // Redirige a la página de registro ("/registrar")
        return "redirect:/registrar";
    }

    //Login
    // Maneja las solicitudes GET para la ruta "/login"
    @GetMapping("/login")
    public String loginPage(Usuario usuario, Model model) {
        // Agrega el mensaje al modelo para ser utilizado en la vista
        model.addAttribute("mensaje", mensaje);
        // Establece el mensaje como nulo
        mensaje = null;
        // Devuelve el nombre de la vista "login"
        return "login";
    }

    // Maneja las solicitudes POST para la ruta "/login"
    @PostMapping("/login")
    public String login(Usuario usuario, Model model) {
        // Busca un usuario existente con el mismo correo electrónico
        Usuario usuario2 = usuarioService.encontrarPorEmail(usuario.getEmail());

        // Verifica si el usuario existe
        if (usuario2 != null) {
            // Verifica si la contraseña proporcionada coincide con la contraseña almacenada
            if (checkPass(usuario.getPass(), usuario2.getPass())) {
                // Verifica si el estado del usuario es activo
                if (usuario2.getEstado().getIdEstado() == 1) {
                    // Establece el usuario como usuario logueado
                    usuarioLogued = usuario2;
                    // Asigna el carrito al usuario logueado
                    carrito = asignarCarrito(usuarioLogued);
                    // Establece la variable de bienvenida
                    bienvenida = 1;
                    // Redirige a la página principal
                    return "redirect:/";
                } else {
                    // Mensaje si el usuario está desactivado y redirige a la página de inicio de sesión
                    mensaje = "Usuario desactivado";
                    model.addAttribute("mensaje", mensaje);
                    return "redirect:/login";
                }
            } else {
                // Mensaje si la contraseña es incorrecta y redirige a la página de inicio de sesión
                mensaje = "Contraseña incorrecta";
                model.addAttribute("mensaje", mensaje);
                return "redirect:/login";
            }
        } else {
            // Mensaje si el usuario no está registrado y redirige a la página de inicio de sesión
            mensaje = "Usuario no Registrado";
            model.addAttribute("mensaje", mensaje);
            return "redirect:/login";
        }
    }

    // Maneja las solicitudes GET para la ruta "/logout"
    @GetMapping("/logout")
    public String logout() {
        // Limpia algunas listas y variables de sesión
        carrito2.clear();
        factura.clear();
        sizeD.clear();
        facturaCreating = null;
        usuarioLogued = null;
        carrito.clear();
        fakeCarrito.clear();

        // Redirige a la página principal ("/")
        return "redirect:/";
    }

    //Carrito
    // Maneja las solicitudes POST para la ruta "/agregar/{idProducto}"
    @PostMapping("/agregar/{idProducto}")
    public String agregarCarrito(Producto producto, @RequestParam(value = "cantidad", required = true) int cantidad, @RequestParam(value = "eliminar", required = false) String eliminar) {
        // Busca el producto por su ID
        producto = productoService.encontrarProducto(producto);
        // Crea una instancia de Producto con el mismo ID
        Producto producto2 = new Producto();
        producto2.setIdProducto(producto.getIdProducto());
        // Crea una instancia de DetalleCarrito
        DetalleCarrito detalleCarrito = new DetalleCarrito();

        // Verifica si la lista de carrito no está vacía
        if (!carrito.isEmpty()) {
            // Configura el producto y la cantidad en el detalle de carrito
            detalleCarrito.setProducto(producto);
            detalleCarrito.setCantidad(cantidad);

            // Verifica si el producto ya está en el carrito (fakeCarrito)
            if (fakeCarrito.indexOf(producto) == -1) {
                // Si no está en el carrito, agrega el detalle de carrito y el producto al carrito y fakeCarrito
                carrito.add(detalleCarrito);
                fakeCarrito.add(producto);
            } else {
                // Si ya está en el carrito, actualiza la cantidad del producto en el carrito
                int resultado = carrito.get(fakeCarrito.indexOf(producto)).getCantidad() + cantidad;
                if (resultado > producto.getStock()) {
                    carrito.get(fakeCarrito.indexOf(producto)).setCantidad(producto.getStock());
                } else {
                    carrito.get(fakeCarrito.indexOf(producto)).setCantidad(resultado);
                }
            }
        } else {
            // Si el carrito está vacío, configura el producto y la cantidad en el detalle de carrito y agrega al carrito y fakeCarrito
            detalleCarrito.setProducto(producto);
            detalleCarrito.setCantidad(cantidad);
            carrito.add(detalleCarrito);
            fakeCarrito.add(producto);
        }

        // Asigna el carrito activo a cada detalle de carrito en el carrito
        for (DetalleCarrito dc : carrito) {
            dc.setCarrito(carritoActive);
        }

        // Guarda todos los detalles de carrito en la base de datos
        detalleCarritoService.guardarTodos(carrito);

        // Redirige a la página principal
        return "redirect:/";
    }

    // Maneja las solicitudes POST para la ruta "/limpiar"
    @PostMapping("/limpiar")
    public String limpiarCarrito() {
        // Elimina todos los detalles de carrito en la base de datos asociados al carrito actual
        detalleCarritoService.eliminarTodos(carrito);
        // Limpia las listas de carrito y fakeCarrito
        carrito.clear();
        fakeCarrito.clear();
        // Redirige a la página principal ("/")
        return "redirect:/";
    }

    // Maneja las solicitudes POST para la ruta "/actualizarCarrito"
    @PostMapping("/actualizarCarrito")
    public String actualizarCarrito(@RequestParam(value = "trueInput", required = true) String datos) {
        // Divide la cadena de datos en un array utilizando el espacio como separador
        String[] separados = datos.split(" ");
        // Listas para almacenar IDs y cantidades
        List<String> ids = new ArrayList<>();
        List<String> cantidades = new ArrayList<>();
        int it = 0;

        // Recorre el array de datos para separar IDs y cantidades
        for (int i = 1; i < separados.length; i++) {
            if (i % 2 == 0) {
                cantidades.add(separados[i]);
            } else {
                ids.add(separados[i]);
            }
        }

        // Actualiza las cantidades en los detalles de carrito
        for (DetalleCarrito dc : carrito) {
            dc.setCantidad(Integer.parseInt(cantidades.get(it)));
            it++;
        }

        // Asigna el carrito activo a cada detalle de carrito en el carrito
        for (DetalleCarrito dc : carrito) {
            dc.setCarrito(carritoActive);
        }

        // Guarda todos los detalles de carrito en la base de datos
        detalleCarritoService.guardarTodos(carrito);

        // Redirige a la página principal
        return "redirect:/";
    }

    // Maneja las solicitudes POST para la ruta "/eliminarCarrito/{idProducto}"
    @PostMapping("/eliminarCarrito/{idProducto}")
    public String eliminarCarrito(Producto producto) {
        // Busca el producto por su ID
        producto = productoService.encontrarProducto(producto);
        // Obtiene el índice del producto en fakeCarrito
        int index = fakeCarrito.indexOf(producto);

        // Verifica si el producto existe
        if (producto != null) {
            // Elimina el detalle de carrito asociado al producto de la base de datos
            detalleCarritoService.eliminar(carrito.get(index));
        }

        // Remueve el producto del carrito y fakeCarrito
        carrito.remove(index);
        fakeCarrito.remove(index);

        // Redirige a la página principal
        return "redirect:/";
    }

    //Dashboard
    // Maneja las solicitudes GET para la ruta "/dashboard"
    @GetMapping("/dashboard")
    public String dashboardPage(Model model) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Limpia algunas listas y variables de sesión
            carrito2.clear();
            factura.clear();
            sizeD.clear();
            facturaCreating = null;

            // Obtiene estadísticas relacionadas con productos, usuarios, carritos, facturas, etc.
            var productos = productoService.listarProductos().size();
            var usuarios = usuarioService.listarUsuarios().size();
            var carritos = carritoService.listarCarritos().size();
            var facturas = facturaService.listarFacturas();
            var detalles = detalleFacturaService.listarDetalleFacturas();

            // Variables para productos y usuarios más vendidos
            Producto pVentas = null;
            int contP = 0;
            var portada = new Imagen();

            // Itera a través de los detalles de factura para encontrar el producto más vendido
            for (DetalleFactura df : detalles) {
                int contador = 0;
                for (DetalleFactura df2 : detalles) {
                    if (df.getProducto().getIdProducto() == df2.getProducto().getIdProducto()) {
                        contador++;
                    }
                }
                if (contP == 0) {
                    contP = contador;
                    pVentas = df.getProducto();
                    portada = getPortada(df.getProducto().getIdProducto());

                } else if (contador > contP) {
                    contP = contador;
                    pVentas = df.getProducto();
                    portada = getPortada(df.getProducto().getIdProducto());
                }
            }

            // Variables para usuarios y ingresos más destacados
            Usuario uVentas = null;
            int contU = 0;
            int ingresos = 0;

            // Itera a través de las facturas para encontrar al usuario más destacado y calcular los ingresos totales
            for (Factura f : facturas) {
                int contador = 0;
                int ingresos2 = 0;
                for (Factura f2 : facturas) {
                    if (f.getUsuario().getIdUsuario() == f2.getUsuario().getIdUsuario()) {
                        contador++;
                        ingresos2 += f2.getCosto();
                    }
                }
                if (contU == 0) {
                    contU = contador;
                    uVentas = f.getUsuario();
                    ingresos = ingresos2;

                } else if (contador > contU) {
                    contU = contador;
                    uVentas = f.getUsuario();
                    ingresos = ingresos2;
                }
            }

            // Agrega las estadísticas al modelo para ser utilizadas en la vista
            model.addAttribute("ingresos", ingresos);
            model.addAttribute("detalles", detalles);
            model.addAttribute("contU", contU);
            model.addAttribute("uVentas", uVentas);
            model.addAttribute("portada", portada);
            model.addAttribute("contP", contP);
            model.addAttribute("pVentas", pVentas);
            model.addAttribute("facturas", facturas);
            model.addAttribute("productos", productos);
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("carritos", carritos);

            // Devuelve el nombre de la vista "dashboardIndex"
            return "dashboardIndex";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes GET para la ruta "/dashboard/productos"
    @GetMapping("/dashboard/productos")
    public String dashboardProductosPage(Model model, Producto producto) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Obtiene la lista de productos, el total de productos y las imágenes relacionadas
            var productos = productoService.listarProductos();
            var productosCount = productos.size();
            var productosNoStock = new ArrayList<Producto>();
            var imagenes = imagenService.listarImagenes();
            var portadas = getPortadas();

            // Itera a través de los productos para encontrar aquellos que no tienen stock
            for (Producto producto2 : productos) {
                if (producto2.getStock() == 0) {
                    productosNoStock.add(producto2);
                }
            }

            // Agrega mensajes al modelo si existen
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Agrega datos al modelo para ser utilizados en la vista "dashboardProductos"
            model.addAttribute("portadas", portadas);
            model.addAttribute("imagenes", imagenes);
            model.addAttribute("productos", productos);
            model.addAttribute("productosCount", productosCount);
            model.addAttribute("sinStock", productosNoStock.size());

            // Devuelve el nombre de la vista "dashboardProductos"
            return "dashboardProductos";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes GET para la ruta "/dashboard/productos/agregarProducto/stage-1"
    @GetMapping("/dashboard/productos/agregarProducto/stage-1")
    public String dashboardAddProductosStage1(Model model, Producto producto, Modelo modelo, Marca marca) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Obtiene la lista de marcas y modelos
            var marcas = marcaService.listarMarcas();
            var modelos = modeloService.listarModelos();

            // Establece la marca seleccionada por defecto si no se ha seleccionado previamente
            if (marcaSelected.getIdMarca() == null) {
                marcaSelected.setIdMarca(1L);
                marcaSelected = marcaService.encontrarMarca(marcaSelected);
            }

            // Agrega mensajes al modelo si existen
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Agrega datos al modelo para ser utilizados en la vista "agregarProductos/addStage1"
            model.addAttribute("selected", marcaSelected);
            model.addAttribute("marcas", marcas);
            model.addAttribute("modelos", modelos);

            // Devuelve el nombre de la vista "agregarProductos/addStage1"
            return "agregarProductos/addStage1";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes POST para la ruta "/actualizarModelos"
    @PostMapping("/actualizarModelos")
    public String actualizarModelos(@RequestParam(value = "idMarca", required = true) String id) {
        // Establece el ID de la marca seleccionada y actualiza la marca seleccionada
        marcaSelected.setIdMarca(Long.parseLong(id));
        marcaSelected = marcaService.encontrarMarca(marcaSelected);

        // Redirige a la página de la primera etapa del formulario de agregar productos
        return "redirect:/dashboard/productos/agregarProducto/stage-1";
    }

    // Maneja las solicitudes POST para la ruta "/agregarModelo"
    @PostMapping("/agregarModelo")
    public String agregarModelo(Modelo modelo) {
        var modelos = modeloService.listarModelos();

        for (Modelo m : modelos) {
            String t1 = m.getNombre().toLowerCase().replaceAll("\\s", "");
            String t2 = modelo.getNombre().toLowerCase().replaceAll("\\s", "");
            if (t1.equals(t2)) {
                // Establece un mensaje indicando que se ha agregado una categoría
                mensaje = "errorModelo";
                // Redirige a la página de la segunda etapa del formulario de agregar productos
                return "redirect:/dashboard/productos/agregarProducto/stage-1";
            }
        }
        
        // Guarda el nuevo modelo
        modeloService.guardarModelo(modelo);

        // Establece un mensaje indicando que se ha agregado un modelo
        mensaje = "modelo";

        // Redirige a la página de la primera etapa del formulario de agregar productos
        return "redirect:/dashboard/productos/agregarProducto/stage-1";
    }

    // Maneja las solicitudes POST para la ruta "/agregarMarca"
    @PostMapping("/agregarMarca")
    public String agregarMarca(Marca marca) {
        var marcas = marcaService.listarMarcas();

        for (Marca m: marcas) {
            String t1 = m.getNombre().toLowerCase().replaceAll("\\s", "");
            String t2 = marca.getNombre().toLowerCase().replaceAll("\\s", "");
            if (t1.equals(t2)) {
                // Establece un mensaje indicando que se ha agregado una categoría
                mensaje = "errorMarca";
                // Redirige a la página de la segunda etapa del formulario de agregar productos
                return "redirect:/dashboard/productos/agregarProducto/stage-1";
            }
        }
        // Guarda la nueva marca
        marcaService.guardarMarca(marca);

        // Establece un mensaje indicando que se ha agregado una marca
        mensaje = "marca";

        // Redirige a la página de la primera etapa del formulario de agregar productos
        return "redirect:/dashboard/productos/agregarProducto/stage-1";
    }

    // Maneja las solicitudes POST para la ruta "/dashboard/productos/agregarProducto/stage-2"
    @PostMapping("/dashboard/productos/agregarProducto/stage-2")
    public String dashboardAddProductosStage2(Model model, Producto producto, Categoria categoria) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Actualiza la marca y el modelo del producto con los objetos completos desde el servicio
            producto.setMarca(marcaService.encontrarMarca(producto.getMarca()));
            producto.setModelo(modeloService.encontrarModelo(producto.getModelo()));
            // Combina el nombre de la marca y el modelo para establecer el nombre del producto
            producto.setNombre(producto.getMarca().getNombre() + " " + producto.getModelo().getNombre());

            // Obtiene la lista de categorías, tallas y géneros
            var categorias = categoriaService.listarCategorias();
            var sizes = sizeService.listarSize();
            var generos = generoService.listarGeneros();

            // Establece el producto que se está creando en el modelo
            productoCreating = producto;

            // Agrega mensajes al modelo si existen
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Agrega datos al modelo para ser utilizados en la vista "agregarProductos/addStage2"
            model.addAttribute("categorias", categorias);
            model.addAttribute("sizes", sizes);
            model.addAttribute("generos", generos);
            model.addAttribute("producto", productoCreating);

            // Devuelve el nombre de la vista "agregarProductos/addStage2"
            return "agregarProductos/addStage2";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes GET para la ruta "/dashboard/productos/agregarProducto/stage-2"
    @GetMapping("/dashboard/productos/agregarProducto/stage-2")
    public String dashboardAddProductosStage2Get(Model model, Producto producto, Categoria categoria) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Obtiene la lista de categorías, tallas y géneros
            var categorias = categoriaService.listarCategorias();
            var sizes = sizeService.listarSize();
            var generos = generoService.listarGeneros();

            // Agrega mensajes al modelo si existen
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Agrega datos al modelo para ser utilizados en la vista "agregarProductos/addStage2"
            model.addAttribute("categorias", categorias);
            model.addAttribute("sizes", sizes);
            model.addAttribute("generos", generos);
            model.addAttribute("producto", productoCreating);

            // Devuelve el nombre de la vista "agregarProductos/addStage2"
            return "agregarProductos/addStage2";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes POST para la ruta "/agregarCategoria"
    @PostMapping("/agregarCategoria")
    public String agregarCategoria(Categoria categoria) {
        var categorias = categoriaService.listarCategorias();

        for (Categoria c : categorias) {
            String t1 = c.getNombre().toLowerCase().replaceAll("\\s", "");
            String t2 = categoria.getNombre().toLowerCase().replaceAll("\\s", "");
            if (t1.equals(t2)) {
                // Establece un mensaje indicando que se ha agregado una categoría
                mensaje = "errorCategoria";
                // Redirige a la página de la segunda etapa del formulario de agregar productos
                return "redirect:/dashboard/productos/agregarProducto/stage-2";
            }
        }

        // Guarda la nueva categoría
        categoriaService.guardarCategoria(categoria);
        // Establece un mensaje indicando que se ha agregado una categoría
        mensaje = "categoria";

        // Redirige a la página de la segunda etapa del formulario de agregar productos
        return "redirect:/dashboard/productos/agregarProducto/stage-2";
    }

    // Maneja las solicitudes POST para la ruta "/dashboard/productos/agregarProducto/stage-3"
    @PostMapping("/dashboard/productos/agregarProducto/stage-3")
    public String dashboardAddProductosStage3(Model model, Producto producto) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Actualiza las propiedades del productoCreating con los objetos completos desde los servicios
            productoCreating.setMinSize(sizeService.encontrarSize(producto.getMinSize()));
            productoCreating.setMaxSize(sizeService.encontrarSize(producto.getMaxSize()));
            productoCreating.setGenero(generoService.encontrarGenero(producto.getGenero()));
            productoCreating.setCategoria(categoriaService.encontrarCategoria(producto.getCategoria()));

            // Agrega mensajes al modelo si existen
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Agrega el productoCreating al modelo para ser utilizado en la vista "agregarProductos/addStage3"
            model.addAttribute("producto", productoCreating);

            // Devuelve el nombre de la vista "agregarProductos/addStage3"
            return "agregarProductos/addStage3";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes GET para la ruta "/dashboard/productos/agregarProducto/stage-3"
    @GetMapping("/dashboard/productos/agregarProducto/stage-3")
    public String dashboardAddProductosStage3Get(Model model, Producto producto) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Agrega mensajes al modelo si existen
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Agrega el productoCreating al modelo para ser utilizado en la vista "agregarProductos/addStage3"
            model.addAttribute("producto", productoCreating);

            // Devuelve el nombre de la vista "agregarProductos/addStage3"
            return "agregarProductos/addStage3";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes POST para la ruta "/dashboard/productos/agregarProducto/stage-4"
    @PostMapping("/dashboard/productos/agregarProducto/stage-4")
    public String dashboardAddProductosStage4(Model model, Producto producto) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Actualiza las propiedades restantes del productoCreating con la información proporcionada
            productoCreating.setStock(producto.getStock());
            productoCreating.setPrecio(producto.getPrecio());
            productoCreating.setDescripcion(producto.getDescripcion());

            // Agrega mensajes al modelo si existen
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Agrega el productoCreating al modelo para ser utilizado en la vista "agregarProductos/addStage4"
            model.addAttribute("producto", productoCreating);

            // Devuelve el nombre de la vista "agregarProductos/addStage4"
            return "agregarProductos/addStage4";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    @GetMapping("/dashboard/productos/agregarProducto/stage-4")
    public String dashboardAddProductosStage4Get(Model model, Producto producto) {
        if (isAdmin()) {
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            model.addAttribute("producto", productoCreating);
            return "agregarProductos/addStage4";
        } else {
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorizacion para acceder aqui";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            return "error";
        }
    }

    // Maneja las solicitudes POST para la ruta "/dashboard/productos/agregarProducto/stage-5"
    @PostMapping("/dashboard/productos/agregarProducto/stage-5")
    public String dashboardAddProductosStage5(Model model, Producto producto, @RequestParam(value = "imagenesRegister", required = true) String imageneString) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Divide la cadena de enlaces de imágenes en un array utilizando la coma como delimitador
            String[] enlaces = imageneString.split(",");

            // Agrega mensajes al modelo si existen
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Agrega los enlaces y el productoCreating al modelo para ser utilizado en la vista "agregarProductos/addStage5"
            model.addAttribute("enlaces", enlaces);
            model.addAttribute("producto", productoCreating);

            // Devuelve el nombre de la vista "agregarProductos/addStage5"
            return "agregarProductos/addStage5";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes POST para la ruta "/dashboard/productos/agregarProducto/stage-6"
    @PostMapping("/dashboard/productos/agregarProducto/stage-6")
    public String dashboardAddProductosStage6(Model model, Producto producto, @RequestParam(value = "imagenesRegister", required = true) String imageneString) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Divide la cadena de enlaces de imágenes en un array utilizando la coma como delimitador
            String[] enlaces = imageneString.split(",");

            // Establece un código inicial para el productoCreating
            productoCreating.setCodigo("aaa");

            Estado estado = new Estado();
            estado.setIdEstado(1L);
            productoCreating.setEstado(estado);

            // Guarda el productoCreating en la base de datos y recupera el mismo producto con un código actualizado
            productoService.guardar(productoCreating);
            productoCreating = productoService.encontrarProducto(productoCreating);
            productoCreating.setCodigo("" + productoCreating.getMarca().getNombre().charAt(0) + productoCreating.getModelo().getNombre().charAt(0) + productoCreating.getIdProducto());

            // Guarda el productoCreating con el nuevo código en la base de datos
            productoService.guardar(productoCreating);

            // Itera sobre los enlaces de imágenes y guarda cada imagen asociada al productoCreating en la base de datos
            for (String enlace : enlaces) {
                Imagen imagen = new Imagen();
                imagen.setProducto(productoCreating);
                imagen.setImagen(enlace);
                imagenService.guardarImagen(imagen);
            }

            // Agrega mensajes al modelo si existen
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Agrega los enlaces y el productoCreating al modelo para ser utilizado en la vista "redirect:/dashboard/productos"
            model.addAttribute("enlaces", enlaces);
            model.addAttribute("producto", productoCreating);

            // Redirige a la página de productos del panel de administración
            return "redirect:/dashboard/productos";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes POST para la edición de un producto en el panel de administración
    @PostMapping("/dashboard/productos/editarProducto/{idProducto}")
    public String editarProducto(Model model, Producto producto, @RequestParam(value = "idProducto", required = true) String id) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Establece el ID del producto utilizando el parámetro de la solicitud
            producto.setIdProducto(Long.parseLong(id));

            // Realiza una limpieza en los carritos eliminando las instancias del producto antiguo
            limpiezaCarritos(producto);

            // Encuentra el producto en la base de datos utilizando el nuevo ID
            Producto producto2 = productoService.encontrarProducto(producto);

            // Actualiza las propiedades del producto con la información proporcionada en la solicitud
            producto2.setStock(producto.getStock());
            producto2.setPrecio(producto.getPrecio());
            producto2.setMinSize(producto.getMinSize());
            producto2.setMaxSize(producto.getMaxSize());
            producto2.setDescripcion(producto.getDescripcion());

            // Guarda el producto actualizado en la base de datos
            productoService.guardar(producto2);

            // Establece un mensaje de éxito
            mensaje = "correcto";

            // Redirige a la página de productos del panel de administración
            return "redirect:/dashboard/productos";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes GET para eliminar un producto en el panel de administración
    @GetMapping("/dashboard/productos/eliminarProducto/{idProducto}")
    public String eliminarProducto(Producto producto, Model model) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {

            // Realiza una limpieza en los carritos eliminando las instancias del producto que se va a eliminar
            limpiezaCarritos(producto);

            //Se busca el producto en la base de datos
            producto = productoService.encontrarProducto(producto);

            //Se crea un objeto de tipo estado
            Estado estado = new Estado();

            if (producto.getEstado().getIdEstado() == 1) {
                estado.setIdEstado(2L);

                // Se aasigna el nuevo estado de el producto y se guarda en la base de datos
                producto.setEstado(estado);
                productoService.guardar(producto);

                // Establece un mensaje indicando que el producto ha sido desactivado
                mensaje = "desactivado";
            } else {
                estado.setIdEstado(1L);

                // Se aasigna el nuevo estado de el producto y se guarda en la base de datos
                producto.setEstado(estado);
                productoService.guardar(producto);

                // Establece un mensaje indicando que el producto ha sido activado
                mensaje = "activado";
            }

            // Redirige a la página de productos del panel de administración
            return "redirect:/dashboard/productos";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes GET para mostrar la página de usuarios en el panel de administración
    @GetMapping("/dashboard/usuarios")
    public String dashboardUsuariosPage(Model model, Usuario usuario) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Obtiene la lista de usuarios
            var usuarios = usuarioService.listarUsuarios();

            // Calcula la cantidad total de usuarios y la cantidad de usuarios inactivos
            var usuariosCount = usuarios.size();
            var usuariosNoActive = new ArrayList<Usuario>();

            // Itera sobre los usuarios y agrega aquellos que están inactivos a la lista
            for (Usuario usuario2 : usuarios) {
                if (usuario2.getEstado().getIdEstado() == 2) {
                    usuariosNoActive.add(usuario2);
                }
            }

            // Agrega los atributos al modelo para ser utilizados en la vista
            model.addAttribute("usuarios", usuarios);
            model.addAttribute("usuariosCount", usuariosCount);
            model.addAttribute("inactive", usuariosNoActive.size());

            // Verifica si hay un mensaje y lo agrega al modelo
            if (mensaje != null) {
                model.addAttribute("mensaje", mensaje);
                mensaje = null;
            } else {
                model.addAttribute("mensaje", mensaje);
            }

            // Devuelve la vista correspondiente
            return "dashboardUsuarios";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorización para acceder aquí";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);

            // Redirige a la página de error
            return "error";
        }
    }

    // Maneja las solicitudes POST para agregar un usuario en el panel de administración
    @PostMapping("/agregarUsuario")
    public String agregarUsuario(Usuario usuario, Model model) {
        // Busca un usuario existente por correo electrónico
        Usuario usuario2 = usuarioService.encontrarPorEmail(usuario.getEmail());

        // Verifica si el usuario no existe
        if (usuario2 == null) {
            // Crea un nuevo objeto Estado y establece su ID
            Estado estado = new Estado();
            estado.setIdEstado(1L);

            // Asigna el estado al usuario
            usuario.setEstado(estado);

            // Guarda el nuevo usuario
            usuarioService.guardar(usuario);

            // Busca el usuario recién creado
            usuario = usuarioService.encontrarUsuario(usuario);

            // Crea un nuevo carrito y lo asocia al usuario
            Carrito carrito2 = new Carrito();
            carrito2.setUsuario(usuario);
            carritoService.guardar(carrito2);

            // Establece un mensaje de éxito
            mensaje = "correcto";

            // Redirige a la página de usuarios en el panel de administración
            return "redirect:/dashboard/usuarios";
        } else {
            // Si el usuario ya existe, establece un mensaje de error
            mensaje = "email";

            // Agrega el mensaje al modelo
            model.addAttribute("mensaje", mensaje);

            // Redirige a la página de usuarios en el panel de administración
            return "redirect:/dashboard/usuarios";
        }
    }

    // Maneja las solicitudes POST para editar un usuario en el panel de administración
    @PostMapping("/editarUsuario/{idUsuario}")
    public String editarUsuario(Usuario usuario, Model model,
            @RequestParam(value = "idUsuario", required = true) String id
    ) {
        // Convierte el ID de usuario de cadena a tipo Long
        usuario.setIdUsuario(Long.parseLong(id));

        // Verifica si el ID del usuario a editar es igual al ID del usuario logueado
        if (usuario.getIdUsuario() == usuarioLogued.getIdUsuario()) {
            // Si es el mismo usuario logueado, establece un mensaje de error y redirige
            mensaje = "error1";
            return "redirect:/dashboard/usuarios";
        } else if (usuario.getIdUsuario() == 1) {
            // Verifica si el ID del usuario a editar es igual a 1 (posiblemente un usuario administrador)
            // Si es así, establece un mensaje de error y redirige
            mensaje = "error2";
            return "redirect:/dashboard/usuarios";
        } else {
            // Si no es el mismo usuario logueado y el ID no es 1, procede a editar el usuario
            // Busca el usuario a editar por ID
            Usuario usuario2 = usuarioService.encontrarUsuario(usuario);

            // Actualiza el estado y el tipo del usuario
            usuario2.setEstado(usuario.getEstado());
            usuario2.setTipo(usuario.getTipo());

            // Guarda los cambios en el usuario
            usuarioService.guardar(usuario2);

            // Establece un mensaje de éxito y redirige
            mensaje = "editado";
            return "redirect:/dashboard/usuarios";
        }
    }

    // Maneja las solicitudes GET para mostrar la página de carritos en el panel de administración
    @GetMapping("/dashboard/carritos")
    public String dashboardCarritosPage(Model model) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Obtiene la lista de carritos
            var carritos = carritoService.listarCarritos();

            // Obtiene el número de carritos
            var carritosCount = carritos.size();

            // Inicializa listas para carritos vacíos y carritos llenos
            var carritosVacios = new ArrayList<Carrito>();
            var carritosLlenos = new ArrayList<Carrito>();

            // Obtiene la lista de detalles de carrito
            var dcs = detalleCarritoService.listarDetalleCarritos();

            // Itera sobre los carritos y detalles de carrito para determinar cuáles están llenos y cuáles están vacíos
            for (Carrito carrito : carritos) {
                for (DetalleCarrito dc : dcs) {
                    if (carrito == dc.getCarrito()) {
                        carritosLlenos.add(carrito);
                    }
                }
            }

            // Itera nuevamente para encontrar carritos vacíos
            for (Carrito carrito : carritos) {
                if (carritosLlenos.indexOf(carrito) == -1) {
                    carritosVacios.add(carrito);
                }
            }

            // Agrega atributos al modelo para ser utilizados en la vista
            model.addAttribute("carritos", carritos);
            model.addAttribute("carritosCount", carritosCount);
            model.addAttribute("empty", carritosVacios);
            model.addAttribute("detalles", dcs);

            // Retorna el nombre de la vista a mostrar
            return "dashboardCarritos";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error y redirige a la página de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorizacion para acceder aqui";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            return "error";
        }
    }

    // Maneja las solicitudes GET para mostrar la página de facturas en el panel de administración
    @GetMapping("/dashboard/facturas")
    public String dashboardFacturasPage(Model model) {
        // Verifica si el usuario logueado es un administrador
        if (isAdmin()) {
            // Obtiene la lista de facturas
            var facturas = facturaService.listarFacturas();

            // Obtiene la lista de detalles de factura
            var detalles = detalleFacturaService.listarDetalleFacturas();

            // Agrega atributos al modelo para ser utilizados en la vista
            model.addAttribute("detalles", detalles);
            model.addAttribute("facturas", facturas);

            // Retorna el nombre de la vista a mostrar
            return "dashboardFacturas";
        } else {
            // Si el usuario no es un administrador, muestra un mensaje de error y redirige a la página de error
            errorCode = "Error 403";
            errorMessage = "No cuentas con la autorizacion para acceder aqui";
            model.addAttribute("code", errorCode);
            model.addAttribute("message", errorMessage);
            return "error";
        }
    }

    //PAGOS
    // Maneja las solicitudes GET para mostrar la página de la primera etapa del proceso de compra
    @GetMapping("/payment/stage1")
    public String paymentStage1Page(Model model) {
        // Limpia la factura y la facturaCreating
        factura.clear();
        sizeD.clear();
        facturaCreating = null;

        int rn = 0;

        Random random = new Random();

        // Obtiene la lista de tallas disponibles
        var sizes = sizeService.listarSize();

        // Verifica si el carrito2 está vacío
        if (carrito2.isEmpty()) {
            // Itera sobre los elementos del carrito original (carrito)
            for (DetalleCarrito dc : carrito) {
                // Si la cantidad en el detalle del carrito es mayor que 1, agrega múltiples detalles al carrito2
                if (dc.getCantidad() > 1) {
                    for (int i = 0; i < dc.getCantidad(); i++) {
                        DetalleCarrito dc2 = new DetalleCarrito();
                        dc2.setCantidad(1);
                        dc2.setCarrito(dc.getCarrito());
                        dc2.setProducto(dc.getProducto());
                        carrito2.add(dc2);
                    }
                } else {
                    // Si la cantidad es 1, agrega el detalle al carrito2
                    carrito2.add(dc);
                }
            }
        }

        for (DetalleCarrito dc : carrito) {
            var sizes2 = sizeService.listarSize();
            int cantidadSizes = Math.abs(dc.getProducto().getMinSize().getNumero() - dc.getProducto().getMaxSize().getNumero()) + 1;
            if (cantidadSizes > dc.getProducto().getStock()) {
                cantidadSizes = dc.getProducto().getStock();
                for (int j = 0; j < cantidadSizes; j++) {
                    int max = dc.getProducto().getMaxSize().getNumero() - j;
                    int numeroAleatorio = random.nextInt(max);
                    SizeDisponibles sd = new SizeDisponibles();
                    sd.setProducto(dc.getProducto());
                    sd.setSize(sizes2.get(numeroAleatorio));
                    sizeD.add(sd);

                    int index = sizes2.indexOf(sd.getSize());
                    sizes2.remove(index);

                }
            } else {
                for (int j = dc.getProducto().getMinSize().getNumero() - 1; j < dc.getProducto().getMaxSize().getNumero(); j++) {
                    SizeDisponibles sd = new SizeDisponibles();
                    sd.setProducto(dc.getProducto());
                    sd.setSize(sizes.get(j));
                    sizeD.add(sd);
                }
            }
        }

        // Agrega atributos al modelo para ser utilizados en la vista
        model.addAttribute(
                "sizes", sizes);
        model.addAttribute(
                "sizesDisponibles", sizeD);
        model.addAttribute(
                "productos", carrito2);
        model.addAttribute(
                "usuario", usuarioLogued);

        // Retorna el nombre de la vista a mostrar
        return "comprar/buyStage1";
    }

    // Maneja las solicitudes POST para la segunda etapa del proceso de compra
    @PostMapping("/payment/stage2")
    public String paymentStage2(Model model, @RequestParam(value = "inputSizes", required = true) String input) {
        // Limpia la factura y la facturaCreating
        factura.clear();
        sizeD.clear();
        facturaCreating = new Factura();

        inputSizes = input;

        // Obtiene los tamaños seleccionados desde el formulario
        String[] sizes = inputSizes.split(",");

        // Configura la facturaCreating con el usuario logueado
        facturaCreating.setUsuario(usuarioLogued);

        // Itera sobre los tamaños seleccionados
        for (int i = 0; i < sizes.length; i++) {
            // Crea un nuevo detalle de factura (df)
            DetalleFactura df = new DetalleFactura();

            // Configura el detalle de factura con la cantidad y producto del carrito2
            df.setCantidad(carrito2.get(i).getCantidad());
            df.setProducto(carrito2.get(i).getProducto());
            df.setPrecio(carrito2.get(i).getProducto().getPrecio());

            // Crea un nuevo objeto Size y configura su ID y número desde los tamaños seleccionados
            Size size = new Size();
            size.setIdSize(Long.parseLong(sizes[i]));
            size.setNumero(Integer.parseInt(sizes[i]));

            // Configura el detalle de factura con el tamaño creado
            df.setSize(size);

            // Agrega el detalle de factura a la lista de la factura
            factura.add(df);
        }

        // Agrega atributos al modelo para ser utilizados en la vista
        model.addAttribute("total", total);
        model.addAttribute("itbisTotal", itbisTotal);
        model.addAttribute("itbis", itbis);
        model.addAttribute("usuario", usuarioLogued);
        model.addAttribute("factura", factura);
        model.addAttribute("sizes", sizes);

        // Retorna el nombre de la vista a mostrar
        return "comprar/buyStage2";
    }

    // Maneja las solicitudes GET a la URL /payment/stage2
    @GetMapping("/payment/stage2")
    public String paymentStage2Get(Model model) {
        // Limpia la factura y la facturaCreating
        factura.clear();
        sizeD.clear();
        facturaCreating = new Factura();

        // Obtiene los tamaños seleccionados desde el formulario (inputSizes)
        String[] sizes = inputSizes.split(",");

        // Configura la facturaCreating con el usuario logueado
        facturaCreating.setUsuario(usuarioLogued);

        // Itera sobre los tamaños seleccionados
        for (int i = 0; i < sizes.length; i++) {
            // Crea un nuevo detalle de factura (df)
            DetalleFactura df = new DetalleFactura();

            // Configura el detalle de factura con la cantidad y producto del carrito2
            df.setCantidad(carrito2.get(i).getCantidad());
            df.setProducto(carrito2.get(i).getProducto());
            df.setPrecio(carrito2.get(i).getProducto().getPrecio());

            // Crea un nuevo objeto Size y configura su ID y número desde los tamaños seleccionados
            Size size = new Size();
            size.setIdSize(Long.parseLong(sizes[i]));
            size.setNumero(Integer.parseInt(sizes[i]));

            // Configura el detalle de factura con el tamaño creado
            df.setSize(size);

            // Agrega el detalle de factura a la lista de la factura
            factura.add(df);
        }

        // Agrega atributos al modelo para ser utilizados en la vista
        model.addAttribute("total", total);
        model.addAttribute("itbisTotal", itbisTotal);
        model.addAttribute("itbis", itbis);
        model.addAttribute("usuario", usuarioLogued);
        model.addAttribute("factura", factura);
        model.addAttribute("sizes", sizes);

        // Retorna el nombre de la vista a mostrar
        return "comprar/buyStage2";
    }

    // Maneja las solicitudes POST para la tercera etapa del proceso de pago con PayPal
    @PostMapping("/payment/stage3")
    public String paymentStage2(@ModelAttribute("order") Order order) {
        try {
            // Crea una descripción para el pago basada en el nombre del usuario logueado
            String desc = "Pago por compra en LH Import Boutique de " + usuarioLogued.getNombre();

            // Configura la descripción en la orden
            order.setDescription(desc);

            // Crea un objeto Payment utilizando el servicio de PayPal
            Payment payment = paypalService.createPayment(
                    order.getPrice(), // Precio de la orden
                    order.getCurrency(), // Moneda de la orden
                    order.getMethod(), // Método de pago
                    order.getIntent(), // Intento de pago
                    order.getDescription(), // Descripción de la orden
                    "http://localhost:8080/" + CANCEL_URL, // URL de cancelación
                    "http://localhost:8080/" + SUCCESS_URL // URL de éxito
            );

            // Itera sobre los enlaces (Links) del objeto Payment
            for (Links link : payment.getLinks()) {
                // Si el enlace tiene relación "approval_url"
                if (link.getRel().equals("approval_url")) {
                    // Redirige a la URL de aprobación de PayPal
                    return "redirect:" + link.getHref();

                }
            }
        } catch (PayPalRESTException ex) {
            // Maneja excepciones de PayPalRESTException (captura y loguea)
            Logger.getLogger(MainController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        // Redirige a la página principal en caso de error
        return "redirect:/";
    }

    // Maneja las solicitudes GET a la URL de cancelación
    @GetMapping(value = CANCEL_URL)
    public String paymentCancel() {
        return "comprar/paymentCancel";
    }

    // Maneja las solicitudes GET a la URL de éxito
    @GetMapping(value = SUCCESS_URL)
    public String paymentSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            // Ejecuta el pago en PayPal
            Payment payment = paypalService.executePayment(paymentId, payerId);

            // Verifica si el pago fue aprobado
            if (payment.getState().equals("approved")) {
                // Actualiza el stock de productos en la base de datos
                for (Producto producto : fakeCarrito) {
                    int index = fakeCarrito.indexOf(producto);
                    int cant = carrito.get(index).getCantidad();
                    producto.setStock(producto.getStock() - cant);
                }

                // Calcula el costo total de la factura y el ITBIS
                Long total2 = 0L;
                for (DetalleCarrito dc : carrito) {
                    total2 += dc.getCantidad() * dc.getProducto().getPrecio();
                }

                Long itbis2 = Math.round(total2 * 0.18);

                // Configura la factura con la información necesaria
                facturaCreating.setCosto(total2);
                facturaCreating.setItbis(itbis2);

                // Guarda los cambios en la base de datos
                productoService.guardarTodos(fakeCarrito);
                detalleCarritoService.eliminarTodos(carrito);
                carrito.clear();
                fakeCarrito.clear();

                // Obtiene la fecha y hora actual
                LocalDateTime fechaHoraActual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String fechaHoraFormateada = fechaHoraActual.format(formatter);
                facturaCreating.setFecha(fechaHoraFormateada);

                // Configura el método de pago como "Paypal"
                facturaCreating.setMetodo("Paypal");

                // Guarda la factura y los detalles de la factura en la base de datos
                facturaService.guardar(facturaCreating);
                facturaService.encontrarFactura(facturaCreating);

                for (DetalleFactura df : factura) {
                    df.setFactura(facturaCreating);
                }
                detalleFacturaService.guardarTodos(factura);

                // Redirige a la página de éxito en la compra
                return "comprar/paymentSuccess";

            }
        } catch (PayPalRESTException ex) {
            Logger.getLogger(MainController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        // Redirige a la página principal si hay algún problema
        return "redirect:/";
    }

    // Maneja las solicitudes POST a la URL /payment/stage4
    @PostMapping("/payment/stage4")
    public String paymentStage3(Model model) {
        // Agrega el usuario logueado al modelo
        model.addAttribute("usuario", usuarioLogued);

        // Retorna la vista correspondiente para la etapa 3 de la compra
        return "comprar/buyStage3";
    }

    // Maneja las solicitudes POST a la URL /payment/stage5
    @PostMapping("/payment/stage5")
    public String paymentStage4() {
        // Itera sobre los productos en el carrito simulado
        for (Producto producto : fakeCarrito) {
            int index = fakeCarrito.indexOf(producto);
            int cant = carrito.get(index).getCantidad();
            // Actualiza el stock de los productos
            producto.setStock(producto.getStock() - cant);
        }

        Long total2 = 0L;
        // Calcula el costo total de la factura
        for (DetalleCarrito dc : carrito) {
            total2 += dc.getCantidad() * dc.getProducto().getPrecio();
        }

        Long itbis2 = Math.round(total2 * 0.18); // Calcula el monto del ITBIS

        // Establece el costo y el ITBIS en la factura
        facturaCreating.setCosto(total2);
        facturaCreating.setItbis(itbis2);

        // Guarda los cambios en los productos y limpia el carrito
        productoService.guardarTodos(fakeCarrito);
        detalleCarritoService.eliminarTodos(carrito);
        carrito.clear();
        fakeCarrito.clear();

        // Obtiene la fecha y hora actual
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHoraFormateada = fechaHoraActual.format(formatter);
        facturaCreating.setFecha(fechaHoraFormateada);

        facturaCreating.setMetodo("Tarjeta"); // Establece el método de pago en la factura

        // Guarda la factura y los detalles asociados
        facturaService.guardar(facturaCreating);
        facturaService.encontrarFactura(facturaCreating);

        for (DetalleFactura df : factura) {
            df.setFactura(facturaCreating);
        }
        detalleFacturaService.guardarTodos(factura);

        // Retorna la vista de éxito de pago
        return "comprar/paymentSuccess";
    }

    // Maneja las solicitudes GET a la URL /pedidos
    @GetMapping("/pedidos")
    public String pedidos(Model model, Factura factura2) {
        // Obtiene la lista de facturas y detalles de factura
        var facturas = facturaService.listarFacturas();
        var dfs = detalleFacturaService.listarDetalleFacturas();

        // Añade atributos al modelo para su uso en la vista
        model.addAttribute("factura", factura2);
        model.addAttribute("facturas", facturas);
        model.addAttribute("detalles", dfs);
        model.addAttribute("usuarioLogued", usuarioLogued);

        // Retorna el nombre de la vista a mostrar
        return "pedidos";
    }

    // Maneja las solicitudes GET a la URL /pedidos/{idFactura}
    @GetMapping("/pedidos/{idFactura}")
    public String pedidosUpdate(Factura factura2, Model model) {
        // Obtiene la lista de facturas y detalles de factura
        var facturas = facturaService.listarFacturas();
        var dfs = detalleFacturaService.listarDetalleFacturas();

        // Encuentra la factura específica usando el servicio
        factura2 = facturaService.encontrarFactura(factura2);

        // Calcula el total y el ITBIS para la factura específica
        Long total2 = 0L;
        for (DetalleFactura df : dfs) {
            if (df.getFactura().getIdFactura() == factura2.getIdFactura()) {
                total2 += df.getCantidad() * df.getPrecio();
            }
        }

        Long itbisTotal2 = Math.round(total2 * 0.18 + total2);
        Long itbis2 = Math.round(total2 * 0.18);

        // Añade atributos al modelo para su uso en la vista
        model.addAttribute("total", total2);
        model.addAttribute("itbisTotal", itbisTotal2);
        model.addAttribute("itbis", itbis2);
        model.addAttribute("factura", factura2);
        model.addAttribute("facturas", facturas);
        model.addAttribute("detalles", dfs);
        model.addAttribute("usuarioLogued", usuarioLogued);

        // Retorna el nombre de la vista a mostrar
        return "pedidos";
    }

}
