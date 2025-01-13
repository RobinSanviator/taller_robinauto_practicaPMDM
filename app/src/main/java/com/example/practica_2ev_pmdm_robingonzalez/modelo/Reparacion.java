package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Reparacion {
    private String tipoReparacion;
    private String estadoReparacion;
    private String diagnostico;
    private String correoMecanicoJefe;

    public Reparacion(){}

    public Reparacion(String tipoReparacion, String estadoReparacion, String diagnostico, String correoMecanicoJefe) {
        this.tipoReparacion = tipoReparacion;
        this.estadoReparacion = estadoReparacion;
        this.diagnostico = diagnostico;
        this.correoMecanicoJefe = correoMecanicoJefe;
    }

    public String getTipoReparacion() {
        return tipoReparacion;
    }

    public void setTipoReparacion(String tipoReparacion) {
        this.tipoReparacion = tipoReparacion;
    }

    public String getEstadoReparacion() {
        return estadoReparacion;
    }

    public void setEstadoReparacion(String estadoReparacion) {
        this.estadoReparacion = estadoReparacion;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getCorreoMecanicoJefe() {
        return correoMecanicoJefe;
    }

    public void setCorreoMecanicoJefe(String correoMecanicoJefe) {
        this.correoMecanicoJefe = correoMecanicoJefe;
    }
}
