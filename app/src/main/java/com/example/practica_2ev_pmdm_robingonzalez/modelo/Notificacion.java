package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Notificacion {
    private String id;
    private String correoEmisor;
    private String correoReceptor;
    private String mensaje;
    private String respuesta;
    private Long fechaEnvio;

    public Notificacion() {

    }

    public Notificacion(String id, String correoEmisor, String correoReceptor,
                        String mensaje, String respuesta) {
        this.id = id;
        this.correoEmisor = correoEmisor;
        this.correoReceptor = correoReceptor;
        this.mensaje = mensaje;
        this.fechaEnvio = System.currentTimeMillis(); //Asigna la fecha actual en milisegundos
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Long getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Long fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }
}
