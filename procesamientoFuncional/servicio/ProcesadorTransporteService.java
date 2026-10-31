package servicio;

import modelo.RegistroValidador;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ProcesadorTransporteService {

    public Map<String, Long> calcularAfluenciaPorEstacion(List<RegistroValidador> registros) {
        return registros.stream()
                .filter(registro -> "entrada".equalsIgnoreCase(registro.accion()))
                .collect(Collectors.toUnmodifiableMap(
                        RegistroValidador::estacion,
                        registro -> 1L,
                        Long::sum
                ));
    }

    public Map<Integer, Long> identificarHorasPico(List<RegistroValidador> registros) {
        return registros.stream()
                .collect(Collectors.toUnmodifiableMap(
                        registro -> registro.timestamp().getHour(),
                        registro -> 1L,
                        Long::sum
                ));
    }

    public Map<String, Long> obtenerRutasMasUtilizadas(List<RegistroValidador> registros) {
        return registros.stream()
                .collect(Collectors.toUnmodifiableMap(
                        RegistroValidador::ruta,
                        registro -> 1L,
                        Long::sum
                ));
    }

    public Map<String, List<String>> obtenerPatronesDeViajePorUsuario(List<RegistroValidador> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(
                        RegistroValidador::idUsuario,
                        Collectors.collectingAndThen(
                                Collectors.toUnmodifiableList(),
                                lista -> lista.stream()
                                        .sorted(Comparator.comparing(RegistroValidador::timestamp))
                                        .map(RegistroValidador::estacion)
                                        .toList()
                        )
                ));
    }

    public double calcularTiempoPromedioEntreEstaciones(List<RegistroValidador> registros) {
        Map<String, List<RegistroValidador>> viajesPorUsuario = registros.stream()
                .collect(Collectors.groupingBy(
                        RegistroValidador::idUsuario,
                        Collectors.collectingAndThen(
                                Collectors.toUnmodifiableList(),
                                lista -> lista.stream()
                                        .sorted(Comparator.comparing(RegistroValidador::timestamp))
                                        .toList()
                        )
                ));

        OptionalDouble promedio = viajesPorUsuario.values().stream()
                .flatMapToLong(viajes -> IntStream.range(0, viajes.size() - 1)
                        .mapToLong(i -> Duration.between(viajes.get(i).timestamp(), viajes.get(i + 1).timestamp()).toMinutes()))
                .average();

        return promedio.orElse(0.0);
    }

    public List<String> detectarSobrecargaEnRutas(List<RegistroValidador> registros, long umbralCritico) {
        Map<String, Long> conteoPorRuta = obtenerRutasMasUtilizadas(registros);
        return conteoPorRuta.entrySet().stream()
                .filter(entry -> entry.getValue() > umbralCritico)
                .map(Map.Entry::getKey)
                .toList();
    }
}