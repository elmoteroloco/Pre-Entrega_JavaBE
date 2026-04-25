##  Registro de Cambios

1_ Creación de estructura de directorios "No-Build" mediante Java Package (Microsoft) en VS Code.

2_ Creación de archivos principales (`Main.java` y `Producto.java`).

3_ Desarrollo básico de los archivos principales de acuerdo a las consignas de la actividad.


    > Clase Producto con sus atributos privados (id, nombre, precio, stock).
    > Contador estático para garantizar la generación de IDs únicos.
    > Uso de métodos equals() y hashCode() para comparación de objetos por ID.
    > Constructores, getters y setters.

4_ Evaluación de requisitos para el "Menú Interactivo", investigación de métodos posibles en Java, elección de While/Switch como fórmula a implementar.

5_ Implementación del Menú interactivo básico en `Main.java` de acuerdo a la consigna de la actividad práctica:

    > "Agregar producto" y lógica de "ID único".
    > "Listar productos" con formato de tabla.
    > Búsqueda por ID y por Nombre con uso de "equals".
    > Lógica de eliminación con validación.
________________________________________________________________________________________________________________________

6_ Investigación sobre posibilidades de establecer persistencia de datos en Java de forma simple en el espacio de trabajo "local". Resolución de usar archivos `.csv` y `NIO.2`, implementación del primer módulo de persistencia (`inventario.csv`).

7_ Investigación sobre buenas prácticas para estructuras de directorios simples en el manejo de inventarios comerciales con opción de ventas, uso de una estructura moderna similar a las habituales en Node y React, creación de directorios para excepciones (`/src/exceptions`), entidades (`/src/model`) y servicios (`/src/service`).

8_ Construcción de las clases `Pedido.java` y `LineaPedido.java` en `/src/model`, también se implementa la forma de modelar con "herencia" y "encapsulamiento" (se incluye la clase `Bebida.java` también en `/src/model`) según lo visto en clase utilizando los modificadores de acceso `private` y `protected`; s extiende el `Menú Inventario` para incluir las nuevas funcionalidades y agregar la mecánica correspondiente a la clase `Bebida.java`. Se inicia éste `Registro de Cambios.md`

    > Se aplica una verificación de existencia del stock solicitado en "Realizar Pedido (Venta)" (consigna).
    > Se crea la excepción personalizada "StockInsuficienteException" (consigna).
    > Cálculos de costo y descuentos automáticos (10% a partir de 50 unidades y 20% a partir de 100, segun consigna).
    > Validación de las entradas mediante métodos "leerInt" y "leerDouble" con "scanner" para evitar crasheos por ingresos inválidos.
    > Creación de la clase "Bebida" con el fin de incorporar un ejemplo de "herencia" al modelo.
    > Se modifica la clase "Producto" estableciendo sus atributos a "protegidos" (id, nombre, precio, stock) de acuerdo con la "herencia" establecida con la nueva clase "Bebida".
    > Se agrega la funcionalidad de "Modificar Producto" y la entrada correspondiente al "Menú".
    > Se agregan las entradas "Realizar Pedidos" y "Listar Pedidos" al "Menú de Inventario".
    > Para mejorar la QoL se implementa una 'llamada' al listado de productos o pedidos antes de responder cada entrada del menú, según corresponda, simplificando la búsqueda y selección de un ID cuando sea necesario.
    > Se hacen tests de funcionamiento del menú y se prueba la validación con excepciones por 'falta de stock'.

9_ Se investiga y desarrolla una mejora UX/UI agregando colores nativos (ANSI) para facilitar la legibilidad del interactivo, también se busca como agregar una opción de 'cancelación' en mitad de las operaciones mediante una excepción personalizada (CancelarException).

10_ Para agregar el concepto y uso de "enums" se establece una lógica completa de 'registro y seguimiento de estado' para los pedidos realizados, creando la entidad `EstadoPedido.java`, el correspondiente "constructor" en `Pedido.java` y los módulos persistentes `pedidos.csv` y `pedidos_detalle.csv` para manejar la relación entre pedidos y productos, su historial y cambios de estado. La mecánica agrega una lógica de "Caja" que reporta ventas y recupera stock en inventario ante las cancelaciones de pedidos:

    > Se desarrolla un reporte que calcula la recaudación real, basada en pedidos "ENTREGADOS" y ventas 'en espera' basadas en pedidos "PENDIENTES".
    > Se establece la devolución automática de stock al inventario cuando un pedido cambia el estado a "CANCELADO", asegurando que los productos vuelvan a estar disponibles.
    > Se agregan las entradas relacionadas al "Menú de Inventario": "Actualizar Estado de Pedido" y "Reporte de Ventas"
________________________________________________________________________________________________________________________

11_ Para mantener coherencia con los "históricos" durante la eliminación de productos en los listados de pedidos se investiga y resuelve el uso de "borrado lógico" (_soft-delete_), implementándose también una entrada para la re-inserción de un producto "eliminado" con éste procedimiento en el "Menú Inventario".

    > Se establece un atributo booleano ('activo'/'inactivo') en la clase "Producto" mediante sobrecarga del  "constructor".
    > También se establece un método para la persistencia del estado del booleano en el ".CSV".
    > Se ajusta la inicialización para que el contador de "IDs" retome desde el "valor máximo histórico" en el ".CSV", evitando la reutilización de índices que generaría conflictos en el listado de "Pedidos".
    > Se agrega la entrada "Reactivar Producto" al "Menú de Inventario"
________________________________________________________________________________________________________________________

12_ Se realiza un "Diagrama de Clases" en Mermaid para integrarlo en la Documentación como imagen incrustada.
________________________________________________________________________________________________________________________

13_ Inserción del script Mermaid en el Readme.md para su visualización en GitHub mediante enlace directo (fallido).
________________________________________________________________________________________________________________________

14_ Refactorización de la Documentación Readme.md incluyendo exitosamente la visualización del Diagrama de Clases del sistema.
________________________________________________________________________________________________________________________

15_ Realizados test y busqueda de errores con asistencia de inteligencia artificial, se contempla el caso de "inventario vacío" y se agrega funcionalidad para la recarga de los modulos CSV en caso de fallos.

    > Se agrega la entrada "Recargar Datos desde CSV" al "Menú de Inventario"
________________________________________________________________________________________________________________________

16_ Manejo de errores. Se corrije un bug de posible "duplicación" al reactivar un pedido "cancelado" y una validación de estado en `actualizarEstadoPedido`.
________________________________________________________________________________________________________________________

17_ Se agregan validaciones varias en "Precio" y "Stock" y se corrigen varios dialogos de interfaz.
________________________________________________________________________________________________________________________

18_ Optimización asistida con IA, modularización de persistencia y migración de validaciones a excepciones.

    > Validaciones de Nombre, Precio y Stock negativos o vacío.
________________________________________________________________________________________________________________________

19_ Se define una jerarquia de excepciones personalizadas para cumplir con la práctica de "responsabilidades únicas" creandose `NombreInvalidoException.java`, `PrecioInvalidoException.java` y `StockInvalidoException.java` y "heredando" de `ValidacionProductoException.java`.
________________________________________________________________________________________________________________________

20_ Se despejan los elementos de la interfaz centralizandolos a `ConsoleView.java` con la responsabilidad única recomendada por Buenas Prácticas.
________________________________________________________________________________________________________________________

21_ Se agrega un normalizador de nombres en `Producto.java`, también se establece la entidad `MenuController.java` que se dedica al manejo de las ejecuciones en el "Menú Inventario", dejando en `Main.java` solo la responsabilidad única de iniciar la aplicación. Se actualiza la Documentación.
________________________________________________________________________________________________________________________

22_ Diversos cambios menores en redundancia de exepciones y solucion de compatibilidad entre Windows y Zsh para normalizar "finales de líneas" (optimización asistida con IA).
________________________________________________________________________________________________________________________

23_ Se mueve el `System.out.println` del "Menú" a un método en `ConsoleView.java.`, se agregan las "excepciones heredadas" actualizando el "Diagrama de Sistema" en la Documentación. Se agrega este "Registro de Cambios" (`changelog.md`) al repositorio y se crea un "Índice" en `Readme.md` (optimización asistida con IA).
