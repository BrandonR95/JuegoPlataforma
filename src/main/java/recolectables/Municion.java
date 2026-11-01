package recolectables;

import entidades.Entidad;
import interfaces.Recolectable;
import personajes.Jugador;

import java.awt.Color;
import java.awt.Graphics2D;

public class Municion extends Entidad implements Recolectable {
    private int cantidad = 5;

    public Municion(double x, double y) {
        super(x, y, 24, 24, "assets/municion.png");
    }

    @Override
    public void recoger(Jugador j) {
        j.agregarMunicion(cantidad);
    }

    @Override
    public void dibujar(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;
        if (imagen != null) {
            g.drawImage(imagen, px, py, ancho, alto, null);
        } else {
            g.setColor(new Color(0x3b3b3b));
            g.fillRect(px, py + 8, 24, 16); // caja
            g.setColor(new Color(0xe0b13a)); // balas
            for (int i = 0; i < 3; i++) g.fillRect(px + 3 + i * 7, py + 1, 5, 12);
        }
    }
}
