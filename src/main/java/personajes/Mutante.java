package personajes;

import entidades.Plataforma;

import java.awt.Color;
import java.awt.Graphics2D;

public class Mutante extends Personaje {
    private static final double VELOCIDAD = 1.5;             // patrullando
    private static final double VELOCIDAD_PERSECUCION = 2.5; // persiguiendo
    private static final double TOLERANCIA_ALTURA = 60;      // para considerar "misma plataforma"
    private static final int TAMANIO = 40;
    private static final Color[] COLORES = {
        new Color(0x6f8a4e), new Color(0x8a4e6f), new Color(0x4e6f8a)
    };

    private int danio = 34;
    private int variante;
    private double limiteIzquierdo;
    private double limiteDerecho;
    private double rangoVision = 250;
    private boolean persiguiendo = false;

    // Vive sobre una plataforma y patrulla de borde a borde de ella
    public Mutante(Plataforma base, double xInicial, int variante) {
        super(xInicial, base.getY() - TAMANIO, TAMANIO, TAMANIO, "assets/mutante" + variante + ".png", 75);
        this.limiteIzquierdo = base.getX();
        this.limiteDerecho = base.getX() + base.getAncho() - ancho;
        this.variante = variante;
        this.velX = VELOCIDAD;
    }

    // Decide si ve al jugador: debe estar cerca y a una altura parecida (su misma plataforma)
    public void detectar(Jugador jugador) {
        double distancia = jugador.getX() - x;
        double difAltura = (jugador.getY() + jugador.getAlto()) - (y + alto);
        persiguiendo = Math.abs(distancia) <= rangoVision && Math.abs(difAltura) <= TOLERANCIA_ALTURA;

        if (persiguiendo) {
            if (Math.abs(distancia) < 4) velX = 0;               // ya lo alcanzo, evita temblar
            else velX = distancia > 0 ? VELOCIDAD_PERSECUCION : -VELOCIDAD_PERSECUCION;
        } else {
            velX = mirandoDerecha ? VELOCIDAD : -VELOCIDAD;       // vuelve a patrullar
        }
    }

    @Override
    public void actualizar() {
        x += velX;
        if (x < limiteIzquierdo) {
            x = limiteIzquierdo;
            if (!persiguiendo) velX = VELOCIDAD;
        } else if (x > limiteDerecho) {
            x = limiteDerecho;
            if (!persiguiendo) velX = -VELOCIDAD;
        }
        if (velX != 0) mirandoDerecha = velX > 0;
    }

    @Override
    public void dibujar(Graphics2D g) {
        int px = (int) x;
        int py = (int) y;
        if (imagen != null) {
            if (mirandoDerecha) g.drawImage(imagen, px, py, ancho, alto, null);
            else g.drawImage(imagen, px + ancho, py, -ancho, alto, null);
        } else {
            g.setColor(COLORES[variante - 1]);
            g.fillRect(px, py, ancho, alto);
            g.setColor(persiguiendo ? new Color(0xff2a1a) : new Color(0xd94b3a)); // ojo mas brillante al perseguir
            int ojoX = mirandoDerecha ? px + 26 : px + 6;
            g.fillRect(ojoX, py + 8, 8, 8);
        }
    }

    public int getDanio() { return danio; }
}
