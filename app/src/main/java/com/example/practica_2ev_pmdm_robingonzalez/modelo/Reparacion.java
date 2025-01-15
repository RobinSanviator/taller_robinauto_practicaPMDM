package com.example.practica_2ev_pmdm_robingonzalez.modelo;

import java.util.ArrayList;
import java.util.List;

public class Reparacion {
    private String matriculaCoche;
    private String tipoReparacion;
    private String estadoReparacion;
    private List<Tarea> tareas;
    private double presupuesto;
    private boolean presupuestoAprobado;
    private long fechaInicio; //Timestamp en milisegundos
    private long fechaFin;
    private String correoMecanicoJefe;
    private String correoCliente;

    //Constructor vacío
    public Reparacion() {
    }

    // Constructor con parámetros, usado para crear la reparación inicial
    public Reparacion(String matriculaCoche, double presupuesto, String correoMecanicoJefe, String correoCliente) {
        this.matriculaCoche = matriculaCoche;
        this.tipoReparacion = "Pendiente"; // No se define aún, se actualizará más tarde
        this.estadoReparacion = "Pendiente"; // Estado inicial
        this.tareas = new ArrayList<>(); // Lista de tareas vacía
        this.presupuesto = presupuesto;
        this.presupuestoAprobado = false; // Por defecto
        this.fechaInicio = System.currentTimeMillis(); // Fecha actual
        this.fechaFin = 0; // No hay fecha de fin al inicio
        this.correoMecanicoJefe = correoMecanicoJefe;
        this.correoCliente = correoCliente;
    }


    public Reparacion(String matriculaCoche, String tipoReparacion, String estadoReparacion,
                      List<Tarea> tareas, double presupuesto, boolean presupuestoAprobado,
                      long fechaFin, String correoMecanicoJefe, String correoCliente) {

        this.matriculaCoche = matriculaCoche;
        this.tipoReparacion = tipoReparacion;
        this.estadoReparacion = "Pendiente"; //Estado inicial
        this.tareas = new ArrayList<>(); //Tareas vacías
        this.presupuesto = presupuesto;
        this.presupuestoAprobado = false; //Por defecto
        this.fechaInicio = System.currentTimeMillis(); //Fecha y hora actual
        this.fechaFin = fechaFin;
        this.correoMecanicoJefe = correoMecanicoJefe;
        this.correoCliente = correoCliente;
    }

    // Método para actualizar el estado del presupuesto
    public void actualizarPresupuestoAprobado(boolean aprobado) {
        this.presupuestoAprobado = aprobado;
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

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public boolean isPresupuestoAprobado() {
        return presupuestoAprobado;
    }

    public void setPresupuestoAprobado(boolean presupuestoAprobado) {
        this.presupuestoAprobado = presupuestoAprobado;
    }

    public long getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public long getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(long fechaFin) {
        this.fechaFin = fechaFin;
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
}
