package trampas;

import entidades.Entidad;

public abstract class Trampa extends Entidad {
    protected int danio;

    public Trampa(double x, double y, int ancho, int alto, String rutaImagen, int danio) {
        super(x, y, ancho, alto, rutaImagen);
        this.danio = danio;
    }

    public int getDanio() { return danio; }
}
