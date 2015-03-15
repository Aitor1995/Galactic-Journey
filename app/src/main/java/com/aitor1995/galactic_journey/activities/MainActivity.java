package com.aitor1995.galactic_journey.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.fragments.MainFragment;
import com.google.android.gms.games.Games;


public class MainActivity extends BaseActivity {
    private static final int REQUEST_ACHIEVEMENTS = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .add(R.id.container, new MainFragment())
                .commit();
    }

    public void onJugarClick(View view) {
        startActivity(new Intent(this, JuegoActivity.class));
    }

    public void onRecordsClick(View view) {
        startActivity(new Intent(this, RecordsActivity.class));
    }

    public void onOpcionesClick(View view) {
        startActivity(new Intent(this, OpcionesActivity.class));
    }

    public void onLogrosClick(View view){
        startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), REQUEST_ACHIEVEMENTS);
    }
}
