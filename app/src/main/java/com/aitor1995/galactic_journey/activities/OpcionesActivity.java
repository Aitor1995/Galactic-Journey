package com.aitor1995.galactic_journey.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.view.WindowManager;

import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.fragments.AjustesFragment;

public class OpcionesActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opciones);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new AjustesFragment())
                .commit();
    }
}
