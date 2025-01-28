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

public class NotificacionAdapter extends RecyclerView.Adapter<NotificacionAdapter.NotificacionViewHolder> {

    private List<Notificacion> notificaciones;
    private Context contexto;


    public NotificacionAdapter(List<Notificacion> notificaciones, Context contexto) {
        this.notificaciones = notificaciones;
        this.contexto = contexto;

    }

    @NonNull
    @Override
    public NotificacionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lista_notificacion, parent, false);
        return new NotificacionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacionViewHolder holder, int position) {
        Notificacion notificacion = notificaciones.get(position);

        holder.textViewNombreEmisor.setText(notificacion.getCorreoEmisor());
        holder.textViewMensaje.setText(notificacion.getMensaje());

        // Mostrar u ocultar botón según si tiene respuesta
        if (notificacion.getRespuesta() == null) {
            holder.buttonResponder.setVisibility(View.VISIBLE);
        } else {
            holder.buttonResponder.setVisibility(View.GONE);
        }

        holder.buttonResponder.setOnClickListener(v -> mostrarDialogoResponder(notificacion, v));

    }


    @Override
    public int getItemCount() {
        return notificaciones.size();
    }


    static class NotificacionViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreEmisor, textViewMensaje;
        MaterialButton buttonResponder;

        public NotificacionViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNombreEmisor = itemView.findViewById(R.id.textViewNombreEmisor);
            textViewMensaje = itemView.findViewById(R.id.textViewMensaje);
            buttonResponder = itemView.findViewById(R.id.buttonResponderNotificacion);
        }
    }

    private void mostrarDialogoResponder(Notificacion notificacion, View view) {
        // Inflar el diseño del diálogo personalizado
        View dialogView = LayoutInflater.from(contexto).inflate(R.layout.cliente_responder_notificacion_propuesta, null);

        // Configuración de elementos del diálogo
        TextView textViewMensajeRecibido = dialogView.findViewById(R.id.textViewMensajeRecibido);
        RadioGroup radioGroupRespuestaDelCliente = dialogView.findViewById(R.id.radioGroupRespuestaDelCliente);

        textViewMensajeRecibido.setText(notificacion.getMensaje()); // Mostrar mensaje recibido

        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
        builder.setView(dialogView)
                .setTitle("Responder Notificación")
                .setPositiveButton("Enviar", (dialog, which) -> {
                    int respuestaSeleccionada = radioGroupRespuestaDelCliente.getCheckedRadioButtonId();

                    // Validar la respuesta seleccionada y actualizar el campo correspondiente
                    if (respuestaSeleccionada == R.id.radioButtonAceptar) {
                        notificacion.setRespuesta("Acepta el presupuesto");
                        ReparacionUtil.actualizarPresupuestoAprobado(notificacion.getCorreoReceptor(), Reparacion.PRESUPUESTO_APROBADO);
                    } else if (respuestaSeleccionada == R.id.radioButtonNoAceptar) {
                        notificacion.setRespuesta("No acepta el presupuesto");
                        ReparacionUtil.actualizarPresupuestoAprobado(notificacion.getCorreoReceptor(), Reparacion.PRESUPUESTO_NO_APROBADO);
                    }
                    // Guardar la respuesta en Firebase
                    guardarRespuestaEnFirebase(notificacion, view);
                })
                .setNegativeButton("Cancelar", null);

        // Mostrar el diálogo
        builder.create().show();
    }

    private void guardarRespuestaEnFirebase(Notificacion notificacion, View view) {
        DatabaseReference ref = FirebaseUtil.getFirebaseDatabase().getReference("Notificaciones");
        if (notificacion.getId() != null) {
            ref.child(notificacion.getId())
                    .setValue(notificacion)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Snackbar.make(view, "Respuesta guardada correctamente", Snackbar.LENGTH_SHORT).show();

                            // Actualizar el estado de la reparación
                            actualizarEstadoReparacion(notificacion);
                        } else {
                            Snackbar.make(view, "Error al guardar respuesta", Snackbar.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Snackbar.make(view, "Error: ID de notificación no encontrado", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void actualizarEstadoReparacion(Notificacion notificacion) {
        // Usar correoReceptor (correo del cliente) para encontrar la reparación correspondiente
        String correoCliente = notificacion.getCorreoReceptor();
        String respuesta = notificacion.getRespuesta();

        // Buscar reparación por correo del cliente
        ReparacionUtil.actualizarEstadoReparacion(correoCliente, respuesta);
    }
}