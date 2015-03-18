package com.aitor1995.galactic_journey.clases;

import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;

/**
 * Clase que representa el panel final de resutados
 */
public class PanelResultados {
    public NinePatchDrawable imagen;

    /**
     * Crea un panel de resultado con una imagen
     * @param ninePatchDrawable imagen del panel
     */
    public PanelResultados(NinePatchDrawable ninePatchDrawable) {
        this.imagen = ninePatchDrawable;
    }

    /**
     * Crea un panel de resultados con una imagen y el rectangulo que va a ocupar
     * @param ninePatchDrawable imagen del panel
     * @param posicion rectangulo que ocupa
     */
    public PanelResultados(NinePatchDrawable ninePatchDrawable, Rect posicion) {
        this(ninePatchDrawable);
        this.imagen.setBounds(posicion);
    }
}
