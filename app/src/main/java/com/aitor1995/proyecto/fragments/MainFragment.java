package com.aitor1995.proyecto.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.views.MainView;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return new MainView(getActivity());
    }

}
