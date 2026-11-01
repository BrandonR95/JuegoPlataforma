package entidades;

import interfaces.Colisionable;
import interfaces.Dibujable;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;

public abstract class Entidad implements Dibujable, Colisionable {
    protected double x;
    protected double y;
    protected int ancho;
    protected int alto;
    protected Image imagen;

    public Entidad(double x, double y, int ancho, int alto, String rutaImagen) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.imagen = cargarImagen(rutaImagen);
    }

    // Si la imagen no existe devuelve null y cada clase dibuja una figura de respaldo.
    private static Image cargarImagen(String ruta) {
        if (ruta == null) return null;
        try {
            File archivo = new File(ruta);
            return archivo.exists() ? ImageIO.read(archivo) : null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Rectangle getLimites() {
        return new Rectangle((int) x, (int) y, ancho, alto);
    }

    @Override
    public abstract void dibujar(Graphics2D g);

    public double getX() { return x; }
    public double getY() { return y; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public int getAncho() { return ancho; }
    public int getAlto() { return alto; }
}
