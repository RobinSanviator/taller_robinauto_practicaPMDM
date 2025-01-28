package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Coche {
    private String marca;
    private String modelo;
    private String matricula;
    private String nombreCliente;
    private String correoCliente;
    private String nombreMecanicoJefe;
    private String correoMecanicoJefe;

    public Coche(){}

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

    public String getCorreoMecanicoJefe() {
        return correoMecanicoJefe;
    }

    public void setCorreoMecanicoJefe(String correoMecanicoJefe) {
        this.correoMecanicoJefe = correoMecanicoJefe;
    }

    public String getNombreMecanicoJefe() {
        return nombreMecanicoJefe;
    }

    public void setNombreMecanicoJefe(String nombreMecanicoJefe) {
        this.nombreMecanicoJefe = nombreMecanicoJefe;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
}

