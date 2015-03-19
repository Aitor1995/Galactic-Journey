package com.aitor1995.galactic_journey.sqlite;

import android.provider.BaseColumns;

public final class RecordsContract {
    /**
     * Representa las columnas de la tabla de records
     */
    public static abstract class RecordEntry implements BaseColumns {
        public static final String NOMBRE_TABLA = "records";
        public static final String COLUMNA_NOMBRE_JUGADOR = "nombre_jugador";
        public static final String COLUMNA_PUNTUACION = "puntuacion";
        public static final String COLUMNA_FECHA = "fecha";
    }
}
