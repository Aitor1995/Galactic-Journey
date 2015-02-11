package com.aitor1995.proyecto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.fragments.AjustesFragment;
import com.aitor1995.proyecto.fragments.MainFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new MainFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else
            super.onBackPressed();
    }

    public void onJugarClick(View view) {
        startActivity(new Intent(this, JuegoActivity.class));
    }

    public void onRecordsClick(View view) {

    }

    public void onOpcionesClick(View view) {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_left_enter, R.animator.slide_left_exit, R.animator.slide_right_enter, R.animator.slide_right_exit)
                .replace(R.id.container, new AjustesFragment())
                .addToBackStack(null)
                .commit();
    }
}
