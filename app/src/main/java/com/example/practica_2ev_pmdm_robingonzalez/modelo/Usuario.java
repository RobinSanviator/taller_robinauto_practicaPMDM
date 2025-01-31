package com.example.practica_2ev_pmdm_robingonzalez.modelo;
/**
 * Clase que representa un usuario en el sistema.
 * Contiene información sobre el usuario, como su nombre, apellidos, correo, teléfono,
 * contraseña y tipo de usuario (por ejemplo, Administrativo, Mecánico, etc.).
 */
public class Usuario {

    // Atributos de la clase
    private int idUsuario; // ID único del usuario
    private String nombre; // Nombre del usuario
    private String apellidos; // Apellidos del usuario
    private String correo; // Correo electrónico del usuario
    private String telefono; // Número de teléfono del usuario
    private String contrasenya; // Contraseña del usuario
    private String tipoUsuario; // Tipo de usuario (por ejemplo, Administrativo, Mecánico, etc.)

    /**
     * Constructor vacío necesario para Firebase y otras operaciones de serialización.
     */
    public Usuario() {}

    /**
     * Constructor para crear una instancia de Usuario con los atributos principales.
     *
     * @param nombre El nombre del usuario.
     * @param apellidos Los apellidos del usuario.
     * @param correo El correo electrónico del usuario.
     * @param telefono El número de teléfono del usuario.
     * @param contrasenya La contraseña del usuario.
     * @param tipoUsuario El tipo de usuario (por ejemplo, Administrativo, Mecánico, etc.).
     */
    public Usuario(String nombre, String apellidos, String correo,
                   String telefono, String contrasenya, String tipoUsuario) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.contrasenya = contrasenya;
        this.tipoUsuario = tipoUsuario;
    }

    // Getters y Setters

    /**
     * Obtiene el ID del usuario.
     *
     * @return El ID del usuario.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario.
     *
     * @param idUsuario El ID del usuario.
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return El nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre El nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del usuario.
     *
     * @return Los apellidos del usuario.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del usuario.
     *
     * @param apellidos Los apellidos del usuario.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return El correo electrónico del usuario.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param correo El correo electrónico del usuario.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene el número de teléfono del usuario.
     *
     * @return El número de teléfono del usuario.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el número de teléfono del usuario.
     *
     * @param telefono El número de teléfono del usuario.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return La contraseña del usuario.
     */
    public String getContrasenya() {
        return contrasenya;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param contrasenya La contraseña del usuario.
     */
    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    /**
     * Obtiene el tipo de usuario.
     *
     * @return El tipo de usuario (por ejemplo, Administrativo, Mecánico, etc.).
     */
    public String getTipoUsuario() {
        return tipoUsuario;
    }

    /**
     * Establece el tipo de usuario.
     *
     * @param tipoUsuario El tipo de usuario (por ejemplo, Administrativo, Mecánico, etc.).
     */
    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
}