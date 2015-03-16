package com.aitor1995.galactic_journey.activities;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.aitor1995.galactic_journey.utils.AjustesApp;
import com.aitor1995.galactic_journey.views.JuegoSurfaceView;

public class JuegoActivity extends BaseActivity {
    private JuegoSurfaceView juegoSurfaceView;
    private boolean pausa = false;
    private AjustesApp ajustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ajustes = AjustesApp.getInstance(this);
        this.juegoSurfaceView = new JuegoSurfaceView(this, mGoogleApiClient);
        this.juegoSurfaceView.setKeepScreenOn(true);
        this.juegoSurfaceView.setFocusable(true);
        this.juegoSurfaceView.setFocusableInTouchMode(true);
        setContentView(juegoSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (this.ajustes.musica) {
            this.pausa = true;
            this.juegoSurfaceView.mediaPlayer.pause();
        }
        if (this.ajustes.controlJuego.equals("giroscopo")) {
            this.juegoSurfaceView.sensorManager.unregisterListener(this.juegoSurfaceView);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.ajustes.musica && pausa) {
            this.juegoSurfaceView.mediaPlayer.start();
        }
        if (this.ajustes.controlJuego.equals("giroscopo")) {
            this.juegoSurfaceView.sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            this.juegoSurfaceView.sensorManager.registerListener(this.juegoSurfaceView, this.juegoSurfaceView.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
            this.juegoSurfaceView.sensorManager.registerListener(this.juegoSurfaceView, this.juegoSurfaceView.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_GAME);
        }
    }
}
