package com.aitor1995.galactic_journey.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.fragments.AjustesFragment;
import com.aitor1995.galactic_journey.fragments.MainFragment;


public class MainActivity extends BaseActivity {

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

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    public void onJugarClick(View view) {
        Intent intent = new Intent(this, JuegoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void onRecordsClick(View view) {
        startActivity(new Intent(this, RecordsActivity.class));
    }

    public void onOpcionesClick(View view) {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_left_enter, R.animator.slide_left_exit, R.animator.slide_right_enter, R.animator.slide_right_exit)
                .replace(R.id.container, new AjustesFragment())
                .addToBackStack(null)
                .commit();
    }
}
