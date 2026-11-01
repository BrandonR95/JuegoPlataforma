package juego;

import interfaces.Recolectable;
import recolectables.Comida;
import recolectables.Filtro;
import recolectables.Municion;
import trampas.Charco;
import trampas.Trampa;
import trampas.Viga;
import entidades.Plataforma;
import entidades.Proyectil;
import personajes.Jugador;
import personajes.Mutante;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Nivel {
    private int numero;
    private Jugador jugador;
    private List<Plataforma> plataformas = new ArrayList<>();
    private List<Mutante> mutantes = new ArrayList<>();
    private List<Proyectil> proyectiles = new ArrayList<>();
    private List<Trampa> trampas = new ArrayList<>();
    private List<Recolectable> recolectables = new ArrayList<>();

    public Nivel(int numero) {
        this.numero = numero;
        this.jugador = new Jugador(100, 300);
        construirNivel();
    }

    private void construirNivel() {
        // Suelo con huecos
        plataformas.add(new Plataforma(0, 480, 700, 60));
        Plataforma suelo2 = new Plataforma(820, 480, 500, 60);
        Plataforma suelo3 = new Plataforma(1440, 480, 400, 60);
        Plataforma suelo4 = new Plataforma(1960, 480, 1040, 60);
        plataformas.add(suelo2);
        plataformas.add(suelo3);
        plataformas.add(suelo4);
        // Plataformas flotantes
        plataformas.add(new Plataforma(400, 380, 150, 20));
        plataformas.add(new Plataforma(950, 380, 140, 20));
        plataformas.add(new Plataforma(1100, 300, 140, 20));
        plataformas.add(new Plataforma(1500, 380, 140, 20));
        plataformas.add(new Plataforma(2200, 380, 200, 20));
        // Mutantes: plataforma donde viven, x inicial, variante
        mutantes.add(new Mutante(suelo2, 1000, 1));
        mutantes.add(new Mutante(suelo3, 1600, 2));
        mutantes.add(new Mutante(suelo4, 2400, 3));
        // Trampas (apoyadas sobre el suelo, que esta en y = 480)
        trampas.add(new Charco(300, 468, 100));
        trampas.add(new Viga(1180, 420));
        trampas.add(new Charco(2050, 468, 120));
        trampas.add(new Viga(2650, 420));
        // Recolectables (sobre plataformas flotantes o sobre el suelo)
        recolectables.add(new Municion(450, 356));
        recolectables.add(new Filtro(1130, 276));
        recolectables.add(new Comida(1550, 356));
        recolectables.add(new Municion(1700, 456));
        recolectables.add(new Comida(2290, 356));
        recolectables.add(new Filtro(2450, 456));
        recolectables.add(new Comida(2850, 456));
    }

    public void actualizar() {
        jugador.actualizar();
        for (Mutante m : mutantes) {
            m.detectar(jugador);
            m.actualizar();
        }
        for (Proyectil p : proyectiles) p.actualizar();
        revisarColisiones();
        proyectiles.removeIf(p -> !p.isActivo());
        mutantes.removeIf(m -> !m.estaVivo());
    }

    public void disparar() {
        Proyectil p = jugador.disparar();
        if (p != null) proyectiles.add(p);
    }

    public void revisarColisiones() {
        for (Plataforma p : plataformas) {
            Rectangle r = jugador.getLimites();
            Rectangle l = p.getLimites();
            if (!r.intersects(l)) continue;

            Rectangle inter = r.intersection(l);
            if (inter.width < inter.height) {
                // choque lateral
                if (r.x < l.x) jugador.setX(jugador.getX() - inter.width);
                else jugador.setX(jugador.getX() + inter.width);
            } else if (r.y < l.y) {
                // aterriza sobre la plataforma
                jugador.setY(jugador.getY() - inter.height);
                jugador.setVelY(0);
                jugador.setEnSuelo(true);
            } else {
                // se golpea la cabeza
                jugador.setY(jugador.getY() + inter.height);
                jugador.setVelY(0);
            }
        }
        recolectables.removeIf(r -> {
            if (jugador.getLimites().intersects(r.getLimites())) {
                r.recoger(jugador);
                return true;
            }
            return false;
        });
        for (Trampa t : trampas) {
            if (jugador.getLimites().intersects(t.getLimites())) {
                jugador.recibirDanio(t.getDanio());
            }
        }
        for (Proyectil pr : proyectiles) {
            Rectangle rp = pr.getLimites();
            if (pr.getX() < 0 || pr.getX() > anchoMundo()) pr.desactivar();
            for (Plataforma p : plataformas) {
                if (pr.isActivo() && rp.intersects(p.getLimites())) pr.desactivar();
            }
            for (Mutante m : mutantes) {
                if (pr.isActivo() && rp.intersects(m.getLimites())) {
                    m.recibirDanio(pr.getDanio());
                    pr.desactivar();
                }
            }
        }
        for (Mutante m : mutantes) {
            if (m.estaVivo() && jugador.getLimites().intersects(m.getLimites())) {
                jugador.recibirDanio(m.getDanio());
            }
        }
        if (jugador.getX() < 0) jugador.setX(0);
    }

    public void dibujar(Graphics2D g) {
        double camX = camara();
        dibujarFondo(g, camX);

        Graphics2D mundo = (Graphics2D) g.create();
        mundo.translate(-camX, 0);
        for (Plataforma p : plataformas) p.dibujar(mundo);
        for (Trampa t : trampas) t.dibujar(mundo);
        for (Recolectable r : recolectables) r.dibujar(mundo);
        for (Mutante m : mutantes) m.dibujar(mundo);
        for (Proyectil p : proyectiles) p.dibujar(mundo);
        jugador.dibujar(mundo);
        mundo.dispose();

        // Niebla por encima de todo
        g.setColor(new Color(120, 105, 85, 45));
        g.fillRect(0, 0, Juego.ANCHO, Juego.ALTO);

        dibujarHUD(g);
    }

    private void dibujarHUD(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(14, 12, 210, 96, 14, 14);
        // barra de vida
        g.setColor(new Color(0x3a1d1d));
        g.fillRect(24, 22, 150, 12);
        g.setColor(new Color(0xc0392b));
        g.fillRect(24, 22, 150 * jugador.getVida() / 100, 12);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        g.drawString("" + jugador.getVida(), 182, 33);
        g.drawString("Filtro: " + (int) jugador.getFiltroActual() + "%  (x" + jugador.getFiltrosInventario() + ")", 24, 56);
        g.drawString("Munición: " + jugador.getMunicion(), 24, 76);
        g.drawString("Comida: " + jugador.getComida(), 24, 96);
    }

    private void dibujarFondo(Graphics2D g, double camX) {
        g.setPaint(new GradientPaint(0, 0, new Color(0x1d2327), 0, Juego.ALTO, new Color(0x7a5f45)));
        g.fillRect(0, 0, Juego.ANCHO, Juego.ALTO);

        // Dos capas de edificios en ruinas con parallax
        capaEdificios(g, camX * 0.15, new Color(0x2b2f31), 5, 160, 140);
        capaEdificios(g, camX * 0.35, new Color(0x15191b), 7, 120, 220);
    }

    private void capaEdificios(Graphics2D g, double desplazamiento, Color color, long semilla, int separacion, int altoBase) {
        Random azar = new Random(semilla);
        g.setColor(color);
        for (int i = 0; i < 40; i++) {
            int w = 60 + azar.nextInt(60);
            int h = altoBase + azar.nextInt(150);
            int px = (int) (i * separacion - desplazamiento);
            g.fillRect(px, 480 - h, w, h);
        }
    }

    private double camara() {
        double objetivo = jugador.getX() - Juego.ANCHO / 2.0;
        return Math.max(0, Math.min(objetivo, anchoMundo() - Juego.ANCHO));
    }

    private int anchoMundo() {
        int max = 0;
        for (Plataforma p : plataformas) max = Math.max(max, (int) p.getX() + p.getAncho());
        return max;
    }

    public boolean jugadorPerdio() {
        return !jugador.estaVivo() || jugador.getY() > Juego.ALTO + 100;
    }

    public Jugador getJugador() { return jugador; }
}
