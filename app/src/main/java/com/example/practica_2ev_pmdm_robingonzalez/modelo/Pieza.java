package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Pieza {
    private String nombre;
    private int cantidad;
    private double precio;
    private int umbralMinimo;
    private int imagenPieza;

    public Pieza(){}

    public Pieza(String nombre, int cantidad, double precio, int umbralMinimo,   int imagenPieza) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
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
