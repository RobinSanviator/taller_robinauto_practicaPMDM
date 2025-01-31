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

/**
 * Adaptador para mostrar las tareas en una lista utilizando RecyclerView.
 * Este adaptador se encarga de inflar el layout de cada tarea y manejar la lógica
 * de interacción con los elementos de la lista, como la actualización del estado de la tarea.
 */
public class TareaAdaptador extends RecyclerView.Adapter<TareaAdaptador.TareaViewHolder> {

    private List<Tarea> listaTareas;  // Lista de tareas a mostrar
    private Context context;           // Contexto para acceder a recursos
    private String idReparacion;      // ID de la reparación asociada

    /**
     * Constructor para inicializar el adaptador con la lista de tareas, contexto y ID de reparación.
     *
     * @param listaTareas Lista de tareas a mostrar.
     * @param context Contexto para acceder a recursos y vistas.
     * @param idReparacion ID de la reparación asociada a las tareas.
     */
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
        return new TareaViewHolder(view);  // Crear y retornar el ViewHolder
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        // Obtener la tarea en la posición correspondiente
        Tarea tarea = listaTareas.get(position);

        // Establecer la descripción de la tarea
        descripcionTarea(holder, tarea);

        // Establecer el estado de la tarea
        estadoTarea(holder, tarea);

        // Establecer el comentario de la tarea en el EditText
        comentarioTarea(holder, tarea);

        // Configurar el botón "Terminado"
        botonTerminado(holder, tarea, position);
    }

    @Override
    public int getItemCount() {
        return listaTareas.size();  // Retorna el número de tareas
    }

    /**
     * ViewHolder para manejar las vistas relacionadas con cada tarea.
     * Este contiene los elementos visuales de cada tarea en la lista.
     */
    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDescripcionTarea, textViewEstadoTarea;  // Vistas de la descripción y estado
        EditText editTextComentarioTarea;  // EditText para el comentario
        MaterialButton buttonTerminado;  // Botón para marcar la tarea como terminada

        /**
         * Constructor que inicializa los elementos del ViewHolder.
         *
         * @param itemView Vista de cada item en la lista.
         */
        public TareaViewHolder(View itemView) {
            super(itemView);
            textViewDescripcionTarea = itemView.findViewById(R.id.textViewDescripcionTarea);
            textViewEstadoTarea = itemView.findViewById(R.id.textViewEstadoTarea);
            editTextComentarioTarea = itemView.findViewById(R.id.editTextComentarioTarea);
            buttonTerminado = itemView.findViewById(R.id.buttonTerminadaTara);
        }
    }

    /**
     * Método para actualizar la descripción de la tarea en el ViewHolder.
     *
     * @param holder El ViewHolder donde se muestra la tarea.
     * @param tarea La tarea que contiene la descripción.
     */
    private void descripcionTarea(TareaViewHolder holder, Tarea tarea) {
        holder.textViewDescripcionTarea.setText(tarea.getDescripcion());
    }

    /**
     * Método para actualizar el estado de la tarea en el ViewHolder.
     * Se cambia el fondo y el texto según el estado de la tarea (Pendiente o Terminada).
     *
     * @param holder El ViewHolder donde se muestra la tarea.
     * @param tarea La tarea que contiene el estado.
     */
    private void estadoTarea(TareaViewHolder holder, Tarea tarea) {
        if (tarea.getEstado().equals("Pendiente")) {
            // Si está pendiente, mostrar fondo amarillo y el botón "Terminado"
            holder.textViewEstadoTarea.setText("Pendiente");
            holder.textViewEstadoTarea.setBackgroundColor(ContextCompat.getColor(context, R.color.color_advertencia));
            holder.buttonTerminado.setVisibility(View.VISIBLE);
        } else {
            // Si está terminada, mostrar fondo verde y ocultar el botón
            holder.textViewEstadoTarea.setText("Terminada");
            holder.textViewEstadoTarea.setBackgroundColor(ContextCompat.getColor(context, R.color.verde));
            holder.buttonTerminado.setVisibility(View.GONE);
        }
    }

    /**
     * Método para establecer el comentario de la tarea en el EditText.
     *
     * @param holder El ViewHolder donde se muestra la tarea.
     * @param tarea La tarea que contiene el comentario.
     */
    private void comentarioTarea(TareaViewHolder holder, Tarea tarea) {
        holder.editTextComentarioTarea.setText(tarea.getComentario());
        holder.editTextComentarioTarea.setTextColor(ContextCompat.getColor(context, R.color.color_texto));
    }

    /**
     * Método para configurar el botón "Terminado" y actualizar el estado de la tarea.
     * Al presionar el botón, se marca la tarea como terminada y se actualiza en Firebase.
     *
     * @param holder El ViewHolder donde se encuentra el botón.
     * @param tarea La tarea que se está terminando.
     * @param position La posición de la tarea en la lista.
     */
    private void botonTerminado(TareaViewHolder holder, Tarea tarea, int position) {
        holder.buttonTerminado.setOnClickListener(v -> {
            // Obtener el comentario ingresado
            String comentario = holder.editTextComentarioTarea.getText().toString().trim();

            // Actualizar el estado y comentario de la tarea
            tarea.setEstado("Terminada");
            tarea.setComentario(comentario);

            // Actualizar la vista
            holder.textViewEstadoTarea.setText("Terminada");
            holder.textViewEstadoTarea.setBackgroundColor(ContextCompat.getColor(context, R.color.verde));
            holder.textViewDescripcionTarea.setBackgroundColor(ContextCompat.getColor(context, R.color.color_desactivado_fondo));
            holder.buttonTerminado.setVisibility(View.GONE);  // El botón desaparece

            // Llamar a la actualización en Firebase
            TareaUtil.actualizarTareaEnFirebase(tarea.getIdTarea(), tarea);

            // Verificar si todas las tareas están terminadas y actualizar la reparación
            TareaUtil.verificarYActualizarEstadoReparacion(tarea.getIdReparacion());

            // Notificar que el ítem ha cambiado (una sola vez)
            notifyItemChanged(position);  // Actualiza la vista
        });
    }
}