package com.aitor1995.galactic_journey.clases;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.util.TypedValue;

public class Boton {
    public NinePatchDrawable imagen;
    public String texto;
    private Paint paint;
    private float dp6;
    private Rect rect = new Rect();

    public Boton(NinePatchDrawable ninePatchDrawable, String texto, Context context, Typeface typeface, int anchoPantalla, int inicioY) {
        this.imagen = ninePatchDrawable;
        this.texto = texto;
        this.paint = new Paint();
        this.paint.setTypeface(typeface);
        float dp22 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, context.getResources().getDisplayMetrics());
        this.paint.setTextSize(dp22);
        this.dp6 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, context.getResources().getDisplayMetrics());
        this.paint.getTextBounds(this.texto, 0, this.texto.length(), this.rect);
        this.rect = new Rect((int) ((anchoPantalla - this.rect.width()) / 2 - this.dp6), (int) (inicioY - this.dp6), (int) ((anchoPantalla + this.rect.width()) / 2 + this.dp6), (int) (inicioY + this.rect.height() + this.dp6));
        this.imagen.setBounds(this.rect);
    }

    public void dibujar(Canvas canvas) {
        this.imagen.draw(canvas);
        canvas.drawText(this.texto, this.rect.left + this.dp6, this.rect.top + this.dp6 * 3, this.paint);
    }

    public boolean isClickBoton(int x, int y) {
        return rect.contains(x, y);
    }
}
