# Sistema de Gestión de Inventario y Pedidos - Pre-Entrega Java

Es una aplicación de consola desarrollada en Java 17 para gestionar un inventario de productos y procesar pedidos de venta. El sistema sigue una arquitectura modular de responsabilidad única, separando la lógica de negocio, la persistencia de datos y la interfaz de usuario.

> [Registro de Cambios >>](changelog.md)

## Índice

- [Sistema de Gestión de Inventario y Pedidos - Pre-Entrega Java](#sistema-de-gestión-de-inventario-y-pedidos---pre-entrega-java)
  - [Índice](#índice)
  - [Características principales](#características-principales)
  - [Estructura del Proyecto](#estructura-del-proyecto)
  - [Ejecución](#ejecución)
  - [Diagrama del Sistema](#diagrama-del-sistema)
  - [Licencia](#licencia)
  - [Talento Tech](#talento-tech)

## Características principales

**Gestión de Inventario (CRUD):** Permite listar, agregar, buscar, modificar, eliminar (desactivar y reactivar) *productos*, realizar, listar, entregar y cancelar *pedidos* y realiza un reporte de ventas mostrando el capital efectivo (*pedidos entregados*) y el crédito (*pedidos pendientes*) de las actividades realizadas. Se utiliza una eliminación "lógica" (desactivación booleana o "soft-delete"), que permite la reactivación de los productos, consiguiendo también con esto mantener la integridad del historial de pedidos mediante la identidad única de los productos.

<p>
  <img src="./src/assets/menuCRUD.png" alt="Vista del Menú Principal" width="400">
  <br><em>Interfaz del Menú Principal (CRUD)</em>
</p>
_____________________________________


**Arquitectura Basada en POO:**

    > **Encapsulamiento** (validaciones en setters)
    > **Herencia** (clase `Bebida`)
    > **Polimorfismo** (sobrescritura de `toString` y `equals`)
    > **Sobrecarga** (múltiples constructores para creación y persistencia).


<p>
  <img src="src\assets\validacionSetter.png" alt="Aplicación de Encapsulamiento" width="500">
  <br><em>Encapsulamiento</em>
</p>
_____________________________________

<p>
  <img src="src\assets\herencia1.png" alt="Aplicación de Herencia" width="300">
  <br><em>Herencia</em>
</p>
_____________________________________

<p>
  <img src="src\assets\polimorfismo.png" alt="Aplicación de Polimorfismo" width="500">
  <br><em>Polimorfismo</em>
</p>
_____________________________________

<p>
  <img src="src\assets\sobrecargaConstructor.png" alt="Aplicación de Sobrecarga" width="500">
  <br><em>Sobrecarga de Constructores</em>
</p>
_____________________________________


**Sistema de Pedidos y Lógica de Negocio:** Gestión de pedidos con actualización de estado interactiva, cálculo automático de descuentos progresivos (10% y 20%) y recupero autonomo de stock ante cancelación.

<p>
  <img src="src\assets\logicaPedidos.png" alt="Lógica de Pedidos y Descuentos" width="500">
  <br><em>Recupero autónomo de Stock en la cancelación de pedidos</em>
</p>
_____________________________________

**Arquitectura Modular:** Separación de responsabilidades mediante servicios especializados (`ProductoService`, `PedidoService`, `PersistenceService`), un Controlador central (`MenuController`) para la orquestación y una Vista desacoplada (`ConsoleView`). Esta estructura garantiza la responsabilidad única, facilitando el mantenimiento y la escalabilidad del sistema.

**Control de Flujo con Enums:** Uso de `EstadoPedido` para manejar el ciclo de vida de las ventas y generar reportes de caja.

<p>
  <img src="src\assets\enum1.png" alt="Implementación de enums" width="500">
  <br><em>Entidad que implementa el enum</em>
</p>

<p>
  <img src="src\assets\enum2.png" alt="Uso del enum" width="500">
  <br><em>Aplicación de enums en el sistema</em>
</p>

**Persistencia de Datos:** Almacenamiento en archivos CSV mediante la API `java.nio`, consiguiendo que la identidad de los productos se mantenga consistente.

<p>
  <img src="src\assets\nioPersist.png" alt="Uso del java.nio" width="500">
  <br><em>Parte de la implementación de persistencia en el sistema</em>
</p>

**Experiencia de Usuario (UX):** Interfaz de consola mejorada con validaciones de entrada, manejo de excepciones personalizadas y resaltado de errores mediante códigos de color ANSI.

<p>
  <img src="src\assets\ux1.png" alt="Ejemplo de excepción personalizada" width="500">
  <br><em>Excepción personalizada para la entrada de nombres de productos implementada mediante el principio de Herencia</em>
</p>

<p>
  <img src="src\assets\ux2.png" alt="Validación heredada desde Runtime Exception" width="500">
  <br><em>Ejemplo de uso de validación, en este caso, usando polimorfismo en la jerarquia de excepciones de dominio, basada en Runtime Exception, implementada en el sistema</em>
</p>


## Estructura del Proyecto

```text
/Pre-Entrega_JavaBE"
|
|___ "/bin"   # (innecesario para el contexto)
|
|___ "/src"
	|
	|___ "/assets"
	|	|
	|	|"herencia1.png"
	|	|"logicaPedidos.png"
	|	|"menuCRUD.png"
	|	|"polimorfismo.png"
	|	|"sobrecargaConstructor.png"
	|	|"validacionSetter.png"
	|
	|___ "/controller"
	|	|
	|	|"MenuController.java"
	|
	|___ "/exceptions"
	|	|
	|	|"NombreInvalidoException.java"
	|	|"PrecioInvalidoException.java"
	|	|"StockInsuficienteException.java"
	|	|"StockInvalidoException.java"
	|	|"ValidacionProductoException.java"
	|
	|___ "/main"
	|	|
	|	|"Main.java"
	|
	|___ "/model"
	|	|
	|	|"Bebida.java"
	|	|"EstadoPedido.java"
	|	|"LineaPedido.java"
	|	|"Pedido.java"
	|	|"Producto.java"
	|
	|___ "/service"
	|	|
	|	|"PedidoService.java"
	|	|"PersistenceService.java"
	|	|"ProductoService.java"
	|
	|___ "/view"
	|	|
	|	|"ConsoleView.java"
	|
	|___".gitignore"
		"changelog.md"
		"inventario.csv"
		"LICENSE"
		"pedidos.csv"
		"pedidos_detalle.csv"
		"README.md"
```

## Ejecución

Desde el directorio raíz en el que se instaló el sistema:

**Compilar:**
```bash
javac -d bin src/**/*.java
```

**Ejecutar:**
```bash
java -cp bin main.Main
```

## Diagrama del Sistema

```mermaid
classDiagram
    class Producto {
        #int id
        #String nombre
        #double precio
        #int stock
        #boolean activo
        +toString() String
        +equals(Object) boolean
    }

    class Bebida {
        -boolean esAlcoholica
    }

    class Pedido {
        -int idPedido
        -List~LineaPedido~ lineas
        -double total
        -double totalSinDescuento
        -EstadoPedido estado
        +agregarProducto(Producto, int, double) void
        +getAhorroTotal() double
    }

    class LineaPedido {
        -Producto producto
        -int cantidad
        -double precioAplicado
    }

    class EstadoPedido {
        <<enumeration>>
        PENDIENTE
        ENTREGADO
        CANCELADO
    }

    class PedidoService {
        -ArrayList~Pedido~ historialPedidos
        +getHistorialPedidos() ArrayList~Pedido~
        +agregarPedidoAlHistorial(Pedido) void
        +actualizarEstadoPedido(List, int, EstadoPedido) void
        +calcularDatosReporte() double[]
    }

    class ProductoService {
        +agregarProducto(List, String, double, int, boolean) void
        +buscarProductoPorCriterio(List, String) Producto
        +buscarProducto(List, int) Producto
        +modificarProducto(List, int, String, double, int) void
        +eliminarProducto(List, int) void
        +reactivarProducto(List, int) void
        +procesarItemVenta(List, Pedido, int, int) double
    }

    class PersistenceService {
        +guardarInventarioCSV(List) void
        +cargarInventarioCSV(List) boolean
        +guardarPedidosCSV(List) void
        +cargarPedidosCSV(List, List) boolean
    }

    class ConsoleView {
        +mostrarMensaje(String) void
        +mostrarError(String) void
        +mostrarAviso(String) void
        +mostrarExito(String) void
        +mostrarCatalogo(List) void
        +mostrarInactivos(List) void
        +mostrarHistorialPedidos(List) void
        +mostrarReporte(double, double, int) void
    }

    class MenuController {
        -ProductoService productoService
        -PedidoService pedidoService
        -PersistenceService persistence
        -ConsoleView view
        -ArrayList~Producto~ inventario
        +iniciar() void
    }

    class StockInsuficienteException {
        +StockInsuficienteException(String mensaje)
    }

    class ValidacionProductoException {
        +ValidacionProductoException(String mensaje)
    }

    class IOException {
        <<Java Library>>
    }

    class NombreInvalidoException {
        +NombreInvalidoException(String mensaje)
    }

    class PrecioInvalidoException {
        +PrecioInvalidoException(String mensaje)
    }

    class StockInvalidoException {
        +StockInvalidoException(String mensaje)
    }

    Producto <|-- Bebida : Herencia
    Pedido "1" *-- "*" LineaPedido : Composición
    LineaPedido "1" --> "1" Producto : Referencia
    Pedido "1" --> "1" EstadoPedido : Usa
    PedidoService "1" o-- "*" Pedido : Gestiona historial
    Producto ..> NombreInvalidoException : lanza
    Producto ..> PrecioInvalidoException : lanza
    Producto ..> StockInvalidoException : lanza
    ValidacionProductoException <|-- NombreInvalidoException : Herencia
    ValidacionProductoException <|-- PrecioInvalidoException : Herencia
    ValidacionProductoException <|-- StockInvalidoException : Herencia
    ProductoService ..> Producto : opera
    ProductoService ..> StockInsuficienteException : lanza
    ProductoService ..> ValidacionProductoException : lanza
    PersistenceService ..> IOException : maneja
    Main ..> MenuController : inicia
    MenuController "1" --> "1" ProductoService : orquesta productos
    MenuController "1" --> "1" PedidoService : orquesta pedidos
    MenuController "1" --> "1" PersistenceService : persiste
    MenuController "1" --> "1" ConsoleView : muestra
    MenuController "1" o-- "*" Producto : maneja inventario
```

## Licencia
Este proyecto está bajo la Licencia MIT. Para más detalles, consultá el archivo LICENSE.

## Talento Tech
Desarrollado como parte de la cursada de Java Backend 26138 como ensayo de estudio, sin fines comerciales - 2026.
