package com.aitor1995.proyecto.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aitor1995.proyecto.R;

public class AjustesApp {
    public static AjustesApp ajustes;

    public static AjustesApp getInstance(Context context) {
        if (ajustes == null) {
            ajustes = new AjustesApp(context);
        }
        return ajustes;
    }

    public static void invalidate() {
        ajustes = null;
    }

    private SharedPreferences sharedPreferences;

    public String controlJuego;
    public boolean musica;
    public int volumenMusica;

    private AjustesApp(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        this.controlJuego = this.sharedPreferences.getString(context.getString(R.string.pref_control_juego), "tactil");
        this.musica = this.sharedPreferences.getBoolean(context.getString(R.string.pref_musica), true);
        this.volumenMusica = this.sharedPreferences.getInt(context.getString(R.string.pref_valor_volumen), 100);
    }
}
