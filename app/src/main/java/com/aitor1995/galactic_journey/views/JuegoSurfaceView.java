package com.aitor1995.galactic_journey.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.NinePatchDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.inputmethod.InputMethodManager;

import com.aitor1995.galactic_journey.BuildConfig;
import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.clases.Boton;
import com.aitor1995.galactic_journey.clases.Fondo;
import com.aitor1995.galactic_journey.clases.Meteorito;
import com.aitor1995.galactic_journey.clases.Nave;
import com.aitor1995.galactic_journey.clases.PanelResultados;
import com.aitor1995.galactic_journey.utils.AjustesApp;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

public class JuegoSurfaceView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    private static final String TAG = JuegoSurfaceView.class.getSimpleName();
    private static final float MIN_DXDY = 2;
    private final SurfaceHolder surfaceHolder;
    private final Context context;
    private final AjustesApp ajustes;
    private final float pixelsPuntuacion;
    private final float pixelsPeticionNombre;
    public MediaPlayer mediaPlayer;
    public SensorManager sensorManager;
    private Fondo[] fondos;
    private Bitmap[] bitmapsMeteoritos;
    private Bitmap[] bitmapsNumeros;
    private Bitmap iconoNaveVida;
    private Bitmap xVida;
    private Bitmap numeroVida;
    private PanelResultados panelResultados;
    private String nombre = "";
    private RectF rectNombre;
    private NinePatchDrawable boton;
    private ArrayList<Meteorito> meteoritos = new ArrayList<>();
    private double puntuacion = 0;
    private Nave nave;
    private Hilo hilo;
    private boolean juegoTerminado = false;
    final private LinkedHashMap<Integer, PointF> posiciones = new LinkedHashMap<>();
    private Random random = new Random();
    private float[] gravity;
    private float[] geomagnetic;
    private float[] orientacion = new float[3];
    private float valorOrientacionReferencia;
    private Paint paint = new Paint();
    private boolean funcionando = false, orientacionPrimeraVez = true;
    private int anchoPantalla;
    private int altoPantalla;
    long tiempoDormido = 0;
    final int FPS = 30;
    final int TPS = 1000000000;
    final int FRAGMENTO_TEMPORAL = TPS / FPS;
    long tiempoReferencia = System.nanoTime();
    private Typeface typeface;
    public InputMethodManager inputMethodManager;
    private Vibrator vibrator;
    private boolean tecladoMostrado = false;
    private Boton botonAceptar;
    private Boton botonCompartir;

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
        this.pixelsPuntuacion = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        this.pixelsPeticionNombre = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, getResources().getDisplayMetrics());
    }

    private void vibrate(int milisegundos) {
        if (this.vibrator == null)
            this.vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        this.vibrator.vibrate(milisegundos);
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
                        Thread.sleep(tiempoDormido / 1000000);
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

                bitmapsMeteoritos = new Bitmap[]{
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorbrown_big),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorbrown_med),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorbrown_small),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorgrey_big),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorgrey_med),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.meteorgrey_small),
                };

                bitmapsNumeros = new Bitmap[]{
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral0),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral1),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral2),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral3),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral4),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral5),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral6),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral7),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral8),
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.numeral9),
                };

                iconoNaveVida = BitmapFactory.decodeResource(context.getResources(), R.drawable.playerlife1_blue);
                xVida = BitmapFactory.decodeResource(context.getResources(), R.drawable.numeralx);
                numeroVida = bitmapsNumeros[3];
            }
        }
    }

    private void dibujar(Canvas canvas) {
        try {
            canvas.drawRGB(255, 255, 255);
            canvas.drawBitmap(this.fondos[0].imagen, this.fondos[0].posicion.x, this.fondos[0].posicion.y, null);
            canvas.drawBitmap(this.fondos[1].imagen, this.fondos[1].posicion.x, this.fondos[1].posicion.y, null);
            canvas.drawBitmap(this.nave.imagen, this.nave.posicion.x, this.nave.posicion.y, null);
            this.paint.setColor(Color.RED);
            this.paint.setStyle(Paint.Style.STROKE);
            if (BuildConfig.DEBUG) {
                for (RectF rectF : this.nave.rectangulos) {
                    canvas.drawRect(rectF, this.paint);
                }
            }
            this.paint.reset();
            this.paint.setColor(Color.RED);
            this.paint.setStyle(Paint.Style.STROKE);
            for (Meteorito meteorito : meteoritos) {
                canvas.drawBitmap(meteorito.imagen, meteorito.posicion.x, meteorito.posicion.y, null);
                if (BuildConfig.DEBUG) {
                    for (RectF rectF : meteorito.rectangulos) {
                        canvas.drawRect(rectF, this.paint);
                    }
                }
            }
            this.paint.reset();
            canvas.drawBitmap(this.iconoNaveVida, (float) (this.anchoPantalla * 0.02), (float) (this.altoPantalla * 0.02), null);
            canvas.drawBitmap(this.xVida, (float) (this.anchoPantalla * 0.05), (float) (this.altoPantalla * 0.025), null);
            canvas.drawBitmap(this.numeroVida, (float) (this.anchoPantalla * 0.065), (float) (this.altoPantalla * 0.025), null);
            ArrayList<Bitmap> numeros = this.crearImagenesPuntuacion((int) puntuacion);
            float posicion = 0.95F;
            for (int i = 0; i < numeros.size(); i++) {
                Bitmap numero = numeros.get(i);
                canvas.drawBitmap(numero, this.anchoPantalla * posicion, (float) (this.altoPantalla * 0.025), null);
                posicion -= 0.02;
            }
            if (this.juegoTerminado) {
                this.paint.setARGB(75, 0, 0, 255);
                canvas.drawRect(0, 0, this.anchoPantalla, this.altoPantalla, this.paint);
                this.paint.reset();

                this.panelResultados.imagen.draw(canvas);
                String punct = (int) this.puntuacion + "";
                this.paint.setTypeface(this.typeface);
                this.paint.setAntiAlias(true);
                this.paint.setTextSize(this.pixelsPuntuacion);
                Rect rect = new Rect();
                this.paint.getTextBounds(punct, 0, punct.length(), rect);
                canvas.drawText(punct, (float) ((this.anchoPantalla - rect.width()) / 2), (float) (this.altoPantalla * 0.35), this.paint);

                this.paint.setTextSize(this.pixelsPeticionNombre);
                this.paint.getTextBounds("Escribe tu nombre: " + nombre, 0, ("Escribe tu nombre: " + nombre).length(), rect);
                PointF pointF = new PointF((float) ((this.anchoPantalla - rect.width()) / 2), (float) (this.altoPantalla * 0.45));
                canvas.drawText("Escribe tu nombre: " + nombre, pointF.x, pointF.y, this.paint);
                this.rectNombre = new RectF(pointF.x - 30, pointF.y - 30, pointF.x + rect.width() + 30, pointF.y + rect.height() + 30);
                this.paint.reset();

                this.botonAceptar.dibujar(canvas);
                this.botonCompartir.dibujar(canvas);
            }
        } catch (Exception ignored) {
        }
    }

    private ArrayList<Bitmap> crearImagenesPuntuacion(int puntuacion) {
        ArrayList<Bitmap> numeros = new ArrayList<>();
        while (puntuacion > 0) {
            switch (puntuacion % 10) {
                case 0:
                    numeros.add(bitmapsNumeros[0]);
                    break;
                case 1:
                    numeros.add(bitmapsNumeros[1]);
                    break;
                case 2:
                    numeros.add(bitmapsNumeros[2]);
                    break;
                case 3:
                    numeros.add(bitmapsNumeros[3]);
                    break;
                case 4:
                    numeros.add(bitmapsNumeros[4]);
                    break;
                case 5:
                    numeros.add(bitmapsNumeros[5]);
                    break;
                case 6:
                    numeros.add(bitmapsNumeros[6]);
                    break;
                case 7:
                    numeros.add(bitmapsNumeros[7]);
                    break;
                case 8:
                    numeros.add(bitmapsNumeros[8]);
                    break;
                case 9:
                    numeros.add(bitmapsNumeros[9]);
                    break;
            }
            puntuacion = puntuacion / 10;
        }
        if (numeros.size() == 0) {
            numeros.add(bitmapsNumeros[0]);
        }
        return numeros;
    }

    private void actualizarFisica() {
        if (!this.juegoTerminado) {
            this.moverFondos();
            if (this.ajustes.controlJuego.equals("tactil")) {
                this.movimientoTactil();
            } else {
                this.movimientoGiroscopio();
            }
            this.moverMeteoritosYColisiones();
            this.crearMeteoritos();
            this.puntuacion += 0.1;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        synchronized (this.surfaceHolder) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                this.inputMethodManager.toggleSoftInputFromWindow(this.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                this.tecladoMostrado = false;
            } else if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (this.nombre.length() > 0)
                    this.nombre = this.nombre.substring(0, this.nombre.length() - 1);
            } else {
                if (this.nombre.length() <= 10)
                    this.nombre += (char) event.getUnicodeChar();
            }
            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public boolean dispatchKeyEventPreIme(@NonNull KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.tecladoMostrado = false;
        }
        return super.dispatchKeyEventPreIme(event);
    }

    private void moverMeteoritosYColisiones() {
        for (int i = meteoritos.size() - 1; i >= 0; i--) {
            Meteorito meteorito = meteoritos.get(i);
            meteorito.mover();
            if (meteorito.posicion.x + meteorito.imagen.getWidth() < 0 || meteorito.posicion.y + meteorito.imagen.getHeight() < 0 || meteorito.posicion.y > this.altoPantalla) {
                meteoritos.remove(i);
            }
            for (RectF rectF : this.nave.rectangulos) {
                for (RectF rectF1 : meteorito.rectangulos) {
                    if (rectF.intersect(rectF1)) {
                        if (--this.nave.vidas == 0) {
                            this.numeroVida = bitmapsNumeros[0];
                            this.juegoTerminado = true;
                            this.typeface = Typeface.createFromAsset(context.getAssets(), "fuente.otf");
                            this.panelResultados = new PanelResultados(
                                    (NinePatchDrawable) context.getResources().getDrawable(R.drawable.metalpanel_greencorner),
                                    new Rect((int) (this.anchoPantalla * 0.2), (int) (this.altoPantalla * 0.2), (int) (this.anchoPantalla * 0.8), (int) (this.altoPantalla * 0.8))
                            );
                            this.botonAceptar = new Boton(
                                    (NinePatchDrawable) this.context.getResources().getDrawable(R.drawable.boton),
                                    "Aceptar",
                                    this.context,
                                    this.typeface,
                                    this.anchoPantalla,
                                    (int) (this.altoPantalla * 0.5)
                            );
                            this.botonCompartir = new Boton(
                                    (NinePatchDrawable) this.context.getResources().getDrawable(R.drawable.boton),
                                    "Compartir",
                                    this.context,
                                    this.typeface,
                                    this.anchoPantalla,
                                    (int) (this.altoPantalla * 0.65)
                            );
                            this.inputMethodManager = (InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE);
                            this.inputMethodManager.toggleSoftInputFromWindow(this.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                        } else {
                            this.meteoritos.remove(i);
                            switch (this.nave.vidas) {
                                case 2:
                                    this.numeroVida = bitmapsNumeros[2];
                                    break;
                                case 1:
                                    this.numeroVida = bitmapsNumeros[1];
                                    break;
                            }
                        }
                        this.vibrate(300);
                        break;
                    }
                }
            }
        }
        puntuacion += 0.1;
    }

    private void crearMeteoritos() {
        //TODO Mejorar la aparicion de meteoritos
        if (this.random.nextInt(13) == 0) {
            int numero = this.random.nextInt(6);
            float posicionY = this.random.nextFloat() * this.altoPantalla;
            Meteorito meteorito;
            if (posicionY < this.altoPantalla * 0.3) {
                meteorito = new Meteorito(this.bitmapsMeteoritos[numero], //imagen
                        this.anchoPantalla, //posicion x
                        posicionY, //posicion y
                        Math.abs((numero % 3) - 3), //tamaño
                        this.random.nextInt(10) + 7, //velocidad
                        this.random.nextInt(20) - 20 //angulo
                );
            } else if (posicionY > this.altoPantalla * 0.7) {
                meteorito = new Meteorito(this.bitmapsMeteoritos[numero], //imagen
                        this.anchoPantalla, //posicion x
                        posicionY, //posicion y
                        Math.abs((numero % 3) - 3), //tamaño
                        this.random.nextInt(10) + 7, //velocidad
                        this.random.nextInt(20) //angulo
                );
            } else {
                meteorito = new Meteorito(this.bitmapsMeteoritos[numero], //imagen
                        this.anchoPantalla, //posicion x
                        posicionY, //posicion y
                        Math.abs((numero % 3) - 3), //tamaño
                        this.random.nextInt(10) + 7, //velocidad
                        this.random.nextInt(40) - 20 //angulo
                );
            }
            this.meteoritos.add(meteorito);
        }
    }

    private void movimientoGiroscopio() {
        if (this.nave.posicion.y < 0 && this.nave.posicion.y <= this.altoPantalla - this.nave.imagen.getHeight()) {
            this.nave.posicion.y = 0;
        } else if (this.nave.posicion.y > this.altoPantalla - this.nave.imagen.getHeight()) {
            this.nave.posicion.y = this.altoPantalla - this.nave.imagen.getHeight();
        } else {
            this.nave.mover((int) ((this.orientacion[2] - this.valorOrientacionReferencia) * -17));
        }
    }

    private void movimientoTactil() {
        //TODO Mejorar movimiento con pantalla táctil
        if (this.posiciones.entrySet().iterator().hasNext()) {
            PointF punto = this.posiciones.entrySet().iterator().next().getValue();
            if (punto.y < (this.nave.posicion.y + this.nave.imagen.getHeight() / 2) && this.nave.posicion.y >= 0) {
                this.nave.mover(-6);
            } else if (punto.y > (this.nave.posicion.y + this.nave.imagen.getHeight() / 2) && (this.nave.posicion.y + this.nave.imagen.getHeight()) <= this.altoPantalla) {
                this.nave.mover(6);
            }
        }
    }

    private void moverFondos() {
        this.fondos[0].mover(3);
        this.fondos[1].mover(3);
        if (this.fondos[0].posicion.x < -this.fondos[0].imagen.getWidth())
            this.fondos[0].posicion.x = this.fondos[1].posicion.x + this.fondos[0].imagen.getWidth();
        if (this.fondos[1].posicion.x < -this.fondos[1].imagen.getWidth())
            this.fondos[1].posicion.x = this.fondos[0].posicion.x + this.fondos[1].imagen.getWidth();
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
    public void onSensorChanged(SensorEvent event) {
        synchronized (this.surfaceHolder) {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    this.gravity = event.values;
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    this.geomagnetic = event.values;
                    break;
            }
            if (this.gravity != null && this.geomagnetic != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, this.gravity, this.geomagnetic);
                if (success) {
                    SensorManager.getOrientation(R, this.orientacion);
                    //TODO mostrar ventana inicial para calibrar orientación
                    if (this.orientacionPrimeraVez) {
                        this.orientacionPrimeraVez = false;
                        this.valorOrientacionReferencia = this.orientacion[2];
                    }
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
                    if (this.juegoTerminado && event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        if (this.rectNombre.contains((int) posicion.x, (int) posicion.y) && !this.tecladoMostrado) {
                            this.tecladoMostrado = true;
                            this.inputMethodManager.toggleSoftInputFromWindow(this.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                        }
                        if (this.botonCompartir.isClickBoton((int) posicion.x, (int) posicion.y)) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.texto_compartir, (int)this.puntuacion));
                            intent.setType("text/plain");
                            this.context.startActivity(Intent.createChooser(intent, getResources().getString(R.string.compartir)));
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    pointerIndex = event.getActionIndex();
                    pointerID = event.getPointerId(pointerIndex);
                    posiciones.remove(pointerID);
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
