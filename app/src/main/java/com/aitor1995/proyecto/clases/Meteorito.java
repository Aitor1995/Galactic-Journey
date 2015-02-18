package com.aitor1995.proyecto.clases;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Meteorito extends Imagen {
    private final int tamanio;
    private final int velocidad;
    private final int angulo;
    public RectF[] rectangulos = new RectF[2];

    public Meteorito(Bitmap imagen, float x, float y, int tamanio, int velocidad, int angulo) {
        super(imagen, x, y);
        this.tamanio = tamanio;
        this.velocidad = velocidad;
        this.angulo = angulo;
        this.setRectangulos();
    }

    public void setRectangulos() {
        int anchoMeteorito = this.imagen.getWidth();
        int altoMeteorito = this.imagen.getHeight();
        float x = this.posicion.x;
        float y = this.posicion.y;

    }

    public void mover() {
        this.posicion.x -= this.velocidad * Math.sin(Math.toRadians(90 - this.angulo));
        this.posicion.y -= this.velocidad * Math.sin(Math.toRadians(this.angulo));
    }
}
