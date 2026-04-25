# Sistema de Gestión de Inventario y Pedidos - Pre-Entrega Java

Es una aplicación de consola desarrollada en Java 17 para gestionar un inventario de productos y procesar pedidos de venta. El sistema sigue una arquitectura modular de responsabilidad única, separando la lógica de negocio, la persistencia de datos y la interfaz de usuario.

> [!Enlace]
> [Registro de Cambios >>](changelog.md)

## Índice

- [Sistema de Gestión de Inventario y Pedidos - Pre-Entrega Java](#sistema-de-gestión-de-inventario-y-pedidos---pre-entrega-java)
  - [Índice](#índice)
  - [Características principales](#características-principales)
  - [Tecnologías utilizadas](#tecnologías-utilizadas)
  - [Estructura del Proyecto](#estructura-del-proyecto)
  - [Ejecución](#ejecución)
  - [Diagrama del Sistema](#diagrama-del-sistema)
  - [Licencia](#licencia)
  - [Talento Tech](#talento-tech)

## Características principales

**Gestión de Inventario (CRUD):** Permite listar, agregar, buscar, modificar, eliminar (desactivar) y reactivar *productos*, realizar, listar, entregar y cancelar *pedidos* y realiza un reporte de ventas mostrando el capital efectivo (*pedidos entregados*) y el crédito (*pedidos pendientes*) de las actividades realizadas. La eliminación es lógica (desactivación o "soft-delete"), permitiendo la reactivación de productos para mantener la integridad del historial de pedidos mediante la identidad de los mismos.

**Arquitectura Basada en POO:**
    - **Encapsulamiento** (validaciones en setters)
    - **Herencia** (clase `Bebida`)
    - **Polimorfismo** (sobrescritura de `toString` y `equals`)
    - **Sobrecarga** (múltiples constructores para creación y persistencia).

**Sistema de Pedidos y Lógica de Negocio:** Gestión de pedidos con múltiples líneas, cálculo automático de descuentos progresivos (10% y 20%) y orquestación de stock según el estado del pedido.

**Arquitectura Modular (SoC):** Separación de responsabilidades mediante Servicios (`ProductoService`, `PersistenceService`) y Vista (`ConsoleView`), siguiendo principios de "bajo acoplamiento".

**Control de Flujo con Enums:** Uso de `EstadoPedido` para manejar el ciclo de vida de las ventas y generar reportes de caja precisos.

**Persistencia de Datos:** Almacenamiento en archivos CSV mediante la API `java.nio`, consiguiendo que la identidad de los productos se mantenga consistente.

**Experiencia de Usuario (UX):** Interfaz de consola mejorada con validaciones de entrada, manejo de excepciones personalizadas y resaltado de errores mediante códigos de color ANSI.

## Tecnologías utilizadas

- **Lenguaje:** Java 17 (JDK 17)
- **Persistencia:** Archivos CSV (CSV Flat Files)

## Estructura del Proyecto

- `src/model`: Contiene las entidades de datos (`Producto`, `Bebida`, `Pedido`, `LineaPedido`, `EstadoPedido`).
- `src/controller`: Orquestación y control del flujo de la interfaz de usuario (`MenuController`).
- `src/service`: Lógica de negocio (`ProductoService`) y servicios de persistencia (`PersistenceService`).
- `src/view`: Manejo de la interfaz de usuario por consola (`ConsoleView`).
- `src/exceptions`: Jerarquía de excepciones personalizadas (`ValidacionProductoException`, `StockInsuficienteException`).
- `src/main`: Clase de arranque que inicializa las dependencias (`Main`).

## Ejecución

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

    class ProductoService {
        -ArrayList~Pedido~ historialPedidos
        +getHistorialPedidos() ArrayList~Pedido~
        +agregarPedidoAlHistorial(Pedido) void
        +agregarProducto(ArrayList, String, double, int, boolean) void
        +buscarProductoPorCriterio(List, String) Producto
        +buscarProducto(List, int) Producto
        +modificarProducto(ArrayList, int, String, double, int) void
        +eliminarProducto(ArrayList, int) void
        +procesarItemVenta(ArrayList, Pedido, int, int) double
        +reactivarProducto(ArrayList, int) void
        +actualizarEstadoPedido(ArrayList, int, EstadoPedido) void
        +calcularDatosReporte() double[]
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
        -ProductoService service
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
    ProductoService "1" o-- "*" Pedido : Gestiona historial
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
    MenuController "1" --> "1" ProductoService : orquesta
    MenuController "1" --> "1" PersistenceService : persiste
    MenuController "1" --> "1" ConsoleView : muestra
    MenuController "1" o-- "*" Producto : maneja inventario
```

## Licencia
Este proyecto está bajo la Licencia MIT. Para más detalles, consultá el archivo LICENSE.

## Talento Tech
Desarrollado como parte de la cursada de Java Backend 26138 como ensayo de estudio, sin fines comerciales - 2026.
