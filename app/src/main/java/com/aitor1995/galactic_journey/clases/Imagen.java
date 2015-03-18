package com.aitor1995.galactic_journey.clases;

import android.graphics.Bitmap;
import android.graphics.PointF;

/**
 * Clase que representa una imagen y su posicion de inicio
 */
public class Imagen {
    public PointF posicion;
    public Bitmap imagen;

    /**
     * Crea una imagen
     * @param imagen bitmap de la imagen
     * @param x coordenada x de la imagen
     * @param y coordenada y de la imagen
     */
    public Imagen(Bitmap imagen, float x, float y) {
        this.imagen = imagen;
        this.posicion = new PointF(x, y);
    }
}
