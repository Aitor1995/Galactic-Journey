package com.aitor1995.galactic_journey.clases;

import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;

public class PanelResultados {
    public NinePatchDrawable imagen;

    public PanelResultados(NinePatchDrawable ninePatchDrawable) {
        this.imagen = ninePatchDrawable;
    }

    public PanelResultados(NinePatchDrawable ninePatchDrawable, Rect posicion) {
        this(ninePatchDrawable);
        this.imagen.setBounds(posicion);
    }
}
