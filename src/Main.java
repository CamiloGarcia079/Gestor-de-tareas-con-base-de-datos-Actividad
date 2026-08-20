import vista.VentanaPrincipal;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // arrancamos la interfaz grafica en el hilo de eventos de Swing (buena practica)
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
