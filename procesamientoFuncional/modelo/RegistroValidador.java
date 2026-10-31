package modelo;

import java.time.LocalDateTime;
import java.util.Objects;

public record RegistroValidador(
        String idUsuario,
        String ruta,
        String estacion,
        String accion,
        LocalDateTime timestamp
) {
    public RegistroValidador {
        Objects.requireNonNull(idUsuario, "idUsuario no puede ser nulo");
        Objects.requireNonNull(ruta, "ruta no puede ser nula");
        Objects.requireNonNull(estacion, "estacion no puede ser nula");
        Objects.requireNonNull(accion, "accion no puede ser nula");
        Objects.requireNonNull(timestamp, "timestamp no puede ser nulo");
    }
}