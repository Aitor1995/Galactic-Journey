package com.aitor1995.proyecto.clases;

import android.graphics.Bitmap;

public class Fondo extends Imagen {

    public Fondo(Bitmap imagen, float x, float y) {
        super(imagen, x, y);
    }

    public void mover(int velocidad) {
        this.posicion.x -= velocidad;
    }
}
