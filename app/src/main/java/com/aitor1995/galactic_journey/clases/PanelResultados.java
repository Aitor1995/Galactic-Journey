package com.aitor1995.galactic_journey.clases;

import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;

/**
 * Representa el panel final de resutados
 */
public class PanelResultados {
    /**
     * Imágen del panel
     */
    public NinePatchDrawable imagen;

    /**
     * @param ninePatchDrawable imágen del panel
     */
    public PanelResultados(NinePatchDrawable ninePatchDrawable) {
        this.imagen = ninePatchDrawable;
    }

    /**
     * @param ninePatchDrawable imágen del panel
     * @param posicion rectángulo que ocupa
     */
    public PanelResultados(NinePatchDrawable ninePatchDrawable, Rect posicion) {
        this(ninePatchDrawable);
        this.imagen.setBounds(posicion);
    }
}
