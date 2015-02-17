package com.aitor1995.proyecto.tests;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.test.AndroidTestCase;

import com.aitor1995.proyecto.R;
import com.aitor1995.proyecto.clases.Meteorito;

public class MeteoritosTest extends AndroidTestCase {
    private Resources resources;
    private Meteorito meteorito;
    private Bitmap meteoritoGrisGrande;
    private Bitmap meteoritoMarronGrande;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.resources = this.getContext().getResources();
        this.meteoritoGrisGrande = BitmapFactory.decodeResource(this.resources, R.drawable.meteorgrey_big3);
        this.meteoritoMarronGrande = BitmapFactory.decodeResource(this.resources, R.drawable.meteorbrown_big3);
    }
    
    public void testComprobarImagenCorrecta(){
        this.meteorito = new Meteorito(this.resources, "marron", 3);
        assertTrue("Las imagenes no son iguales.", this.meteoritoMarronGrande.sameAs(this.meteorito.imagen));
        this.meteorito = new Meteorito(this.resources, "gris", 3);
        assertTrue("Las imagenes no son iguales.", this.meteoritoGrisGrande.sameAs(this.meteorito.imagen));
    }

    public void testComprobarSaltaExcepcionCrear() {
        try {
            this.meteorito = new Meteorito(this.resources, "marron", 5);
            fail("Debería haber fallado");
        } catch (Exception ignored) {
        }
        try {
            this.meteorito = new Meteorito(this.resources, "marron", 0);
            fail("Debería haber fallado");
        } catch (Exception ignored) {
        }
        try {
            this.meteorito = new Meteorito(this.resources, "gris", 5);
            fail("Debería haber fallado");
        } catch (Exception ignored) {
        }
        try {
            this.meteorito = new Meteorito(this.resources, "gris", 0);
            fail("Debería haber fallado");
        } catch (Exception ignored) {
        }
        try {
            this.meteorito = new Meteorito(this.resources, "verde", 3);
            fail("Debería haber fallado");
        } catch (Exception ignored) {
        }
        try {
            this.meteorito = new Meteorito(this.resources, "verde", 0);
            fail("Debería haber fallado");
        } catch (Exception ignored) {
        }
        try {
            this.meteorito = new Meteorito(this.resources, "verde", 5);
            fail("Debería haber fallado");
        } catch (Exception ignored) {
        }

    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

}