package recolectables;

import entidades.Entidad;
import interfaces.Recolectable;
import personajes.Jugador;

import java.awt.Color;
import java.awt.Graphics2D;

public class Comida extends Entidad implements Recolectable {

    public Comida(double x, double y) {
        super(x, y, 24, 24, "assets/comida.png");
    }

    @Override
    public void recoger(Jugador j) {
        j.agregarComida();
    }

    @Override
    public void dibujar(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;
        if (imagen != null) {
            g.drawImage(imagen, px, py, ancho, alto, null);
        } else {
            g.setColor(new Color(0xc9792b)); // lata
            g.fillRoundRect(px + 2, py + 4, 20, 20, 6, 6);
            g.setColor(new Color(0xf2d9a0));
            g.fillRect(px + 2, py + 11, 20, 6); // etiqueta
            g.setColor(new Color(0x8c8c8c));
            g.fillRect(px + 4, py + 2, 16, 3); // tapa
        }
    }
}
