package com.aitor1995.galactic_journey.views;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.aitor1995.galactic_journey.BuildConfig;
import com.aitor1995.galactic_journey.R;
import com.aitor1995.galactic_journey.activities.RecordsActivity;
import com.aitor1995.galactic_journey.clases.Boton;
import com.aitor1995.galactic_journey.clases.Fondo;
import com.aitor1995.galactic_journey.clases.Meteorito;
import com.aitor1995.galactic_journey.clases.Nave;
import com.aitor1995.galactic_journey.clases.PanelResultados;
import com.aitor1995.galactic_journey.sqlite.RecordsContract;
import com.aitor1995.galactic_journey.sqlite.RecordsSQLiteHelper;
import com.aitor1995.galactic_journey.utils.AjustesApp;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Random;

public class JuegoSurfaceView extends SurfaceView implements SurfaceHolder.Callback, SensorEventListener {
    /**
     * Tag para los logs
     */
    private static final String TAG = JuegoSurfaceView.class.getSimpleName();
    /**
     * Diferencia de pixels para el move de la pantalla táctil
     */
    private static final float MIN_DXDY = 2;
    /**
     *
     */
    private final SurfaceHolder surfaceHolder;
    /**
     * Contexto de la aplicación
     */
    private final Context context;
    /**
     * Ajustes de la aplicación
     */
    private final AjustesApp ajustes;
    /**
     * Número de pixels para el tamaño del texto de la puntuacion
     */
    private final float pixelsPuntuacion;
    /**
     * Para la música del juego
     */
    public MediaPlayer mediaPlayer;
    /**
     * Para controlar los sensores
     */
    public SensorManager sensorManager;
    /**
     * Fondos de la aplicación
     */
    private Fondo[] fondos;
    /**
     * Bitmaps de los meteroitos
     */
    private Bitmap[] bitmapsMeteoritos;
    /**
     * Bitmaps de los números
     */
    private Bitmap[] bitmapsNumeros;
    /**
     * Bitmap del icono de la vida
     */
    private Bitmap iconoNaveVida;
    /**
     * Bitmap X
     */
    private Bitmap xVida;
    /**
     * Bitmap del numero de vidas
     */
    private Bitmap numeroVida;
    /**
     * Panel de resultados
     */
    private PanelResultados panelResultados;
    /**
     * Lsta de meteoritos en pantalla
     */
    private ArrayList<Meteorito> meteoritos = new ArrayList<>();
    /**
     * Puntuación
     */
    private double puntuacion = 0;
    /**
     * Nave
     */
    private Nave nave;
    /**
     * Hilo que controla la física y el dibujo
     */
    private Hilo hilo;
    /**
     * Juego terminado o no
     */
    private boolean juegoTerminado = false;
    /**
     * Hash con las posiciones de las pulsaciones en la pantalla
     */
    final private LinkedHashMap<Integer, PointF> posiciones = new LinkedHashMap<>();
    /**
     * Generador de numeros aleatorios
     */
    private Random random = new Random();
    /**
     * Array para datos del acelerómetro
     */
    private float[] gravity;
    /**
     * Array para datos del sensor de campo magnético
     */
    private float[] geomagnetic;
    /**
     * Array para los datos de la orientación
     */
    private float[] orientacion = new float[3];
    /**
     * Valor de orientacion de referencia
     */
    private float valorOrientacionReferencia;
    /**
     * Pincel de la aplicación
     */
    private Paint paint = new Paint();
    /**
     * Controla si el hilo sigue funcionando o no
     */
    private boolean funcionando = false;
    /**
     * Si lee la orientación por primera vez
     */
    private boolean orientacionPrimeraVez = true;
    /**
     * Ancho de la pantalla del dispositivo
     */
    private int anchoPantalla;
    /**
     * Alto de la pantalla del dispositivo
     */
    private int altoPantalla;
    /**
     * Tiempo para dormir el hilo
     */
    long tiempoDormido = 0;
    /**
     * FPS deseados
     */
    final int FPS = 30;
    /**
     * Ticks que queramos que se produzcan en un segundo
     */
    final int TPS = 1000000000;
    /**
     * Tiempo que dura la vuelta
     */
    final int FRAGMENTO_TEMPORAL = TPS / FPS;
    /**
     * Tiempo de referencia
     */
    long tiempoReferencia = System.nanoTime();
    /**
     * Tipo de letra de los textos
     */
    private Typeface typeface;
    /**
     * Vibracion del telefono
     */
    private Vibrator vibrator;
    /**
     * Botón aceptar
     */
    private Boton botonAceptar;
    /**
     * Botón compartir
     */
    private Boton botonCompartir;
    /**
     * Cliente para las API de Google
     */
    private GoogleApiClient mGoogleApiClient;
    /**
     * Getiona los efectos de la aplicación
     */
    private SoundPool efectos;
    /**
     * Efectos de la aplicación
     */
    private int sonidoFinJuego ,sonidoExplosion, sonidoClickBoton;

    public JuegoSurfaceView(Context context, GoogleApiClient googleApiClient) {
        super(context);
        this.context = context;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.ajustes = AjustesApp.getInstance(this.context);
        if (this.ajustes.musica) {
            this.efectos = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            this.sonidoFinJuego = this.efectos.load(this.context, R.raw.fin_juego, 1);
            this.sonidoExplosion = this.efectos.load(this.context, R.raw.explode, 1);
            this.sonidoClickBoton = this.efectos.load(this.context, R.raw.click, 1);
            this.mediaPlayer = MediaPlayer.create(this.context, R.raw.musica_fondo);
            this.mediaPlayer.setVolume((float) this.ajustes.volumenMusica / 100, (float) this.ajustes.volumenMusica / 100);
            this.mediaPlayer.setLooping(true);
            this.mediaPlayer.start();
        }
        this.hilo = new Hilo();
        this.pixelsPuntuacion = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
        this.mGoogleApiClient = googleApiClient;
    }

    /**
     * Hace vibrar el dispositivo una cantidad de milisegundo
     *
     * @param milisegundos milisegundos que vibra el dispositivo
     */
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

        /**
         * Para o inicia el hilo
         * @param flag true para iniciar y false para parar
         */
        public void setFuncionando(boolean flag) {
            funcionando = flag;
        }

        /**
         * Establece el ancho y alto de la pantalla y redimensiona las imagenes
         * @param width ancho de la pantalla
         * @param height alto de la pantalla
         */
        public void setSurfaceSize(int width, int height) {
            synchronized (surfaceHolder) {
                anchoPantalla = width;
                altoPantalla = height;

                Bitmap bitmapFondo = BitmapFactory.decodeResource(context.getResources(), R.drawable.fondo);
                bitmapFondo = Bitmap.createScaledBitmap(bitmapFondo, bitmapFondo.getWidth(), altoPantalla, true);
                fondos = new Fondo[2];
                fondos[0] = new Fondo(bitmapFondo, 0, 0);
                fondos[1] = new Fondo(bitmapFondo, fondos[0].posicion.x + bitmapFondo.getWidth() - 10, 0);

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
                numeroVida = bitmapsNumeros[nave.vidas];
            }
        }
    }

    /**
     * Dibuja en el canvas
     * @param canvas canvas donde se pinta
     */
    private void dibujar(Canvas canvas) {
        try {
            // Dibuja los fondos
            canvas.drawBitmap(this.fondos[0].imagen, this.fondos[0].posicion.x, this.fondos[0].posicion.y, null);
            canvas.drawBitmap(this.fondos[1].imagen, this.fondos[1].posicion.x, this.fondos[1].posicion.y, null);
            // Dibuja la nave
            canvas.drawBitmap(this.nave.imagen, this.nave.posicion.x, this.nave.posicion.y, null);
            // Dibuja los rectangulos de la nave
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
            // Dibuja los meteoritos con sus rectangulos
            for (Meteorito meteorito : meteoritos) {
                canvas.drawBitmap(meteorito.imagen, meteorito.posicion.x, meteorito.posicion.y, null);
                if (BuildConfig.DEBUG) {
                    for (RectF rectF : meteorito.rectangulos) {
                        canvas.drawRect(rectF, this.paint);
                    }
                }
            }
            this.paint.reset();
            // Dibuja las vidas
            canvas.drawBitmap(this.iconoNaveVida, (float) (this.anchoPantalla * 0.02), (float) (this.altoPantalla * 0.02), null);
            canvas.drawBitmap(this.xVida, (float) (this.anchoPantalla * 0.05), (float) (this.altoPantalla * 0.025), null);
            canvas.drawBitmap(this.numeroVida, (float) (this.anchoPantalla * 0.065), (float) (this.altoPantalla * 0.025), null);
            // Dibuja la puntuacion
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
                // Dibuja el panel de resultados
                this.panelResultados.imagen.draw(canvas);
                // Dibuja la puntuacion
                String punct = (int) this.puntuacion + "";
                this.paint.setTypeface(this.typeface);
                this.paint.setAntiAlias(true);
                this.paint.setTextSize(this.pixelsPuntuacion);
                Rect rect = new Rect();
                this.paint.getTextBounds(punct, 0, punct.length(), rect);
                canvas.drawText(punct, (float) ((this.anchoPantalla - rect.width()) / 2), (float) (this.altoPantalla * 0.35), this.paint);
                // Dibuja los botones
                this.botonAceptar.dibujar(canvas);
                this.botonCompartir.dibujar(canvas);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Crea una array de bitmaps con los numeros de la puntuacion
     * @param puntuacion puntuacion a pasar a bitmaps
     * @return array de bitmaps con los numeros
     */
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

    /**
     * Actualiza la fisica de la aplicación
     */
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
            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                if (puntuacion == 2000) {
                    Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_conseguir_2000_puntos));
                }
                if (puntuacion == 5000) {
                    Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_conseguir_5000_puntos));
                }
                if (puntuacion == 1000 && this.nave.vidas == 3) {
                    Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_conseguir_1000_puntos_con_3_vidas));
                }
                if (puntuacion == 3000 && this.nave.vidas == 3) {
                    Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_conseguir_3000_puntos_con_3_vidas));
                }
            }
        }
    }

    /**
     * Mueve los meteoritos y comprueba las colisiones. Controlando las vidas y logros del juego.
     */
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
                            if (this.ajustes.musica) {
                                this.efectos.play(this.sonidoFinJuego, this.ajustes.volumenMusica, this.ajustes.volumenMusica, 1, 0, 1);
                            }
                            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                                Games.Achievements.unlock(mGoogleApiClient, getResources().getString(R.string.achievement_jugar_por_primera_vez));
                                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_jugar_5_veces_al_juego), 1);
                                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_jugar_10_veces_al_juego), 1);
                                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_jugar_20_veces_al_juego), 1);
                                Games.Achievements.increment(mGoogleApiClient, getResources().getString(R.string.achievement_jugar_50_veces_al_juego), 1);
                            }
                            this.typeface = Typeface.createFromAsset(context.getAssets(), "fuente.otf");
                            this.panelResultados = new PanelResultados(
                                    (NinePatchDrawable) context.getResources().getDrawable(R.drawable.metalpanel_greencorner),
                                    new Rect((int) (this.anchoPantalla * 0.2), (int) (this.altoPantalla * 0.2), (int) (this.anchoPantalla * 0.8), (int) (this.altoPantalla * 0.8))
                            );
                            this.botonAceptar = new Boton(
                                    (NinePatchDrawable) this.context.getResources().getDrawable(R.drawable.boton),
                                    context.getResources().getString(R.string.aceptar),
                                    this.context,
                                    this.typeface,
                                    this.anchoPantalla,
                                    (int) (this.altoPantalla * 0.5)
                            );
                            this.botonCompartir = new Boton(
                                    (NinePatchDrawable) this.context.getResources().getDrawable(R.drawable.boton),
                                    context.getResources().getString(R.string.compartir),
                                    this.context,
                                    this.typeface,
                                    this.anchoPantalla,
                                    (int) (this.altoPantalla * 0.65)
                            );
                        } else {
                            if (this.ajustes.musica) {
                                this.efectos.play(this.sonidoExplosion, this.ajustes.volumenMusica, this.ajustes.volumenMusica, 1, 0, 1);
                            }
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

    /**
     * Crea los meteoritos de manera aleatoria
     */
    private void crearMeteoritos() {
        //TODO Mejorar la aparicion de meteoritos
        if (this.random.nextInt(10) == 0) {
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

    /**
     * Controla el movimiento de la nave mediante el giroscopio
     */
    private void movimientoGiroscopio() {
        if (this.nave.posicion.y < 0 && this.nave.posicion.y <= this.altoPantalla - this.nave.imagen.getHeight()) {
            this.nave.posicion.y = 0;
        } else if (this.nave.posicion.y > this.altoPantalla - this.nave.imagen.getHeight()) {
            this.nave.posicion.y = this.altoPantalla - this.nave.imagen.getHeight();
        } else {
            this.nave.mover((int) ((this.orientacion[2] - this.valorOrientacionReferencia) * -30));
        }
    }

    /**
     * Controla el movimiento de la nave mediante la pantalla tactil
     */
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

    /**
     * Mueve los fondos del juego
     */
    private void moverFondos() {
        this.fondos[0].mover(3);
        this.fondos[1].mover(3);
        if (this.fondos[0].posicion.x < -this.fondos[0].imagen.getWidth())
            this.fondos[0].posicion.x = this.fondos[1].posicion.x + this.fondos[0].imagen.getWidth();
        if (this.fondos[1].posicion.x < -this.fondos[1].imagen.getWidth())
            this.fondos[1].posicion.x = this.fondos[0].posicion.x + this.fondos[1].imagen.getWidth();
    }

    /**
     * Devuelve el hilo del surface view
     */
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

    /**
     * Consigue el string de la fecha actual
     * @return fecha actual en formato yyyy-MM-dd HH:mm:ss
     */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
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
                        // Click boton compartir
                        if (this.botonCompartir.isClickBoton((int) posicion.x, (int) posicion.y)) {
                            if (this.ajustes.musica)
                                this.efectos.play(this.sonidoClickBoton, this.ajustes.volumenMusica, this.ajustes.volumenMusica, 1, 0, 1);
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.texto_compartir, (int) this.puntuacion));
                            intent.setType("text/plain");
                            this.context.startActivity(Intent.createChooser(intent, getResources().getString(R.string.compartir)));
                        }
                        // Click boton aceptar
                        if (this.botonAceptar.isClickBoton((int) posicion.x, (int) posicion.y)) {
                            if (this.ajustes.musica)
                                this.efectos.play(this.sonidoClickBoton, this.ajustes.volumenMusica, this.ajustes.volumenMusica, 1, 0, 1);
                            LayoutInflater inflater = LayoutInflater.from(this.context);
                            AlertDialog alert = new AlertDialog.Builder(context)
                                    .setView(inflater.inflate(R.layout.introducir_nombre_dialog, null))
                                    .create();
                            alert.show();
                            final EditText editText = (EditText) alert.findViewById(R.id.editText);
                            Button boton = (Button) alert.findViewById(R.id.buttonAceptar);
                            boton.setTypeface(typeface);
                            boton.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!editText.getText().toString().trim().equals("")) {
                                        RecordsSQLiteHelper recordsSQLiteHelper = new RecordsSQLiteHelper(context);
                                        SQLiteDatabase db = recordsSQLiteHelper.getWritableDatabase();
                                        ContentValues values = new ContentValues();
                                        values.put(RecordsContract.RecordEntry.COLUMNA_NOMBRE_JUGADOR, editText.getText().toString());
                                        values.put(RecordsContract.RecordEntry.COLUMNA_PUNTUACION, (int) puntuacion);
                                        values.put(RecordsContract.RecordEntry.COLUMNA_FECHA, getDateTime());
                                        db.insert(RecordsContract.RecordEntry.NOMBRE_TABLA, null, values);
                                        db.close();
                                    }
                                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
                                        Games.Leaderboards.submitScore(mGoogleApiClient, getResources().getString(R.string.leaderboard_marcador), (int) puntuacion);
                                    Intent intent = new Intent(context, RecordsActivity.class);
                                    context.startActivity(intent);
                                }
                            });
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
