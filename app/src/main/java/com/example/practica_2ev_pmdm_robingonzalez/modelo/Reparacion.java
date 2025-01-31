package com.example.practica_2ev_pmdm_robingonzalez.modelo;

import java.util.ArrayList;
import java.util.List;

public class Reparacion {
    public static final String PRESUPUESTO_APROBADO = "Presupuesto aceptado";
    public static final String PRESUPUESTO_NO_APROBADO = "Presupuesto no aceptado";
    public static final String PRESUPUESTO_NO_RESPONDIDO = "No ha respondido";

    private String idReparacion;
    private String matriculaCoche;
    private String tipoReparacion;
    private String estadoReparacion;
    private List<Tarea> tareas;
    private Double presupuesto;
    private Long fechaInicio;
    private List<String> correosMecanicosAsignados;
    private String correoMecanicoJefe;
    private String correoCliente;
    private String presupuestoAprobado;

    // Constructor vacío
    public Reparacion() {}

    // Constructor con parámetros
    public Reparacion(String matriculaCoche, String correoMecanicoJefe,
                      String correoCliente, List<String> correosMecanicosAsignados) {
        this.matriculaCoche = matriculaCoche;
        this.tipoReparacion = "Pendiente";
        this.estadoReparacion = "Pendiente";
        this.tareas = new ArrayList<>();
        this.presupuesto = null;
        this.fechaInicio = System.currentTimeMillis();
        this.correoMecanicoJefe = correoMecanicoJefe;
        this.correoCliente = correoCliente;
        this.correosMecanicosAsignados = correosMecanicosAsignados != null ? correosMecanicosAsignados : new ArrayList<>();
        this.presupuestoAprobado = "No ha respondido";
    }

    // Getter y Setter para idReparacion
    public String getIdReparacion() {
        return idReparacion;
    }

    public void setIdReparacion(String idReparacion) {
        this.idReparacion = idReparacion;
    }

    public String getMatriculaCoche() {
        return matriculaCoche;
    }

    public void setMatriculaCoche(String matriculaCoche) {
        this.matriculaCoche = matriculaCoche;
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

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Long getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }


    public List<String> getCorreosMecanicosAsignados() {
        return correosMecanicosAsignados;
    }

    public void setCorreosMecanicosAsignados(List<String> correosMecanicosAsignados) {
        this.correosMecanicosAsignados = correosMecanicosAsignados;
    }

    public String getCorreoMecanicoJefe() {
        return correoMecanicoJefe;
    }

    public void setCorreoMecanicoJefe(String correoMecanicoJefe) {
        this.correoMecanicoJefe = correoMecanicoJefe;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    public String getPresupuestoAprobado() {
        return presupuestoAprobado;
    }

    public void setPresupuestoAprobado(String presupuestoAprobado) {
        this.presupuestoAprobado = presupuestoAprobado;
    }
}
