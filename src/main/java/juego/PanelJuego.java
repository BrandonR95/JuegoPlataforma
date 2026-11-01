package juego;

import personajes.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class PanelJuego extends JPanel implements Runnable, KeyListener {
    private List<Nivel> niveles = new ArrayList<>();
    private int nivelActual = 0;
    private boolean enEjecucion = false;

    private volatile boolean izquierda;
    private volatile boolean derecha;
    private volatile boolean arriba;
    private volatile boolean disparando;

    public PanelJuego() {
        setPreferredSize(new Dimension(Juego.ANCHO, Juego.ALTO));
        setFocusable(true);
        addKeyListener(this);
        niveles.add(new Nivel(1));
    }

    public void iniciar() {
        enEjecucion = true;
        new Thread(this).start();
        requestFocusInWindow();
    }

    @Override
    public void run() {
        long duracionFrame = 1_000_000_000L / Juego.FPS;
        while (enEjecucion) {
            long inicio = System.nanoTime();
            actualizarJuego();
            repaint();
            long sobrante = duracionFrame - (System.nanoTime() - inicio);
            try {
                Thread.sleep(Math.max(1, sobrante / 1_000_000));
            } catch (InterruptedException e) {
                enEjecucion = false;
            }
        }
    }

    private void actualizarJuego() {
        Nivel nivel = niveles.get(nivelActual);
        Jugador jugador = nivel.getJugador();
        if (izquierda) jugador.moverIzquierda();
        if (derecha) jugador.moverDerecha();
        if (arriba) jugador.saltar();
        if (disparando) nivel.disparar();

        nivel.actualizar();

        if (nivel.jugadorPerdio()) {
            niveles.set(nivelActual, new Nivel(nivelActual + 1)); // reinicia el nivel
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        niveles.get(nivelActual).dibujar(g2);
    }

    @Override
    public void keyPressed(KeyEvent e) { cambiarTecla(e.getKeyCode(), true); }

    @Override
    public void keyReleased(KeyEvent e) { cambiarTecla(e.getKeyCode(), false); }

    @Override
    public void keyTyped(KeyEvent e) { }

    private void cambiarTecla(int codigo, boolean presionada) {
        switch (codigo) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> izquierda = presionada;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> derecha = presionada;
            case KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_SPACE -> arriba = presionada;
            case KeyEvent.VK_J, KeyEvent.VK_X -> disparando = presionada;
            default -> { }
        }
    }
}
