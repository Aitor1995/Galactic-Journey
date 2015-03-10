package com.aitor1995.galactic_journey.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.aitor1995.galactic_journey.R;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "fuente.otf");
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        Button boton = (Button) view.findViewById(R.id.botonJugar);
        boton.setTypeface(typeface);
        boton = (Button) view.findViewById(R.id.botonRecords);
        boton.setTypeface(typeface);
        boton = (Button) view.findViewById(R.id.buttonOpciones);
        boton.setTypeface(typeface);
        return view;
    }

}
