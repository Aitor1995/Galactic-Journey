package com.aitor1995.galactic_journey.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.utils.AjustesApp;

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
