package servicio;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import modelo.ResultadoConexion;

public class ConexionService {

    private static final Logger LOG = Logger.getLogger(ConexionService.class.getName());
    private static final String DRIVER_SQLITE = "org.sqlite.JDBC";

    public ResultadoConexion probarConexion(String url, String usuario, String contrasena) {
        if (url == null || url.isBlank()) {
            return ResultadoConexion.fallo("Falta la URL",
                    "Escribe una URL de conexión, por ejemplo: jdbc:sqlite:inventario.db");
        }

        url = url.trim();
        LOG.info("Intentando conectar a " + url);

        try {
            validarUrlSqlite(url);
            Class.forName(DRIVER_SQLITE);
            LOG.info("Driver de SQLite cargado");

            try (Connection conexion = abrirConexion(url, usuario, contrasena)) {
                DatabaseMetaData datos = conexion.getMetaData();
                String detalle = "Motor: " + datos.getDatabaseProductName() + " " + datos.getDatabaseProductVersion()
                        + "\nDriver: " + datos.getDriverName() + " " + datos.getDriverVersion()
                        + "\nURL: " + datos.getURL()
                        + "\nLa conexión se cerró correctamente al terminar la prueba.";
                LOG.info("Conexion exitosa, se cierra la conexion");
                return ResultadoConexion.exito(detalle);
            }

        } catch (ClassNotFoundException e) {
            LOG.warning("No se encontro el driver: " + e.getMessage());
            return ResultadoConexion.fallo("Fallo de conexión: driver no encontrado",
                    "No se encontró la clase " + e.getMessage() + " en el classpath."
                            + "\nRevisa que el .jar del driver esté en la carpeta lib/ y que lo incluyas al ejecutar (-cp).");

        } catch (SQLException e) {
            LOG.warning("Error al conectar: " + e.getMessage());
            return ResultadoConexion.fallo("Fallo de conexión",
                    "Mensaje técnico: " + e.getMessage()
                            + "\nSQLState: " + e.getSQLState() + " | Código: " + e.getErrorCode());
        }
    }

    private Connection abrirConexion(String url, String usuario, String contrasena) throws SQLException {
        if (usuario == null || usuario.isBlank()) {
            return DriverManager.getConnection(url);
        }
        return DriverManager.getConnection(url, usuario.trim(), contrasena);
    }

    private void validarUrlSqlite(String url) throws SQLException {
        if (!url.toLowerCase().startsWith("jdbc:sqlite:")) {
            throw new SQLException("La URL \"" + url + "\" no es de SQLite. "
                    + "Debe empezar por jdbc:sqlite:, por ejemplo jdbc:sqlite:inventario.db");
        }
    }
}
