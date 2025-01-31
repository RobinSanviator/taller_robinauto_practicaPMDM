package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.FirebaseUtil;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.ReparacionUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Notificacion;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Reparacion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

/**
 * Adaptador para gestionar la lista de notificaciones en un RecyclerView.
 * Permite mostrar notificaciones enviadas a los clientes y responderlas si es necesario.
 */
public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder> {

    private List<Notificacion> notificaciones;
    private Context contexto;

    /**
     * Constructor del adaptador de notificaciones.
     *
     * @param notificaciones Lista de notificaciones a mostrar.
     * @param contexto Contexto de la aplicación.
     */
    public NotificacionAdapter(List<Notificacion> notificaciones, Context contexto) {
        this.notificaciones = notificaciones;
        this.contexto = contexto;
    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla la vista de cada elemento de la lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        // Obtiene la notificación en la posición actual
        Notificacion notificacion = notificaciones.get(position);

        // Muestra el emisor y el mensaje en la interfaz
        holder.textViewNombreEmisor.setText(notificacion.getCorreoEmisor());
        holder.textViewMensaje.setText(notificacion.getMensaje());

        // Si la notificación no tiene respuesta, muestra el botón de responder
        if (notificacion.getRespuesta() == null) {
            holder.buttonResponder.setVisibility(View.VISIBLE);
        } else {
            holder.buttonResponder.setVisibility(View.GONE);
        }

        // Configura el botón para mostrar el diálogo de respuesta
        holder.buttonResponder.setOnClickListener(v -> mostrarDialogoResponder(notificacion, v));
    }

    @Override
    public int getItemCount() {
        return notificaciones.size();
    }

    /**
     * ViewHolder para las notificaciones.
     */
    static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreEmisor, textViewMensaje;
        MaterialButton buttonResponder;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView Vista de la notificación.
         */
        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreEmisor = itemView.findViewById(R.id.textViewNombreEmisor);
            textViewMensaje = itemView.findViewById(R.id.textViewMensaje);
            buttonResponder = itemView.findViewById(R.id.buttonResponderNotificacion);
        }
    }

    /**
     * Muestra un cuadro de diálogo para que el usuario responda a una notificación.
     *
     * @param notificacion Notificación que se va a responder.
     * @param view Vista desde donde se activó el diálogo.
     */
    private void mostrarDialogoResponder(Notificacion notificacion, View view) {
        // Infla la vista del diálogo de respuesta
        View dialogView = LayoutInflater.from(contexto).inflate(R.layout.cliente_responder_notificacion_propuesta, null);

        // Configura los elementos del diálogo
        TextView textViewMensajeRecibido = dialogView.findViewById(R.id.textViewMensajeRecibido);
        RadioGroup radioGroupRespuestaDelCliente = dialogView.findViewById(R.id.radioGroupRespuestaDelCliente);

        // Muestra el mensaje recibido en el diálogo
        textViewMensajeRecibido.setText(notificacion.getMensaje());

        // Crea el cuadro de diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setView(dialogView)
                .setTitle("Responder Notificación")
                .setPositiveButton("Enviar", (dialog, which) -> {
                    int respuestaSeleccionada = radioGroupRespuestaDelCliente.getCheckedRadioButtonId();

                    // Verifica qué opción fue seleccionada y guarda la respuesta
                    if (respuestaSeleccionada == R.id.radioButtonAceptar) {
                        notificacion.setRespuesta("Acepta el presupuesto");
                        ReparacionUtil.actualizarPresupuestoAprobado(notificacion.getCorreoReceptor(), Reparacion.PRESUPUESTO_APROBADO);
                    } else if (respuestaSeleccionada == R.id.radioButtonNoAceptar) {
                        notificacion.setRespuesta("No acepta el presupuesto");
                        ReparacionUtil.actualizarPresupuestoAprobado(notificacion.getCorreoReceptor(), Reparacion.PRESUPUESTO_NO_APROBADO);
                    }

                    // Guarda la respuesta en Firebase
                    guardarRespuestaEnFirebase(notificacion, view);
                })
                .setNegativeButton("Cancelar", null);

        // Muestra el diálogo en pantalla
        builder.create().show();
    }

    /**
     * Guarda la respuesta del usuario en Firebase.
     *
     * @param notificacion Notificación que se está actualizando.
     * @param view Vista desde donde se activó la acción.
     */
    private void guardarRespuestaEnFirebase(Notificacion notificacion, View view) {
        DatabaseReference ref = FirebaseUtil.getFirebaseDatabase().getReference("Notificaciones");

        // Verifica si la notificación tiene un ID antes de intentar guardarla
        if (notificacion.getId() != null) {
            ref.child(notificacion.getId())
                    .setValue(notificacion)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(view, "Respuesta guardada correctamente", Snackbar.LENGTH_SHORT).show();

                            // Actualiza el estado de la reparación correspondiente
                            actualizarEstadoReparacion(notificacion);
                        } else {
                            Snackbar.make(view, "Error al guardar respuesta", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Snackbar.make(view, "Error: ID de notificación no encontrado", Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * Actualiza el estado de la reparación en función de la respuesta del cliente.
     *
     * @param notificacion Notificación que contiene la respuesta del cliente.
     */
    private void actualizarEstadoReparacion(Notificacion notificacion) {
        // Extrae el correo del cliente de la notificación
        String correoCliente = notificacion.getCorreoReceptor();
        String respuesta = notificacion.getRespuesta();

        // Actualiza la reparación en función de la respuesta del cliente
        ReparacionUtil.actualizarEstadoReparacion(correoCliente, respuesta);
    }
}
