package aplicacion;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import servicio.ConexionService;
import vista.ConexionTester;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            usarTemaDelSistema();
            ConexionTester ventana = new ConexionTester(new ConexionService());
            ventana.setVisible(true);
        });
    }

    private static void usarTemaDelSistema() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("No se pudo cargar el tema del sistema, se usa el de Swing");
        }
    }
}
