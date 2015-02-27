package com.aitor1995.proyecto.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aitor1995.proyecto.sqlite.RecordsContract.RecordEntry;

public class RecordsSQLiteHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Proyecto.db";
    public static final int DATABASE_VERSION = 1;

    public RecordsSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RecordEntry.NOMBRE_TABLA + " (" +
                RecordEntry._ID + " INTEGER PRIMARY KEY," +
                RecordEntry.COLUMNA_NOMBRE_JUGADOR + " TEXT," +
                RecordEntry.COLUMNA_PUNTUACION + " INTEGER," +
                RecordEntry.COLUMNA_FECHA + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                " );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Posible actualización futura
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onUpgrade(db, oldVersion, newVersion);
    }
}
