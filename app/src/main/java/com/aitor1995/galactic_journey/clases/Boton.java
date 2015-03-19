package com.aitor1995.galactic_journey.clases;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;

/**
 * Representa un botón con su texto
 */
public class Boton {
    /**
     * Imágen del botón
     */
    public NinePatchDrawable imagen;
    /**
     * Texto del botón
     */
    public String texto;
    /**
     * Pincel para el texto
     */
    private Paint paint;
    /**
     * 8dp en pixels
     */
    private float dp8;
    /**
     * Rectángulo donde se va a pintar el botón
     */
    private Rect rect = new Rect();

    /**
     * @param ninePatchDrawable imágen del botón
     * @param texto texto del botón
     * @param context contexto de la aplicación
     * @param typeface tipo de letra del texto
     * @param anchoPantalla ancho del la pantalla del dispositivo
     * @param inicioY posicion en Y donde empezar a dibujar el bot´pn
     */
    public Boton(NinePatchDrawable ninePatchDrawable, String texto, Context context, Typeface typeface, int anchoPantalla, int inicioY) {
        this.imagen = ninePatchDrawable;
        this.texto = texto;
        this.paint = new Paint();
        this.paint.setTypeface(typeface);
        float dp22 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, context.getResources().getDisplayMetrics());
        this.paint.setTextSize(dp22);
        this.dp8 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
        this.paint.getTextBounds(this.texto, 0, this.texto.length(), this.rect);
        this.rect = new Rect((int) ((anchoPantalla - this.rect.width()) / 2 - this.dp8), (int) (inicioY - this.dp8), (int) ((anchoPantalla + this.rect.width()) / 2 + this.dp8), (int) (inicioY + this.rect.height() + this.dp8));
        this.imagen.setBounds(this.rect);
    }

    /**
     * Dibuja el botón en el canvas
     * @param canvas canvas donde dibujar el botón
     */
    public void dibujar(Canvas canvas) {
        this.imagen.draw(canvas);
        canvas.drawText(this.texto, this.rect.left + this.dp8, this.rect.top + this.dp8 * 3, this.paint);
    }

    /**
     * Comprueba si el punto está dentro del botón
     * @param x coordenada x del punto
     * @param y coordenada y del punto
     * @return true si esta dentro del botón o false en el caso contrario
     */
    public boolean isClickBoton(int x, int y) {
        return rect.contains(x, y);
    }
}
