package com.aitor1995.proyecto.activities;

import android.os.Bundle;

import com.aitor1995.proyecto.views.JuegoSurfaceView;

public class JuegoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new JuegoSurfaceView(this));
    }

}
