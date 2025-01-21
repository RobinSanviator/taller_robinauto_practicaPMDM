package com.example.practica_2ev_pmdm_robingonzalez.modelo;

import java.util.ArrayList;
import java.util.List;

public class Reparacion {
    private String matriculaCoche;
    private String tipoReparacion;
    private String estadoReparacion;
    private List<Tarea> tareas;
    private double presupuesto;
    private Long fechaInicio; //Timestamp en milisegundos
    private Long fechaFin;
    private List<String> correosMecanicosAsignados;
    private String correoMecanicoJefe;
    private String correoCliente;

    //Constructor vacío
    public Reparacion() {
    }

    // Constructor con parámetros, usado para crear la reparación inicial
    public Reparacion(String matriculaCoche, double presupuesto, String correoMecanicoJefe,
                      String correoCliente, List<String> correosMecanicosAsignados) {
        this.matriculaCoche = matriculaCoche;
        this.tipoReparacion = "Pendiente"; // No se define aún, se actualizará más tarde
        this.estadoReparacion = "Pendiente"; // Estado inicial
        this.tareas = new ArrayList<>(); // Lista de tareas vacía
        this.presupuesto = presupuesto;
        this.fechaInicio = System.currentTimeMillis(); // Fecha actual
        this.fechaFin = null; // No hay fecha de fin al inicio
        this.correoMecanicoJefe = correoMecanicoJefe;
        this.correoCliente = correoCliente;
        this.correosMecanicosAsignados = correosMecanicosAsignados != null ? correosMecanicosAsignados : new ArrayList<>();
    }


    // Constructor con parámetros adicionales para reparaciones con tareas y otros detalles
    public Reparacion(String matriculaCoche, String tipoReparacion, String estadoReparacion,
                      List<Tarea> tareas, double presupuesto, boolean presupuestoAprobado,
                      Long fechaFin, String correoMecanicoJefe, String correoCliente) {

        this.matriculaCoche = matriculaCoche;
        this.tipoReparacion = tipoReparacion;
        this.estadoReparacion = estadoReparacion; // Se usa el valor proporcionado
        this.tareas = tareas != null ? tareas : new ArrayList<>(); // Asegurarse de que las tareas no sean nulas
        this.presupuesto = presupuesto;
        this.fechaInicio = System.currentTimeMillis(); // Fecha y hora actual
        this.fechaFin = fechaFin;
        this.correoMecanicoJefe = correoMecanicoJefe;
        this.correoCliente = correoCliente;
        this.correosMecanicosAsignados = new ArrayList<>();
    }




    //Método para finalizar el estado y asignar la fecha de fin
    public void finalizarReparacion() {
        // Asigna la fecha actual como la fecha de finalización
        this.fechaFin = System.currentTimeMillis();
        // Puedes actualizar también el estado de la reparación si es necesario
        this.estadoReparacion = "Finalizada";
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
}
