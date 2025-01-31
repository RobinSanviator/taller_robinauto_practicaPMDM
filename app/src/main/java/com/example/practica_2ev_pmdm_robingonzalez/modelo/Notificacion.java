package com.example.practica_2ev_pmdm_robingonzalez.modelo;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Clase que representa una notificación en el sistema.
 * Contiene información sobre el emisor, receptor, mensaje, respuesta y fecha de envío.
 * Además, proporciona un método para guardar la notificación en Firebase.
 */
public class Notificacion {

    // Atributos de la clase
    private String id; // ID único de la notificación
    private String correoEmisor; // Correo del emisor de la notificación
    private String correoReceptor; // Correo del receptor de la notificación
    private String mensaje; // Mensaje de la notificación
    private String respuesta; // Respuesta a la notificación (opcional)
    private Long fechaEnvio; // Fecha de envío de la notificación en milisegundos

    /**
     * Constructor vacío necesario para Firebase y otras operaciones de serialización.
     */
    public Notificacion() {}

    /**
     * Constructor para crear una instancia de Notificacion con los atributos principales.
     *
     * @param id El ID de la notificación.
     * @param correoEmisor El correo del emisor de la notificación.
     * @param correoReceptor El correo del receptor de la notificación.
     * @param mensaje El mensaje de la notificación.
     * @param respuesta La respuesta a la notificación (opcional).
     */
    public Notificacion(String id, String correoEmisor, String correoReceptor,
                        String mensaje, String respuesta) {
        this.id = id;
        this.correoEmisor = correoEmisor;
        this.correoReceptor = correoReceptor;
        this.mensaje = mensaje;
        this.respuesta = respuesta;
        this.fechaEnvio = System.currentTimeMillis(); // Asigna la fecha actual en milisegundos
    }

    // Getters y Setters

    /**
     * Obtiene el ID de la notificación.
     *
     * @return El ID de la notificación.
     */
    public String getId() {
        return id;
    }

    /**
     * Establece el ID de la notificación.
     *
     * @param id El ID de la notificación.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el correo del emisor de la notificación.
     *
     * @return El correo del emisor.
     */
    public String getCorreoEmisor() {
        return correoEmisor;
    }

    /**
     * Establece el correo del emisor de la notificación.
     *
     * @param correoEmisor El correo del emisor.
     */
    public void setCorreoEmisor(String correoEmisor) {
        this.correoEmisor = correoEmisor;
    }

    /**
     * Obtiene el correo del receptor de la notificación.
     *
     * @return El correo del receptor.
     */
    public String getCorreoReceptor() {
        return correoReceptor;
    }

    /**
     * Establece el correo del receptor de la notificación.
     *
     * @param correoReceptor El correo del receptor.
     */
    public void setCorreoReceptor(String correoReceptor) {
        this.correoReceptor = correoReceptor;
    }

    /**
     * Obtiene el mensaje de la notificación.
     *
     * @return El mensaje de la notificación.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el mensaje de la notificación.
     *
     * @param mensaje El mensaje de la notificación.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Obtiene la respuesta a la notificación.
     *
     * @return La respuesta a la notificación.
     */
    public String getRespuesta() {
        return respuesta;
    }

    /**
     * Establece la respuesta a la notificación.
     *
     * @param respuesta La respuesta a la notificación.
     */
    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    /**
     * Obtiene la fecha de envío de la notificación en milisegundos.
     *
     * @return La fecha de envío en milisegundos.
     */
    public Long getFechaEnvio() {
        return fechaEnvio;
    }

    /**
     * Establece la fecha de envío de la notificación en milisegundos.
     *
     * @param fechaEnvio La fecha de envío en milisegundos.
     */
    public void setFechaEnvio(Long fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    /**
     * Guarda la notificación en Firebase en el nodo "Notificaciones".
     * Genera un ID único para la notificación y la almacena en la base de datos.
     */
    public void guardarNotificacionEnFirebase() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Notificaciones");
        String idNotificacion = database.push().getKey();  // Obtiene una clave única para la notificación
        this.setId(idNotificacion);

        // Guarda la notificación en Firebase
        database.child(idNotificacion).setValue(this)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Notificacion", "Notificación guardada exitosamente");
                    } else {
                        Log.d("Notificacion", "Error al guardar la notificación", task.getException());
                    }
                });
    }
}