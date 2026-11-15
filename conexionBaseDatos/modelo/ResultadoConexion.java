package modelo;

public record ResultadoConexion(boolean exitoso, String titulo, String detalle) {

    public static ResultadoConexion exito(String detalle) {
        return new ResultadoConexion(true, "Conexión exitosa", detalle);
    }

    public static ResultadoConexion fallo(String titulo, String detalle) {
        return new ResultadoConexion(false, titulo, detalle);
    }

    public String comoTexto() {
        return (exitoso ? "✔ " : "✘ ") + titulo + System.lineSeparator()
                + System.lineSeparator() + detalle;
    }
}
