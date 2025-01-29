package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Tarea {
    private String descripcion;
    private String estado;
    private String correoMecanicoAsignado;
    private String comentario; // Comentarios del mecánico sobre la tarea

    public Tarea(){}

    public Tarea(String descripcion, String estado, String correoMecanicoAsignado, String comentario) {
        this.descripcion = descripcion;
        this.estado = estado;
        this.correoMecanicoAsignado = correoMecanicoAsignado;
        this.comentario = comentario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCorreoMecanicoAsignado() {
        return correoMecanicoAsignado;
    }

    public void setCorreoMecanicoAsignado(String correoMecanicoAsignado) {
        this.correoMecanicoAsignado = correoMecanicoAsignado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
