package trampas;

import java.awt.Color;
import java.awt.Graphics2D;

public class Viga extends Trampa {

    public Viga(double x, double y) {
        super(x, y, 22, 60, "assets/viga.png", 20);
    }

    @Override
    public void dibujar(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;
        if (imagen != null) {
            g.drawImage(imagen, px, py, ancho, alto, null);
        } else {
            g.setColor(new Color(0x6b3f2a));
            g.fillRect(px, py + 10, ancho, alto - 10);
            // punta rota
            int[] xs = {px, px + 6, px + 11, px + 16, px + ancho};
            int[] ys = {py + 10, py, py + 8, py + 2, py + 10};
            g.fillPolygon(xs, ys, xs.length);
            g.setColor(new Color(0x9a5b3a)); // oxido
            g.fillRect(px + 3, py + 14, 3, alto - 18);
        }
    }
}
