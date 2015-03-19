package com.aitor1995.galactic_journey.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aitor1995.galactic_journey.R;

/**
 * Ajustes de la aplicación
 */
public class AjustesApp {
    public static AjustesApp ajustes;

    /**
     * Crea una nueva instancia de la clase si esta no existe
     * @param context contexto de la aplicacion
     * @return ajustes de la aplicacion
     */
    public static AjustesApp getInstance(Context context) {
        if (ajustes == null) {
            ajustes = new AjustesApp(context);
        }
        return ajustes;
    }

    /**
     * Invalida los ajustes guardados en la clase
     */
    public static void invalidate() {
        ajustes = null;
    }

    /**
     * Archivo donde se guardan las preferencias
     */
    private SharedPreferences sharedPreferences;

    /**
     * Tipo de control de juego
     */
    public String controlJuego;
    /**
     * Si quiere o no música
     */
    public boolean musica;
    /**
     * Nivel del volumen de la música
     */
    public int volumenMusica;

    /**
     * @param context contexto de la aplicacion
     */
    private AjustesApp(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        this.controlJuego = this.sharedPreferences.getString(context.getString(R.string.pref_control_juego), "giroscopo");
        this.musica = this.sharedPreferences.getBoolean(context.getString(R.string.pref_musica), true);
        this.volumenMusica = this.sharedPreferences.getInt(context.getString(R.string.pref_valor_volumen), 100);
    }
}
