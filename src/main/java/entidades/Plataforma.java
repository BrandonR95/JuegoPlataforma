package entidades;

import java.awt.Color;
import java.awt.Graphics2D;

public class Plataforma extends Entidad {

    public Plataforma(double x, double y, int ancho, int alto) {
        super(x, y, ancho, alto, "assets/plataforma.png");
    }

    @Override
    public void dibujar(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;
        if (imagen != null) {
            for (int dx = 0; dx < ancho; dx += imagen.getWidth(null)) {
                g.drawImage(imagen, px + dx, py, null);
            }
        } else {
            g.setColor(new Color(0x2e2a26));
            g.fillRect(px, py, ancho, alto);
            g.setColor(new Color(0x6b5b48)); // borde superior
            g.fillRect(px, py, ancho, 5);
        }
    }
}
