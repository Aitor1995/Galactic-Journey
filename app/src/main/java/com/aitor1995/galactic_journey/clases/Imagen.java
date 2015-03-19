package com.aitor1995.galactic_journey.clases;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Representa una imágen y su posición de inicio
 */
public class Imagen {
    /**
     * Posicion de la imágen
     */
    public PointF posicion;
    /**
     * Bitmap de la imágen
     */
    public Bitmap imagen;

    /**
     * @param imagen bitmap de la imágen
     * @param x coordenada x de la imágen
     * @param y coordenada y de la imágen
     */
    public Imagen(Bitmap imagen, float x, float y) {
        this.imagen = imagen;
        this.posicion = new PointF(x, y);
    }
}
