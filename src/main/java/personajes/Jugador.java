package personajes;

import entidades.Proyectil;
import juego.Juego;
import juego.Nivel;

import java.awt.Color;
import java.awt.Graphics2D;

public class Jugador extends Personaje {
    private static final double VELOCIDAD = 4;
    private static final double FUERZA_SALTO = -12;
    private static final double GRAVEDAD = 0.6;
    private static final double VELOCIDAD_CAIDA_MAX = 15;

    private double filtroActual = 100;
    private int filtrosInventario = 1;
    private int municion = 6;
    private int comida = 0;
    private double tiempoInvulnerable = 0;
    private boolean enSuelo = false;
    private double tiempoRecarga = 0;

    public Jugador(double x, double y) {
        super(x, y, 32, 48, "assets/jugador.png", 100);
    }

    public void moverIzquierda() {
        velX = -VELOCIDAD;
        mirandoDerecha = false;
    }

    public void moverDerecha() {
        velX = VELOCIDAD;
        mirandoDerecha = true;
    }

    public void saltar() {
        if (enSuelo) {
            velY = FUERZA_SALTO;
            enSuelo = false;
        }
    }

    public void agregarFiltro() { filtrosInventario++; }
    public void agregarMunicion(int cantidad) { municion += cantidad; }
    public void agregarComida() { comida++; }

    // Devuelve el proyectil, o null si no hay municion o aun esta en espera
    public Proyectil disparar() {
        if (municion <= 0 || tiempoRecarga > 0) return null;
        municion--;
        tiempoRecarga = 0.5;
        double px = mirandoDerecha ? x + ancho : x - Proyectil.ANCHO;
        return new Proyectil(px, y + 20, mirandoDerecha);
    }

    @Override
    public void recibirDanio(int cantidad) {
        if (tiempoInvulnerable > 0) return;
        super.recibirDanio(cantidad);
        tiempoInvulnerable = 1.0;
    }

    @Override
    public void actualizar() {
        velY = Math.min(velY + GRAVEDAD, VELOCIDAD_CAIDA_MAX);
        x += velX;
        y += velY;
        velX = 0; // se vuelve a fijar cada frame segun las teclas
        enSuelo = false; // Nivel lo vuelve a poner en true si aterriza
        if (tiempoInvulnerable > 0) tiempoInvulnerable -= Juego.DT;
        if (tiempoRecarga > 0) tiempoRecarga -= Juego.DT;
    }

    @Override
    public void dibujar(Graphics2D g) {
        // Parpadea mientras es invulnerable
        if (tiempoInvulnerable > 0 && ((int) (tiempoInvulnerable * 10)) % 2 == 0) return;

        int px = (int) x;
        int py = (int) y;
        if (imagen != null) {
            if (mirandoDerecha) g.drawImage(imagen, px, py, ancho, alto, null);
            else g.drawImage(imagen, px + ancho, py, -ancho, alto, null);
        } else {
            g.setColor(new Color(0x7d8f6a));
            g.fillRect(px, py, ancho, alto);
            g.setColor(new Color(0x1b2024)); // visor de la mascara
            int visorX = mirandoDerecha ? px + 16 : px + 4;
            g.fillRect(visorX, py + 8, 12, 10);
        }
    }

    public void setEnSuelo(boolean enSuelo) { this.enSuelo = enSuelo; }
    public int getComida() { return comida; }
    public int getMunicion() { return municion; }
    public double getFiltroActual() { return filtroActual; }
    public int getFiltrosInventario() { return filtrosInventario; }
}
