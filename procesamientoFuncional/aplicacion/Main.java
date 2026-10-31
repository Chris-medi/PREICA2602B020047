package aplicacion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import modelo.RegistroValidador;
import servicio.ProcesadorTransporteService;

public class Main {
    public static void main(String[] args) {
        List<RegistroValidador> datosSimulados = List.of(
                new RegistroValidador("U001", "Ruta A", "Estacion San Antonio", "entrada", LocalDateTime.of(2026, 10, 31, 7, 15)),
                new RegistroValidador("U001", "Ruta A", "Estacion Parque Berrio", "salida", LocalDateTime.of(2026, 10, 31, 7, 35)),
                new RegistroValidador("U002", "Ruta B", "Estacion Poblado", "entrada", LocalDateTime.of(2026, 10, 31, 7, 20)),
                new RegistroValidador("U002", "Ruta B", "Estacion Aguacatala", "salida", LocalDateTime.of(2026, 10, 31, 7, 50)),
                new RegistroValidador("U003", "Ruta A", "Estacion San Antonio", "entrada", LocalDateTime.of(2026, 10, 31, 8, 10)),
                new RegistroValidador("U001", "Ruta B", "Estacion Poblado", "entrada", LocalDateTime.of(2026, 10, 31, 17, 30)),
                new RegistroValidador("U003", "Ruta A", "Estacion Hospital", "salida", LocalDateTime.of(2026, 10, 31, 8, 30))
        );

        ProcesadorTransporteService servicio = new ProcesadorTransporteService();

        System.out.println("Reporte");

        Map<String, Long> afluencia = servicio.calcularAfluenciaPorEstacion(datosSimulados);

        System.out.println("\nAfluencia por estacion:");

        for (Map.Entry<String, Long> entrada : new TreeMap<>(afluencia).entrySet()) {
            System.out.println(" - " + entrada.getKey() + ": " + entrada.getValue() + " entradas");
        }

        Map<Integer, Long> horasPico = servicio.identificarHorasPico(datosSimulados);

        System.out.println("\nRegistros por hora (formato 24 horas):");

        for (Map.Entry<Integer, Long> entrada : new TreeMap<>(horasPico).entrySet()) {
            String unidad = entrada.getValue() == 1 ? "registro" : "registros";
            System.out.printf(" - %02d:00: %d %s%n", entrada.getKey(), entrada.getValue(), unidad);
        }

        Map<String, Long> rutasPopulares = servicio.obtenerRutasMasUtilizadas(datosSimulados);

        System.out.println("\nUso por ruta:");

        for (Map.Entry<String, Long> entrada : new TreeMap<>(rutasPopulares).entrySet()) {
            System.out.println(" - " + entrada.getKey() + ": " + entrada.getValue() + " registros");
        }

        Map<String, List<String>> patronesViaje = servicio.obtenerPatronesDeViajePorUsuario(datosSimulados);

        System.out.println("\nPatrones de viaje por usuario:");
        
        for (Map.Entry<String, List<String>> entrada : new TreeMap<>(patronesViaje).entrySet()) {
            System.out.println(" - " + entrada.getKey() + ":");
            for (String estacion : entrada.getValue()) {
                System.out.println("    -> " + estacion);
            }
        }

        double tiempoPromedio = servicio.calcularTiempoPromedioEntreEstaciones(datosSimulados);
        System.out.println("\nTiempo promedio entre registros: " + tiempoPromedio + " minutos");

        List<String> rutasCriticas = servicio.detectarSobrecargaEnRutas(datosSimulados, 2L);
        System.out.println("\nRutas criticas (> 2 registros):");
        for (String ruta : new TreeSet<>(rutasCriticas)) {
            System.out.println(" - " + ruta);
        }
    }
}