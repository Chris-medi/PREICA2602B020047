package vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import modelo.ResultadoConexion;
import servicio.ConexionService;

public class ConexionTester extends JFrame {

    private static final Logger LOG = Logger.getLogger(ConexionTester.class.getName());

    private static final Color VERDE = new Color(0, 128, 0);
    private static final Color ROJO = new Color(200, 0, 0);
    private static final String URL_POR_DEFECTO = "jdbc:sqlite:inventario.db";

    private ConexionService conexionService;

    private JTextField campoUrl;
    private JTextField campoUsuario;
    private JPasswordField campoContrasena;
    private JButton botonProbar;
    private JTextArea areaResultado;

    public ConexionTester(ConexionService conexionService) {
        super("Probador de conexiones JDBC - SQLite");
        this.conexionService = conexionService;
        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        panelPrincipal.add(crearEncabezado(), BorderLayout.NORTH);
        panelPrincipal.add(crearPanelFormulario(), BorderLayout.CENTER);
        panelPrincipal.add(crearPanelResultado(), BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        getRootPane().setDefaultButton(botonProbar);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new GridLayout(2, 1));

        JLabel titulo = new JLabel("Prueba de conexión a base de datos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));

        JLabel ayuda = new JLabel("Para SQLite deja usuario y contraseña vacíos.");
        ayuda.setForeground(Color.GRAY);

        encabezado.add(titulo);
        encabezado.add(ayuda);
        return encabezado;
    }

    private JPanel crearPanelFormulario() {
        JPanel formulario = new JPanel(new GridLayout(3, 2, 8, 8));
        formulario.setBorder(BorderFactory.createTitledBorder("Datos de conexión"));

        campoUrl = new JTextField(URL_POR_DEFECTO, 30);
        campoUsuario = new JTextField(30);
        campoContrasena = new JPasswordField(30);

        formulario.add(new JLabel("URL de conexión:"));
        formulario.add(campoUrl);
        formulario.add(new JLabel("Usuario:"));
        formulario.add(campoUsuario);
        formulario.add(new JLabel("Contraseña:"));
        formulario.add(campoContrasena);
        return formulario;
    }

    private JPanel crearPanelResultado() {
        JPanel panelResultado = new JPanel(new BorderLayout(5, 5));

        botonProbar = new JButton("Probar conexión");
        botonProbar.addActionListener(this::probarConexion);

        areaResultado = new JTextArea(8, 40);
        areaResultado.setEditable(false);
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);
        areaResultado.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        areaResultado.setText("Aquí aparecerá el resultado de la prueba.");

        JScrollPane scroll = new JScrollPane(areaResultado);
        scroll.setBorder(BorderFactory.createTitledBorder("Resultado"));

        panelResultado.add(botonProbar, BorderLayout.NORTH);
        panelResultado.add(scroll, BorderLayout.CENTER);
        return panelResultado;
    }

    private void probarConexion(ActionEvent evento) {
        LOG.info("Se presiono el boton Probar conexion");

        String url = campoUrl.getText();
        String usuario = campoUsuario.getText();
        String contrasena = new String(campoContrasena.getPassword());

        ResultadoConexion resultado = conexionService.probarConexion(url, usuario, contrasena);
        mostrarResultado(resultado);
    }

    private void mostrarResultado(ResultadoConexion resultado) {
        if (resultado.exitoso()) {
            areaResultado.setForeground(VERDE);
        } else {
            areaResultado.setForeground(ROJO);
        }
        areaResultado.setText(resultado.comoTexto());
        areaResultado.setCaretPosition(0);
    }
}
