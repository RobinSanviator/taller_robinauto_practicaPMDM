package com.example.practica_2ev_pmdm_robingonzalez.modelo;

/**
 * Clase que representa un coche en el sistema.
 * Contiene información sobre la marca, modelo, matrícula, cliente asociado y mecánico jefe asignado.
 */
public class Coche {

    // Atributos de la clase
    private String marca; // Marca del coche
    private String modelo; // Modelo del coche
    private String matricula; // Matrícula del coche
    private String nombreCliente; // Nombre del cliente asociado al coche
    private String correoCliente; // Correo del cliente asociado al coche
    private String nombreMecanicoJefe; // Nombre del mecánico jefe asignado al coche
    private String correoMecanicoJefe; // Correo del mecánico jefe asignado al coche

    /**
     * Constructor vacío necesario para Firebase y otras operaciones de serialización.
     */
    public Coche() {}

    /**
     * Constructor para crear una instancia de Coche con todos los atributos.
     *
     * @param marca La marca del coche.
     * @param modelo El modelo del coche.
     * @param matricula La matrícula del coche.
     * @param nombreCliente El nombre del cliente asociado al coche.
     * @param correoCliente El correo del cliente asociado al coche.
     * @param nombreMecanicoJefe El nombre del mecánico jefe asignado al coche.
     * @param correoMecanicoJefe El correo del mecánico jefe asignado al coche.
     */
    public Coche(String marca, String modelo, String matricula, String nombreCliente, String correoCliente,
                 String nombreMecanicoJefe, String correoMecanicoJefe) {
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.nombreCliente = nombreCliente;
        this.correoCliente = correoCliente;
        this.nombreMecanicoJefe = nombreMecanicoJefe;
        this.correoMecanicoJefe = correoMecanicoJefe;
    }

    // Getters y Setters

    /**
     * Obtiene el correo del mecánico jefe asignado al coche.
     *
     * @return El correo del mecánico jefe.
     */
    public String getCorreoMecanicoJefe() {
        return correoMecanicoJefe;
    }

    /**
     * Establece el correo del mecánico jefe asignado al coche.
     *
     * @param correoMecanicoJefe El correo del mecánico jefe.
     */
    public void setCorreoMecanicoJefe(String correoMecanicoJefe) {
        this.correoMecanicoJefe = correoMecanicoJefe;
    }

    /**
     * Obtiene el nombre del mecánico jefe asignado al coche.
     *
     * @return El nombre del mecánico jefe.
     */
    public String getNombreMecanicoJefe() {
        return nombreMecanicoJefe;
    }

    /**
     * Establece el nombre del mecánico jefe asignado al coche.
     *
     * @param nombreMecanicoJefe El nombre del mecánico jefe.
     */
    public void setNombreMecanicoJefe(String nombreMecanicoJefe) {
        this.nombreMecanicoJefe = nombreMecanicoJefe;
    }

    /**
     * Obtiene el correo del cliente asociado al coche.
     *
     * @return El correo del cliente.
     */
    public String getCorreoCliente() {
        return correoCliente;
    }

    /**
     * Establece el correo del cliente asociado al coche.
     *
     * @param correoCliente El correo del cliente.
     */
    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    /**
     * Obtiene el nombre del cliente asociado al coche.
     *
     * @return El nombre del cliente.
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Establece el nombre del cliente asociado al coche.
     *
     * @param nombreCliente El nombre del cliente.
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * Obtiene la matrícula del coche.
     *
     * @return La matrícula del coche.
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * Establece la matrícula del coche.
     *
     * @param matricula La matrícula del coche.
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * Obtiene el modelo del coche.
     *
     * @return El modelo del coche.
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Establece el modelo del coche.
     *
     * @param modelo El modelo del coche.
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Obtiene la marca del coche.
     *
     * @return La marca del coche.
     */
    public String getMarca() {
        return marca;
    }

    /**
     * Establece la marca del coche.
     *
     * @param marca La marca del coche.
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }
}
