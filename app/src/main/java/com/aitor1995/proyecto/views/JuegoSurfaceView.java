package com.aitor1995.proyecto.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aitor1995.proyecto.BuildConfig;
import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.clases.Fondo;
import com.aitor1995.proyecto.clases.Nave;
import com.aitor1995.proyecto.utils.AjustesApp;

import java.util.LinkedHashMap;

public class JuegoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = JuegoSurfaceView.class.getSimpleName();
    private static final float MIN_DXDY = 2;
    private final SurfaceHolder surfaceHolder;
    private final Context context;
    private final AjustesApp ajustes;
    public MediaPlayer mediaPlayer;
    private Fondo[] fondos;
    private Nave nave;
    private Hilo hilo;
    final private static LinkedHashMap<Integer, PointF> posiciones = new LinkedHashMap<>();
    private Paint paint = new Paint();
    private boolean funcionando = false;
    private int anchoPantalla;
    private int altoPantalla;
    long tiempoDormido = 0;
    final int FPS = 30;
    final int TPS = 1000000000;
    final int FRAGMENTO_TEMPORAL = TPS / FPS;
    long tiempoReferencia = System.nanoTime();

    public JuegoSurfaceView(Context context) {
        super(context);
        setFocusable(true);
        this.context = context;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.ajustes = AjustesApp.getInstance(this.context);
        if (this.ajustes.musica) {
            this.mediaPlayer = MediaPlayer.create(this.context, R.raw.musica_fondo);
            this.mediaPlayer.setVolume((float) this.ajustes.volumenMusica / 100, (float) this.ajustes.volumenMusica / 100);
            this.mediaPlayer.setLooping(true);
            this.mediaPlayer.start();
        }
        this.hilo = new Hilo();
    }

    private class Hilo extends Thread {

        @Override
        public void run() {
            while (funcionando) {
                Canvas c = null;
                try {
                    c = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        actualizarFisica();
                        dibujar(c);
                    }
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                tiempoReferencia += FRAGMENTO_TEMPORAL;
                tiempoDormido = tiempoReferencia - System.nanoTime();
                if (tiempoDormido > 0) {
                    try {

                        Thread.sleep(tiempoDormido / 1000000); //Convertimos a ms
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void setFuncionando(boolean flag) {
            funcionando = flag;
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (surfaceHolder) {
                anchoPantalla = width;
                altoPantalla = height;

                Bitmap bitmapFondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
                bitmapFondo = Bitmap.createScaledBitmap(bitmapFondo, bitmapFondo.getWidth(), altoPantalla, true);
                fondos = new Fondo[2];
                fondos[0] = new Fondo(bitmapFondo, 0, 0);
                fondos[1] = new Fondo(bitmapFondo, fondos[0].posicion.x + bitmapFondo.getWidth(), 0);

                Bitmap bitmapNave = BitmapFactory.decodeResource(context.getResources(), R.drawable.nave1_azul);
                nave = new Nave(bitmapNave, (float) (0.1 * anchoPantalla), (altoPantalla - bitmapNave.getHeight()) / 2);
            }
        }
    }

    private void dibujar(Canvas canvas) {
        try {
            canvas.drawRGB(255, 255, 255);
            canvas.drawBitmap(this.fondos[0].imagen, this.fondos[0].posicion.x, this.fondos[0].posicion.y, null);
            canvas.drawBitmap(this.fondos[1].imagen, this.fondos[1].posicion.x, this.fondos[1].posicion.y, null);
            canvas.drawBitmap(this.nave.imagen, this.nave.posicion.x, this.nave.posicion.y, null);
            if (BuildConfig.DEBUG) {
                for (RectF rectF : this.nave.rectangulos) {
                    this.paint.setColor(Color.RED);
                    this.paint.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(rectF, this.paint);
                    this.paint.reset();
                }
            }
        } catch (Exception ignored) {
        }
    }

    private void actualizarFisica() {
        this.fondos[0].mover(3);
        this.fondos[1].mover(3);
        if (this.fondos[0].posicion.x < -this.fondos[0].imagen.getWidth())
            this.fondos[0].posicion.x = this.fondos[1].posicion.x + this.fondos[0].imagen.getWidth();
        if (this.fondos[1].posicion.x < -this.fondos[1].imagen.getWidth())
            this.fondos[1].posicion.x = this.fondos[0].posicion.x + this.fondos[1].imagen.getWidth();
        if (posiciones.entrySet().iterator().hasNext()) {
            PointF punto = posiciones.entrySet().iterator().next().getValue();
            if (punto.y < (this.nave.posicion.y + this.nave.imagen.getHeight() / 2)) {
                this.nave.mover(-4);
            } else {
                this.nave.mover(4);
            }
        }
    }

    public Hilo getHilo() {
        return this.hilo;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (this.hilo.getState() == Thread.State.TERMINATED) {
            this.hilo = new Hilo();
        }
        this.hilo.setFuncionando(true);
        this.hilo.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.hilo.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        this.hilo.setFuncionando(false);
        try {
            this.hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        synchronized (this.surfaceHolder) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    int pointerIndex = event.getActionIndex();
                    int pointerID = event.getPointerId(pointerIndex);
                    PointF posicion = new PointF(event.getX(pointerIndex), event.getY(pointerIndex));
                    posiciones.put(pointerID, posicion);
                    Log.d(TAG, posiciones.toString());
                    Log.d(TAG, event.toString());
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    pointerIndex = event.getActionIndex();
                    pointerID = event.getPointerId(pointerIndex);
                    posiciones.remove(pointerID);
                    Log.d(TAG, posiciones.toString());
                    Log.d(TAG, event.toString());
                    break;
                case MotionEvent.ACTION_MOVE:
                    for (int i = 0; i < event.getPointerCount(); i++) {
                        pointerID = event.getPointerId(i);
                        posicion = posiciones.get(pointerID);
                        if (posicion != null) {
                            if (Math.abs(posicion.x - event.getX(i)) > MIN_DXDY || Math.abs(posicion.y - event.getY(i)) > MIN_DXDY)
                                posicion.set(event.getX(i), event.getY(i));
                        }
                    }
                    break;
            }
            return true;
        }
    }
}
