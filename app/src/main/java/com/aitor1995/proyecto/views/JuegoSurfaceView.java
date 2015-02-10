package com.aitor1995.proyecto.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.clases.Fondo;
import com.aitor1995.proyecto.utils.AjustesApp;

public class JuegoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private static final String TAG = JuegoSurfaceView.class.getSimpleName();
    private final SurfaceHolder surfaceHolder;
    private final Context context;
    private final AjustesApp ajustes;
    public MediaPlayer mediaPlayer;
    private Fondo[] fondos;
    private Hilo hilo;
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
        setFocusable(true);
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
            }
        }
    }

    private void dibujar(Canvas canvas) {
        try {
            canvas.drawRGB(255, 255, 255);
            canvas.drawBitmap(fondos[0].imagen, fondos[0].posicion.x, fondos[0].posicion.y, null);
            canvas.drawBitmap(fondos[1].imagen, fondos[1].posicion.x, fondos[1].posicion.y, null);
        } catch (Exception ignored) {
        }
    }

    private void actualizarFisica() {
        fondos[0].mover(3);
        fondos[1].mover(3);
        if (fondos[0].posicion.x < -fondos[0].imagen.getWidth())
            fondos[0].posicion.x = fondos[1].posicion.x + fondos[0].imagen.getWidth();
        if (fondos[1].posicion.x < -fondos[1].imagen.getWidth())
            fondos[1].posicion.x = fondos[0].posicion.x + fondos[1].imagen.getWidth();
    }

    public Hilo getHilo() {
        return hilo;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (hilo.getState() == Thread.State.TERMINATED) {
            hilo = new Hilo();
        }
        hilo.setFuncionando(true);
        hilo.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        hilo.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hilo.setFuncionando(false);
        try {
            hilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
