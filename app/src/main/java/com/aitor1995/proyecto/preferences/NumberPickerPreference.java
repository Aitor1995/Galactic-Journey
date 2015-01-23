package com.aitor1995.proyecto.preferences;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.aitor1995.proyecto.R;

public class NumberPickerPreference extends DialogPreference {

    private static final int DEFAULT_VALUE = 100;
    private int mMin, mMax, mDefault;

    private NumberPicker mNumberPicker;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray numberPickerType = context.obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference, 0, 0);

        mMax = numberPickerType.getInt(R.styleable.NumberPickerPreference_max, 100);
        mMin = numberPickerType.getInt(R.styleable.NumberPickerPreference_min, 0);

        numberPickerType.recycle();
    }

    @Override
    protected View onCreateDialogView() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.number_picker_dialog, null);

        mNumberPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mNumberPicker.setMaxValue(mMax);
        mNumberPicker.setMinValue(mMin);
        mNumberPicker.setValue(getPersistedInt(mDefault));
        mNumberPicker.setWrapSelectorWheel(false);

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
            persistInt(mNumberPicker.getValue());
            setSummary(getContext().getResources().getString(R.string.pref_valor_volumen_summary, mNumberPicker.getValue()));
        }
    }
}
