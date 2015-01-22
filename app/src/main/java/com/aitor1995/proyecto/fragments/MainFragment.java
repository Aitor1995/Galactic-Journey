package com.aitor1995.proyecto.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aitor1995.proyecto.R;
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
        System.err.println("jugarClick");
    }

    @Override
    public void onRecordsClick() {

    }

    @Override
    public void onOpcionesClick() {
        getActivity().getFragmentManager().beginTransaction()
                .replace(R.id.container, new AjustesFragment())
                .addToBackStack("preferencias")
                .commit();
    }
}
