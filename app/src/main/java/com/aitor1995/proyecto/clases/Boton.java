package com.aitor1995.proyecto.clases;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;

public class Boton {
    public NinePatchDrawable imagen;
    public String texto;
    private int anchoPantalla;
    private int inicioY;
    private Paint paint;


    public Boton(NinePatchDrawable ninePatchDrawable, String texto, Typeface typeface, int anchoPantalla, int inicioY) {
        this.imagen = ninePatchDrawable;
        this.texto = texto;
        this.anchoPantalla = anchoPantalla;
        this.inicioY = inicioY;
        this.paint = new Paint();
        this.paint.setTypeface(typeface);
        //this.paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, context.getResources().getDisplayMetrics()));
    }

    public void dibujar(Canvas canvas) {
        this.imagen.draw(canvas);
        canvas.drawText(this.texto, 0, 0, this.paint);
    }
}
