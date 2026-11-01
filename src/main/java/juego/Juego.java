package juego;

import javax.swing.*;

public class Juego {
    public static final int ANCHO = 960;
    public static final int ALTO = 540;
    public static final int FPS = 60;
    public static final double DT = 1.0 / FPS;

    public static void main(String[] arg) {
        SwingUtilities.invokeLater(() -> {
            JFrame ventana = new JFrame("Superficie");
            PanelJuego panel = new PanelJuego();
            ventana.add(panel);
            ventana.pack();
            ventana.setResizable(false);
            ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
            panel.iniciar();
        });
    }
}
