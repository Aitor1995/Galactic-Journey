package com.aitor1995.galactic_journey.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.adapters.AdapterRecords;
import com.aitor1995.galactic_journey.sqlite.RecordsContract.RecordEntry;
import com.aitor1995.galactic_journey.sqlite.RecordsSQLiteHelper;
import com.aitor1995.galactic_journey.utils.AjustesApp;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;

public class RecordsActivity extends BaseActivity {
    /**
     * Código para el resultCode
     */
    private static final int REQUEST_LEADERBOARD = 10000;
    /**
     * Gestiona los efectos de la aplicación
     */
    private SoundPool soundPool;
    /**
     * Efecto de click a un botón
     */
    private int soundClickBoton;
    /**
     * Ajustes de la aplicación
     */
    private AjustesApp ajustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.ajustes = AjustesApp.getInstance(this);
        setContentView(R.layout.activity_records);
        RecordsSQLiteHelper recordsSQLiteHelper = new RecordsSQLiteHelper(this);
        SQLiteDatabase db = recordsSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RecordEntry.NOMBRE_TABLA + " ORDER BY " + RecordEntry.COLUMNA_PUNTUACION + " DESC, " + RecordEntry.COLUMNA_FECHA + " DESC", null);
        ListView lvItems = (ListView) findViewById(R.id.listView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fuente.otf");
        AdapterRecords adapterRecords = new AdapterRecords(this, cursor, typeface);
        lvItems.setAdapter(adapterRecords);
        ((Button) findViewById(R.id.buttonMenu)).setTypeface(typeface);
        ((Button) findViewById(R.id.buttonMarcadores)).setTypeface(typeface);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (!mSharedPreferences.getBoolean(BaseActivity.KEY_INICIO_SESION, true)) {
            findViewById(R.id.buttonMarcadores).setVisibility(View.INVISIBLE);
        }
        if (this.ajustes.musica) {
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            soundClickBoton = soundPool.load(this, R.raw.click, 1);
        }
    }

    public void onMenuClick(View view) {
        if (this.ajustes.musica)
            soundPool.play(soundClickBoton, this.ajustes.volumenMusica, this.ajustes.volumenMusica, 1, 0, 1);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
    }

    public void onMarcadoresClick(View view) {
        if (this.ajustes.musica)
            soundPool.play(soundClickBoton, this.ajustes.volumenMusica, this.ajustes.volumenMusica, 1, 0, 1);
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, getString(R.string.leaderboard_marcador)), REQUEST_LEADERBOARD);
        }
    }
}
