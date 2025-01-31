package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Tarea {
    private String idTarea;
    private String idReparacion;
    private String descripcion;
    private String estado;
    private String correoMecanicoAsignado;
    private String comentario;

    // Constructor vacío requerido por Firebase
    public Tarea() {}

    // Constructor con parámetros
    public Tarea(String idTarea, String idReparacion, String descripcion, String estado, String correoMecanicoAsignado, String comentario) {
        this.idTarea = idTarea;
        this.idReparacion = idReparacion;
        this.descripcion = descripcion;
        this.estado = estado;
        this.correoMecanicoAsignado = correoMecanicoAsignado;
        this.comentario = comentario;
    }

    // Getters y Setters
    public String getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }

    public String getIdReparacion() {
        return idReparacion;
    }

    public void setIdReparacion(String idReparacion) {
        this.idReparacion = idReparacion;
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


