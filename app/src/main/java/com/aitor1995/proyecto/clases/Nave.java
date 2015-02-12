package com.aitor1995.proyecto.clases;

import android.graphics.Bitmap;
import android.graphics.RectF;

public class Nave extends Imagen {
    public RectF[] rectangulos = new RectF[2];

    public Nave(Bitmap imagen, float x, float y) {
        super(imagen, x, y);
        this.setRectangulos();
    }

    public void setRectangulos() {
        int anchoNave = this.imagen.getWidth();
        int altoNave = this.imagen.getHeight();
        float x = this.posicion.x;
        float y = this.posicion.y;
        //Rectangulo alas
        this.rectangulos[0] = new RectF(
                (float) (x + anchoNave * 0.15),
                y,
                (float) (x + anchoNave * 0.59),
                y + altoNave
        );
        //Rectangulo cuerpo
        this.rectangulos[1] = new RectF(
                x,
                (float) (y + altoNave * 0.41),
                x + anchoNave,
                (float) (y + altoNave * 0.59)
        );
    }
}
