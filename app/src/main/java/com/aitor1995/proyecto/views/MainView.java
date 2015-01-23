package com.aitor1995.proyecto.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.listeners.OnButtonClick;

public class MainView extends View {
    private OnButtonClick mListener;
    private int anchoPantalla;
    private int altoPantalla;
    private Bitmap btnJugar;
    private Bitmap btnJugarPulsado;
    private Bitmap btnRecords;
    private Bitmap btnRecordsPulsado;
    private Bitmap btnOpciones;
    private Bitmap btnOpcionesPulsado;
    private boolean isBtnJugarPulsado;
    private boolean isBtnRecordsPulsado;
    private boolean isBtnOpcionesPulsado;

    public MainView(Context context) {
        super(context);
        Resources resources = context.getResources();
        btnJugar = BitmapFactory.decodeResource(resources, R.drawable.btn_jugar);
        btnJugarPulsado = BitmapFactory.decodeResource(resources, R.drawable.btn_jugar_pulsado);
        btnRecords = BitmapFactory.decodeResource(resources, R.drawable.btn_records);
        btnRecordsPulsado = BitmapFactory.decodeResource(resources, R.drawable.btn_records_pulsado);
        btnOpciones = BitmapFactory.decodeResource(resources, R.drawable.btn_opciones);
        btnOpcionesPulsado = BitmapFactory.decodeResource(resources, R.drawable.btn_opciones_pulsado);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = (anchoPantalla - btnJugar.getWidth()) / 2;
        float y = altoPantalla * 0.4f;
        canvas.drawBitmap(isBtnJugarPulsado ? btnJugarPulsado : btnJugar, x, y, null);
        x = (anchoPantalla - btnRecords.getWidth()) / 2;
        y = altoPantalla * 0.55f;
        canvas.drawBitmap(isBtnRecordsPulsado ? btnRecordsPulsado : btnRecords, x, y, null);
        x = (anchoPantalla - btnOpciones.getWidth()) / 2;
        y = altoPantalla * 0.7f;
        canvas.drawBitmap(isBtnOpcionesPulsado ? btnOpcionesPulsado : btnOpciones, x, y, null);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        int accion = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (accion) {
            case MotionEvent.ACTION_DOWN:
                float iniX = (anchoPantalla - btnJugar.getWidth()) / 2;
                float iniY = altoPantalla * 0.4f;
                if (x > iniX && x < iniX + btnJugar.getWidth() && y > iniY && y < iniY + btnJugar.getHeight())
                    isBtnJugarPulsado = true;
                iniX = (anchoPantalla - btnRecords.getWidth()) / 2;
                iniY = altoPantalla * 0.55f;
                if (x > iniX && x < iniX + btnRecords.getWidth() && y > iniY && y < iniY + btnRecords.getHeight())
                    isBtnRecordsPulsado = true;
                iniX = (anchoPantalla - btnOpciones.getWidth()) / 2;
                iniY = altoPantalla * 0.7f;
                if (x > iniX && x < iniX + btnOpciones.getWidth() && y > iniY && y < iniY + btnOpciones.getHeight())
                    isBtnOpcionesPulsado = true;
                break;
            case MotionEvent.ACTION_UP:
                if (isBtnJugarPulsado) {
                    isBtnJugarPulsado = false;
                    mListener.onJugarClick();
                }
                if (isBtnRecordsPulsado) {
                    isBtnRecordsPulsado = false;
                    mListener.onRecordsClick();
                }
                if (isBtnOpcionesPulsado) {
                    isBtnOpcionesPulsado = false;
                    mListener.onOpcionesClick();
                }
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.anchoPantalla = w;
        this.altoPantalla = h;
    }

    public void onButtonSelected(OnButtonClick listener) {
        this.mListener = listener;
    }
}
