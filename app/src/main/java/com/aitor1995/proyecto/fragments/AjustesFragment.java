package com.aitor1995.proyecto.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.utils.AjustesApp;

public class AjustesFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.opciones);
    }

    @Override
    public void onResume() {
        super.onResume();
        AjustesApp.invalidate();
    }
}
