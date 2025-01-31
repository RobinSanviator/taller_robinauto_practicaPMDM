package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.TareaUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Tarea;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class TareaAdaptador extends RecyclerView.Adapter<TareaAdaptador.TareaViewHolder> {

    private List<Tarea> listaTareas;
    private Context context;
    private String idReparacion;

    public TareaAdaptador(List<Tarea> listaTareas, Context context, String idReparacion) {
        this.listaTareas = listaTareas;
        this.context = context;
        this.idReparacion = idReparacion;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout de cada tarea
        View view = LayoutInflater.from(context).inflate(R.layout.lista_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = listaTareas.get(position);

        // Establecer la descripción de la tarea
        descripcionTarea(holder, tarea);

        // Establecer el estado de la tarea
        estadoTarea(holder, tarea);

        // Establecer el comentario en el EditText
        comentarioTarea(holder, tarea);

        // Configurar el botón "Terminado"
        botonTerminado(holder, tarea, position);
    }


    @Override
    public int getItemCount() {
        return listaTareas.size();
    }

    // ViewHolder para manejar las vistas
    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDescripcionTarea, textViewEstadoTarea;
        EditText editTextComentarioTarea;
        MaterialButton buttonTerminado;

        public TareaViewHolder(View itemView) {
            super(itemView);
            textViewDescripcionTarea = itemView.findViewById(R.id.textViewDescripcionTarea);
            textViewEstadoTarea = itemView.findViewById(R.id.textViewEstadoTarea);
            editTextComentarioTarea = itemView.findViewById(R.id.editTextComentarioTarea);
            buttonTerminado = itemView.findViewById(R.id.buttonTerminadaTara);
        }
    }

    private void descripcionTarea(TareaViewHolder holder, Tarea tarea) {
        holder.textViewDescripcionTarea.setText(tarea.getDescripcion());
    }

    private void estadoTarea(TareaViewHolder holder, Tarea tarea) {
        if (tarea.getEstado().equals("Pendiente")) {
            holder.textViewEstadoTarea.setText("Pendiente");
            holder.textViewEstadoTarea.setBackgroundColor(ContextCompat.getColor(context, R.color.color_advertencia)); // Fondo pendiente
            holder.buttonTerminado.setVisibility(View.VISIBLE); // Mostrar botón "Terminado"
        } else {
            holder.textViewEstadoTarea.setText("Terminada");
            holder.textViewEstadoTarea.setBackgroundColor(ContextCompat.getColor(context, R.color.verde)); // Fondo verde para terminado
            holder.buttonTerminado.setVisibility(View.GONE); // Ocultar el botón "Terminado"
        }
    }

    private void comentarioTarea(TareaViewHolder holder, Tarea tarea) {
        holder.editTextComentarioTarea.setText(tarea.getComentario());
        holder.editTextComentarioTarea.setTextColor(ContextCompat.getColor(context, R.color.color_texto));
    }

    private void botonTerminado(TareaViewHolder holder, Tarea tarea, int position) {
        holder.buttonTerminado.setOnClickListener(v -> {
            // Obtener el comentario ingresado
            String comentario = holder.editTextComentarioTarea.getText().toString().trim();

            // Actualizar el estado y comentario de la tarea
            tarea.setEstado("Terminada");
            tarea.setComentario(comentario); // Actualizar el comentario

            // Actualizar el fondo y estado en la interfaz
            holder.textViewEstadoTarea.setText("Terminada");
            holder.textViewEstadoTarea.setBackgroundColor(ContextCompat.getColor(context, R.color.verde));
            holder.textViewDescripcionTarea.setBackgroundColor(ContextCompat.getColor(context, R.color.color_desactivado_fondo));
            holder.buttonTerminado.setVisibility(View.GONE); // El botón desaparece

            // Llamar al método de actualización en Firebase
            TareaUtil.actualizarTareaEnFirebase(tarea.getIdTarea(), tarea);

            // Llamar a la función para verificar si todas las tareas de la reparación están terminadas
            TareaUtil.verificarYActualizarEstadoReparacion(tarea.getIdReparacion());

            // Notificar que el ítem ha cambiado (una sola vez)
            notifyItemChanged(position); // Actualiza la vista
        });
    }

}
