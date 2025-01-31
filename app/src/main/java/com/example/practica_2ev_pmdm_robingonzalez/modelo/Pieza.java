package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Pieza {
    private String nombre;
    private int cantidad;
    private double precio;
    private int umbralMinimo;
    private int imagenPieza;

    /**
     * Constructor vacío de la clase Pieza.
     * Se utiliza para crear una instancia de Pieza sin inicializar sus atributos.
     */
    public Pieza(){}

    /**
     * Constructor de la clase Pieza que inicializa todos los atributos.
     *
     * @param nombre El nombre de la pieza.
     * @param cantidad La cantidad disponible de la pieza.
     * @param precio El precio unitario de la pieza.
     * @param umbralMinimo El umbral mínimo de cantidad de la pieza.
     * @param imagenPieza El identificador de la imagen de la pieza.
     */
    public Pieza(String nombre, int cantidad, double precio, int umbralMinimo, int imagenPieza) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.umbralMinimo = umbralMinimo;
        this.imagenPieza = imagenPieza;
    }

    /**
     * Obtiene el nombre de la pieza.
     *
     * @return El nombre de la pieza.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la pieza.
     *
     * @param nombre El nombre de la pieza a establecer.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la cantidad disponible de la pieza.
     *
     * @return La cantidad disponible de la pieza.
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad disponible de la pieza.
     *
     * @param cantidad La cantidad de la pieza a establecer.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario de la pieza.
     *
     * @return El precio de la pieza.
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio unitario de la pieza.
     *
     * @param precio El precio de la pieza a establecer.
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el umbral mínimo de cantidad de la pieza.
     *
     * @return El umbral mínimo de la pieza.
     */
    public int getUmbralMinimo() {
        return umbralMinimo;
    }

    /**
     * Establece el umbral mínimo de cantidad de la pieza.
     *
     * @param umbralMinimo El umbral mínimo de la pieza a establecer.
     */
    public void setUmbralMinimo(int umbralMinimo) {
        this.umbralMinimo = umbralMinimo;
    }

    /**
     * Obtiene el identificador de la imagen asociada con la pieza.
     *
     * @return El identificador de la imagen de la pieza.
     */
    public int getImagenPieza() {
        return imagenPieza;
    }

    /**
     * Establece el identificador de la imagen asociada con la pieza.
     *
     * @param imagenPieza El identificador de la imagen de la pieza a establecer.
     */
    public void setImagenPieza(int imagenPieza) {
        this.imagenPieza = imagenPieza;
    }
}

