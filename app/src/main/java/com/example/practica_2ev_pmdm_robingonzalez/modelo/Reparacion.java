package com.example.practica_2ev_pmdm_robingonzalez.modelo;

import java.util.ArrayList;
import java.util.List;

public class Reparacion {
    public static final String PRESUPUESTO_APROBADO = "Presupuesto aceptado";
    public static final String PRESUPUESTO_NO_APROBADO = "Presupuesto no aceptado";
    public static final String PRESUPUESTO_NO_RESPONDIDO = "No ha respondido";

    private String matriculaCoche;
    private String tipoReparacion;
    private String estadoReparacion;
    private List<Tarea> tareas;
    private Double presupuesto;
    private Long fechaInicio; // Timestamp en milisegundos
    private Long fechaFin;
    private List<String> correosMecanicosAsignados;
    private String correoMecanicoJefe;
    private String correoCliente;
    private String presupuestoAprobado;

    // Constructor vacío
    public Reparacion() {
        // Inicializar valores por defecto si se usa el constructor vacío
        this.tipoReparacion = "Pendiente";
        this.estadoReparacion = "Pendiente";
        this.tareas = new ArrayList<>();
        this.presupuestoAprobado = PRESUPUESTO_NO_RESPONDIDO;
    }

    // Constructor con parámetros
    public Reparacion(String matriculaCoche, String correoMecanicoJefe,
                      String correoCliente, List<String> correosMecanicosAsignados) {
        this.matriculaCoche = matriculaCoche;
        this.tipoReparacion = "Pendiente"; // Inicial
        this.estadoReparacion = "Pendiente"; // Inicial
        this.tareas = new ArrayList<>(); // Lista vacía
        this.presupuesto = null; // Presupuesto no definido al inicio
        this.fechaInicio = System.currentTimeMillis(); // Fecha actual
        this.fechaFin = null; // Sin fecha de fin inicial
        this.correoMecanicoJefe = correoMecanicoJefe;
        this.correoCliente = correoCliente;
        this.correosMecanicosAsignados = correosMecanicosAsignados != null ? correosMecanicosAsignados : new ArrayList<>();
        this.presupuestoAprobado = PRESUPUESTO_NO_RESPONDIDO; // Valor por defecto
    }

    // Getters y setters
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

    public Long getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Long fechaFin) {
        this.fechaFin = fechaFin;
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
