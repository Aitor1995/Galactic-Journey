package com.aitor1995.galactic_journey.clases;

import android.graphics.Bitmap;
import android.graphics.RectF;

/**
 * Representa a un meteorito
 */
public class Meteorito extends Imagen {
    /**
     * Tamaño del meteorito
     */
    private final int tamanio;
    /**
     * Velocidad del meteorito
     */
    private final int velocidad;
    /**
     * Ángulo del meteorito
     */
    private final int angulo;
    /**
     * Rectángulos de los meteoritos
     */
    public RectF[] rectangulos = new RectF[3];

    /**
     * @param imagen bitmap del meteorito
     * @param x posicion x del punto
     * @param y posicion y del punto
     * @param tamanio tamaño del meteorito
     * @param velocidad velocidad de movimiento del meteorito
     * @param angulo angulo del meteorito
     */
    public Meteorito(Bitmap imagen, float x, float y, int tamanio, int velocidad, int angulo) {
        super(imagen, x, y);
        this.tamanio = tamanio;
        this.velocidad = velocidad;
        this.angulo = angulo;
        this.setRectangulos();
    }

    /**
     * Establece los rectangulos del meteorito dependiendo del tamaño
     */
    public void setRectangulos() {
        int anchoMeteorito = this.imagen.getWidth();
        int altoMeteorito = this.imagen.getHeight();
        float x = this.posicion.x;
        float y = this.posicion.y;
        switch (this.tamanio) {
            case 3:
                this.rectangulos[0] = new RectF(
                        (float) (x + anchoMeteorito * 0.02),
                        (float) (y + altoMeteorito * 0.27),
                        (float) (x + anchoMeteorito * 0.87),
                        (float) (y + altoMeteorito * 0.68)
                );
                this.rectangulos[1] = new RectF(
                        (float) (x + anchoMeteorito * 0.19),
                        (float) (y + altoMeteorito * 0.17),
                        (float) (x + anchoMeteorito * 0.82),
                        (float) (y + altoMeteorito * 0.89)
                );
                this.rectangulos[2] = new RectF(
                        (float) (x + anchoMeteorito * 0.29),
                        (float) (y + altoMeteorito * 0.085),
                        (float) (x + anchoMeteorito * 0.62),
                        (float) (y + altoMeteorito * 0.17)
                );
                break;
            case 2:
            case 1:
                this.rectangulos[0] = new RectF(
                        (float) (x + anchoMeteorito * 0.3),
                        (float) (y + altoMeteorito * 0.04),
                        (float) (x + anchoMeteorito * 0.9),
                        (float) (y + altoMeteorito * 0.72)
                );
                this.rectangulos[1] = new RectF(
                        (float) (x + anchoMeteorito * 0.115),
                        (float) (y + altoMeteorito * 0.23),
                        (float) (x + anchoMeteorito * 0.3),
                        (float) (y + altoMeteorito * 0.72)
                );
                this.rectangulos[2] = new RectF(
                        (float) (x + anchoMeteorito * 0.16),
                        (float) (y + altoMeteorito * 0.72),
                        (float) (x + anchoMeteorito * 0.675),
                        (float) (y + altoMeteorito * 0.86)
                );
                break;
        }
    }

    /**
     * Mueve el meteorito teniendo en cuenta su velocidad y ángulo
     */
    public void mover() {
        this.posicion.x -= this.velocidad * Math.sin(Math.toRadians(90 - this.angulo));
        this.posicion.y -= this.velocidad * Math.sin(Math.toRadians(this.angulo));
        this.setRectangulos();
    }
}
