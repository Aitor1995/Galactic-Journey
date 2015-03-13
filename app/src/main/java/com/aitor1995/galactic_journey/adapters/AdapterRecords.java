package com.aitor1995.galactic_journey.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.sqlite.RecordsContract;

public class AdapterRecords extends CursorAdapter {
    private Typeface typeface;

    class ViewHolder {
        TextView textViewNombre;
        TextView textViewFecha;
        TextView textViewPuntuacion;
    }

    public AdapterRecords(Context context, Cursor c, Typeface typeface) {
        super(context, c, 0);
        this.typeface = typeface;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new ViewHolder();
            viewHolder.textViewNombre = (TextView) view.findViewById(R.id.textViewNombre);
            viewHolder.textViewNombre.setTypeface(this.typeface);
            viewHolder.textViewFecha = (TextView) view.findViewById(R.id.textViewFecha);
            viewHolder.textViewFecha.setTypeface(this.typeface);
            viewHolder.textViewPuntuacion = (TextView) view.findViewById(R.id.textViewPuntuacion);
            viewHolder.textViewPuntuacion.setTypeface(this.typeface);
        }
        viewHolder.textViewNombre.setText(cursor.getString(cursor.getColumnIndex(RecordsContract.RecordEntry.COLUMNA_NOMBRE_JUGADOR)));
        viewHolder.textViewFecha.setText(cursor.getString(cursor.getColumnIndex(RecordsContract.RecordEntry.COLUMNA_FECHA)));
        viewHolder.textViewPuntuacion.setText(cursor.getString(cursor.getColumnIndex(RecordsContract.RecordEntry.COLUMNA_PUNTUACION)));
    }
}
