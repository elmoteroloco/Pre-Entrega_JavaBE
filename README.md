# Sistema de Gestión de Inventario y Pedidos - Pre-Entrega Java

Es una aplicación de consola desarrollada en Java 17 para gestionar un inventario de productos y procesar pedidos de venta. El sistema sigue una arquitectura modular separando la lógica de negocio, la persistencia de datos y la interfaz de usuario.

## Características principales

**Gestión de Inventario (CRUD):** Permite listar, agregar, buscar, modificar, eliminar (desactivar) y reactivar *productos*, realizar, listar, entregar y cancelar *pedidos* y reliza un reporte de ventas mostrando el capital efectivo (*pedidos entregados*) y el crédito (*pedidos pendientes*) de las actividades realizadas. La eliminación es lógica (desactivación o "soft-delete"), permitiendo la reactivación de productos para mantener la integridad del historial de pedidos mediante la identidad consecutiva de los productos.

**Arquitectura Basada en POO:**
    - **Encapsulamiento** (validaciones en setters)
    - **Herencia** (clase `Bebida`)
    - **Polimorfismo** (sobrescritura de `toString` y `equals`)
    - **Sobrecarga** (múltiples constructores para creación y persistencia).

**Sistema de Pedidos y Lógica de Negocio:** Gestión de pedidos con múltiples líneas, cálculo automático de descuentos progresivos (10% y 20%) y orquestación de stock según el estado del pedido.

**Arquitectura Modular (SoC):** Separación de responsabilidades mediante Servicios (`ProductoService`, `PersistenceService`) y Vista (`ConsoleView`), siguiendo principios de bajo acoplamiento.

**Control de Flujo con Enums:** Uso de `EstadoPedido` para manejar el ciclo de vida de las ventas y generar reportes de caja precisos.

**Persistencia de Datos Robusta:** Almacenamiento en múltiples archivos CSV mediante la API `java.nio`, garantizando que la identidad de los productos se mantenga consistente.

**Experiencia de Usuario (UX):** Interfaz de consola mejorada con validaciones de entrada, manejo de excepciones personalizadas y resaltado de errores mediante códigos de color ANSI.

## Tecnologías utilizadas

- **Lenguaje:** Java 17 (JDK 17)
- **Persistencia:** Archivos CSV (CSV Flat Files)

## Estructura del Proyecto

- `src/model`: Contiene las entidades de datos (`Producto`, `Bebida`, `Pedido`, `LineaPedido`, `EstadoPedido`).
- `src/service`: Lógica de negocio (`ProductoService`) y servicios de persistencia (`PersistenceService`).
- `src/view`: Manejo de la interfaz de usuario por consola (`ConsoleView`).
- `src/exceptions`: Jerarquía de excepciones personalizadas (`ValidacionProductoException`, `StockInsuficienteException`).
- `src/main`: Punto de entrada de la aplicación (`Main`).

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
        +agregarProducto(ArrayList, String, double, int, boolean) void
        +buscarProductoPorCriterio(List, String) Producto
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
        +mostrarCatalogo(List) void
        +mostrarInactivos(List) void
        +mostrarHistorialPedidos(List) void
        +mostrarReporte(double, double, int) void
    }

    class StockInsuficienteException {
        +StockInsuficienteException(String mensaje)
    }

    class ValidacionProductoException {
        +ValidacionProductoException(String mensaje)
    }

    Producto <|-- Bebida : Herencia
    Pedido "1" *-- "*" LineaPedido : Composición
    LineaPedido "1" --> "1" Producto : Referencia
    Pedido "1" --> "1" EstadoPedido : Usa
    ProductoService "1" o-- "*" Pedido : Gestiona historial
    ProductoService ..> Producto : opera
    ProductoService ..> StockInsuficienteException : lanza
    ProductoService ..> ValidacionProductoException : lanza
    Main ..> ProductoService : coordina
    Main ..> PersistenceService : coordina
    Main ..> ConsoleView : visualiza
```

## Licencia
Este proyecto está bajo la Licencia MIT. Para más detalles, consultá el archivo LICENSE.

---
Desarrollado como parte de la cursada de Java Backend 26138 como ensayo de estudio, sin fines comerciales - 2026.
