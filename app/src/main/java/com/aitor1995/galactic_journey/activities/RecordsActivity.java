package com.aitor1995.galactic_journey.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ListView;

import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.adapters.AdapterRecords;
import com.aitor1995.galactic_journey.sqlite.RecordsContract;
import com.aitor1995.galactic_journey.sqlite.RecordsSQLiteHelper;

public class RecordsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);
        RecordsSQLiteHelper recordsSQLiteHelper = new RecordsSQLiteHelper(this);
        SQLiteDatabase db = recordsSQLiteHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + RecordsContract.RecordEntry.NOMBRE_TABLA, null);
        ListView lvItems = (ListView) findViewById(R.id.listView);
        AdapterRecords adapterRecords = new AdapterRecords(this, cursor, Typeface.createFromAsset(getAssets(), "fuente.otf"));
        lvItems.setAdapter(adapterRecords);
    }
}
