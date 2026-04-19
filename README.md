# Sistema de Gestión de Inventario y Pedidos - Pre-Entrega Java

Este proyecto es una aplicación de consola robusta desarrollada en Java 17 para la gestión de un inventario de productos y el procesamiento de pedidos de venta. Fue diseñado siguiendo principios de Programación Orientada a Objetos (POO) y buenas prácticas de desarrollo.

## Características principales

- **Gestión de Inventario (CRUD):** Permite listar, agregar, buscar, modificar y eliminar productos.
- **Modelado con Herencia:** Implementación de clases especializadas (ej. `Bebida` que hereda de `Producto`) para manejar atributos específicos.
- **Sistema de Pedidos Detallado:** Creación de pedidos con múltiples ítems (`LineaPedido`), cálculo automático de subtotales y descuentos por cantidad.
- **Gestión de Estados:** Uso de `Enums` para controlar el flujo de los pedidos (`PENDIENTE`, `ENTREGADO`, `CANCELADO`) con orquestación automática de stock en caso de cancelaciones.
- **Persistencia de Datos:** Almacenamiento persistente mediante archivos CSV utilizando la API `java.nio` (NIO.2).
- **Experiencia de Usuario (UX):** Interfaz de consola mejorada con validaciones de entrada, manejo de excepciones personalizadas y resaltado de errores mediante códigos de color ANSI.

## Tecnologías utilizadas

- **Lenguaje:** Java 17 (JDK 17)
- **Entorno:** Visual Studio Code
- **Terminal:** Zsh / MSYS2
- **Persistencia:** Archivos planos (CSV)

## Estructura del Proyecto

- `src/model`: Contiene las entidades de datos (`Producto`, `Bebida`, `Pedido`, `LineaPedido`, `EstadoPedido`).
- `src/service`: Lógica de negocio y manejo de archivos (`ProductoService`).
- `src/exceptions`: Excepciones personalizadas (`StockInsuficienteException`).
- `src/main`: Punto de entrada de la aplicación (`Main`).

## Ejecución

Para compilar el proyecto:
```bash
javac -d bin src/**/*.java
```

Para ejecutar la aplicación:
```bash
java -cp bin main.Main
```

## Licencia
Este proyecto está bajo la Licencia MIT. Para más detalles, consultá el archivo LICENSE.

---
Desarrollado como parte de la cursada de Java Backend 26138 y como ensayo de estudio sin fines comerciales - 2026.
