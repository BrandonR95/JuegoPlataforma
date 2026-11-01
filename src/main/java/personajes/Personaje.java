package personajes;

import entidades.Entidad;
import interfaces.Actualizable;

public abstract class Personaje extends Entidad implements Actualizable {
    protected int vida;
    protected double velX;
    protected double velY;
    protected boolean mirandoDerecha = true;

    public Personaje(double x, double y, int ancho, int alto, String rutaImagen, int vida) {
        super(x, y, ancho, alto, rutaImagen);
        this.vida = vida;
    }

    public void recibirDanio(int cantidad) {
        vida = Math.max(0, vida - cantidad);
    }

    public boolean estaVivo() {
        return vida > 0;
    }

    @Override
    public abstract void actualizar();

    public int getVida() { return vida; }
    public double getVelY() { return velY; }
    public void setVelY(double velY) { this.velY = velY; }
}
