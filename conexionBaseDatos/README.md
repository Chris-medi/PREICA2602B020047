# PREICA2602B020047
Programación Orientada a Objetos II (LEONARDO MONTES) - PREICA2602B020047

## EA2 – Procesamiento funcional

```bash
javac -d out/ea3 $(find conexionBaseDatos -name "*.java")
java -cp "out/ea3:conexionBaseDatos/lib/*" aplicacion.Main
```
```


### Estructura del proyecto

```
conexionBaseDatos/
├── aplicacion/Main.java               → punto de entrada, abre la ventana en el hilo de Swing (EDT)
├── vista/ConexionTester.java          → JFrame: arma la interfaz y tiene el ActionListener del botón
├── servicio/ConexionService.java      → toda la lógica JDBC (DriverManager, try-catch, try-with-resources)
├── modelo/ResultadoConexion.java      → record con el resultado (éxito/fallo, título y detalle)
├── lib/sqlite-jdbc-3.46.1.3.jar       → driver JDBC de SQLite
└── evidencias/                        → capturas de ejecución
```

Flujo de responsabilidades:

La ventana **no sabe nada de JDBC**: solo lee los campos, llama al servicio y pinta el resultado. El servicio **no sabe nada de Swing**: recibe texto y devuelve un `ResultadoConexion`. Así cada clase tiene una sola responsabilidad y el servicio se podría reutilizar desde otra interfaz.



### Requisitos para ejecutar

- JDK 17 o superior (probado con OpenJDK 25).
- El driver ya viene en `lib/sqlite-jdbc-3.46.1.3.jar`.

> No se usa ningún framework: solo Java estándar (Swing + JDBC). JDBC (`java.sql`) es una API de interfaces; el `.jar` de `lib/` es el driver que la implementa para SQLite. Sin él aparece la `ClassNotFoundException`.

