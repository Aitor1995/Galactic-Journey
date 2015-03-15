package com.aitor1995.galactic_journey.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.adapters.AdapterRecords;
import com.aitor1995.galactic_journey.sqlite.RecordsContract.RecordEntry;
import com.aitor1995.galactic_journey.sqlite.RecordsSQLiteHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class RecordsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        RecordsSQLiteHelper recordsSQLiteHelper = new RecordsSQLiteHelper(this);
        SQLiteDatabase db = recordsSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RecordEntry.NOMBRE_TABLA + " ORDER BY " + RecordEntry.COLUMNA_PUNTUACION + " DESC, " + RecordEntry.COLUMNA_FECHA + " DESC", null);
        ListView lvItems = (ListView) findViewById(R.id.listView);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fuente.otf");
        AdapterRecords adapterRecords = new AdapterRecords(this, cursor, typeface);
        lvItems.setAdapter(adapterRecords);
        ((Button) findViewById(R.id.buttonMenu)).setTypeface(typeface);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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
