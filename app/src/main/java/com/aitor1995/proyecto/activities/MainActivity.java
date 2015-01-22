package com.aitor1995.proyecto.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.fragments.MainFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

}
