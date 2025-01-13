package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Coche {
    private String marca;
    private String modelo;
    private String matricula;
    private String correoMecanicoJefe;


    public Coche(){

    }

    public Coche(String marca, String modelo, String matricula, String correoMecanicoJefe) {
        this.marca = marca;
        this.modelo = modelo;
        this.matricula = matricula;
        this.correoMecanicoJefe = correoMecanicoJefe;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCorreoMecanicoJefe() {
        return correoMecanicoJefe;
    }

    public void setCorreoMecanicoJefe(String correoMecanicoJefe) {
        this.correoMecanicoJefe = correoMecanicoJefe;
    }
}

