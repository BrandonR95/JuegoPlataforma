package entidades;

import interfaces.Actualizable;

import java.awt.Color;
import java.awt.Graphics2D;

public class Proyectil extends Entidad implements Actualizable {
    public static final int ANCHO = 14;
    private static final double VELOCIDAD = 10;

    private double velX;
    private int danio = 25;
    private boolean activo = true;

    public Proyectil(double x, double y, boolean haciaDerecha) {
        super(x, y, ANCHO, 6, "assets/proyectil.png");
        this.velX = haciaDerecha ? VELOCIDAD : -VELOCIDAD;
    }

    @Override
    public void actualizar() {
        x += velX;
    }

    @Override
    public void dibujar(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;
        if (imagen != null) {
            g.drawImage(imagen, px, py, ancho, alto, null);
        } else {
            g.setColor(new Color(0xffc857));
            g.fillRect(px, py, ancho, alto);
            g.setColor(new Color(0xfff2c4));
            g.fillRect(px + 3, py + 2, ancho - 6, 2);
        }
    }

    public int getDanio() { return danio; }
    public boolean isActivo() { return activo; }
    public void desactivar() { activo = false; }
}
