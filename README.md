# Sistema de Gestión de Inventario y Pedidos - Pre-Entrega Java

Es una aplicación de consola desarrollada en Java 17 para gestionar un inventario de productos y procesar los pedidos de venta. Se diseño con principios de Programación Orientada a Objetos (POO) , código límpio y buenas prácticas.

## Características principales

- **Gestión de Inventario (CRUD):** Permite listar, agregar, buscar, modificar y eliminar productos.
- **Modelado con Herencia:** Implementación de clases especializadas (ej. `Bebida` que hereda de `Producto`) para manejar atributos específicos.
- **Sistema de Pedidos Detallado:** Creación de pedidos con múltiples ítems (`LineaPedido`), cálculo automático de subtotales y descuentos por cantidad.
- **Conceptos de POO Aplicados:** Uso de encapsulamiento, herencia, polimorfismo (sobrescritura de `toString` y `equals`) y sobrecarga de métodos y constructores.
- **Gestión de Estados y Stock:** Uso de `Enums` para controlar el flujo de los pedidos con orquestación automática de stock en caso de cancelaciones.
- **Persistencia de Datos:** Almacenamiento persistente en archivos CSV automatizado mediante el uso de la API `java.nio` (NIO.2).
- **Experiencia de Usuario (UX):** Interfaz de consola mejorada con validaciones de entrada, manejo de excepciones personalizadas y resaltado de errores mediante códigos de color ANSI.

## Tecnologías utilizadas

- **Lenguaje:** Java 17 (JDK 17)
- **Persistencia:** Archivos planos (CSV)

## Estructura del Proyecto

- `src/model`: Contiene las entidades de datos (`Producto`, `Bebida`, `Pedido`, `LineaPedido`, `EstadoPedido`).
- `src/service`: Lógica de negocio y manejo de archivos (`ProductoService`).
- `src/exceptions`: Excepciones personalizadas (`StockInsuficienteException`).
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
        +isEsAlcoholica() boolean
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
        +getProducto() Producto
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
        +listarProductos(ArrayList) void
        +listarProductosInactivos(ArrayList) void
        +procesarItemVenta(ArrayList, Pedido, int, int) double
        +reactivarProducto(ArrayList, int) void
        +actualizarEstadoPedido(ArrayList, int, EstadoPedido) void
        +guardarCSV(ArrayList) void
    }

    class StockInsuficienteException {
        +StockInsuficienteException(String mensaje)
    }

    Producto <|-- Bebida : Herencia
    Pedido "1" *-- "*" LineaPedido : Composición
    LineaPedido "1" --> "1" Producto : Referencia
    Pedido "1" --> "1" EstadoPedido : Usa
    ProductoService "1" o-- "*" Pedido : Gestiona historial
    ProductoService ..> Producto : Opera sobre
    ProductoService ..> StockInsuficienteException : Lanza
```

## Licencia
Este proyecto está bajo la Licencia MIT. Para más detalles, consultá el archivo LICENSE.

---
Desarrollado como parte de la cursada de Java Backend 26138 como ensayo de estudio, sin fines comerciales - 2026.
