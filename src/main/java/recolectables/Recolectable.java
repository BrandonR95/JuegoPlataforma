package interfaces;

import personajes.Jugador;

public interface Recolectable extends Colisionable, Dibujable {
    void recoger(Jugador j);
}
