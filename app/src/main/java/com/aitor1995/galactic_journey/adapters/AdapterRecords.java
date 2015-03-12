package com.aitor1995.galactic_journey.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class AdapterRecords extends CursorAdapter {

    class ViewHolder {

    }

    public AdapterRecords(Context context, Cursor c, Typeface typeface) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

    }
}
