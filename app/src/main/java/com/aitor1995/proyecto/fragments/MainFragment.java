package com.aitor1995.proyecto.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.activities.JuegoActivity;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }


    public void onJugarClick() {
        startActivity(new Intent(getActivity(), JuegoActivity.class));
    }


    public void onRecordsClick() {

    }


    public void onOpcionesClick() {
        getActivity().getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_left_enter, R.animator.slide_left_exit, R.animator.slide_right_enter, R.animator.slide_right_exit)
                .replace(R.id.container, new AjustesFragment())
                .addToBackStack(null)
                .commit();
    }
}
