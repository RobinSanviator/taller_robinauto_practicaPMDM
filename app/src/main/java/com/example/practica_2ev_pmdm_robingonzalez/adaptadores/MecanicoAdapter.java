package com.example.practica_2ev_pmdm_robingonzalez.adaptadores;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practica_2ev_pmdm_robingonzalez.R;
import com.example.practica_2ev_pmdm_robingonzalez.clases_de_ayuda.TareaUtil;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Tarea;
import com.example.practica_2ev_pmdm_robingonzalez.modelo.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;


/**
 * Adaptador para mostrar la lista de mecánicos en un RecyclerView.
 * Permite asignar tareas a los mecánicos seleccionados.
 */
public class MecanicoAdapter extends RecyclerView.Adapter<MecanicoAdapter.MecanicoViewHolder> {
    private List<Usuario> listaMecanicos; // Lista de mecánicos disponibles
    private Context contexto; // Contexto de la aplicación
    private String idReparacion; // ID de la reparación asociada

    /**
     * Constructor del adaptador.
     * @param listaMecanicos Lista de mecánicos a mostrar.
     * @param contexto Contexto de la aplicación.
     * @param idReparacion ID de la reparación a la que se asignarán tareas.
     */
    public MecanicoAdapter(List<Usuario> listaMecanicos, Context contexto, String idReparacion) {
        this.listaMecanicos = listaMecanicos;
        this.contexto = contexto;
        this.idReparacion = idReparacion;
    }

    /**
     * Establece el ID de la reparación.
     * @param idReparacion Nuevo ID de la reparación.
     */
    public void setIdReparacion(String idReparacion) {
        this.idReparacion = idReparacion;
    }

    @NonNull
    @Override
    public MecanicoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflamos la vista del item del mecánico
        View view = LayoutInflater.from(contexto).inflate(R.layout.lista_mecanico_tarea, parent, false);
        return new MecanicoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MecanicoViewHolder holder, int position) {
        // Obtenemos el mecánico de la lista según la posición
        Usuario mecanico = listaMecanicos.get(position);
        holder.textViewNombreApellidoMecanico.setText(mecanico.getNombre() + " " + mecanico.getApellidos());
        holder.textViewCorreoMecanico.setText(mecanico.getCorreo());

        // Configuramos el click en el item para abrir el diálogo de definición de tarea
        holder.itemView.setOnClickListener(v -> {
            if (idReparacion != null && !idReparacion.isEmpty()) {
                abrirDialogoDefinirTarea(mecanico);
            } else {
                Log.e("MecanicoAdapter", "idReparacion está vacío o nulo.");
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMecanicos.size(); // Devolvemos el número total de mecánicos
    }

    /**
     * ViewHolder que contiene las vistas de cada mecánico.
     */
    public static class MecanicoViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNombreApellidoMecanico;
        TextView textViewCorreoMecanico;

        public MecanicoViewHolder(@NonNull View itemView) {
            super(itemView);
            // Asignamos los TextView de la vista
            textViewNombreApellidoMecanico = itemView.findViewById(R.id.tvNombreApellidoMecanico);
            textViewCorreoMecanico = itemView.findViewById(R.id.tvCorreoMecanico);
        }
    }

    /**
     * Muestra un diálogo para definir una tarea a un mecánico seleccionado.
     * @param mecanico Mecánico al que se le asignará la tarea.
     */
    private void abrirDialogoDefinirTarea(Usuario mecanico) {
        // Crear el diálogo con MaterialAlertDialogBuilder
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(contexto);
        View view = LayoutInflater.from(contexto).inflate(R.layout.mecanico_jefe_tarea_dialogo, null);

        // Vincular los elementos de la vista del diálogo
        TextView textViewCorreoMecanicoTarea = view.findViewById(R.id.textViewCorreoMecanicoTarea);
        AutoCompleteTextView autoCompleteDefinirTareaMecanicoJefe = view.findViewById(R.id.autoCompleteDefinirTareaMecanicoJefe);

        textViewCorreoMecanicoTarea.setText("Mecánico: " + mecanico.getCorreo());
        configurarAdaptadorAutoCompletadoTareas(autoCompleteDefinirTareaMecanicoJefe);

        // Configurar las opciones del diálogo
        builder.setView(view)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    // Obtener la descripción de la tarea ingresada por el usuario
                    String descripcionTarea = autoCompleteDefinirTareaMecanicoJefe.getText().toString().trim();

                    if (descripcionTarea.isEmpty()) {
                        Snackbar.make(((Activity) contexto).findViewById(android.R.id.content), "La descripción de la tarea no puede estar vacía.", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    // Crear la tarea con idReparacion
                    Tarea tarea = new Tarea(null, idReparacion, descripcionTarea, "Pendiente", mecanico.getCorreo(), "");

                    // Guardar la tarea en Firebase
                    TareaUtil.guardarTareaEnFirebase(idReparacion, tarea);

                    Snackbar.make(((Activity) contexto).findViewById(android.R.id.content), "Tarea asignada correctamente a " + mecanico.getNombre(), Snackbar.LENGTH_LONG).show();
                })
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();
                    Snackbar.make(((Activity) contexto).findViewById(android.R.id.content), "Cancelada la tarea", Snackbar.LENGTH_LONG).show();
                })
                .show();
    }

    /**
     * Configura un adaptador para el AutoCompleteTextView con una lista de tareas predefinidas.
     * @param autoCompleteDefinirTareaMecanicoJefe AutoCompleteTextView donde se mostrarán las opciones.
     */
    private void configurarAdaptadorAutoCompletadoTareas(AutoCompleteTextView autoCompleteDefinirTareaMecanicoJefe) {
        // Obtenemos el array de tareas desde los recursos
        String[] tareasArray = contexto.getResources().getStringArray(R.array.tareas_mecanicos);
        ArrayAdapter<String> tareasAdapter = new ArrayAdapter<>(contexto, android.R.layout.simple_dropdown_item_1line, tareasArray);
        autoCompleteDefinirTareaMecanicoJefe.setAdapter(tareasAdapter);
    }
}
