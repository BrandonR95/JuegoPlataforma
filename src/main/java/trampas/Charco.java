package trampas;

import java.awt.Color;
import java.awt.Graphics2D;

public class Charco extends Trampa {

    public Charco(double x, double y, int ancho) {
        super(x, y, ancho, 12, "assets/charco.png", 10);
    }

    @Override
    public void dibujar(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;
        if (imagen != null) {
            g.drawImage(imagen, px, py, ancho, alto, null);
        } else {
            g.setColor(new Color(0x3f8a32));
            g.fillRoundRect(px, py, ancho, alto, 10, 10);
            g.setColor(new Color(0xa6ff7a)); // brillo radiactivo
            g.fillRect(px + 6, py + 2, ancho - 12, 3);
        }
    }
}
