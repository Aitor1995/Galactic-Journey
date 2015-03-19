package com.aitor1995.galactic_journey.clases;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Representa una nave
 */
public class Nave extends Imagen {
    /**
     * Rectángulos de la nave
     */
    public RectF[] rectangulos = new RectF[2];
    /**
     * Vidas de la nave
     */
    public int vidas = 3;

    /**
     * @param imagen imágen de la nave
     * @param x coordenada x del punto
     * @param y coordenada y del punto
     */
    public Nave(Bitmap imagen, float x, float y) {
        super(imagen, x, y);
        this.setRectangulos();
    }

    /**
     * Establece los rectángulos para las colisiones
     */
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

    /**
     * Mueve la nave con una velocidad determinada
     * @param velocidad velocidad de movimiento de la nave
     */
    public void mover(int velocidad) {
        this.posicion.y += velocidad;
        this.setRectangulos();
    }
}
