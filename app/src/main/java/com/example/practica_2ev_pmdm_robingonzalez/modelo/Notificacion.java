package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Notificacion {

    private String correoEmisor;
    private String correoReceptor;
    private String mensaje;
    private String respuesta;
    private boolean aprobarPresupuesto;
    private Long fechaEnvio;

    public Notificacion() {

    }

    public Notificacion(String correoEmisor, String correoReceptor, String mensaje, String respuesta, boolean aprobarPresupuesto) {
        this.correoEmisor = correoEmisor;
        this.correoReceptor = correoReceptor;
        this.mensaje = mensaje;
        this.respuesta = respuesta;
        this.aprobarPresupuesto = aprobarPresupuesto;
        this.fechaEnvio = System.currentTimeMillis(); //Asigna la fecha actual en milisegundos
    }

    public String getCorreoEmisor() {
        return correoEmisor;
    }

    public void setCorreoEmisor(String correoEmisor) {
        this.correoEmisor = correoEmisor;
    }

    public String getCorreoReceptor() {
        return correoReceptor;
    }

    public void setCorreoReceptor(String correoReceptor) {
        this.correoReceptor = correoReceptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isAprobarPresupuesto() {
        return aprobarPresupuesto;
    }

    public void setAprobarPresupuesto(boolean aprobarPresupuesto) {
        this.aprobarPresupuesto = aprobarPresupuesto;
    }

    public Long getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Long fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
