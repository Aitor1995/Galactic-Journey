package com.aitor1995.proyecto.clases;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import com.aitor1995.proyecto.R;

public class Meteorito extends Imagen {
    public RectF[] rectangulos = new RectF[2];
    
    public Meteorito(Resources res, String color, int tamanio){
        if(color.equals("gris")){
            if(tamanio==3){
                this.imagen = BitmapFactory.decodeResource(res, R.drawable.meteorgrey_big3);
            }else if(tamanio==2){
                
            }else if(tamanio==1){
                
            }else{
                throw new IllegalArgumentException(String.format("El tamaño '%d' no es válido.", tamanio));
            }
        }else if(color.equals("marron")){
            if(tamanio==3){
                this.imagen = BitmapFactory.decodeResource(res, R.drawable.meteorbrown_big3);
            }else if(tamanio==2){

            }else if(tamanio==1){

            }else{
                throw new IllegalArgumentException(String.format("El tamaño '%d' no es válido.", tamanio));
            }
        }else{
            throw new IllegalArgumentException(String.format("El color '%s' no es válido.", color));
        }
    }

    public void setRectangulos() {
        int anchoMeteorito = this.imagen.getWidth();
        int altoMeteorito = this.imagen.getHeight();
        float x = this.posicion.x;
        float y = this.posicion.y;
        
    }
}
