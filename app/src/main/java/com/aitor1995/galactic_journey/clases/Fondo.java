package com.aitor1995.galactic_journey.clases;

import android.graphics.Bitmap;

/**
 * Representa el fondo del juego
 */
public class Fondo extends Imagen {

    /**
     * @param imagen bitmap del fondo
     * @param x coordenada x del punto
     * @param y coordenada y del punto
     */
    public Fondo(Bitmap imagen, float x, float y) {
        super(imagen, x, y);
    }

    /**
     * Mueve el fondo con una determinada velocidad
     * @param velocidad velocidad de movimiento del fondo
     */
    public void mover(int velocidad) {
        this.posicion.x -= velocidad;
    }
}
