package com.aitor1995.proyecto.views;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.aitor1995.proyecto.listeners.OnButtonClick;

public class MainView extends View {
    private OnButtonClick mListener;

    public MainView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    public void onButtonSelected(OnButtonClick listener) {
        this.mListener = listener;
    }
}
