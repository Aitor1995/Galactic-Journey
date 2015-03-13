package com.aitor1995.galactic_journey.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aitor1995.galactic_journey.BuildConfig;
import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.adapters.AdapterRecords;
import com.aitor1995.galactic_journey.sqlite.RecordsContract.RecordEntry;
import com.aitor1995.galactic_journey.sqlite.RecordsSQLiteHelper;
import com.amazon.device.ads.AdLayout;
import com.amazon.device.ads.AdRegistration;

public class RecordsActivity extends BaseActivity {

    private AdLayout adView;
    private boolean funcionando = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        AdRegistration.setAppKey("9d9e772de63240418e46b953f837741b");
        if (BuildConfig.DEBUG) {
            AdRegistration.enableTesting(true);
            AdRegistration.enableLogging(true);
        }
        RecordsSQLiteHelper recordsSQLiteHelper = new RecordsSQLiteHelper(this);
        SQLiteDatabase db = recordsSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RecordEntry.NOMBRE_TABLA + " ORDER BY " + RecordEntry.COLUMNA_PUNTUACION + " DESC, " + RecordEntry.COLUMNA_FECHA + " DESC", null);
        ListView lvItems = (ListView) findViewById(R.id.listView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fuente.otf");
        AdapterRecords adapterRecords = new AdapterRecords(this, cursor, typeface);
        lvItems.setAdapter(adapterRecords);
        ((Button) findViewById(R.id.buttonMenu)).setTypeface(typeface);
        this.adView = (AdLayout) findViewById(R.id.adview);
        this.adView.loadAd();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (funcionando) {
                    try {
                        Thread.sleep(40000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    adView.loadAd();
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.funcionando = false;
        this.adView.destroy();
    }

    public void onMenuClick(View view) {
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
}
