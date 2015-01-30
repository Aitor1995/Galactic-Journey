package com.aitor1995.proyecto.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.clases.Fondo;

public class JuegoSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private final SurfaceHolder mSurfaceHolder;
    private final Context mContext;
    private Fondo[] fondos;
    private Hilo mHilo;
    private boolean funcionando = false;
    private int mAnchoPantalla;
    private int mAltoPantalla;
    long tiempoDormido = 0; //Tiempo que va a dormir el hilo
    final int FPS = 30; // Nuestro objetivo
    final int TPS = 1000000000;  //Ticks en un segundo para la función usada nanoTime()
    final int FRAGMENTO_TEMPORAL = TPS / FPS; // Espacio de tiempo en el que haremos todo de forma repetida
    // Tomamos un tiempo de referencia actual en nanosegundos
    // más preciso que currenTimeMillis()
    long tiempoReferencia = System.nanoTime();

    public JuegoSurfaceView(Context context) {
        super(context);
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mContext = context;
        mHilo = new Hilo();
        setFocusable(true);
    }

    private class Hilo extends Thread {

        @Override
        public void run() {
            while (funcionando) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas();
                    synchronized (mSurfaceHolder) {
                        actualizarFisica();
                        dibujar(c);
                    }
                } finally {
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                // Calculamos el siguiente instante temporal donde volveremos
                // a actualizar y pintar
                tiempoReferencia += FRAGMENTO_TEMPORAL;

                // El tiempo que duerme será el siguiente menos el
                // Actual (Ya ha terminado de pintar y actualizar)
                tiempoDormido = tiempoReferencia - System.nanoTime();

                //Si tarda mucho, dormimos.
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
            synchronized (mSurfaceHolder) {
                mAnchoPantalla = width;
                mAltoPantalla = height;

                Bitmap bitmapFondo = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fondo);
                bitmapFondo = Bitmap.createScaledBitmap(bitmapFondo, bitmapFondo.getWidth(), mAltoPantalla, true);
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
        return mHilo;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mHilo.getState() == Thread.State.TERMINATED) {
            mHilo = new Hilo();
        }
        mHilo.setFuncionando(true);
        mHilo.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mHilo.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mHilo.setFuncionando(false);
        try {
            mHilo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
