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
        this.rectangulos[0] = new RectF(
                (float) (x + anchoNave * 0.4),
                y,
                (float) (x + anchoNave * 0.6),
                altoNave
        );
        this.rectangulos[1] = new RectF(
                x,
                (float) (y + altoNave * 0.4),
                anchoNave,
                (float) (y + altoNave * 0.8)
        );
    }
}
