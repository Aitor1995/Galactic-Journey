package com.aitor1995.proyecto.activities;

import android.os.Bundle;

import com.aitor1995.proyecto.utils.AjustesApp;
import com.aitor1995.proyecto.views.JuegoSurfaceView;

public class JuegoActivity extends BaseActivity {
    private JuegoSurfaceView juegoSurfaceView;
    private boolean pausa = false;
    private AjustesApp ajustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ajustes = AjustesApp.getInstance(this);
        this.juegoSurfaceView = new JuegoSurfaceView(this);
        setContentView(juegoSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.ajustes.musica) {
            this.pausa = true;
            this.juegoSurfaceView.mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.ajustes.musica && pausa) {
            this.juegoSurfaceView.mediaPlayer.start();
        }
    }
}
