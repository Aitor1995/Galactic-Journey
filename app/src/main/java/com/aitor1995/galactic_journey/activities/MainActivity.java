package com.aitor1995.galactic_journey.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;

import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.fragments.MainFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.games.Games;


public class MainActivity extends BaseActivity {
    private static final int REQUEST_ACHIEVEMENTS = 10000;
    private SoundPool soundPool;
    private int soundClickBoton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new MainFragment())
                .commit();
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundClickBoton = soundPool.load(this, R.raw.click, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mSharedPreferences.getBoolean(BaseActivity.KEY_INICIO_SESION, true)) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(KEY_INICIO_SESION, true);
            editor.apply();
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        super.onConnected(bundle);
        findViewById(R.id.buttonLogros).setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        super.onConnectionFailed(connectionResult);
        findViewById(R.id.buttonLogros).setVisibility(View.INVISIBLE);
    }

    public void onJugarClick(View view) {
        soundPool.play(soundClickBoton, 1, 1, 1, 0, 1);
        startActivity(new Intent(this, JuegoActivity.class));
    }

    public void onRecordsClick(View view) {
        soundPool.play(soundClickBoton, 1, 1, 1, 0, 1);
        startActivity(new Intent(this, RecordsActivity.class));
    }

    public void onOpcionesClick(View view) {
        soundPool.play(soundClickBoton, 1, 1, 1, 0, 1);
        startActivity(new Intent(this, OpcionesActivity.class));
    }

    public void onLogrosClick(View view) {
        soundPool.play(soundClickBoton, 1, 1, 1, 0, 1);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
        }
    }
}
