package recolectables;

import entidades.Entidad;
import interfaces.Recolectable;
import personajes.Jugador;

import java.awt.Color;
import java.awt.Graphics2D;

public class Filtro extends Entidad implements Recolectable {

    public Filtro(double x, double y) {
        super(x, y, 24, 24, "assets/filtro.png");
    }

    @Override
    public void recoger(Jugador j) {
        j.agregarFiltro();
    }

    @Override
    public void dibujar(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;
        if (imagen != null) {
            g.drawImage(imagen, px, py, ancho, alto, null);
        } else {
            g.setColor(new Color(0x5b7f95));
            g.fillRoundRect(px + 2, py, 20, 24, 8, 8);
            g.setColor(new Color(0xbfe3f2));
            g.fillRect(px + 6, py + 6, 12, 3);
            g.fillRect(px + 6, py + 12, 12, 3);
            g.fillRect(px + 6, py + 18, 12, 3);
        }
    }
}
