package com.aitor1995.proyecto.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.activities.JuegoActivity;
import com.aitor1995.proyecto.listeners.OnButtonClick;
import com.aitor1995.proyecto.views.MainView;

public class MainFragment extends Fragment implements OnButtonClick {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainView view = new MainView(getActivity());
        view.onButtonSelected(this);
        return view;
    }

    @Override
    public void onJugarClick() {
        startActivity(new Intent(getActivity(), JuegoActivity.class));
    }

    @Override
    public void onRecordsClick() {

    }

    @Override
    public void onOpcionesClick() {
        getActivity().getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_left_enter, R.animator.slide_left_exit, R.animator.slide_right_enter, R.animator.slide_right_exit)
                .replace(R.id.container, new AjustesFragment())
                .addToBackStack(null)
                .commit();
    }
}
