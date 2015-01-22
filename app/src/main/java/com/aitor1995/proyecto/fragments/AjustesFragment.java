package com.aitor1995.proyecto.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.aitor1995.proyecto.R;

public class AjustesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.opciones);
    }
}
