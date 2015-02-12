package com.aitor1995.proyecto.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import com.aitor1995.proyecto.R;

public class SeekPreference extends DialogPreference {

    private static final int DEFAULT_VALUE = 100;
    private int mMax, mDefault;

    private SeekBar mSeekBar;

    public SeekPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray numberPickerType = context.obtainStyledAttributes(attrs, R.styleable.SeekPreference, 0, 0);

        mMax = numberPickerType.getInt(R.styleable.SeekPreference_max, 100);

        numberPickerType.recycle();
    }

    @Override
    protected View onCreateDialogView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.seek_dialog, null);

        mSeekBar = (SeekBar) view.findViewById(R.id.seekBar);
        mSeekBar.setMax(mMax);
        mSeekBar.setProgress(mDefault);

        return view;
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue)
            mDefault = getPersistedInt(DEFAULT_VALUE);
        else {
            mDefault = (int) defaultValue;
            persistInt(mDefault);
        }
        setSummary(getContext().getResources().getString(R.string.pref_valor_volumen_summary, mDefault));
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, DEFAULT_VALUE);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            mDefault = mSeekBar.getProgress();
            persistInt(mSeekBar.getProgress());
            setSummary(getContext().getResources().getString(R.string.pref_valor_volumen_summary, mSeekBar.getProgress()));
        }
    }
}
