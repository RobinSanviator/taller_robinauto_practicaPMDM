package com.example.practica_2ev_pmdm_robingonzalez.modelo;

public class Tarea {
    private String idTarea;
    private String idReparacion;
    private String descripcion;
    private String estado;
    private String correoMecanicoAsignado;
    private String comentario;

    /**
     * Constructor vacío requerido por Firebase.
     * Este constructor es necesario para la deserialización desde Firebase.
     */
    public Tarea() {}

    /**
     * Constructor de la clase Tarea que inicializa los atributos principales.
     *
     * @param idTarea El ID de la tarea.
     * @param idReparacion El ID de la reparación asociada.
     * @param descripcion La descripción de la tarea.
     * @param estado El estado de la tarea.
     * @param correoMecanicoAsignado El correo del mecánico asignado a la tarea.
     * @param comentario El comentario asociado con la tarea.
     */
    public Tarea(String idTarea, String idReparacion, String descripcion, String estado, String correoMecanicoAsignado, String comentario) {
        this.idTarea = idTarea;
        this.idReparacion = idReparacion;
        this.descripcion = descripcion;
        this.estado = estado;
        this.correoMecanicoAsignado = correoMecanicoAsignado;
        this.comentario = comentario;
    }

    /**
     * Obtiene el ID de la tarea.
     *
     * @return El ID de la tarea.
     */
    public String getIdTarea() {
        return idTarea;
    }

    /**
     * Establece el ID de la tarea.
     *
     * @param idTarea El ID de la tarea a establecer.
     */
    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }

    /**
     * Obtiene el ID de la reparación asociada con la tarea.
     *
     * @return El ID de la reparación asociada.
     */
    public String getIdReparacion() {
        return idReparacion;
    }

    /**
     * Establece el ID de la reparación asociada con la tarea.
     *
     * @param idReparacion El ID de la reparación a establecer.
     */
    public void setIdReparacion(String idReparacion) {
        this.idReparacion = idReparacion;
    }

    /**
     * Obtiene la descripción de la tarea.
     *
     * @return La descripción de la tarea.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la tarea.
     *
     * @param descripcion La descripción de la tarea a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el estado de la tarea.
     *
     * @return El estado de la tarea.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la tarea.
     *
     * @param estado El estado de la tarea a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el correo electrónico del mecánico asignado a la tarea.
     *
     * @return El correo electrónico del mecánico asignado.
     */
    public String getCorreoMecanicoAsignado() {
        return correoMecanicoAsignado;
    }

    /**
     * Establece el correo electrónico del mecánico asignado a la tarea.
     *
     * @param correoMecanicoAsignado El correo electrónico del mecánico a establecer.
     */
    public void setCorreoMecanicoAsignado(String correoMecanicoAsignado) {
        this.correoMecanicoAsignado = correoMecanicoAsignado;
    }

    /**
     * Obtiene el comentario asociado con la tarea.
     *
     * @return El comentario de la tarea.
     */
    public String getComentario() {
        return comentario;
    }

    /**
     * Establece el comentario asociado con la tarea.
     *
     * @param comentario El comentario de la tarea a establecer.
     */
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}



