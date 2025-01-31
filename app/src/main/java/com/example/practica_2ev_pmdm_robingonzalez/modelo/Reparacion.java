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

    /**
     * Constructor vacío de la clase Reparacion.
     * Se utiliza para crear una instancia de Reparacion sin inicializar sus atributos.
     */
    public Reparacion() {}

    /**
     * Constructor de la clase Reparacion que inicializa los atributos principales.
     *
     * @param matriculaCoche La matrícula del coche relacionado con la reparación.
     * @param correoMecanicoJefe El correo electrónico del mecánico jefe asignado a la reparación.
     * @param correoCliente El correo electrónico del cliente que solicita la reparación.
     * @param correosMecanicosAsignados Una lista de correos electrónicos de los mecánicos asignados a la reparación.
     */
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

    /**
     * Obtiene el ID de la reparación.
     *
     * @return El ID de la reparación.
     */
    public String getIdReparacion() {
        return idReparacion;
    }

    /**
     * Establece el ID de la reparación.
     *
     * @param idReparacion El ID de la reparación a establecer.
     */
    public void setIdReparacion(String idReparacion) {
        this.idReparacion = idReparacion;
    }

    /**
     * Obtiene la matrícula del coche relacionado con la reparación.
     *
     * @return La matrícula del coche.
     */
    public String getMatriculaCoche() {
        return matriculaCoche;
    }

    /**
     * Establece la matrícula del coche relacionado con la reparación.
     *
     * @param matriculaCoche La matrícula del coche a establecer.
     */
    public void setMatriculaCoche(String matriculaCoche) {
        this.matriculaCoche = matriculaCoche;
    }

    /**
     * Obtiene el tipo de la reparación.
     *
     * @return El tipo de la reparación.
     */
    public String getTipoReparacion() {
        return tipoReparacion;
    }

    /**
     * Establece el tipo de la reparación.
     *
     * @param tipoReparacion El tipo de la reparación a establecer.
     */
    public void setTipoReparacion(String tipoReparacion) {
        this.tipoReparacion = tipoReparacion;
    }

    /**
     * Obtiene el estado de la reparación.
     *
     * @return El estado de la reparación.
     */
    public String getEstadoReparacion() {
        return estadoReparacion;
    }

    /**
     * Establece el estado de la reparación.
     *
     * @param estadoReparacion El estado de la reparación a establecer.
     */
    public void setEstadoReparacion(String estadoReparacion) {
        this.estadoReparacion = estadoReparacion;
    }

    /**
     * Obtiene la lista de tareas asociadas con la reparación.
     *
     * @return La lista de tareas.
     */
    public List<Tarea> getTareas() {
        return tareas;
    }

    /**
     * Establece la lista de tareas asociadas con la reparación.
     *
     * @param tareas La lista de tareas a establecer.
     */
    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }

    /**
     * Obtiene el presupuesto de la reparación.
     *
     * @return El presupuesto de la reparación.
     */
    public Double getPresupuesto() {
        return presupuesto;
    }

    /**
     * Establece el presupuesto de la reparación.
     *
     * @param presupuesto El presupuesto a establecer.
     */
    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    /**
     * Obtiene la fecha de inicio de la reparación.
     *
     * @return La fecha de inicio en formato de timestamp.
     */
    public Long getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio de la reparación.
     *
     * @param fechaInicio La fecha de inicio a establecer en formato de timestamp.
     */
    public void setFechaInicio(Long fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la lista de correos electrónicos de los mecánicos asignados a la reparación.
     *
     * @return La lista de correos electrónicos de los mecánicos asignados.
     */
    public List<String> getCorreosMecanicosAsignados() {
        return correosMecanicosAsignados;
    }

    /**
     * Establece la lista de correos electrónicos de los mecánicos asignados a la reparación.
     *
     * @param correosMecanicosAsignados La lista de correos electrónicos de los mecánicos a establecer.
     */
    public void setCorreosMecanicosAsignados(List<String> correosMecanicosAsignados) {
        this.correosMecanicosAsignados = correosMecanicosAsignados;
    }

    /**
     * Obtiene el correo electrónico del mecánico jefe asignado a la reparación.
     *
     * @return El correo electrónico del mecánico jefe.
     */
    public String getCorreoMecanicoJefe() {
        return correoMecanicoJefe;
    }

    /**
     * Establece el correo electrónico del mecánico jefe asignado a la reparación.
     *
     * @param correoMecanicoJefe El correo electrónico del mecánico jefe a establecer.
     */
    public void setCorreoMecanicoJefe(String correoMecanicoJefe) {
        this.correoMecanicoJefe = correoMecanicoJefe;
    }

    /**
     * Obtiene el correo electrónico del cliente asociado con la reparación.
     *
     * @return El correo electrónico del cliente.
     */
    public String getCorreoCliente() {
        return correoCliente;
    }

    /**
     * Establece el correo electrónico del cliente asociado con la reparación.
     *
     * @param correoCliente El correo electrónico del cliente a establecer.
     */
    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    /**
     * Obtiene el estado del presupuesto aprobado para la reparación.
     *
     * @return El estado del presupuesto aprobado.
     */
    public String getPresupuestoAprobado() {
        return presupuestoAprobado;
    }

    /**
     * Establece el estado del presupuesto aprobado para la reparación.
     *
     * @param presupuestoAprobado El estado del presupuesto aprobado a establecer.
     */
    public void setPresupuestoAprobado(String presupuestoAprobado) {
        this.presupuestoAprobado = presupuestoAprobado;
    }
}
