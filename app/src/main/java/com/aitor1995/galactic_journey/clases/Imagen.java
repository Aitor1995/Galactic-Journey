package com.aitor1995.galactic_journey.clases;

import android.graphics.Bitmap;
import android.graphics.PointF;

public class Imagen {
    public PointF posicion;
    public Bitmap imagen;

    public Imagen(Bitmap imagen, float x, float y) {
        this.imagen = imagen;
        this.posicion = new PointF(x, y);
    }
}
