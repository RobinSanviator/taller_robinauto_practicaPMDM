package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Pieza {
    private String nombre;
    private int cantidad;
    private int umbralMinimo;
    private int imagenPieza;

    public Pieza(){}

    public Pieza(String nombre, int cantidad, int umbralMinimo,   int imagenPieza) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.umbralMinimo = umbralMinimo;
        this.imagenPieza = imagenPieza;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getUmbralMinimo() {
        return umbralMinimo;
    }

    public void setUmbralMinimo(int umbralMinimo) {
        this.umbralMinimo = umbralMinimo;
    }

    public int getImagenPieza() {
        return imagenPieza;
    }

    public void setImagenPieza(int imagenPieza) {
        this.imagenPieza = imagenPieza;
    }
}
